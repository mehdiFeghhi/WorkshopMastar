package com.project;

public class Management_DB
{
	public int Management_Id;

	public Management_DB(int management_Id) {
		Management_Id = management_Id;
	}
	public Managment makeManagment(){
		Managment managment = new Managment();
		managment.setId(this.Management_Id);
		return managment;
	}
}
