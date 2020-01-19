package com.project;

import java.util.ArrayList;

public class Managment implements RoleOfWorkShape {
    int id;
    public Managment(){
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getMyClass() {
        return "3";
    }
}
