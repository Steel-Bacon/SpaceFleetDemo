package com.example.student.spacefleetdemo.RecyclerView;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.student.spacefleetdemo.R;
import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;
import com.example.student.spacefleetdemo.ship.ShipDesign;

import java.util.ArrayList;

public class ShipClassInfoAdapter extends RecyclerView.Adapter<ShipClassInfoAdapter.ViewHolder>{
    private ArrayList<ShipDesign> shipDesignList;

    public ShipClassInfoAdapter(ArrayList<ShipDesign> shipDesignList) {
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
        if (shipDesignList.isEmpty()){
            holder.TV_moduleInfo.setText("無設計");
            holder.itemView.setOnClickListener(null);
        }else {
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
        if (shipDesignList.isEmpty()){
            return 1;
        }else {
            return shipDesignList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(shipDesign.getClassName())
                    .setMessage(new StringBuilder().append("體積:").append(shipDesign.getSize())
                                        .append(" 結構:").append(shipDesign.getStructure())
                                        .append(" 裝甲:").append(shipDesign.getArmor())
                                        .append(" 造價:").append(shipDesign.getCost())
                                        .append("\n").append(stringBuilder))
                    .setPositiveButton("確定", null)
                    .setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shipDesignList.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .show();
        }
    }
}
