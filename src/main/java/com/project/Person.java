package com.project;

import java.awt.*;
import java.util.ArrayList;
enum Gender {
        male,female;
        }
public class Person  {
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
    private ArrayList<RoleOfWorkShape> roleOfWorkShapes = new ArrayList<RoleOfWorkShape>() ;
    private Image image;

    public Person() {
    }

    public void setRoleOfWorkShapes(ArrayList<RoleOfWorkShape> roleOfWorkShapes) {
        this.roleOfWorkShapes = roleOfWorkShapes;
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
    public RoleOfWorkShape findOurType(Object object){

        for (RoleOfWorkShape i : roleOfWorkShapes){
            if (i.getClass() == object.getClass())
                return i;
        }
        if (Student.class == object.getClass()){
            Student student = new Student();
            this.roleOfWorkShapes.add(student);
            return student;

        }
        else if (object.getClass() == Greater.class){
            Greater greater = new Greater();
            this.roleOfWorkShapes.add(greater);
            return greater;
        }
        else if (object.getClass() == Managment.class){
            Managment whoMakeWorkShape = new Managment();
            this.roleOfWorkShapes.add(whoMakeWorkShape);
            return whoMakeWorkShape;
        }
        else
            return null;
    }
    public boolean is_this_role_in_our_person(Object object){
        for (RoleOfWorkShape i : roleOfWorkShapes)
            if (i.getClass() == object.getClass())
                return true;
        return false;
    }
    public boolean addToArrayListOfRole(RoleOfWorkShape roleOfWorkShape){
        for (RoleOfWorkShape i : this.roleOfWorkShapes)
            if (i.getClass() == roleOfWorkShape.getClass())
                return false;
        this.roleOfWorkShapes.add(roleOfWorkShape);
        return true;
    }
    @Override
    public String toString() {
        return this.name + " // " + this.lastName + " // " + this.emailAddress;
    }

}
