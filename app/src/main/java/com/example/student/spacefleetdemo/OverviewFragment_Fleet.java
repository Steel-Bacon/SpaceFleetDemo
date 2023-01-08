package com.example.student.spacefleetdemo;

import android.annotation.SuppressLint;
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

import com.example.student.spacefleetdemo.RecyclerView.FleetInfoAdapter;
import com.example.student.spacefleetdemo.RecyclerView.ShipClassInfoAdapter;
import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;
import com.example.student.spacefleetdemo.ship.Ship;
import com.example.student.spacefleetdemo.ship.ShipDesign;

import java.util.ArrayList;

public class OverviewFragment_Fleet extends Fragment{
    private Button btn_buildNewShip;
    private RecyclerView RV_fleetOverview;
    private static ArrayList<Ship> fleet = new ArrayList<>();
    private static FleetInfoAdapter fleetInfoAdapter = new FleetInfoAdapter(fleet);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overviewfragment_fleet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewId();
    }

    private void findViewId() {
        btn_buildNewShip = getView().findViewById(R.id.btn_buildNewShip);
        btn_buildNewShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildNewShipDialogFragment newModuleDialog = new BuildNewShipDialogFragment();
                newModuleDialog.show(getFragmentManager(), "BuildNewShipDialogFragment");
            }
        });

        RV_fleetOverview = getView().findViewById(R.id.RV_fleetOverview);
        RV_fleetOverview.setAdapter(fleetInfoAdapter);
        RV_fleetOverview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static ArrayList<Ship> getFleet(){
        return fleet;
    }

    public static void notifyDataChange(){
        fleetInfoAdapter.notifyDataSetChanged();
    }

    public static class BuildNewShipDialogFragment extends DialogFragment{
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
            public ShipDesignAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView view = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.module_info, parent, false);
                return new ShipDesignAdapter.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ShipDesignAdapter.ViewHolder holder, int position) {
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
                                                                                            .append(" 造價:").append(shipDesign.getCost())
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

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.buildnewship, null))

                    .setNegativeButton("關閉", null);

            if(shipDesign.getCost() <= OverviewFragment_Overview.resource){
                builder.setPositiveButton("建造", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OverviewFragment_Overview.resource -= shipDesign.getCost();
                        if (ET_newShipName.length() > 0) {
                            fleet.add(new Ship(ET_newShipName.getText().toString(), shipDesign));
                            fleetInfoAdapter.notifyDataSetChanged();
                        } else {
                            fleet.add(new Ship(shipDesign.getClassName(), shipDesign));
                            fleetInfoAdapter.notifyDataSetChanged();
                        }
                        OverviewFragment_Overview.refresh();
                    }
                });
            }

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
