package com.project;

public class Qualification_Answer_DB
{
	public int Qualification_Answer_id;
	public int Qualifition_Id;

	public String answer;

	public Qualification_Answer_DB(int qualification_Answer_id, int qualifition_Id, String answer) {
		Qualification_Answer_id = qualification_Answer_id;
		Qualifition_Id = qualifition_Id;
		this.answer = answer;
	}
}
