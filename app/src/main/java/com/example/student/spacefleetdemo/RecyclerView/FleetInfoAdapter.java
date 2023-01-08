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
import com.example.student.spacefleetdemo.ship.Ship;
import com.example.student.spacefleetdemo.ship.ShipDesign;

import java.util.ArrayList;

public class FleetInfoAdapter extends RecyclerView.Adapter<FleetInfoAdapter.ViewHolder>{
    private ArrayList<Ship> fleet;

    public FleetInfoAdapter(ArrayList<Ship> fleet) {
        this.fleet = fleet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_info, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(fleet.isEmpty()){
            holder.TV_shipInfo.setText("無船艦");
            holder.itemView.setOnClickListener(null);
        }else {
            Ship ship = fleet.get(position);
            holder.TV_shipInfo.setText(new StringBuilder().append(ship.getName()).append("\n ").append(ship.getShipDesign().getClassName()));
            holder.bind(ship, position);
        }
    }

    @Override
    public int getItemCount() {
        if(fleet.isEmpty()){
            return 1;
        }else {
            return fleet.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int position;
        private Ship ship;
        private TextView TV_shipInfo;

        public ViewHolder(TextView itemView) {
            super(itemView);
            TV_shipInfo = itemView;
        }

        public void bind(Ship ship, int position){
            this.ship = ship;
            this.position = position;
            TV_shipInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ShipDesign shipDesign = ship.getShipDesign();
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
            builder.setTitle(ship.getName())
                    .setMessage(new StringBuilder().append(shipDesign.getClassName())
                            .append("\n體積:").append(shipDesign.getSize())
                            .append(" 結構:").append(ship.getStructure()).append("/").append(shipDesign.getStructure())
                            .append(" 裝甲:").append(shipDesign.getArmor())
                            .append("\n").append(stringBuilder))
                    .setPositiveButton("確定", null)
                    .setNegativeButton("拆解", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fleet.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .show();
        }
    }
}
