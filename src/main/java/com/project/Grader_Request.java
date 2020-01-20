package com.project;


import java.io.Serializable;

public class Grader_Request extends Requests implements Serializable {
    private int id;
    private Grader grader;
    private Type_Request type = Type_Request.BeGreater;
    public Grader_Request(){
        super();

    }
    public Grader_Request(String request, HoldWorkShop holdWorkShop, Grader grader) {
        super(request,holdWorkShop);
        this.grader = grader;
    }

    public Grader getGrader() {
        return grader;
    }

    public void setGrader(Grader grader) {
        this.grader = grader;
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
    public String getMyClass(){
        return  "2";
    }
}
