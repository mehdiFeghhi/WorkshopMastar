package com.project;


public class RequestGreater extends Requests {
    private int id;
    private Greater greater;
    private Type_Request type = Type_Request.BeGreater;
    public RequestGreater(){
        super();

    }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(Type_Request type) {
        this.type = type;
    }
    public void setAcceptly(Accetply acceptly){
        super.setAccetply(acceptly);
    }
    @Override
    public HoldWorkShop getHoldWorkShop(){
        return super.getHoldWorkShop();
    }
}
