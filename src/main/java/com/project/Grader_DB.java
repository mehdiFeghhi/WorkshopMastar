package com.project;

public class Grader_DB
{
	public int Grader_Id;
    public int role_of_workshop_id;
    public int deleted;
    public Grader_DB(int grader_id, int role_of_workshop_id, int deleted) {
        this.Grader_Id = grader_id;
        this.role_of_workshop_id = role_of_workshop_id;
        this.deleted = deleted;
    }
}
