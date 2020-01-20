package com.project;

public class Grader implements RoleOfWorkShape {
   private int id;
   public Grader(){
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getMyClass() {
        return "2";
    }
}
