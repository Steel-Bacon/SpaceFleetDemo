package com.example.student.spacefleetdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;
import com.example.student.spacefleetdemo.ship.Ship;
import com.example.student.spacefleetdemo.ship.ShipDesign;

import java.util.ArrayList;

import static com.example.student.spacefleetdemo.MainActivity.setButtonClickable;

public class OverviewFragment_Overview extends Fragment{
    private static Button btn_nextTurn;
    private Button btn_newDummyShip;
    private Button btn_newEnemyShip;
    private static TextView TV_overview;
    private static int income = 2;
    private static int turn = 0;
    public static int resource = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overviewfragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TV_overview = getView().findViewById(R.id.TV_overview);
        btn_nextTurn = getView().findViewById(R.id.btn_nextTurn);
        btn_nextTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Research.addResearchPoint(2);
                resource += income;
                turn++;
                income += turn/4+1;
                for (int i=0; i<turn/5+1; i++) {
                    addRandomEnemy(i+1, turn/(i*3+1)-1);
                }
                refresh();
            }
        });
        btn_newDummyShip = getView().findViewById(R.id.btn_newDummyShip);
        btn_newDummyShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment_Battle.addDummyShip();
            }
        });
        btn_newEnemyShip = getView().findViewById(R.id.btn_newEnemyShip);
        btn_newEnemyShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildEnemyShipDialogFragment newModuleDialog = new BuildEnemyShipDialogFragment();
                newModuleDialog.show(getFragmentManager(), "BuildEnemyShipDialogFragment");
            }
        });
        refresh();
    }

    public void addRandomEnemy(int size, int number){
        if(turn == 1){
            OverviewFragment_Battle.addDummyShip();
        }else {
            int armor = (int) (Math.random()*size*2+1);
            ArrayList<ModuleDesign> moduleList = new ArrayList<>();
            moduleList.add(randomEngine(size));
            Weapon weapon = randomWeapon(size);
            moduleList.add(weapon);
            moduleList.add(weapon);
            ShipDesign shipDesign = new ShipDesign(size, size+armor, armor, "(敵)船艦"+size+"-"+armor, moduleList);
            for (int i=0; i<number; i++) {
                OverviewFragment_Battle.getEnemyFleet().add(new Ship(shipDesign.getClassName(), shipDesign));
            }
        }
    }

    public Weapon randomWeapon(int size){
        int range = (int) (size*(Math.random()*turn+1));
        int reload = size*(range/size)/turn+1;
        String name;
        if(size<2){
            name = size+"0mm機槍 type"+turn;
        }else if(size<5){
            name = size+"0mm機炮 type"+turn;
        }else if(size<10){
            name = size+"0mm速射炮 type"+turn;
        }else {
            name = size+"0mm主炮 type"+turn;
        }
        return new Weapon(size, range, reload, size, name);
    }

    public Engine randomEngine(int size){
        int power = (size+1)*size/2;
        String name = "P"+power+"/"+size+" 引擎";
        return new Engine(power, size, name);
    }

    public static void refresh(){
        TV_overview.setText(new StringBuilder().append("回合:").append(turn)
                        .append("\n資源:").append(resource)
                        .append("\n收入:").append(income));
        if(OverviewFragment_Battle.getEnemyFleet() == null || OverviewFragment_Battle.getEnemyFleet().isEmpty()){
            setButtonClickable(btn_nextTurn, true);
        }else {
            setButtonClickable(btn_nextTurn, false);
        }
    }

    public static class BuildEnemyShipDialogFragment extends DialogFragment {
        private RecyclerView RV_shipDesignList;
        private ShipDesignAdapter shipDesignAdapter = new ShipDesignAdapter(DesignFragment_Ship.getShipDesignList());

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_moduledesignlist, null))
                    .setNegativeButton("關閉", null);
            return builder.create();
        }

        @Override
        public void onStart() {
            super.onStart();
            RV_shipDesignList = getDialog().findViewById(R.id.RV_designList);
            RV_shipDesignList.setAdapter(shipDesignAdapter);
            RV_shipDesignList.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        public class ShipDesignAdapter extends RecyclerView.Adapter<ShipDesignAdapter.ViewHolder> {
            private ArrayList<ShipDesign> shipDesignList;

            public ShipDesignAdapter(ArrayList<ShipDesign> shipDesignList) {
                this.shipDesignList = shipDesignList;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView view = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.module_info, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                if (shipDesignList.isEmpty()) {
                    holder.TV_moduleInfo.setText("無設計");
                    holder.itemView.setOnClickListener(null);
                } else {
                    ShipDesign shipDesign = shipDesignList.get(position);
                    holder.TV_moduleInfo.setText(new StringBuilder().append(shipDesign.getClassName())
                            .append("\n 體積:").append(shipDesign.getSize())
                            .append(" 結構:").append(shipDesign.getStructure())
                            .append(" 裝甲:").append(shipDesign.getArmor()));
                    holder.bind(shipDesign, position);
                }
            }

            @Override
            public int getItemCount() {
                if (shipDesignList.isEmpty()) {
                    return 1;
                } else {
                    return shipDesignList.size();
                }
            }

            public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                private int position;
                private ShipDesign shipDesign;
                private TextView TV_moduleInfo;

                public ViewHolder(TextView itemView) {
                    super(itemView);
                    TV_moduleInfo = itemView;
                }

                public void bind(ShipDesign shipDesign, int position) {
                    this.shipDesign = shipDesign;
                    this.position = position;
                    TV_moduleInfo.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < shipDesign.getModuleList().size(); i++){
                        switch (shipDesign.getModuleList().get(i).getModuleType()){
                            case ModuleDesign.ENGINE:
                                Engine engine = (Engine) shipDesign.getModuleList().get(i);
                                stringBuilder.append("\n").append(engine.getName())
                                        .append("\n 體積:").append(engine.getSize())
                                        .append(" 出力:").append(engine.getPower());
                                break;
                            case ModuleDesign.WEAPON:
                                Weapon weapon = (Weapon) shipDesign.getModuleList().get(i);
                                stringBuilder.append("\n").append(weapon.getName())
                                        .append("\n 體積:").append(weapon.getSize())
                                        .append(" 口徑:").append(weapon.getCaliber())
                                        .append(" 射程:").append(weapon.getRange())
                                        .append(" 裝填時間:").append(weapon.getReload());
                                break;
                        }
                    }

                    NameShipDialog nameShipDialog = new NameShipDialog().setText(new StringBuilder().append(shipDesign.getClassName())
                            .append("\n體積:").append(shipDesign.getSize())
                            .append(" 結構:").append(shipDesign.getStructure())
                            .append(" 裝甲:").append(shipDesign.getArmor())
                            .append("\n").append(stringBuilder).toString());
                    nameShipDialog.setShipDesign(shipDesign);
                    nameShipDialog.show(getFragmentManager(), "NameShipDialog");
                }
            }
        }
    }

    public static class NameShipDialog extends DialogFragment{
        private ShipDesign shipDesign;
        private String text;
        private EditText ET_newShipName;
        private TextView TV_newShipInfo;

        private ArrayList<Ship> enemyFleet = OverviewFragment_Battle.getEnemyFleet();

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.buildnewship, null))
                    .setPositiveButton("建造", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(ET_newShipName.length() > 0){
                                enemyFleet.add(new Ship("(敵)"+ET_newShipName.getText().toString(), shipDesign));
                            }else {
                                enemyFleet.add(new Ship("(敵)"+shipDesign.getClassName(), shipDesign));
                            }
                            if(OverviewFragment_Battle.enemyFleetAdapter != null){
                                OverviewFragment_Battle.notifyDataChange();
                            }
                        }
                    })
                    .setNegativeButton("關閉", null);
            return builder.create();
        }

        @Override
        public void onStart() {
            super.onStart();
            ET_newShipName = getDialog().findViewById(R.id.ET_newShipName);
            TV_newShipInfo = getDialog().findViewById(R.id.TV_newShipInfo);
            TV_newShipInfo.setText(text);
        }

        public NameShipDialog setText(String text){
            this.text = text;
            return this;
        }

        public NameShipDialog setShipDesign(ShipDesign shipDesign){
            this.shipDesign = shipDesign;
            return this;
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            super.show(manager, tag);
        }
    }
}
