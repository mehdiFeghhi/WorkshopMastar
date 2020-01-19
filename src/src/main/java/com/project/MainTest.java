package com.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) throws Exception {
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.start();
//        DataSave dataSave = new DataSave();
//        Person firstAdmin = new Person();
//        firstAdmin.setGender(Gender.male);
//        firstAdmin.setNationalCode("1850427933");
//        firstAdmin.setEmailAddress("mf1378mf@yahoo.com");
//        firstAdmin.setName("mehdi");
//        firstAdmin.setLastName("Feghhi");
//        firstAdmin.setUser("mf1378mf");
//        firstAdmin.setPass("come_on");
//        Addmin addmin = new Addmin("mf1378mf");
//        firstAdmin.addToArrayListOfRole(addmin);
//        addToPerson(firstAdmin,dataSave);
//        Scanner myobject = new Scanner(System.in);
//        String input;
//        int input2;
//        boolean contunie = true;
//        boolean a = true;
//        while (a) {
//        System.out.println("What do you want ? ((enter your number)) ");
//        System.out.println("1) Login ");
//        System.out.println("2) sign up ");
//        System.out.println("3) login as Admin ");
//        System.out.println("0) exit");
//        input2 = myobject.nextInt();
//            boolean b = true;
//            switch (input2) {
//                case 1:
//                    while (b) {
//                        System.out.println("Please Enter your user : ");
//                        String user = myobject.next();
//                        System.out.println("Please Enter your password : ");
//                        String password = myobject.next();
//                        boolean s = check(user,password,dataSave);
//                        if (s == false){
//                            System.out.println("your input is Wrong ");
//                            System.out.println("What do you want ?");
//                            System.out.println("1)stay");
//                            System.out.println("2)exit");
//                            input2 = myobject.nextInt();
//                            if (input2 == 2)
//                                break;
//                        }
//                        else {
//                            boolean d = true;
//                            Person personLogin = findPerson(user,dataSave);
//                            while (d) {
//                                System.out.println("What do you want ?");
//                                System.out.println("1)see your course you spend ");
//                                System.out.println("2)see course you can get ");
//                                System.out.println("3)see course you do still ");
//                                System.out.println("4)see all course you was it's greater ");
//                                System.out.println("5)see course you was it's greater new ");
//                                System.out.println("6)see your request");
//                                System.out.println("7)Request for workShape ");
//                                System.out.println("8)Request for greater of workShape ");
//                                System.out.println("9)edit your profile ");
//                                System.out.println("0)Exit");
//                                System.out.println();
//                            }
//                        }
//
//                    }
//                    break;
//                case 2:
//                    boolean fire = false;
//                    while (!fire) {
//                            System.out.println("please enter your user_name :");
//                            String firstUser = myobject.next();
//                            fire = if_username_valid(firstUser,dataSave);
//                            if (!fire)
//                                System.out.println("your user_name is wrong ");
//                        }
//                        Person person = new Person();
//                        System.out.println("please enter your password : ");
//                        person.setPass(myobject.next());
//                        System.out.println("Please enter your name : ");
//                        person.setName(myobject.next());
//                        System.out.println("Please enter your last name : ");
//                        person.setLastName(myobject.next());
//                        System.out.println("Please enter your email : ");
//                        person.setEmailAddress(myobject.next());
//                        System.out.println("Please enter your national code : ");
//                        person.setNationalCode(myobject.next());
//                        boolean genderLoop = true;
//                        Gender gender;
//                        while (genderLoop) {
//                            System.out.println("Please enter your Gender male(1)/female(2): ");
//                            int typeicalyGender = myobject.nextInt();
//                            if (typeicalyGender == 1) {
//                                person.setGender(Gender.male);
//                                genderLoop = false;
//                            }
//                            else if (typeicalyGender == 2){
//                                person.setGender(Gender.female);
//                                genderLoop = false;
//                            }
//
//                        }
//                        dataSave.persons.add(person);
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        String jsonString = objectMapper.writeValueAsString(person);
//                        JsonNode jsonNode = objectMapper.readTree(jsonString);
//                        System.out.println("you add to server success");
//                    break;
//                case 3:
//                    boolean wh = true;
//                    while(wh){
//                        System.out.println("Please Enter UsernameOFAdmin : ");
//                        String adminUser = myobject.next();
//                        System.out.println("Please Enter Password : ");
//                        String passWord = myobject.next();
//                        boolean hast = checkAdmin(adminUser,passWord,dataSave);
//                        if (hast == false){
//                            System.out.println("your input is Wrong ");
//                            System.out.println("What do you want ?");
//                            System.out.println("1)stay");
//                            System.out.println("2)exit");
//                            System.out.println();
//                            input2 = myobject.nextInt();
//                            if (input2 == 2)
//                                break;
//                        }
//                        else {
//                            boolean doOurWork = true;
//                            Person admin  = findPerson(adminUser,dataSave);
//                            while(doOurWork){
//                                System.out.println("What do you want ? ");
//                                System.out.println("1)make new workShape ");
//                                System.out.println("2)add new admin");
//                                System.out.println("0)exit");
//
//                            }
//
//                        }
//                    }
//                    break;
//                default:
//                    System.out.println("your out put is wrong");
//            }
//        }
//
//    }
//
//    private static boolean checkAdmin(String adminUser, String passWord, DataSave dataSave) {
//        for (Person i : dataSave.persons)
//            if (i.getUser().equals(adminUser) && i.getUser().equals(passWord)){
//                if (i.is_this_role_in_our_person(Type.Admin))
//                    return true;
//                return false;
//            }
//        return false;
//    }
//
//    private static boolean addToPerson(Person person, DataSave dataSave) {
//
//        for (Person i : dataSave.persons){
//            if(i.getUser().equals(person.getUser()))
//                return false;
//        }
//        dataSave.persons.add(person);
//        return true;
//    }
//
//
//    private static Person findPerson(String user,DataSave dataSave) {
//            for (Person i : dataSave.persons){
//                if (i.getUser().equals(user)){
//                    return i;
//                }
//            }
//            return null;
//    }
//
//    private static boolean if_username_valid(String user, DataSave dataSave) {
//        for (Person i : dataSave.persons){
//            if ((i.getUser()).equals(user))
//                return false;
//        }
//        return true;
//    }
//
//    private static boolean check(String user, String password,DataSave dataSave) {
//        for (Person i : dataSave.persons) {
//            if (i.getUser().equals(user) && i.getPass().equals(password))
//                return true;
//            else
//                return false;
//        }
//        return false;
//
//    }

    }
}
