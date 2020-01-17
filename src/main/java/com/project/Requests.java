package com.project;

import java.io.Serializable;

enum Type_Request{
    BeGreater,BeInvolve;
}
enum Accetply{
    Accept,Reject,Pending;
}


abstract public class Requests implements Serializable {
    private String massage;
    private Accetply accetply = Accetply.Pending;
    private HoldWorkShop holdWorkShop;
    public Requests(String request , HoldWorkShop holdWorkShop) {
        this.massage = request;
        this.holdWorkShop = holdWorkShop;
    }
    public Requests() {

    }

    public HoldWorkShop getHoldWorkShop() {
        return holdWorkShop;
    }

    public void setHoldWorkShop(HoldWorkShop holdWorkShop) {
        this.holdWorkShop = holdWorkShop;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Accetply getAccetply() {
        return accetply;
    }

    public void setAccetply(Accetply accetply) {
        this.accetply = accetply;
    }
}
