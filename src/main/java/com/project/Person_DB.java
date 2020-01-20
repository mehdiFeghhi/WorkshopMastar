package com.project;

public class Person_DB
{
	public int Person_Id;
	public int gender ;
	public String name ;
	public String lastName;
	public String date_birthday;
	public String user;
	public String pass;
	public String tozihat;
	public String nationalCode;
    public String phoneNumber;
    public String emailAddress;
    public int is_Active;

	public Person_DB(int person_Id, int gender, String name, String lastName, String date_birthday, String user, String pass, String tozihat, String nationalCode, String phoneNumber, String emailAddress, int is_Active) {
		Person_Id = person_Id;
		this.gender = gender;
		this.name = name;
		this.lastName = lastName;
		this.date_birthday = date_birthday;
		this.user = user;
		this.pass = pass;
		this.tozihat = tozihat;
		this.nationalCode = nationalCode;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.is_Active = is_Active;
	}
	public Person makePerson(){
		DataBaseConnection dataBaseConnection = new DataBaseConnection();
		Person person = new Person();
		person.setPass(this.pass);
		if (this.is_Active == 1) {
			person.setIs_Active(true);
		}
		else if (this.is_Active == 0){
			person.setIs_Active(false);
		}
		person.setUser(this.user);
		person.setId(this.Person_Id);
		person.setDate_birthday(this.date_birthday);
		if(this.gender == 1){
			person.setGender(Gender.male);
		}
		else if(this.gender == 0){
			person.setGender(Gender.female);
		}
		////find it's role of this person
		person.setPhoneNumber(this.phoneNumber);
		person.setNationalCode(this.nationalCode);
		person.setLastName(this.lastName);
		person.setName(this.name);
		person.setEmailAddress(this.emailAddress);
		return person;
	}

}
