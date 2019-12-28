package com.project;


public class RequestGreater extends Requests {
    private Greater greater;
    private Type_Request type = Type_Request.BeGreater;
    public RequestGreater(String request,HoldWorkShop holdWorkShop, Greater greater) {
        super(request,holdWorkShop);
        this.greater = greater;
    }

    public Greater getGreater() {
        return greater;
    }

    public void setGreater(Greater greater) {
        this.greater = greater;
    }

    public Type_Request getType() {
        return type;
    }

    public void setType(Type_Request type) {
        this.type = type;
    }
    public void setAcceptly(Accetply acceptly){
        super.setAccetply(acceptly);
    }
}
