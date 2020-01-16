package com.project;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class DataSave {
    private ArrayList<Person> persons = new ArrayList<Person>();
    private ArrayList<Workshop> workshops = new ArrayList<Workshop>();
    private ArrayList<HoldWorkShop> holdWorkShops = new ArrayList<HoldWorkShop>();
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<Requests> requests = new ArrayList<Requests>();
    private ArrayList<Requirments> requirments = new ArrayList<Requirments>();
    private ArrayList<GroupStatus> groupStatuses = new ArrayList<GroupStatus>();
    public ArrayList<RequestGreater>getAllGreaterRequestThatThisPersonSend(int id){
        RequestGreater requestGreater = new RequestGreater();
        ArrayList<RequestGreater> allOfThem = new ArrayList<RequestGreater>();
        for (Requests i : this.requests){
            if(i.getClass().equals(RequestGreater.class) ){
                requestGreater = (RequestGreater) i;
                if (requestGreater.getGreater().getId() == id)
                    allOfThem.add(requestGreater);
            }

        }
        return allOfThem;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(ArrayList<Workshop> workshops) {
        this.workshops = workshops;
    }

    public ArrayList<HoldWorkShop> getHoldWorkShops() {
        return holdWorkShops;
    }

    public void setHoldWorkShops(ArrayList<HoldWorkShop> holdWorkShops) {
        this.holdWorkShops = holdWorkShops;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Requests> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Requests> requests) {
        this.requests = requests;
    }

    public ArrayList<Requirments> getRequirments() {
        return requirments;
    }

    public void setRequirments(ArrayList<Requirments> requirments) {
        this.requirments = requirments;
    }

    public ArrayList<RequestStudent> getAllStudentRequestThatThisPersonSend(int id) {

        RequestStudent requestStudent = new RequestStudent();
        ArrayList<RequestStudent> allOfThem = new ArrayList<RequestStudent>();
        for (Requests i : this.requests){
            if(i.getClass().equals(RequestStudent.class) ){
                requestStudent = (RequestStudent) i;
                if (requestStudent.getStudent().getId() == id)
                    allOfThem.add(requestStudent);
            }

        }
        return allOfThem;
    }

    public ArrayList<Workshop> findALLworkShopThatThisPersonSpend(int id) {
        ArrayList<Workshop> workshops = new ArrayList<Workshop>();
        ArrayList<RequestStudent> requestStudents = this.getAllStudentRequestThatThisPersonSend(id);
        for (RequestStudent i : requestStudents){
            if (i.getAccetply().equals(Accetply.Accept)){
                workshops.add(i.getHoldWorkShop().getWorkshop());
            }
        }
        return workshops;

    }

    public ArrayList<Workshop> findALLworkShopThatPrerequisiteWithThisWorkshop(int id) {
        ArrayList<Workshop> workshops = new ArrayList<Workshop>();
        for (Requirments i : this.requirments){
            if(i.getMainWorkshop().getId() == id){
                workshops.add(i.getWorkShopNeed());
            }
        }
        return workshops;
    }

    public boolean isthisMangmentOfTHisWorkShop(int id, int idWorkShop) {
        for (HoldWorkShop i : this.holdWorkShops){
            if(i.getId() == idWorkShop && i.getManagment().id == id)
                return true;
        }
        return false;
    }

    public void updateThisRequestInDataBase(RequestStudent requestStudent) {
        requestStudent.setId(this.requests.size()+1);
        this.requests.add(requestStudent);
    }

    public JsonObject seeAllRequestStudent(int workShopId) {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        Person person = null;
        int n = 0;
        RequestStudent requestStudent = new RequestStudent();
        for(Requests i : this.requests){
            if (i.getClass().equals(RequestStudent.class)){
                requestStudent  = (RequestStudent)i;
                if(requestStudent.getHoldWorkShop().getId() == workShopId)
                    person = findPersonOfThisStudent(requestStudent.getStudent().getId());
                    jsonObject2.put("getMassage",requestStudent.getMassage())
                                .put("getAcceptly",requestStudent.getAccetply().toString())
                                .put("getRequestId",requestStudent.getId())
                                .put("getIdStudent",requestStudent.getStudent().getId())
                                .put("getName",person.getName())
                                .put("getLastName",person.getLastName())
                                .put("getGender",person.getGender())
                                .put("getUser",person.getUser());
                    jsonObject1.put(String.valueOf(n),jsonObject2);
                    jsonObject2.clear();
                    n++;
            }

        }
        return jsonObject.put(String.valueOf(workShopId),jsonObject1);
    }

    public Person findPersonOfThisManagment(int id) {
        Greater greater = null;
        for (Person i : persons){
            for(RoleOfWorkShape s : i.getRoleOfWorkShapes()){
                if(s.getClass().equals(Greater.class)){
                    greater = (Greater) s;
                    if(greater.getId() == id)
                        return i;

                }
            }
        }
        return null;
    }

    public Person findPersonOfThisStudent(int id) {
        Student student = null;
        for (Person i : persons){
            for(RoleOfWorkShape s : i.getRoleOfWorkShapes()){
                if(s.getClass().equals(Student.class)){
                    student = (Student) s;
                    if(student.getId() == id)
                        return i;

                }
            }
        }
        return null;
    }

    public Person finPersonINdataBase(String user, String pass) {
        for (Person i : this.persons){
            if(i.getUser().equals(user) && i.getPass().equals(pass))
                return i;
        }
        return null;
    }

    public boolean addNewHoldWorkShop(HoldWorkShop holdWorkShop) {
        for (HoldWorkShop i : holdWorkShops){
            if(i.getId() == holdWorkShop.getId())
                return false;
        }
        holdWorkShop.setId(holdWorkShops.size()+1);
        holdWorkShops.add(holdWorkShop);
        return true;
    }

    public boolean AddNewWorkShopTOdataBase(Workshop workshop) {
        for(Workshop i : workshops){
            if (i.getId() == workshop.getId() || i.getTitle().equals(workshop.getTitle()))
                return false;
        }
        workshop.setId(workshops.size()+1);
        workshops.add(workshop);
        return true;
    }

    public void AddNewReqirmentsToDataBase(Requirments requirments) {
        this.requirments.add(requirments);
    }

    public ArrayList<Workshop> findThisWorkShop(JsonArray workShopPrerequisite) {
        int id ;
        ArrayList<Workshop> workshops = new ArrayList<Workshop>();
        for (Object i : workShopPrerequisite){
             id = (int)i;
            for (Workshop s : workshops){
                if (s.getId() == id) {
                    workshops.add(s);
                    break;
                }

            }
        }
        return workshops;
    }

    public RequestStudent getOneRequestStudent(int requestStudentId) {
        RequestStudent requestStudent = new RequestStudent();
        for (Requests i : requests){
            if (i.getClass().equals(RequestStudent.class)){
                requestStudent = (RequestStudent)i;
                if (requestStudent.getId() == requestStudentId)
                    return requestStudent;

            }
        }
        return null;
    }

    public void AddNewGroupStatusToDatabase(GroupStatus groupStatus) {
        for (GroupStatus i : groupStatuses){
            if(i.getRoleOfWorkShape().equals(groupStatus.getRoleOfWorkShape()) && i.getGroup().equals(groupStatus.getGroup()))
                return;
        }
        this.groupStatuses.add(groupStatus);
    }


    public Person findPersonIndataBase2(String user, String pass) {
        for(Person i : persons){
            if(i.getUser().equals(user) && i.getEmailAddress().equals(pass)){
                    return i;
            }
        }
        return null;
    }

    public Person findPersonByUser(String user) {
        for (Person i :persons){
            if (i.getUser().equals(user))
                return i;
        }
        return null;
    }

    public ArrayList<GroupStatus> getALLGroupStatuseINdataBase() {
        return groupStatuses;
    }

    public ArrayList<Group> getALLGroupINdataBase() {
        return this.groups;
    }


    public boolean searchInDataBase(String user) {
        for (Person i : persons)
            if (i.getUser().equals(user))
                return true;
        return false;
    }

    public void updateInPersonINdataBase(Person person) {
        for(Person i : persons){
            if(i.getUser().equals(person.getUser())){
                if(person.getEmailAddress()!= null)
                    i.setEmailAddress(person.getEmailAddress());
                if(person.getPass()!= null)
                    i.setPass(person.getPass());
                if(person.getNationalCode()!= null)
                    i.setNationalCode(person.getNationalCode());
                if(person.getLastName() != null)
                    i.setLastName(person.getLastName());
                if(person.getName() != null)
                    i.setLastName(person.getLastName());
                if(person.getGender() != null)
                    i.setGender(person.getGender());
                if(person.getPhoneNumber() != null)
                    i.setPhoneNumber(person.getPhoneNumber());
                if (person.getDate_birthday() != null)
                    i.setDate_birthday(person.getDate_birthday());
            }
        }
    }
}
