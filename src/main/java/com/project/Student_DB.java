package com.project;

public class Student_DB
{
	public int student_ID;
	public int Rol_of_WorkShop_ID;

	public Student_DB(int student_ID, int rol_of_WorkShop_ID) {
		this.student_ID = student_ID;
		Rol_of_WorkShop_ID = rol_of_WorkShop_ID;
	}
	public Student makeStudent(){
		Student student = new Student();
		student.setId(this.student_ID);
		return student;
	}

}
