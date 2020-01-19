package com.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.omg.CORBA.Request;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSave implements Serializable {
    private ArrayList<Person> persons = new ArrayList<Person>();
    private ArrayList<Workshop> workshops = new ArrayList<Workshop>();
    private ArrayList<HoldWorkShop> holdWorkShops = new ArrayList<HoldWorkShop>();
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<Requests> requests = new ArrayList<Requests>();
    private ArrayList<Requirments> requirments = new ArrayList<Requirments>();
    private ArrayList<GroupStatus> groupStatuses = new ArrayList<GroupStatus>();
    private ArrayList<Form> forms = new ArrayList<Form>();
    private ArrayList<AbsForm> absForms = new ArrayList<AbsForm>();
    private ArrayList<Qualifition> qualifitionArrayList = new ArrayList<Qualifition>();
    public DataSave(){
        if (SaveFIle.loadFromFileArrayList("personsArrayList.ser") != null)
            persons = SaveFIle.loadFromFileArrayList("personsArrayList.ser");
        if(SaveFIle.loadFromFileArrayList("workshopsArrayList.ser") != null)
            workshops = SaveFIle.loadFromFileArrayList("workshopsArrayList.ser");
        if (SaveFIle.loadFromFileArrayList("holdWorkShopsArrayList.ser") != null)
            holdWorkShops = SaveFIle.loadFromFileArrayList("holdWorkShopsArrayList.ser");
        if (SaveFIle.loadFromFileArrayList("groupsArrayList.ser") != null)
            groups = SaveFIle.loadFromFileArrayList("groupsArrayList.ser");
        if(SaveFIle.loadFromFileArrayList("requestsArrayList.ser") != null)
            requests = SaveFIle.loadFromFileArrayList("requestsArrayList.ser");
        if(SaveFIle.loadFromFileArrayList("requirmentsArrayList.ser") != null)
            requirments = SaveFIle.loadFromFileArrayList("requirmentsArrayList.ser") ;
        if (SaveFIle.loadFromFileArrayList("groupStatus.ser") != null)
            groupStatuses = SaveFIle.loadFromFileArrayList("groupStatus.ser");
        if (SaveFIle.loadFromFileArrayList("qualifitionArrayList.ser") != null)
            qualifitionArrayList = SaveFIle.loadFromFileArrayList("qualifitionArrayList.ser");
        if (SaveFIle.loadFromFileArrayList("Form.ser") != null)
            forms = SaveFIle.loadFromFileArrayList("Form.ser");
        if (SaveFIle.loadFromFileArrayList("absForms.ser") != null)
            absForms = SaveFIle.loadFromFileArrayList("absForms.ser");

    }
    public void saveInFile(){
            SaveFIle.saveArrayListInFile("personsArrayList.ser",persons);
            SaveFIle.saveArrayListInFile("workshopsArrayList.ser",workshops);
            SaveFIle.saveArrayListInFile("holdWorkShopsArrayList.ser",holdWorkShops);
            SaveFIle.saveArrayListInFile("groupsArrayList.ser",groups);
            SaveFIle.saveArrayListInFile("requestsArrayList.ser",requests);
            SaveFIle.saveArrayListInFile("requirmentsArrayList.ser",requirments); ;
            SaveFIle.saveArrayListInFile("groupStatus.ser",groupStatuses);
            SaveFIle.saveArrayListInFile("qualifitionArrayList.ser",qualifitionArrayList);
            SaveFIle.saveArrayListInFile("Form.ser",forms);
            SaveFIle.saveArrayListInFile("absForms.ser",absForms);

    }
    public ArrayList<RequestGreater>getAllGreaterRequestThatThisPersonSend(int id){
        RequestGreater requestGreater = new RequestGreater();
        ArrayList<RequestGreater> allOfThem = new ArrayList<RequestGreater>();
        for (Requests i : this.requests){
            if(i instanceof RequestGreater){
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
            if(i instanceof RequestStudent){
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
            if (i instanceof RequestStudent){
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
                if(s instanceof Greater){
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
                if(s instanceof Student){
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
            id = Integer.getInteger((String) i);
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


    public Person findPersonIndataBase2(String user, String email) {
        for(Person i : persons){
            if(i.getUser().equals(user) && i.getEmailAddress().equals(email)){
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

    public boolean AddNewGroupTOdatabase(Group group) {
        for (Group i : groups){
            if(i.getHoldWorkShop().getId() == group.getHoldWorkShop().getId() && i.getName().equals(group.getName()))
                return false;
        }
        this.groups.add(group);
        return true;
    }

    public Group getOneGroupFrommDataBase(int numberGroup, String groupName, int numberIdWorkShop) {
        for (Group i :groups){
            if(i.getHoldWorkShop().getId() == numberIdWorkShop  && i.getNumber() == numberGroup && i.getName().equals(groupName)){
                return i;
            }
        }
        return null;
    }

    public RequestGreater getOneRequestGreater(Integer requestGreaterId) {
        RequestGreater requestGreater = null;
        for(Requests i : this.requests){
            if(i instanceof RequestGreater){
                requestGreater = (RequestGreater) i;
                if(requestGreater.getId() == requestGreaterId)
                    return requestGreater;
            }
        }
        return null;
    }

    public boolean AddNewPersonTodataBase(Person newPerson) {
            for(Person i : persons){
                if(i.getUser().equals(newPerson.getUser()))
                    return false;
            }
            newPerson.setId(persons.size()+1);
            newPerson.setIs_Active(true);
            persons.add(newPerson);
            return true;
    }

    public HoldWorkShop findThisHoldWorkShop(int id) {
        for(HoldWorkShop i : holdWorkShops){
            if(i.getId() == id){
                return i;
            }
        }
        return null;
    }


    public Person findPersonOfThisGreater(int id) {
        Greater greater = null;
        for(Person i : persons){
            if(i.is_this_role_in_our_person(Greater.class)){
                greater = (Greater) i.findOurType("2");
                if(greater.getId() == id)
                    return i;
            }
        }
        return null;
    }

    public ArrayList<RequestGreater> findAllRequestGreater(int id) {
        ArrayList<RequestGreater> requestGreaters = new ArrayList<RequestGreater>();
        RequestGreater requestGreater = new RequestGreater();
        for(Requests i : requests){
            if(i instanceof RequestGreater){
                    requestGreater = (RequestGreater) i;
                    if(requestGreater.getId() == id)
                        requestGreaters.add(requestGreater);

            }
        }
        return requestGreaters;
    }
    public ArrayList<RequestStudent> findAllRequestStudent(int id) {
        ArrayList<RequestStudent> requestStudents = new ArrayList<RequestStudent>();
        RequestStudent requestStudent = new RequestStudent();
        for(Requests i : requests){
            if(i instanceof RequestStudent){
                requestStudent = (RequestStudent) i;
                if(requestStudent.getId() == id)
                    requestStudents.add(requestStudent);

            }
        }
        return requestStudents;

    }

    public ArrayList<HoldWorkShop> findWorkShophaveThisManager(int id) {
        ArrayList<HoldWorkShop>holdWorkShops = new ArrayList<HoldWorkShop>();
        for(HoldWorkShop i : this.holdWorkShops){
            if (i.getManagment().id == id)
                holdWorkShops.add(i);
        }
        return holdWorkShops;
    }

    public boolean deletRequesOf(int requestID) {
        RequestStudent  requestStudent = null;
        RequestGreater  requestGreater = null;
        for(Requests i : requests){
            if(i instanceof RequestStudent){
                requestStudent = (RequestStudent) i;
                if(requestStudent.getId() == requestID) {
                    this.requests.remove(i);
                    return true;
                }
            }
            else if(i.getClass().equals(RequestGreater.class)){
                requestGreater = (RequestGreater) i;
                if(requestGreater.getId() == requestID) {
                    this.requests.remove(i);
                    return true;
                }
            }

        }
        return false;
    }


    public boolean AddToRequestListINDataBase(RequestStudent newRequestStudent) {
        RequestStudent requestStudent = new RequestStudent();
         for (Requests i : requests){
             if(i instanceof RequestStudent){
                requestStudent = (RequestStudent) i;
                if (requestStudent.student.id == requestStudent.getStudent().getId() && requestStudent.getHoldWorkShop().getId() == newRequestStudent.getHoldWorkShop().getId())
                    return false;
             }

         }
         requests.add(newRequestStudent);
         return true;
    }

    public boolean AddToRequestListINDataBase(RequestGreater newRequestGreater) {
        RequestGreater requestGreater = new RequestGreater();
        for (Requests i : requests){
            if(i instanceof RequestGreater){
                requestGreater = (RequestGreater) i;
                if (requestGreater.getGreater().getId() == newRequestGreater.getGreater().getId() && newRequestGreater.getHoldWorkShop().getId() == requestGreater.getHoldWorkShop().getId())
                    return false;
            }

        }
        requests.add(newRequestGreater);
        return true;
    }


    public ArrayList<Requests> seeAllRequestArrayList(int workShopID) {
        ArrayList<Requests> requestment = new ArrayList<Requests>();


        for(Requests i : this.requests){
            if (i.getHoldWorkShop().getId() == workShopID){
                requestment.add(i);
            }

        }
        return requestment;
     }

    public ArrayList<GroupStatus> getALLGroupStatuseINdataBaseOfThisWorkShope(int workShopId) {
        ArrayList<GroupStatus> groupStatuses = new ArrayList<GroupStatus>();
        for(GroupStatus i : this.groupStatuses){
            if (i.getGroup().getHoldWorkShop().getId() == workShopId)
                groupStatuses.add(i);
        }
        return groupStatuses;
    }

    public boolean AddNewAbCForm(AbsForm absFormOfMe) {
        absFormOfMe.setNumber(this.absForms.size());
        this.absForms.add(absFormOfMe);
        return true;
    }

    public AbsForm findAbcFormFromDataBaseById(int id_number_abcForm) {
        for (AbsForm i : this.absForms){
            if(i.getNumber() == id_number_abcForm)
                return i;
        }
        return null;
    }

    public boolean SaveFormInDataBase(Form form1) {
        this.forms.add(form1);
        return true;
    }

    public JsonObject seeAllABCformInDataBase() throws JsonProcessingException {
        JsonObject jsonObject = new JsonObject();
        ObjectMapper objectMapper = new ObjectMapper();
        int dd = 0;
        for(AbsForm i : this.absForms){
            jsonObject.put(String.valueOf(0),objectMapper.writeValueAsString(i));
        }
        return jsonObject;
    }


}
