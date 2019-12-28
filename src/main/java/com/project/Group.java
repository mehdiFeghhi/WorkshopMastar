package com.project;

public class Group {
    private int number;
    private String name;
    private String Head;
    private int id;
    private HoldWorkShop holdWorkShop;
    public Group(String name, int id, HoldWorkShop holdWorkShop, int number){
        this.name = name;
        this.id = id;
        this.number = number;
        this.holdWorkShop = holdWorkShop;

    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getHead() {
        return Head;
    }

    public int getId() {
        return id;
    }

    public HoldWorkShop getHoldWorkShop() {
        return holdWorkShop;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHead(String head) {
        Head = head;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoldWorkShop(HoldWorkShop holdWorkShop) {
        this.holdWorkShop = holdWorkShop;
    }
}
