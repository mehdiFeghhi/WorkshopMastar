package com.project;

public class Qualifition_DB
{
	public int Qualifition_Id;
    public int roleTiler_Id;
    public int roleTarget_Id;//role of workshop link

    public Qualifition_DB(int qualifition_Id, int roleTiler_Id, int roleTarget_Id) {
        Qualifition_Id = qualifition_Id;
        this.roleTiler_Id = roleTiler_Id;
        this.roleTarget_Id = roleTarget_Id;
    }
}
