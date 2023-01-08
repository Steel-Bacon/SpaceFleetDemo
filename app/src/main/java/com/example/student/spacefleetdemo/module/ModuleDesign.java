package com.example.student.spacefleetdemo.module;

public abstract class ModuleDesign {
    final public static int WEAPON = 0, ENGINE = 1;
    private int moduleType,size;
    private String name;

    public ModuleDesign(int designType, int size, String name){
        moduleType = designType;
        this.size = size;
        this.name = name;
    }

    public int getModuleType() {
        return moduleType;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
