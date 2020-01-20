package com.project;

public class Group_Status_Attendence_DB
{
	public int GroupStatus_id;
	public int Group_Status_Attendence_Id;
	public int attendence; // that bool thingy

	public Group_Status_Attendence_DB(int groupStatus_id, int group_Status_Attendence_Id, int attendence) {
		GroupStatus_id = groupStatus_id;
		Group_Status_Attendence_Id = group_Status_Attendence_Id;
		this.attendence = attendence;
	}
}
