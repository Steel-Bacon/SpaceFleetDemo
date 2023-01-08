package com.example.student.spacefleetdemo.ship;

import com.example.student.spacefleetdemo.module.Engine;
import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;

import java.util.ArrayList;
import java.util.Arrays;

public class Ship{
    private static int idCount = 0;
    private int shipID, structure, position = 0, speed = 0, enginePower = 0, weight, moduleCount;
    private String name;
    private boolean[] workingModule;
    private ShipDesign shipDesign;
    private ArrayList<WeaponSet> weaponSetList = new ArrayList<>();

    public Ship(String name, ShipDesign shipDesign) {
        ArrayList<ModuleDesign> moduleList = shipDesign.getModuleList();
        this.name = name;
        this.shipDesign = shipDesign;
        this.structure = shipDesign.getStructure();
        weight = shipDesign.getSize() + shipDesign.getArmor();
        moduleCount = moduleList.size();
        workingModule = new boolean[moduleCount];
        Arrays.fill(workingModule,true);
        for (int i=0; i < moduleList.size(); i++){
            if(moduleList.get(i) instanceof Weapon){
                weaponSetList.add(new WeaponSet((Weapon) moduleList.get(i), i));
            }
        }
        resetSpeed();
        shipID = idCount;
        idCount++;
    }

    public int getShipID() {
        return shipID;
    }

    public int getStructure(){
        return structure;
    }

    public int getPosition(){
        return position;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public boolean isAbleToAttack() {
        boolean ableToAttack = false;
        ModuleDesign module;
        for (int i=0; i < moduleCount; i++){
            module = shipDesign.getModuleList().get(i);
            if(module instanceof Weapon && workingModule[i]){
                ableToAttack = true;
                break;
            }
        }
        return ableToAttack;
    }

    public boolean isModuleWorking(int position){
        return workingModule[position];
    }

    public int getMaxAttackRange(){
        int maxAttackRange = 0;
        ModuleDesign module;
        for (int i=0; i < moduleCount; i++){
            module = shipDesign.getModuleList().get(i);
            if(module instanceof Weapon && workingModule[i] && ((Weapon) module).getRange() > maxAttackRange){
                maxAttackRange = ((Weapon) module).getRange();
            }
        }
        return maxAttackRange;
    }

    public ShipDesign getShipDesign() {
        return shipDesign;
    }

    public int getSize(){
        return shipDesign.getSize();
    }

    public int getArmor(){
        return shipDesign.getArmor();
    }

    public ArrayList<WeaponSet> getWeaponSetList() {
        return weaponSetList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStructure(int structure){
        this.structure = structure;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void reset(){
        Arrays.fill(workingModule,true);
        resetEnginePower();
        resetSpeed();
        resetStructure();
    }

    public void resetStructure(){
        this.structure = shipDesign.getStructure();
    }

    public void resetSpeed() {
        resetEnginePower();
        speed = (enginePower*10)/weight;
    }

    public void resetEnginePower() {
        enginePower = 0;
        for(int i = 0; i < moduleCount; i++) {
            if(workingModule[i] && shipDesign.getModuleList().get(i) instanceof Engine)
                enginePower += ((Engine) shipDesign.getModuleList().get(i)).getPower();
        }
    }

    public class WeaponSet{
        private Weapon weapon;
        private int weaponPosition, weaponRang, weaponReload = 0;

        public WeaponSet(Weapon weapon, int weaponPosition) {
            this.weapon = weapon;
            this.weaponPosition = weaponPosition;
            this.weaponRang = weapon.getRange();
        }

        public Weapon getWeapon() {
            return weapon;
        }

        public int getWeaponPosition() {
            return weaponPosition;
        }

        public int getWeaponRang() {
            return weaponRang;
        }

        public int getWeaponReload() {
            return weaponReload;
        }

        public void setWeaponReload() {
            this.weaponReload = weapon.getReload();
        }

        public void resetWeaponReload(){
            this.weaponReload = 0;
        }

        public void reload(){
            if(getWeaponReload()>0) {
                weaponReload--;
            }
        }
    }
}
