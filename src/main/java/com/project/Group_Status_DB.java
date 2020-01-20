package com.project;

public class Group_Status_DB
{
    public int Group_Status_id;
    public int Group_Id;
    public int RoleOfWorkshape_Id;

    public Group_Status_DB(int group_Status_id, int group_Id, int roleOfWorkshape_Id) {
        Group_Status_id = group_Status_id;
        Group_Id = group_Id;
        RoleOfWorkshape_Id = roleOfWorkshape_Id;
    }
}
