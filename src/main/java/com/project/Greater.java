package com.project;

public class Greater implements RoleOfWorkShape{
   private String  user;
   public Greater(String user){
       this.user = user;
   }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
