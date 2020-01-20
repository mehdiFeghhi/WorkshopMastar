package com.project;

public class Grop {
    public int group_id;
    public int number;
    public String name;
    public String Head;
    public int holdWorkShop_id;

    public Grop(int group_id, int number, String name, String head, int holdWorkShop_id) {
        this.group_id = group_id;
        this.number = number;
        this.name = name;
        Head = head;
        this.holdWorkShop_id = holdWorkShop_id;
    }

    public Grop(int gid, String name, String head, int number, int workshopid) {
        this.group_id = group_id;
        this.number = number;
        this.name = name;
        Head = head;
        this.holdWorkShop_id = holdWorkShop_id;
    }
}
