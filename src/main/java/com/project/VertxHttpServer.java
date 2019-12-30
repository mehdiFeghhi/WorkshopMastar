package com.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.util.*;


public class VertxHttpServer extends AbstractVerticle {
    String user = null;
    String pass = null;
    String email = null;
    Boolean State;
    Person newPerson = null;
    Workshop newWorkshop = null;
    HoldWorkShop newHoldWorkShop = null;
    RequestStudent newRequestStudent = null;
    RequestGreater newRequestGreater = null;
//    JsonObject jsonMongo = new JsonObject().put("host","127.0.0.1")
//            .put("port","27017")
//            .put("username","mehdi")
//            .put("password","3339539")
//            .put("db_name","Workshopes");
    ArrayList<Person> persons  = new ArrayList<Person>();
    Map<String,Person> mapLogin = new HashMap<String,Person>();
    Map<String,Date> mapValidtionCode = new HashMap<>();
    @Override
    public void start() throws Exception {
        Vertx vertx = Vertx.vertx() ;
       // MongoClient client = MongoClient.createShared(vertx,jsonMongo) ;
       // MongoDb MyDataBase = new MongoDb(client);
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/loggine").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            this.user = json.getString("user");
            this.pass = json.getString("pass");
            System.out.println(json.toString());
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
//            MyDataBase.findThisPerson(user,pass,res ->{
//                try {
//                    newPerson = makePerson(res.result());
//                } catch (IOException e) {
//                   response.end("{\"status\":3}");
//                    e.printStackTrace();
//                }
//            });
        newPerson = findPersonIndatabase(this.user,this.pass);
        if (newPerson != null){
                JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm("HS256")
                                .setPublicKey("keyboard cat")
                                .setSymmetric(true)));

