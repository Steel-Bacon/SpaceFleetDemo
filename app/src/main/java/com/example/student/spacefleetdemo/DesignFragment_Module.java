package com.example.student.spacefleetdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.student.spacefleetdemo.RecyclerView.ModuleInfoAdapter;
import com.example.student.spacefleetdemo.module.EngineDesigner;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.WeaponDesigner;

import java.util.ArrayList;

public class DesignFragment_Module extends Fragment{
    private Button btn_newModuleDesign;
    private RecyclerView RV_moduleDesign;
    private static ArrayList<ModuleDesign> moduleDesignList = new ArrayList<>();
    private static ModuleInfoAdapter moduleInfoAdapter = new ModuleInfoAdapter(moduleDesignList);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.designfragment_module,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewId();
    }

    private void findViewId() {
        btn_newModuleDesign = getView().findViewById(R.id.btn_newModuleList);
        btn_newModuleDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("設計新模組")
                        .setNeutralButton("取消",null)
                        .setNegativeButton("新武器",newDesign)
                        .setPositiveButton("新引擎",newDesign)
                        .show();
            }
        });

        RV_moduleDesign = getView().findViewById(R.id.RV_moduleDesign);
        RV_moduleDesign.setAdapter(moduleInfoAdapter);
        RV_moduleDesign.setLayoutManager(new LinearLayoutManager(getContext()));
        moduleInfoAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
    }

    private DialogInterface.OnClickListener newDesign = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent;
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    intent = new Intent(getActivity(), WeaponDesigner.class);
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    intent = new Intent(getActivity(), EngineDesigner.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public static ArrayList<ModuleDesign> getModuleDesignList() {
        return moduleDesignList;
    }

    public static void addModuleDesign(ModuleDesign newDesign) {
        moduleDesignList.add(newDesign);
        moduleInfoAdapter.notifyDataSetChanged();
    }

    public static void deleteModuleDesign(int index){
        moduleDesignList.remove(index);
        moduleInfoAdapter.notifyDataSetChanged();
    }
}
