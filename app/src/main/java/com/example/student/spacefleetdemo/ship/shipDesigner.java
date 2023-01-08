package com.example.student.spacefleetdemo.ship;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.spacefleetdemo.DesignFragment_Module;
import com.example.student.spacefleetdemo.DesignFragment_Ship;
import com.example.student.spacefleetdemo.Fragment_Research;
import com.example.student.spacefleetdemo.R;
import com.example.student.spacefleetdemo.RecyclerView.ModuleInfoAdapter;
import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.EngineDesigner;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;

import java.util.ArrayList;
import java.util.List;

import static com.example.student.spacefleetdemo.MainActivity.setButtonClickable;

public class shipDesigner extends AppCompatActivity{
    private Button btn_increaseShipSize, btn_reduceShipSize, btn_increaseShipArmor, btn_reduceShipArmor, btn_cancelShip, btn_confirmShip;
    private TextView TV_setShipSize, TV_setShipArmor, TV_shipTechPoint, TV_shipLV;
    private EditText ET_shipClassName;
    private ViewPager VP_shipDesigner;

    private int shipBodyTech = Fragment_Research.getTechLv(Fragment_Research.SHIPBODY), techPoint = shipBodyTech;
    private static int setSize = 0, setArmor = 0, size = 1, armor = 1, emptySpace = 3;
    private static ArrayList<ModuleDesign> moduleList = new ArrayList<>();

