package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupStatus implements Serializable {
    private int id;
    private Group group;
    private RoleOfWorkShape roleOfWorkShape;
    private ArrayList<Boolean> attendance ;

    public GroupStatus(Group group, RoleOfWorkShape roleOfWorkShape) {
        this.group = group;
        this.roleOfWorkShape = roleOfWorkShape;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public RoleOfWorkShape getRoleOfWorkShape() {
        return roleOfWorkShape;
    }

    public ArrayList<Boolean> getAttendance() {
        return attendance;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setRoleOfWorkShape(RoleOfWorkShape roleOfWorkShape) {
        this.roleOfWorkShape = roleOfWorkShape;
    }

    public void setAttendance(ArrayList<Boolean> attendance) {
        this.attendance = attendance;
    }

}
