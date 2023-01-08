package com.example.student.spacefleetdemo.module;

public class Engine extends ModuleDesign{
    private int power;

    public Engine(int power, int size ,String name) {
        super(ModuleDesign.ENGINE, size, name);
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
