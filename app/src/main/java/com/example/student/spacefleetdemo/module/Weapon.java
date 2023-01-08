package com.example.student.spacefleetdemo.module;

import com.example.student.spacefleetdemo.ship.ShipDesign;

public class Weapon extends ModuleDesign{
    private int caliber,range,reload,length;

    public Weapon(int caliber, int range, int reload, int size, String name){
        super(WEAPON,size,name);
        this.caliber = caliber;
        this.range = range;
        this.reload = reload;
        length = range/caliber;
    }

    public int getCaliber() {
        return caliber;
    }

    public int getRange() {
        return range;
    }

    public int getReload() {
        return reload;
    }

    public int getLength(){
        return length;
    }
}
