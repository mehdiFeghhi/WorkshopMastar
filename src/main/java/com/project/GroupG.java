package com.project;

import java.io.Serializable;

public class GroupG implements Serializable {
    private int number;
    private String name;
    private String Head;
    private int id;
    private HoldWorkShop holdWorkShop;
    public GroupG(String name, HoldWorkShop holdWorkShop, int number){
        this.name = name;
        this.number = number;
        this.holdWorkShop = holdWorkShop;

    }

    public GroupG() {

    }

    public GroupG(String name, String head, Integer number) {
        this.name = name;
        this.Head = head;
        this.number = number;
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
