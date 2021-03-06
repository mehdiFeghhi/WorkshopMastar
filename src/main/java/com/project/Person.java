package com.project;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
enum Gender {
        male,female;
        }
public class Person  implements Serializable {
    int id;
    Gender gender ;
    private String name ;
    private String lastName;
    private String date_birthday;
    private String user;
    private String pass;
    private String tozihat;
    private String nationalCode;
    private String phoneNumber;
    private String emailAddress;
    private boolean is_Active;
    private ArrayList<RoleOfWorkShape> roleOfWorkShapes = new ArrayList<RoleOfWorkShape>() ;
    private Image image;

    public Person() {
        this.is_Active = true;
    }

    public Person(String name, String lastName, String date_birthday, String user, String pass,String nationalCode, String phoneNumber, String emailAddress) {
        this.name = name;
        this.lastName = lastName;
        this.date_birthday = date_birthday;
        this.user = user;
        this.pass = pass;
        this.nationalCode = nationalCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.is_Active = true;
        this.gender = Gender.male;
    }

    public Person(String name, String lastName, String date_birthday, String user,String nationalCode, String phoneNumber, String emailAddress) {
        this.name = name;
        this.lastName = lastName;
        this.date_birthday = date_birthday;
        this.user = user;
        this.nationalCode = nationalCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.is_Active = true;
        this.gender = Gender.male;
    }

    public Person(String user, String email) {
        this.user = user;
        this.emailAddress = email;
    }

    public Person clone(){
        Person person = new Person(this.getName(),this.getLastName(),this.date_birthday,this.getUser(),this.nationalCode,this.getPhoneNumber(),this.getEmailAddress());
        person.setId(this.id);
        person.setGender(this.getGender());
        person.setTozihat(this.getTozihat());
        return person;
    }
    public void setRoleOfWorkShapes(ArrayList<RoleOfWorkShape> roleOfWorkShapes) {
        this.roleOfWorkShapes = roleOfWorkShapes;
    }

    public boolean getIs_Active() {
        return is_Active;
    }
    public void setIs_Active(boolean is_Active) {
        this.is_Active = is_Active;
    }

    public ArrayList<RoleOfWorkShape> getRoleOfWorkShapes() {
        return roleOfWorkShapes;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate_birthday() {
        return date_birthday;
    }

    public void setDate_birthday(String date_birthday) {
        this.date_birthday = date_birthday;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getTozihat() {
        return tozihat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTozihat(String tozihat) {
        this.tozihat = tozihat;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public RoleOfWorkShape findOurType(String object){

        for (RoleOfWorkShape i : roleOfWorkShapes) {
            // if (i.getClass().equals(object.getClass()))
            //     return i;
            if (object.equals("1") && i.getMyClass().equals("1"))
                return i;
            else if (object.equals("2") && i.getMyClass().equals("2"))
                return i;
            else if (object.equals("3") && i.getMyClass().equals("3"))
                return  i;
            else if (object.equals("0") && i.getMyClass().equals("0"))
                return i;
        }
        if (object.equals("1")){
            Student student = new Student();
            student.setId(this.id);
            this.roleOfWorkShapes.add(student);

            return student;

        }
        else if (object.equals("2")){
            Grader grader = new Grader();
            grader.setId(this.id);
            this.roleOfWorkShapes.add(grader);
            return grader;
        }
        else if (object.equals("3")){
            Managment whoMakeWorkShape = new Managment();
            whoMakeWorkShape.setId(this.id);
            this.roleOfWorkShapes.add(whoMakeWorkShape);
            return whoMakeWorkShape;
        }
        else
            return null;
    }
    /////////////////////////////////////////////////////////////////////////////////////

















    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean is_this_role_in_our_person(Grader grader){
        for (RoleOfWorkShape i : roleOfWorkShapes)
            if (i instanceof Grader)
                return true;
        return false;
    }
    public boolean is_this_role_in_our_person(Student student){
        for (RoleOfWorkShape i : roleOfWorkShapes)
            if (i instanceof Student)
                return true;
        return false;
    }
    public boolean is_this_role_in_our_person(Addmin addmin){
        for (RoleOfWorkShape i : roleOfWorkShapes)
            if (i instanceof Addmin)
                return true;
        return false;
    }
    public boolean is_this_role_in_our_person(Managment managment){
        for (RoleOfWorkShape i : roleOfWorkShapes)
            if (i instanceof Managment)
                return true;
        return false;
    }
    public boolean addToArrayListOfRole(RoleOfWorkShape roleOfWorkShape){
        for (RoleOfWorkShape i : this.roleOfWorkShapes)
            if (i.getClass().equals(roleOfWorkShape.getClass()))
                return false;
        this.roleOfWorkShapes.add(roleOfWorkShape);

        return true;
    }
    @Override
    public String toString() {
        return this.name + " // " + this.lastName + " // " + this.emailAddress;
    }

}
