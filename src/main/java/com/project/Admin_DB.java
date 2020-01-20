package com.project;

public class Admin_DB
{
	public int Admin_Id;
    public int adminType;
    public int deleted;

    public Admin_DB(int admin_id, int admin_Id, int adminType, int deleted) {
        Admin_Id = admin_Id;
        this.adminType = adminType;
        this.deleted = deleted;
    }

    public Addmin makeAddmin(){
        Addmin addmin = new Addmin();
        if (this.adminType == 1){
            addmin.setAdminType(AdminType.General);
        }
        else if (this.adminType == 2){
            addmin.setAdminType(AdminType.Soldier);
        }
        addmin.setId(this.Admin_Id);
        return addmin;
    }
}