                String token = provider.generateToken(new JsonObject().put("userName",user));
                mapLogin.put(token,newPerson);
                response.end("{\"status\": 1 \"validation\": "+token+"}");
            }
            else
                response.end("{\"status\":0}");
        });

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.GET, "/logOut").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();

            System.out.println("ya ali");
            String token = json.getString("token");
            System.out.println(json.toString());
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            if (mapLogin.containsKey(token)) {
                updateInPersonINdataBase(mapLogin.get(token));
                mapLogin.remove(token);
                response.end("{\"status\":1}");
            }
            else {
                response.end("{\"status\":0}");
            }

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/editPerson").handler(rc ->{
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type","application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            if (mapLogin.containsKey(json.getString("token"))){
                newPerson = null;
                try {
                    newPerson = objectMapper.readValue(json.getJsonObject("person").toString(), Person.class);
                    if (this.searchInDataBase(newPerson.getUser())) {
                        updateInPersonINdataBase(newPerson);
                        response.end("{\"status\":1}");
                    } else
                        response.end("{\"status\":0}");
                } catch (IOException e) {
                    e.printStackTrace();
                    response.end("{\"status\":0}");
                }
            }else
                response.end("{\"status\":0}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/requestStudent").handler(rc ->{
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type","application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String token = json.getString("token");
            String massage = json.getString("massage");
            if (mapLogin.containsKey(token))
                 newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            String pay = json.getString("pay");
            int id = json.getInteger("workShopHandler");
            newHoldWorkShop = findThisHoldWorkShop(id);
            if (newHoldWorkShop == null)
                response.end("{\"status\":0}");
                Student student = (Student) newPerson.findOurType(Student.class);
                if (!this.isThisPersonIsInOneOfTheGroupOfThisWorkShop(newHoldWorkShop,student.getId())) {
                    if (pay.equals("2")) {
                        if (!newHoldWorkShop.getIs_installment())
                            response.end("{\"status\":3}");
                    }
                    if (pay.equals("2")){
                        Installment payment = new Installment(newHoldWorkShop.getMoney(),newHoldWorkShop.getPayMoneyInHowTimes());
                        newRequestStudent = new RequestStudent(massage,newHoldWorkShop,(Student) newPerson.findOurType(new Student()),payment);
                    }
                    else {
                        Pay payment = new Pay(newHoldWorkShop.getMoney(),newHoldWorkShop.getIs_installment());
                        newRequestStudent = new RequestStudent(massage,newHoldWorkShop,(Student) newPerson.findOurType(new Student()),payment);
                    }
                    // bayad tozihat ye chiz ezafeh konam
                    if (AddToRequestListINDataBase(newRequestStudent)) {
                        response.end("{\"status\":1}");
                    }
                    else
                        response.end("{\"status\":2");// this person request before
                }
                    response.end("{\"status\":0}");

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/sendEmail").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            this.user = json.getString("setUser");
            this.email = json.getString("setEmailAddress");
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            final String codeValidation;
            if (json.containsKey("IN")) {                 //have account or have not;

                if ((newPerson = findInDataBase2(user, email)) != null) {
                    codeValidation = make_Password(6);
                    OurEmail ourEmail = new OurEmail();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (ourEmail.sendMail(email, codeValidation)) //if email send succesful we add to our list of valicationCods string validation
                                mapValidtionCode.put(codeValidation,new Date());
                        }
                    });
                    t.start();
                    response.end("{\"status\": 1 }");
                } else

                    response.end("{\"status\":0}");
            }
            else {
                codeValidation = make_Password(6);
                OurEmail ourEmail = new OurEmail();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(ourEmail.sendMail(email,codeValidation))
                            mapValidtionCode.put(codeValidation,new Date());

                    }
                });


            }
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/CheckValidationCode").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            String validCode = json.getString("ValidationCode");//getValidationCode
            if (mapValidtionCode.containsKey(validCode)){
                JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm("HS256")
                                .setPublicKey("keyboard cat")
                                .setSymmetric(true)));

                String token = provider.generateToken(new JsonObject().put("userName",user));
                mapLogin.put(token,new Person());
                response.end("{\"status\":1\"token\":"+token+"}");
            }
            else
                response.end("{\"status\":0}");
        });

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/forgetPass").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();

            if (json.containsKey("token") && mapLogin.containsKey(json.containsKey("token"))) {
                if (findInDataBase2(json.getJsonObject("person").getString("setUser"), json.getJsonObject("person").getString("setEmailAddress")) != null) {
                    try {
                        newPerson = objectMapper.readValue(json.getJsonObject("person").toString(),Person.class);
                    } catch (IOException e) {
                        response.end("{\"status\":0}");
                        e.printStackTrace();
                    }
                    updateInPersonINdataBase(newPerson);
                    response.end("{\"status\":1}");
                } else
                    response.end("{\"status\":0}");
            }
            else
                response.end("{\"status\":0}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/singUp").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();

            if (json.containsKey("token") && mapLogin.containsKey(json.containsKey("token"))){
                newPerson = mapLogin.get(json.getString("token"));
                try {
                    newPerson = objectMapper.readValue(json.getJsonObject("person").toString(),Person.class);
                    mapLogin.put(json.getString("token"),newPerson);
                    AddPersonTodataBase(newPerson);
                    response.end("{\"status\":1}");

                } catch (IOException e) {
                    System.out.println("this person can't make");
                    response.end("{\"status\":0}");
                    e.printStackTrace();
                }
            }
            else
                response.end("{\"status\":0}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/requestStudentCancel").handler(rc -> {
                    JsonObject json = rc.getBodyAsJson();
                    HttpServerResponse response = rc.response();
                    response.putHeader("content-type", "application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    String token = json.getString("token");
                    int requestID = json.getInteger("WorkShopID");
                    if (mapLogin.containsKey(token))
                        newPerson = mapLogin.get(token);
                    else
                        response.end("{\"status\":0}");
                    if(deletRequestOf(requestID))
                        response.end("{\"status\":1}");
                    else
                        response.end("{\"status\":0}");
                });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllHistoryOfThisGreater").handler(rc ->{
           JsonObject json = rc.getBodyAsJson();
           HttpServerResponse response = rc.response();
           String token = json.getString("token");
           if(mapLogin.containsKey(token))
               newPerson = mapLogin.get(token);
           else
               response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            JsonObject HistoryOfHim = findHistoryOfGreater(json.getInteger("idGreater"));
            response.end("{\"status\":1\"information\":"+HistoryOfHim+"}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.GET,"/seeAllRecentWorkShop").handler(rc ->{
            JsonObject json = seeAllRecentWorkShop();
            HttpServerResponse response = rc.response();
            response.end("{\"status\":1"+json+"}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllGreaterRequestOneWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");

            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            JsonObject AllGreaterRequest = seeAllRequestGreater(jsonObject.getInteger("WorkShopId"));

            response.end("{\"status\":1\"information\":"+AllGreaterRequest+"}");

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AcceptRequestGreater").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            int numberGroup = jsonObject.getInteger("GroupNumber");
            String groupName = jsonObject.getString("GroupName");
            RequestGreater = getOneRequestGreater(jsonObject.getInteger("RequestGreaterId"));
            newRequestGreater.setAccetply(Accetply.Accept);

            response.end("{\"status\":1\"information\":1}");

        });
        router.route().handler(BodyHandler.create());
        httpServer
                .requestHandler(router::accept)
                .listen(5000);

        }

    private Object getOneRequestGreater(Integer requestGreaterId) {
    }

    private void AddPersonTodataBase(Person newPerson) {
    }

    private HoldWorkShop findThisHoldWorkShop(int id) {
    }

    private JsonObject seeAllRequestGreater(int id) {
        ArrayList<RequestGreater> requestGreaters = findAllRequestGreater(id);
        JsonObject jsonObject = new JsonObject();
        int d = 0;
        for (RequestGreater i : requestGreaters){
            JsonObject jsonObject1 = new JsonObject().put("idGreater",i.getGreater().getId());
            Person person = findPersonOfThisGreater(i.getGreater().getId());
            jsonObject1.put("name",person.getName());
            jsonObject1.put("lastName",person.getLastName());
            jsonObject1.put("userName",person.getUser());
            jsonObject.put(String.valueOf(d),jsonObject);

        }
        return jsonObject;
    }

    private Person findPersonOfThisGreater(int id) {
    }

    private ArrayList<RequestGreater> findAllRequestGreater(int id) {

    }

    private ArrayList<HoldWorkShop> findWorkShophaveThisManager(int id) {
    }

    private JsonObject seeAllRecentWorkShop() {
        ArrayList<HoldWorkShop> holdWorkShop = getALLHoldWorkShop();
        int d = 0;
        Date date = new Date();
        JsonObject jsonObject = new JsonObject();
        for(HoldWorkShop i : holdWorkShop){
            if(i.getStart().after(date)){
                JsonObject jsonObject1 = new JsonObject();
                Person person = findPersonOfThisManagment(i.getManagment().id);
                jsonObject1.put("NameWorkShop",i.getName())
                            .put("Management",person.getName()+"  "+person.getLastName())
                            .put("DateStart",i.getStart())
                            .put("DateEnd",i.getEnd())
                            .put("Money",i.getMoney())
                            .put("IsInstallment",i.getIs_installment())
                            .put("Title",i.getWorkshop().getTitle())
                            .put("Description",i.getWorkshop().getDescription());
                jsonObject.put(String.valueOf(d),jsonObject1);
                d++;
            }
        }
        return jsonObject;
    }

    private Person findPersonOfThisManagment(int id) {
        return new Person();

    }

    private boolean deletRequestOf(int requestID) {
        return true;
    }

    private boolean deletRequestOf(Person newPerson, String workShopHandlerID) {
       return true;
    }

    private boolean AddToRequestListINDataBase(RequestStudent newRequestStudent) {
        return true;
    }


    private boolean isThisPersonIsInOneOfTheGroupOfThisWorkShop(HoldWorkShop newWorkshop, int id) {
        ArrayList<RequestStudent> requestStudents = findAllRequestStudent(newWorkshop.getId());
        for (RequestStudent i : requestStudents){
                if (i.student.getId() == id)
                    return true;
        }
        return false;

    }

    private ArrayList<RequestStudent> findAllRequestStudent(int id) {
    }

    private Person findPersonIndatabase(String user,String pass){
        return new Person();
    }
    private Person makePerson(List<JsonObject> result) throws IOException {
        JsonObject jsonObject = new JsonObject();
        ObjectMapper objectMapper = new ObjectMapper();
        if (result.isEmpty())
            return null;
        else{
            jsonObject = result.get(0);
            Person person = objectMapper.readValue(jsonObject.toString(),Person.class);
            return person;
        }
    }
    private Person findInDataBase2(String user, String pass) {
        return new Person();
    }

    private void upadateDataBaseWithWorkShop(Workshop newWorkshop) {
    }

    private Pay findPayThisWorkShopInDataBase(String id) {
        return new Pay(10000,true);
    }

    private Workshop findWorkShopeInDataBase(String id, String name) {
        return new Workshop(id,name);
    }

    private void updateInPersonINdataBase(Person person) {
    }

    private boolean searchInDataBase(String user) {
        return true;
    }

    private  Person findInDataBase(String Usr,String Pass){
        Person person = new Person();
        //person = in data base
        return  person;
    }
    private boolean AddToDataBass(Person person){
        if (searchInDataBase(person.getUser())) {
            System.out.println(person.toString());
            return false;
        }
        else
            addPersonTodataBase(person);
            return true;

    }

    private void addPersonTodataBase(Person person) {
    }
    private boolean isThisWorkShopeInDatabase(String name , String ID){
        return true;
    }
    private boolean isThisPayInstallment(String Id){
       return true;
    }
    private JsonObject findHistoryOfGreater(Integer id){
            ArrayList<GroupStatus> AllGroupStatuse = getALLGroupStatuseINdataBase();
            JsonObject jsonObject = new JsonObject();
            int d = 0;
            for (GroupStatus i : AllGroupStatuse){
                if(i.getRoleOfWorkShape().getClass() == Greater.class){
                    Greater greater=(Greater)i.getRoleOfWorkShape();
                    if (greater.getId() == id){
                        Group group = i.getGroup();
                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.put("NameWorkShop",i.getGroup().getHoldWorkShop().getName());
                        jsonObject1.put("Title",i.getGroup().getHoldWorkShop().getWorkshop().getTitle());
                        Person person = findPersonOfThisManagment(i.getGroup().getHoldWorkShop().getId());
                        jsonObject1.put("Teacher",person.getName()+"  "+person.getLastName());
                        jsonObject.put(String.valueOf(d),jsonObject1);
                        d++;
                    }
                }

            }
            return jsonObject;
    }

    private ArrayList<HoldWorkShop> getALLHoldWorkShop() {
    }

    private ArrayList<Group> getALLGroupINdataBase() {
    }

    private ArrayList<GroupStatus> getALLGroupStatuseINdataBase() {
    }

    static String make_Password(int len) {
        System.out.println("Generating password using random() : ");
        System.out.print("Your new password is : ");

        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        //String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";


        //String values = Capital_chars + numbers ;
        String values = numbers;
        // Using random method
        Random rndm_method = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password[i] = values.charAt(rndm_method.nextInt(values.length()));

        }
        return new String(password);
    }
}
