package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupStatus implements Serializable {
    private int id;
    private double statues;
    private GroupG groupG;
    private RoleOfWorkShape roleOfWorkShape;
    private ArrayList<Boolean> attendance ;
    public GroupStatus(GroupG groupG, RoleOfWorkShape roleOfWorkShape) {
        this.groupG = groupG;
        this.roleOfWorkShape = roleOfWorkShape;
        this.statues = -1;
    }

    public double getStatues() {
        return statues;
    }

    public void setStatues(double statues) {
        this.statues = statues;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupG getGroupG() {
        return groupG;
    }

    public RoleOfWorkShape getRoleOfWorkShape() {
        return roleOfWorkShape;
    }

    public ArrayList<Boolean> getAttendance() {
        return attendance;
    }

    public void setGroupG(GroupG groupG) {
        this.groupG = groupG;
    }

    public void setRoleOfWorkShape(RoleOfWorkShape roleOfWorkShape) {
        this.roleOfWorkShape = roleOfWorkShape;
    }

    public void setAttendance(ArrayList<Boolean> attendance) {
        this.attendance = attendance;
    }

}
