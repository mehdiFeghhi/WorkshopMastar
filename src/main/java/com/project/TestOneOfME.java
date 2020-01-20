package com.project;

public class TestOneOfME {
    public static void main(String[] args){
        Person person = new Person();
        Managment managment = (Managment) person.findOurType("3");
        if (managment == null){
            System.out.println("yes Ok");
        }
        if (person.is_this_role_in_our_person(new Addmin())){
            System.out.println("\nyes");
        }
        if (person.is_this_role_in_our_person(new Student())){
            System.out.println("Ok");
        }
        if (person.is_this_role_in_our_person(new Managment())){
            System.out.println("mehdi ok it ");
        }


    }
}
