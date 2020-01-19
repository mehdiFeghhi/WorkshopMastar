package com.project;

import java.io.Serializable;
import java.util.ArrayList;

public class Qualifition implements Serializable {
    ArrayList<String> answer;
    RoleOfWorkShape roleTiler;
    RoleOfWorkShape roleTarget;
    Form form;
    public Qualifition(ArrayList<String> answer, RoleOfWorkShape roleTiler, RoleOfWorkShape roleTarget,Form form) {
        this.answer = answer;
        this.roleTiler = roleTiler;
        this.roleTarget = roleTarget;
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public RoleOfWorkShape getRoleTiler() {
        return roleTiler;
    }

    public RoleOfWorkShape getRoleTarget() {
        return roleTarget;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public void setRoleTiler(RoleOfWorkShape roleTiler) {
        this.roleTiler = roleTiler;
    }

    public void setRoleTarget(RoleOfWorkShape roleTarget) {
        this.roleTarget = roleTarget;
    }
}
