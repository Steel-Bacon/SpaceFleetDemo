package com.example.student.spacefleetdemo.RecyclerView;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student.spacefleetdemo.DesignFragment_Module;
import com.example.student.spacefleetdemo.R;
import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;

import java.util.ArrayList;

public class ModuleInfoAdapter extends RecyclerView.Adapter<ModuleInfoAdapter.ViewHolder>{
    private ArrayList<ModuleDesign> moduleInfoList;

    public ModuleInfoAdapter(ArrayList<ModuleDesign> moduleInfoList) {
        this.moduleInfoList = moduleInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(moduleInfoList.isEmpty()){
            holder.TV_moduleInfo.setText("無設計");
            holder.itemView.setOnClickListener(null);
        }else{
            switch (moduleInfoList.get(position).getModuleType()) {
                case ModuleDesign.ENGINE:
                    Engine engine = (Engine) moduleInfoList.get(position);
                    holder.TV_moduleInfo.setText(new StringBuilder().append(engine.getName())
                                                        .append("\n 體積:").append(engine.getSize())
                                                        .append(" 出力:").append(engine.getPower()));
                    holder.bind(engine, position);
                    break;
                case ModuleDesign.WEAPON:
                    Weapon weapon = (Weapon) moduleInfoList.get(position);
                    holder.TV_moduleInfo.setText(new StringBuilder().append(weapon.getName())
                                                        .append("\n 體積:").append(weapon.getSize())
                                                        .append(" 口徑:").append(weapon.getCaliber())
                                                        .append(" 射程:").append(weapon.getRange())
                                                        .append(" 裝填時間:").append(weapon.getReload()));
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
        private int position;
        private ModuleDesign module;
        private TextView TV_moduleInfo;

        public ViewHolder(TextView itemView) {
            super(itemView);
            TV_moduleInfo = itemView;
        }

        public void bind(ModuleDesign moduleDesign, int position){
            module = moduleDesign;
            this.position = position;
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
                            .setPositiveButton("確定", null)
                            .setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    moduleInfoList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
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
                            .setPositiveButton("確定", null)
                            .setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    moduleInfoList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .show();
                    break;
            }
        }
    }
}
