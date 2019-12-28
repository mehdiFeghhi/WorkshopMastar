package com.project;

public class RequestStudent extends Requests {
    int id;
    Student student;
    Type_Request type_request = Type_Request.BeInvolve;
    Pay pay;
    public RequestStudent(String request,HoldWorkShop holdWorkShop, Student student,Pay pay) {
        super(request,holdWorkShop);
        this.student = student;
        this.pay = pay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAceptly(Accetply aceptly){
        super.setAccetply(aceptly);
    }
    public Student getStudent() {
        return student;
    }

    public Type_Request getType_request() {
        return type_request;
    }

    public Pay getPay() {
        return pay;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setType_request(Type_Request type_request) {
        this.type_request = type_request;
    }

    public void setPay(Pay pay) {
        this.pay = pay;
    }

}