    private ShipDesignFragment_preview previewFragment = new ShipDesignFragment_preview();
    private ShipDesignFragment_ModuleList moduleListFragment = new ShipDesignFragment_ModuleList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipdesigner);
        findViewId();

        TV_setShipSize.setText("艦體大小：" + setSize);
        TV_setShipArmor.setText("艦體裝甲：" + setArmor);
        TV_shipTechPoint.setText("剩餘科技點：" + techPoint);
        TV_shipLV.setText("艦體等級：" + shipBodyTech);

        if(techPoint == 0){
            setButtonClickable(btn_increaseShipSize,false);
            setButtonClickable(btn_increaseShipArmor,false);
        }
        setButtonClickable(btn_reduceShipSize, false);
        setButtonClickable(btn_reduceShipArmor, false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void findViewId() {
        btn_increaseShipSize = findViewById(R.id.btn_increaseShipSize);
        btn_increaseShipSize.setOnClickListener(btnOnClickListener);
        btn_reduceShipSize = findViewById(R.id.btn_reduceShipSize);
        btn_reduceShipSize.setOnClickListener(btnOnClickListener);
        btn_increaseShipArmor = findViewById(R.id.btn_increaseShipArmor);
        btn_increaseShipArmor.setOnClickListener(btnOnClickListener);
        btn_reduceShipArmor = findViewById(R.id.btn_reduceShipArmor);
        btn_reduceShipArmor.setOnClickListener(btnOnClickListener);
        btn_cancelShip = findViewById(R.id.btn_cancelShip);
        btn_cancelShip.setOnClickListener(btnOnClickListener);
        btn_confirmShip = findViewById(R.id.btn_confirmShip);
        btn_confirmShip.setOnClickListener(btnOnClickListener);

        TV_setShipSize = findViewById(R.id.TV_setShipSize);
        TV_setShipArmor = findViewById(R.id.TV_setShipArmor);
        TV_shipTechPoint = findViewById(R.id.TV_shipTechPoint);
        TV_shipLV = findViewById(R.id.TV_shipLV);

        ET_shipClassName = findViewById(R.id.ET_shipClassName);

        VP_shipDesigner = findViewById(R.id.VP_shipDesigner);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(previewFragment);
        fragmentList.add(moduleListFragment);
        VP_shipDesigner.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
    }

    private Button.OnClickListener btnOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_increaseShipSize:
                    setSize++;
                    size++;
                    techPoint--;
                    break;
                case R.id.btn_reduceShipSize:
                    setSize--;
                    size--;
                    techPoint++;
                    break;
                case R.id.btn_increaseShipArmor:
                    setArmor++;
                    armor++;
                    techPoint--;
                    break;
                case R.id.btn_reduceShipArmor:
                    setArmor--;
                    armor--;
                    techPoint++;
                    break;
                case R.id.btn_cancelShip:
                    setSize = 0; setArmor = 0; size = 1; armor = 1; emptySpace = 3;
                    moduleList.clear();
                    finish();
                    break;
                case R.id.btn_confirmShip:
                    if(ET_shipClassName.getText().toString().equals("")){
                        Toast.makeText(shipDesigner.this,"請輸入艦級",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(emptySpace < 0){
                        Toast.makeText(shipDesigner.this,"剩餘空間不足",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String className = ET_shipClassName.getText().toString();
                    ShipDesign newShipDesign = new ShipDesign(size, size+armor, armor, className, moduleList);
                    DesignFragment_Ship.addShipDesign(newShipDesign);
                    setSize = 0; setArmor = 0; size = 1; armor = 1; emptySpace = 3;
                    moduleList.clear();
                    finish();
                    break;
            }
            refresh();
        }
    };

    private void refresh() {
        resetEmptySpace();

        TV_setShipSize.setText("艦體大小：" + setSize);
        TV_setShipArmor.setText("艦體裝甲：" + setArmor);
        TV_shipTechPoint.setText("剩餘科技點：" + techPoint);
        TV_shipLV.setText("艦體等級：" + shipBodyTech);
        previewFragment.refresh();
        moduleListFragment.refresh();

        setButtonClickable(btn_increaseShipSize, true);
        setButtonClickable(btn_reduceShipSize, true);
        setButtonClickable(btn_increaseShipArmor, true);
        setButtonClickable(btn_reduceShipArmor, true);
        setButtonClickable(btn_cancelShip, true);
        setButtonClickable(btn_confirmShip, true);
        if(techPoint == 0){
            setButtonClickable(btn_increaseShipSize,false);
            setButtonClickable(btn_increaseShipArmor,false);
        }
        if(setSize == 0){
            setButtonClickable(btn_reduceShipSize, false);
        }
        if(setArmor == 0){
            setButtonClickable(btn_reduceShipArmor, false);
        }
    }

    private static void resetEmptySpace(){
        emptySpace = 3 + 3*setSize*setSize - setArmor;
        for(int i = 0; i < moduleList.size(); i++){
            emptySpace -= moduleList.get(i).getSize();
        }
    }

    public static class ShipDesignFragment_preview extends Fragment{
        private static TextView TV_shipSizePreview, TV_shipArmorPreview, TV_shipSpacePreview;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.shipdesignerfragment_preview,container,false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TV_shipSizePreview = getActivity().findViewById(R.id.TV_shipSizePreview);
            TV_shipArmorPreview = getActivity().findViewById(R.id.TV_shipArmorPreview);
            TV_shipSpacePreview = getActivity().findViewById(R.id.TV_shipSpacePreview);
            refresh();
        }

        public static void refresh(){
            TV_shipSizePreview.setText("體積：" + size);
            TV_shipArmorPreview.setText("裝甲：" + armor);
            TV_shipSpacePreview.setText("剩餘空間：" + emptySpace);
        }
    }

    public static class ShipDesignFragment_ModuleList extends Fragment{
        private static TextView TV_shipRemainSpace;
        private Button btn_addShipModule;
        private RecyclerView RV_shipDesignerModuleList;
        private static ModuleInfoAdapter moduleInfoAdapter = new ModuleInfoAdapter(moduleList);

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.shipdesignerfragment_modulelist,container,false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TV_shipRemainSpace = getActivity().findViewById(R.id.TV_shipRemainSpace);
            btn_addShipModule = getActivity().findViewById(R.id.btn_addShipModule);
            btn_addShipModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModuleDesignDialogFragment newModuleDialog = new ModuleDesignDialogFragment();
                    newModuleDialog.show(getFragmentManager(), "ModuleDesignDialogFragment");
                }
            });
            RV_shipDesignerModuleList = getActivity().findViewById(R.id.RV_shipDesignerModuleList);
            RV_shipDesignerModuleList.setAdapter(moduleInfoAdapter);
            RV_shipDesignerModuleList.setLayoutManager(new LinearLayoutManager(getContext()));
            moduleInfoAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    resetEmptySpace();
                    refresh();
                    ShipDesignFragment_preview.refresh();
                }
            });
            refresh();
        }

        public static void refresh(){
            TV_shipRemainSpace.setText("剩餘空間：" + emptySpace);
        }

        public static class ModuleDesignDialogFragment extends DialogFragment{
            private RecyclerView RV_designList;
            private ModuleListAdapter moduleListAdapter = new ModuleListAdapter(DesignFragment_Module.getModuleDesignList());

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
                RV_designList = getDialog().findViewById(R.id.RV_designList);
                RV_designList.setAdapter(moduleListAdapter);
                RV_designList.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ViewHolder>{
                private ArrayList<ModuleDesign> moduleInfoList;

                public ModuleListAdapter(ArrayList<ModuleDesign> moduleInfoList) {
                    super();
                    this.moduleInfoList = moduleInfoList;
                }

                @NonNull
                @Override
                public ModuleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    TextView view = (TextView)LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.module_info, parent, false);
                    return new ModuleListAdapter.ViewHolder(view);
                }

                @Override
                public void onBindViewHolder(@NonNull ModuleListAdapter.ViewHolder holder, int position) {
                    if(moduleInfoList.isEmpty()){
                        holder.TV_moduleInfo.setText("無設計");
                        holder.itemView.setOnClickListener(null);
                        holder.itemView.setOnLongClickListener(null);
                    }else{
                        switch (moduleInfoList.get(position).getModuleType()) {
                            case ModuleDesign.ENGINE:
                                Engine engine = (Engine) moduleInfoList.get(position);
                                holder.TV_moduleInfo.setText(new StringBuilder().append(engine.getName())
                                        .append("\n 體積:").append(engine.getSize())
                                        .append(" 出力:").append(engine.getPower()).toString());
                                holder.bind(engine, position);
                                break;
                            case ModuleDesign.WEAPON:
                                Weapon weapon = (Weapon) moduleInfoList.get(position);
                                holder.TV_moduleInfo.setText(new StringBuilder().append(weapon.getName())
                                        .append("\n 體積:").append(weapon.getSize())
                                        .append(" 口徑:").append(weapon.getCaliber())
                                        .append(" 射程:").append(weapon.getRange())
                                        .append(" 裝填時間:").append(weapon.getReload()).toString());
                                holder.bind(weapon, position);
                        }
                    }
                }

                @Override
                public int getItemCount() {
                    if(moduleInfoList.isEmpty()){
                        return 1;
                    }else{
                        return moduleInfoList.size();
                    }
                }

                public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
                    private ModuleDesign module;
                    private TextView TV_moduleInfo;
                    public ViewHolder(TextView itemView) {
                        super(itemView);
                        TV_moduleInfo = itemView;
                    }

                    public void bind(ModuleDesign moduleDesign, int position){
                        module = moduleDesign;
                        TV_moduleInfo.setOnClickListener(this);
                    }

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        switch (module.getModuleType()) {
                            case ModuleDesign.ENGINE:
                                Engine engine = (Engine) module;
                                builder.setTitle(engine.getName())
                                        .setMessage(new StringBuilder().append("類型:引擎")
                                                .append("\n體積:").append(engine.getSize())
                                                .append("\n出力:").append(engine.getPower()))
                                        .setPositiveButton("安裝", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                moduleList.add(module);
                                                moduleInfoAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();
                                break;
                            case ModuleDesign.WEAPON:
                                Weapon weapon = (Weapon) module;
                                builder.setTitle(weapon.getName())
                                        .setMessage(new StringBuilder().append("類型:武器")
                                                .append("\n體積:").append(weapon.getSize())
                                                .append("\n口徑:").append(weapon.getCaliber())
                                                .append("\n射程:").append(weapon.getRange())
                                                .append("\n裝填時間:").append(weapon.getReload()))
                                        .setPositiveButton("安裝", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                moduleList.add(module);
                                                moduleInfoAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();
                                break;
                        }
                    }
                }
            }
        }
    }
}
