package com.example.student.spacefleetdemo.ship;

import com.example.student.spacefleetdemo.module.ModuleDesign;
import com.example.student.spacefleetdemo.module.Weapon;

import java.util.ArrayList;

public class ShipDesign {
    private int size,structure,armor,maxAttackRange = 0,cost = 0;
    private String className;
    private ArrayList<ModuleDesign> moduleList = new ArrayList<>();
    private boolean ableToAttack = false;

    public ShipDesign(int size, int structure, int armor, String className, ArrayList<ModuleDesign> moduleList) {
        this.size = size;
        this.structure = structure;
        this.armor = armor;
        this.className = className;
        for(int i=0; i < moduleList.size(); i++){
            this.moduleList.add(moduleList.get(i));
            cost += moduleList.get(i).getSize();
            if(moduleList.get(i) instanceof Weapon){
                ableToAttack = true;
                if(((Weapon) moduleList.get(i)).getRange() > maxAttackRange){
                    maxAttackRange = ((Weapon) moduleList.get(i)).getRange();
                }
            }
        }
        cost += size*3;
    }

    public int getSize() {
        return size;
    }

    public int getStructure() {
        return structure;
    }

    public int getArmor() {
        return armor;
    }

    public int getCost(){
        return cost;
    }

    public String getClassName() {
        return className;
    }

    public boolean isAbleToAttack(){
        return ableToAttack;
    }

    public int getMaxAttackRange(){
        return maxAttackRange;
    }

    public void setClassName(String name) {
        this.className = className;
    }

    public void clearModuleList(){
        moduleList.clear();
    }

    public ArrayList<ModuleDesign> getModuleList() {
        return moduleList;
    }
}
