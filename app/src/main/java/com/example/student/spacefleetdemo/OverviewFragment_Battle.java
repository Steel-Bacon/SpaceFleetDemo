package com.example.student.spacefleetdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.student.spacefleetdemo.RecyclerView.CommonAdapter;
import com.example.student.spacefleetdemo.RecyclerView.ViewHolder;
import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;
import com.example.student.spacefleetdemo.ship.Ship;
import com.example.student.spacefleetdemo.ship.ShipDesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.student.spacefleetdemo.MainActivity.setButtonClickable;

public class OverviewFragment_Battle extends Fragment{
    private Button btn_battle;
    private RecyclerView RV_battle;
    private Timer timer;
    private View.OnClickListener defaultListener;
    private static ArrayList<String> combatLog = new ArrayList<>();
    private CommonAdapter<String> combatLogAdapter;
    private static ArrayList<Ship> enemyFleet = new ArrayList<>();
    public static CommonAdapter<Ship> enemyFleetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overviewfragment_battle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enemyFleetAdapter = new CommonAdapter<Ship>(getContext(), R.layout.module_info, enemyFleet) {
            @Override
            public void setItemToHolder(ViewHolder holder, final Ship ship) {
                final ShipDesign shipDesign = ship.getShipDesign();
                TextView textView = holder.getView(R.id.TV_moduleInfo);
                textView.setText(new StringBuilder().append(ship.getName())
                        .append("\n ").append(shipDesign.getClassName()));
                holder.setOnClickListener(R.id.TV_moduleInfo, new View.OnClickListener() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setPositiveButton("關閉", null)
                                .setTitle(ship.getName())
                                .setMessage(new StringBuilder().append(shipDesign.getClassName())
                                        .append("\n體積:").append(shipDesign.getSize())
                                        .append(" 結構:").append(ship.getStructure()).append("/").append(shipDesign.getStructure())
                                        .append(" 裝甲:").append(shipDesign.getArmor())
                                        .append("\n").append(stringBuilder).toString())
                                .show();
                    }
                });
            }

            @Override
            public void setNoItemView(ViewHolder holder) {
                TextView textView = holder.getView(R.id.TV_moduleInfo);
                textView.setText("無敵對船艦");
                holder.setOnClickListener(R.id.TV_moduleInfo, null);
            }
        };

        combatLogAdapter = new CommonAdapter<String>(getContext(), R.layout.module_info, combatLog) {
            @Override
            public void setItemToHolder(ViewHolder holder, String combatLog) {
                TextView textView = holder.getView(R.id.TV_moduleInfo);
                textView.setText(combatLog);
                holder.setOnClickListener(R.id.TV_moduleInfo, null);
                if (combatLog.contains("(敵)") && combatLog.indexOf("(敵)") < 3) {
                    textView.setTextColor(Color.rgb(255, 100, 100));
                } else if(combatLog.contains("未命中") || combatLog.equals("未擊穿")){
                    textView.setTextColor(Color.rgb(150, 150, 150));
                }else{
                    textView.setTextColor(Color.rgb(0, 0, 0));
                }
            }

            @Override
            public void setNoItemView(ViewHolder holder) {
                TextView textView = holder.getView(R.id.TV_moduleInfo);
                textView.setText("戰鬥開始中...");
                holder.setOnClickListener(R.id.TV_moduleInfo, null);
            }
        };

        RV_battle = getView().findViewById(R.id.RV_battle);
        RV_battle.setLayoutManager(new LinearLayoutManager(getContext()));
        RV_battle.setAdapter(enemyFleetAdapter);

        btn_battle = getView().findViewById(R.id.btn_battle);
        defaultListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RV_battle.setAdapter(combatLogAdapter);
                combatLogAdapter.notifyDataSetChanged();
                btn_battle.setText("戰鬥進行中");
                setButtonClickable(btn_battle, false);
                timer = new Timer();
                timer.schedule(new Combat(),100,100);
            }
        };
        btn_battle.setOnClickListener(defaultListener);
    }

    public static void addDummyShip() {
        ShipDesign dummyShip = new ShipDesign(1,1,1,"甲標的",new ArrayList<ModuleDesign>());
        enemyFleet.add(new Ship("(靶船)甲標的", dummyShip));
    }

    public static ArrayList<Ship> getEnemyFleet(){
        return enemyFleet;
    }

    public static void notifyDataChange(){
        enemyFleetAdapter.notifyDataSetChanged();
    }

    public static ArrayList<String> getCombatLog() {
        return combatLog;
    }

    public class Combat extends TimerTask{
        private int battleField = 100, maxSpeed = 0, maxViewRange = 0, playerShipCount, playerRemainShipCount, enemyShipCount, enemyRemainShipCount, turn;
        private ArrayList<Ship> playerFleet, enemyFleet;
        private boolean[] playerSpoted, enemySpoted;
        private ArrayList<ShipCombatAI> playerFleetAI = new ArrayList<>(), enemyFleetAI = new ArrayList<>();

        Combat() {
            super();
            playerFleet = OverviewFragment_Fleet.getFleet();
            playerShipCount = playerFleet.size();
            playerRemainShipCount = playerShipCount;
            playerSpoted = new boolean[playerShipCount];
            Arrays.fill(playerSpoted, true);
            enemyFleet = OverviewFragment_Battle.enemyFleet;
            enemyShipCount = enemyFleet.size();
            enemyRemainShipCount = enemyShipCount;
            enemySpoted = new boolean[enemyShipCount];
            Arrays.fill(enemySpoted, true);
            Ship ship;
            for (int i = 0; i < playerShipCount; i++){//setup player fleet into battlefield
                playerFleet.get(i).setPosition(0);
                if(playerFleet.get(i).getSpeed() > maxSpeed) maxSpeed = playerFleet.get(i).getSpeed();
                playerFleetAI.add(new ShipCombatAI(playerFleet.get(i), enemyFleet, enemySpoted));
            }
            for (int i = 0; i < enemyShipCount; i++){//setup enemy fleet into battlefield
                if(enemyFleet.get(i).getSpeed() > maxSpeed) maxSpeed = enemyFleet.get(i).getSpeed();
            }
            battleField += maxSpeed;
            for (int i = 0; i < enemyShipCount; i++){
                enemyFleet.get(i).setPosition(battleField);
                enemyFleetAI.add(new ShipCombatAI(enemyFleet.get(i), playerFleet, playerSpoted));
            }
            turn = 0;
        }

        @Override
        public void run() {
            if(playerShipCount==0 && enemyShipCount==0){//player or enemy don't have any ship at the start
                combatLog.add("無戰鬥");
                btn_battle.setText("返回");
                setButtonClickable(btn_battle, true);
                btn_battle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RV_battle.setAdapter(enemyFleetAdapter);
                        combatLog.clear();
                        btn_battle.setText("開始模擬戰鬥");
                        btn_battle.setOnClickListener(defaultListener);
                    }
                });
                timer.cancel();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combatLogAdapter.notifyDataSetChanged();//update screen before end combat
                    }
                });
                return;
            }

            if(playerRemainShipCount==0 || enemyRemainShipCount==0) {
                if(enemyRemainShipCount==0){
                    combatLog.add("我方艦隊勝利");
                }else {
                    combatLog.add("我方艦隊戰敗");
                }
                btn_battle.setText("戰鬥結束");
                setButtonClickable(btn_battle, true);
                btn_battle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=playerShipCount-1; i>=0; i--){//remove player's dead ship
                            if(playerFleet.get(i).getStructure() == 0){
                                playerFleet.remove(i);
                            }
                        }
                        for (int i=enemyShipCount-1; i>=0; i--){//remove enemy's dead ship
                            if(enemyFleet.get(i).getStructure() == 0){
                                enemyFleet.remove(i);
                            }
                        }
                        RV_battle.setAdapter(enemyFleetAdapter);
                        OverviewFragment_Battle.notifyDataChange();
                        OverviewFragment_Fleet.notifyDataChange();
                        combatLog.clear();
                        btn_battle.setText("開始模擬戰鬥");
                        btn_battle.setOnClickListener(defaultListener);
                    }
                });
                timer.cancel();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combatLogAdapter.notifyDataSetChanged();//update screen before end combat
                    }
                });
                return;
            }

            combatLog.add("　　回合"+turn);
            turn++;

            for (int i=0; i<playerFleetAI.size(); i++){//make every player ship take one action
                playerFleetAI.get(i).action();
            }

            for (int i=0; i<enemyFleetAI.size(); i++){//make every enemy ship take one action
                enemyFleetAI.get(i).action();
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    combatLogAdapter.notifyDataSetChanged();//update screen
                }
            });
        }

        public void move(Ship ship, int movement){
            ship.setPosition(ship.getPosition()+movement);
        }

        public void attack(Weapon weapon, Ship target){
            int length = weapon.getLength()-1;//weapon length help penetrate armor and hit target

            int penetration = weapon.getCaliber();
            if(length>penetration){
                penetration += length/2;
            }else {
                penetration = length;
            }

            int hitChance = 50;
            hitChance = 100*(target.getSize()+length)/(target.getSpeed()/3+weapon.getCaliber());

            int effectArmor = (int) ((Math.random()+0.5)*(target.getArmor()-penetration));
            if(effectArmor<0){
                effectArmor = 0;
            }

            if(Math.random()*100 < hitChance){//hit
                if(weapon.getCaliber() > effectArmor){//penetrate armor
                    int damage = weapon.getCaliber() - effectArmor;
                    if(target.getStructure() > damage){//target survived
                        target.setStructure(target.getStructure()-damage);
                        combatLog.add("命中，傷害"+damage);
                    }else if(target.getStructure() == 0) {//hit dead target
                        combatLog.add("命中殘骸");
                    }else {//target destroyed
                        target.setStructure(0);
                        shipDestroyed(target.getShipID());
                        combatLog.add("命中，傷害"+damage+"，摧毀目標");
                    }
                }else {//failed to penetrate armor
                    combatLog.add("未擊穿");
                }
            }else {//miss
                combatLog.add("未命中("+hitChance+"%)");
            }
        }

        public int getDistance(Ship ship, Ship target){
            return Math.abs(ship.getPosition() - target.getPosition());
        }

        public void shipDestroyed(int shipID){
            for (int i = 0; i < enemyFleet.size(); i++){
                if(enemyFleet.get(i).getShipID() == shipID){
                    enemyFleet.get(i).setName(enemyFleet.get(i).getName()+"(殘骸)");
                    enemyRemainShipCount--;
                    enemySpoted[i] = false;
                    return;
                }
            }
            for(int i = 0; i < playerFleet.size(); i++){
                if(playerFleet.get(i).getShipID() == shipID){
                    playerFleet.get(i).setName(playerFleet.get(i).getName()+"(殘骸)");
                    playerRemainShipCount--;
                    playerSpoted[i] = false;
                    return;
                }
            }
        }

        public class ShipCombatAI{
            private Ship ship;
            private ArrayList<Ship.WeaponSet> shipWeaponSetList;
            private ArrayList<Ship> hostileFleet;
            private int hostileCount;
            private boolean[] hostileShipSpoted;
            private boolean direction;

            public ShipCombatAI(Ship ship, ArrayList<Ship> hostileFleet, boolean[] hostileShipSpoted) {
                this.ship = ship;
                shipWeaponSetList = ship.getWeaponSetList();
                this.hostileFleet = hostileFleet;
                this.hostileShipSpoted = hostileShipSpoted;
                hostileCount = hostileFleet.size();
                direction = ship.getPosition() == 0;
            }

            public void action(){
                if(ship.getStructure()==0){//dead ship doesn't do anything
                    return;
                }

                Ship target;
                int distance, closestRange = battleField;

                for (int i=0; i<shipWeaponSetList.size(); i++){//reload every weapon
                    shipWeaponSetList.get(i).reload();
                }

                for (int i = 0; i < hostileCount; i++){
                    target = hostileFleet.get(i);
                    distance = getDistance(ship, target);
                    if(hostileShipSpoted[i] && distance<closestRange){
                        closestRange = distance;
                    }
                    if(hostileShipSpoted[i] && distance<=ship.getMaxAttackRange()){
                        for (int j = 0; j < shipWeaponSetList.size(); j++){
                            Ship.WeaponSet weaponSet = shipWeaponSetList.get(j);
                            if(ship.isModuleWorking(weaponSet.getWeaponPosition()) && weaponSet.getWeaponReload()==0 && distance<=weaponSet.getWeaponRang()){
                                combatLog.add(ship.getName()+"("+ship.getShipID()+")"+"以"+weaponSet.getWeapon().getName()+"射擊"+target.getName()+"("+target.getShipID()+")"+",距離"+distance);
                                attack(weaponSet.getWeapon(), target);
                                weaponSet.setWeaponReload();
                            }
                        }
                        return;//don't move if enemy within range
                    }
                }

                if(direction) {//move player ship
                    if(ship.getSpeed()>closestRange-ship.getMaxAttackRange()){
                        move(ship, closestRange-ship.getMaxAttackRange());
                    }else {
                        move(ship, ship.getSpeed());
                    }
                }else {//move enemy ship
                    if(ship.getSpeed()>closestRange-ship.getMaxAttackRange()){
                        move(ship, -(closestRange-ship.getMaxAttackRange()));
                    }else {
                        move(ship, -ship.getSpeed());
                    }
                }
                if(ship.getSpeed()>0) {
                    combatLog.add(ship.getName() + "前進至" + ship.getPosition() + "處");
                }
            }
        }
    }
}
