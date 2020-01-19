package com.project;

public class Student implements RoleOfWorkShape {
    int id;

    public Student() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getMyClass() {
        return "1";
    }
}
