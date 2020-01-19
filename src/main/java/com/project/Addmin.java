package com.project;

public class Addmin implements RoleOfWorkShape {
    private int Id;
    private AdminType adminType;
    public Addmin(AdminType addmin){
        this.adminType = addmin;
    }

    public Addmin() {
        this.adminType = AdminType.Soldier;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public AdminType getAdminType() {
        return adminType;
    }

    @Override
    public String getMyClass() {
        return "0";
    }
}
