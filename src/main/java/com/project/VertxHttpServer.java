package com.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
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
    DataSave dataSave = new DataSave();
//    JsonObject jsonMongo = new JsonObject().put("host","127.0.0.1")
//            .put("port","27017")
//            .put("username","mehdi")
//            .put("password","3339539")
//            .put("db_name","Workshopes");
    Map<String,Person> mapLogin = new HashMap<String,Person>();
    Map<Integer,ValidationProperty> mapValidtionCode = new HashMap<>();
    public VertxHttpServer (){
        if (SaveFIle.loadHashMap("maplagin123") != null)
            this.mapLogin = SaveFIle.loadHashMap("maplagin123") ;
        if (SaveFIle.loadHashMap("mapValiditionCode") != null)
            this.mapValidtionCode = SaveFIle.loadHashMap("mapValiditionCode");
    }
    @Override
    public void start() throws Exception {

        dataSave.getPersons().add(new Person("Ramin","Roshan","1378-10-27","ramin153","12345678","2560443090","09397021876","raminrowshan153@gmail.com"));

        Vertx vertx = Vertx.vertx() ;
       // MongoClient client = MongoClient.createShared(vertx,jsonMongo) ;
       // MongoDb MyDataBase = new MongoDb(client);
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/login").handler(rc -> {
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
                if(!newPerson.getIs_Active())
                    response.end("{\"status\":13");//this person Can't activity
                if(newPerson.is_this_role_in_our_person(Addmin.class)){
                    response.end("{\"status\": 100 ,\"validation\": "+token+"}");//this person is Admin
                }
                else {
                    SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
                    SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
                    response.end("{\"status\": 1 ,\"validation\": " + token + "}");
                }
            }
            else
                response.end("{\"status\":0}");
        });

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/person").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            ObjectMapper objectMapper = new ObjectMapper();

            String token = json.getString("token");
            System.out.println(json.toString());
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            if (mapLogin.containsKey(token)) {
                String jason = null;
                try {
                    Person myperson4 = mapLogin.get(token).clone();
                    jason = objectMapper.writeValueAsString(myperson4);
                } catch (JsonProcessingException e) {
                    System.out.println("make mistack");
                    response.end("{\"status\":2}");
                    e.printStackTrace();
                }
                response.end("{\"status\":1,\"person\":"+jason+"}");
            }
            else {
                response.end("{\"status\":0}");
            }

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
                SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
                SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
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
            else {
                boolean canSendRequest = true;
                Student student = (Student) newPerson.findOurType(Student.class);
                ArrayList<Workshop> workshopPrerequisites = findALLworkShopThatPrerequisiteWithThisWorkshop(newHoldWorkShop.getWorkshop().getId());
                JsonObject jsonObject = new JsonObject();
                ArrayList<String>titleMain = null;
                ArrayList<String>titleStudent = null;
                if (!workshopPrerequisites.isEmpty()){
                    ArrayList<Workshop> workshopsStudentSpend = findALLworkShopThatThisPersonSpend(student.getId());
                    for (Workshop i :workshopPrerequisites){
                        titleMain.add(i.getTitle());
                    }
                    for (Workshop d :workshopsStudentSpend){
                        titleStudent.add(d.getTitle());
                    }
                    int f = 0;
                    for (String s : titleMain){
                        if (!titleStudent.contains(s)){
                            canSendRequest = false;
                            jsonObject.getString(String.valueOf(f),s);
                            f++;
                        }
                    }
                    if(!canSendRequest){
                        response.end("{\"status\":5,\"workShopMustSpend\":"+jsonObject+"}");//must spend this workshop befor
                    }


                }
                JsonObject json2 = new JsonObject();
                json2 = PersonHoldWorkShopThatHaveInThisTime(newPerson,newHoldWorkShop.getStart(),newHoldWorkShop.getEnd(),newHoldWorkShop.getHourStart(),newHoldWorkShop.getHourEnd());
                if (!json2.isEmpty())
                    response.end("{\"status\":5,\"workShopMustSpend\":"+json2+"}");//have this WorkShopInThisTime
                if (!this.isThisPersonIsInOneOfTheGroupOfThisWorkShop(newHoldWorkShop, student.getId())) {
                    if (pay.equals("2")) {
                        if (!newHoldWorkShop.getIs_installment())
                            response.end("{\"status\":3}");
                    }
                    if (pay.equals("2")) {
                        Installment payment = new Installment(newHoldWorkShop.getMoney(), newHoldWorkShop.getPayMoneyInHowTimes());
                        newRequestStudent = new RequestStudent(massage, newHoldWorkShop, (Student) newPerson.findOurType(new Student()), payment);
                    } else {
                        Pay payment = new Pay(newHoldWorkShop.getMoney(), newHoldWorkShop.getIs_installment());
                        newRequestStudent = new RequestStudent(massage, newHoldWorkShop, (Student) newPerson.findOurType(new Student()), payment);
                    }
                    // bayad tozihat ye chiz ezafeh konam
                    if (AddToRequestListINDataBase(newRequestStudent)) {
                        response.end("{\"status\":1}");
                    } else
                        response.end("{\"status\":2");// this person request before
                }
                response.end("{\"status\":0}");
            }

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/sendEmail").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            this.user = json.getString("setUser");
            this.email = json.getString("setEmailAddress");
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            final String codeValidation;
            if (json.getString("IN").equals("1")) {                 //have account or have not;

                if ((newPerson = findInDataBase2(user, email)) != null) {
                    codeValidation = make_Password(6);
                    OurEmail ourEmail = new OurEmail();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (ourEmail.sendMail(email, codeValidation)) //if email send succesful we add to our list of valicationCods string validation
                                mapValidtionCode.put(mapValidtionCode.size()+1,new ValidationProperty(new Date(),user,codeValidation));
                        }
                    });
                    t.start();
                    SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
                    SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
                    response.end("{\"status\": 1 }");
                } else

                    response.end("{\"status\":0}");
            }
            else {
                if ((newPerson = findPersonByUser(user)) != null){
                    response.end("{\"status\":0}");
                }
                else {
                    codeValidation = make_Password(6);
                    OurEmail ourEmail = new OurEmail();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (ourEmail.sendMail(email, codeValidation))
                                mapValidtionCode.put(mapValidtionCode.size() + 1, new ValidationProperty(new Date(), user, codeValidation));

                        }
                    });
                    SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
                    SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
                    response.end("{\"status\": 1 }");
                }
            }
        });

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/requestStudentCancelFromStudent").handler(rc -> {
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
        router.route(HttpMethod.GET, "/afterFiveHundred").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            Date date = new Date();
            for(int i = 0 ;i <=  mapValidtionCode.size() ; i++){
                if((mapValidtionCode.get(i).date.getTime()-date.getTime()) > 500000)
                    mapValidtionCode.remove(i);
            }
            SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
            SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
            response.end("{\"status\":1}");
        });


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/PersonStatusInHoldWorkShop").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String token = json.getString("token");
            String username1 = json.getString("user");
            int workShopID = json.getInteger("WorkShopID");
            if (mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            String number = situationInThisHoldWorkshop(workShopID,username1);
            response.end("{\"status\":"+number+"}");
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,json.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid

            JsonObject HistoryOfHim = findHistoryOfGreater(json.getInteger("idGreater"));
            response.end("{\"status\":1\"information\":"+HistoryOfHim+"}");
        });



        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.GET,"/seeAllRecentWorkShop").handler(rc ->{
            JsonObject json = seeAllRecentWorkShop();
            HttpServerResponse response = rc.response();
            response.end("{\"status\":1,\"seeAllRecent\":"+json+"}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/CheckValidationCode").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            String validCode = json.getString("ValidationCode");//getValidationCode
            this.user = json.getString("user");
            for (int d = mapValidtionCode.size();d > 0 ; d--) {
                if (mapValidtionCode.get(d).token.equals(validCode) && mapValidtionCode.get(d).user.equals(user)) {
                    JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
                            .addPubSecKey(new PubSecKeyOptions()
                                    .setAlgorithm("HS256")
                                    .setPublicKey("keyboard cat")
                                    .setSymmetric(true)));

                    String token = provider.generateToken(new JsonObject().put("userName", user));
                    mapLogin.put(token, new Person());
                    SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);
                    SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);
                    response.end("{\"status\":1 ,\"token\":" + token + "}");
                }
            }
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
        router.route(HttpMethod.POST,"/requestStudentCancelFromStudent").handler(rc -> {
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,json.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid

            JsonObject HistoryOfHim = findHistoryOfGreater(json.getInteger("idGreater"));
            response.end("{\"status\":1\"information\":"+HistoryOfHim+"}");
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid

            JsonObject AllGreaterRequest = seeAllRequestGreater(jsonObject.getInteger("WorkShopId"));

            response.end("{\"status\":1\"information\":"+AllGreaterRequest.toString()+"}");

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllStudentRequestOneWorkShop").handler(rc ->{
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid

            JsonObject allRequestStudnet = seeAllRequestStudent(jsonObject.getInteger("WorkShopId"));
            if (allRequestStudnet == null)
                response.end("{\"status\":3}");//we haven't any Student Request
            response.end("{\"status\":1\"information\":"+allRequestStudnet+"}");

        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllHoldWorkShopThatManagmentHave").handler(rc ->{
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            JsonObject allWorkShopOfThisManagment = null;
            int dd = 0;
            ArrayList<HoldWorkShop>Thathave  = findWorkShophaveThisManager(managment.id);
            for(HoldWorkShop i: Thathave){
                JsonObject json2 = new JsonObject()
                        .put("setId",i.getId())
                        .put("setName",i.getName())
                        .put("setStart",i.getStart())
                        .put("setEnd",i.getEnd());
                allWorkShopOfThisManagment.put(String.valueOf(dd),json2);
                dd++;
            }
            response.end("{\"status\":1\"information\":"+allWorkShopOfThisManagment.toString()+"}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/InstallmentPay").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            RequestStudent requestStudent;
            if((requestStudent = getOneRequestStudent(jsonObject.getInteger("RequestStudentId")))== null)
                response.end("{\"status\":0}");
            int howMuchPay  = jsonObject.getInteger("HowMuchPay");

            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            if (requestStudent.getPay().getClass().equals(Installment.class)){
                Installment installment = (Installment) requestStudent.getPay();
                if(!installment.decreseInstallment(howMuchPay))
                    response.end("{\"status\":4}");//we can haven't this mount
                updateThisRequestInDataBase(requestStudent);
                response.end("{\"status\":1}");
            }
            else {
                response.end("{\"status\":03}");//this person have not installmentPay
            }
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/payCompactly").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            RequestStudent requestStudent;
            if((requestStudent = getOneRequestStudent(jsonObject.getInteger("RequestStudentId")))== null)
                response.end("{\"status\":0}");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            if (requestStudent.getPay().getClass().equals(Installment.class)){
                Installment installment = (Installment) requestStudent.getPay();
                if(!installment.decreseInstallment(installment.getHow_much_installment_must_pay()))
                    response.end("{\"status\":4}");//we can haven't this mount
                updateThisRequestInDataBase(requestStudent);
                response.end("{\"status\":1}");
            }
            else {
                requestStudent.getPay().ChangePayComplite();
                updateThisRequestInDataBase(requestStudent);
                response.end("{\"status\":1}");
            }
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            int numberGroup = jsonObject.getInteger("GroupNumber");
            String groupName = jsonObject.getString("GroupName");
            int numberIdWorkShop = jsonObject.getInteger("IdGroup");
            Group group ;
            if((group = getOnGroupFromDataBase(numberGroup,groupName,numberIdWorkShop))== null)
                    response.end("{\"status\":0}");

            RequestGreater requestGreater;
            if((requestGreater = getOneRequestGreater(jsonObject.getInteger("RequestGreaterId")))== null)
                response.end("{\"status\":0}");
            if (newRequestGreater.getAccetply() != Accetply.Accept) {
                newRequestGreater.setAccetply(Accetply.Accept);
                GroupStatus groupStatus = new GroupStatus(group, requestGreater.getGreater());
                AddNewGroupStatusToDatabase(groupStatus);
                response.end("{\"status\":1}");
            }
            response.end("{\"status\":0}");

        });

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AcceptRequestStudent").handler(rc ->{
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
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            RequestStudent requestStudent;
            if((requestStudent = getOneRequestStudent(jsonObject.getInteger("RequestStudentId")))== null)
                response.end("{\"status\":0}");
            if(requestStudent.getPay().isPayComplite()) {
                int numberGroup = jsonObject.getInteger("GroupNumber");
                String groupName = jsonObject.getString("GroupName");
                int numberIdWorkShop = jsonObject.getInteger("IdGroup");
                Group group ;
                if((group = getOnGroupFromDataBase(numberGroup, groupName, numberIdWorkShop))== null)
                    response.end("{\"status\":0}");
                if (requestStudent.getAccetply() != Accetply.Accept) {
                    requestStudent.setAccetply(Accetply.Accept);
                    GroupStatus groupStatus = new GroupStatus(group, requestStudent.getStudent());
                    AddNewGroupStatusToDatabase(groupStatus);
                    response.end("{\"status\":1}");
                }
                response.end("{\"status\":0}");
            }
            else
                response.end("{\"status\":3}");//this person don't pay it's payment Complite
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewGroup").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            ObjectMapper objectMapper = new ObjectMapper();
            Group group = null;
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Managment.class)){
                response.end("{\"status\":0}");
            }
            Managment managment = (Managment) newPerson.findOurType(Managment.class);
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            try {
                group = objectMapper.readValue(jsonObject.getJsonObject("Group").toString(),Group.class);
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":5}");//can get json correctly
            }
            HoldWorkShop holdWorkShop = findThisHoldWorkShop(jsonObject.getInteger("IdWorkShop"));
            group.setHoldWorkShop(holdWorkShop);
            if(AddNewGroupTodatabase(group))
                response.end("{\"status\":1}");
            response.end("{\"status\":0}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Addmin.class)){
                response.end("{\"status\":0}");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Workshop workshop = null;
            try {
                workshop = objectMapper.readValue(jsonObject.getJsonObject("WorkShop").toString(),Workshop.class);
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
            }
            if(!AddNewWorkShopTOdataBase(workshop))
                response.end("{\"status\":0}");
            ArrayList<Workshop>workshopsPrerequisite = findThisWorkShop(jsonObject.getJsonArray("WorkShopPrerequisite"));
            for(Workshop i: workshopsPrerequisite){
                AddNewReqirmentsToDataBase(new Requirments(i,workshop, Relation.Prerequisite));
            }
            ArrayList<Workshop>workshopsNeed = findThisWorkShop(jsonObject.getJsonArray("WorkShopNeed"));
            for (Workshop i: workshopsNeed){
                AddNewReqirmentsToDataBase(new Requirments(i,workshop,Relation.TheNeed));
            }
            response.end("{\"status\":1}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewHoldWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Addmin.class)){
                response.end("{\"status\":0}");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Person person = findPersonIndataBase(jsonObject.getString("user"),jsonObject.getString("NationCode"));
            if(person == null)
                response.end("{\"status\":4}");//can't find this person in dataBase
            HoldWorkShop holdWorkShop = null;
            try {
                holdWorkShop = objectMapper.readValue(jsonObject.getJsonObject("HoldWorkShop").toString(), HoldWorkShop.class);
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
            }
            holdWorkShop.setManagment((Managment) person.findOurType(Managment.class));
            if(!addNewHoldWorkShop(holdWorkShop))
                response.end("{\"status\":0}");
            response.end("{\"status\":1}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AdminSeeAllPerson").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = null;
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Addmin.class)){
                response.end("{\"status\":0}");
            }
            ArrayList<Person> persons = allPersonIndataBase();
            int dd = 0;
            Addmin addmin = (Addmin)newPerson.findOurType(Addmin.class);
            for(Person i : persons){
                if(!i.is_this_role_in_our_person(Addmin.class)|| (addmin.getAdminType() == AdminType.General)) {
                    JsonObject jsonObject1 = new JsonObject().put("setId", i.getId())
                            .put("setGender", i.getGender())
                            .put("setUser", i.getUser())
                            .put("setName",i.getName())
                            .put("setNationalCode",i.getNationalCode())
                            .put("setLastName",i.getLastName())
                            .put("setDate_birthday",i.getDate_birthday())
                            .put("setActivity",i.getIs_Active());
                    json.put(String.valueOf(dd),jsonObject1);
                    dd++;
                }
            }
            response.end("{\"status\":1\"information\":"+json+"}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AdminChangePersonActivity").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = null;
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Addmin.class)){
                response.end("{\"status\":0}");
            }
            Addmin addmin = (Addmin)newPerson.findOurType(Addmin.class);
            Person person = findPersonIndataBase(json.getString("user"),json.getString("nationCode"));
            if(person == null)
                response.end("{\"status\":0}");
            if(!person.is_this_role_in_our_person(Addmin.class) || addmin.getAdminType() == AdminType.General){
                boolean activity = json.getBoolean("activity");
                person.setIs_Active(activity);
                response.end("{\"status\":1}");
            }
            response.end("{\"status\":13}");//Don't Access
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/LogoutAllPersonInSystem").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = null;
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(Addmin.class)){
                response.end("{\"status\":0}");
            }
            Addmin addmin = (Addmin)newPerson.findOurType(Addmin.class);
            String Level = jsonObject.getString("Level");
            List keys = new ArrayList(mapLogin.keySet());
            if(Level.equals("AllPerson")){
                if (addmin.getAdminType() == AdminType.General) {
                    mapLogin.clear();
                    mapLogin.put(jsonObject.getString("token"), newPerson);
                }
            }
            else{
                for (int i = 0 ; i < keys.size();i++){
                    if (!mapLogin.get(keys.get(i)).is_this_role_in_our_person(Addmin.class))
                        mapLogin.remove(keys.get(i));
                }

            }
            response.end("{\"status\":Ok}");
        });

        httpServer
                .requestHandler(router::accept)
                .listen(5000);

        }

    private String situationInThisHoldWorkshop(int workShopID, String username1) {
        HoldWorkShop holdWorkShop = findThisHoldWorkShop(workShopID);
        Person person = findPersonByUser(username1);
        Managment managment = null;
        if (person.is_this_role_in_our_person(Managment.class))
            managment = (Managment) person.findOurType(Managment.class);
            if (holdWorkShop.getManagment().id == managment.id)
                return "managment";
        dataSave.seeAllRequestArrayList(workShopID);
        return "sdfsafd";
    }

    private boolean AddNewGroupTodatabase(Group group) {
        return  dataSave.AddNewGroupTOdatabase(group);
    }

    private Person findPersonByUser(String user) {
        return dataSave.findPersonByUser(user);
    }


    private ArrayList<Person> allPersonIndataBase() {
        return dataSave.getPersons();
    }

    private Person findPersonIndataBase(String user,String nationCode) {
        return dataSave.finPersonINdataBase(user,nationCode);
    }

    private boolean addNewHoldWorkShop(HoldWorkShop holdWorkShop) {
        return dataSave.addNewHoldWorkShop(holdWorkShop);
    }

    private boolean AddNewWorkShopTOdataBase(Workshop workshop) {
        return dataSave.AddNewWorkShopTOdataBase(workshop);
    }

    private void AddNewReqirmentsToDataBase(Requirments requirments) {
        dataSave.AddNewReqirmentsToDataBase(requirments);
    }

    private ArrayList<Workshop> findThisWorkShop(JsonArray workShopPrerequisite) {
        return dataSave.findThisWorkShop(workShopPrerequisite);
    }

    private JsonObject PersonHoldWorkShopThatHaveInThisTime(Person newPerson, Date start, Date end, LocalTime hourStart, LocalTime hourEnd) {
            Student student = (Student)newPerson.findOurType(Student.class);
            Greater greater = (Greater)newPerson.findOurType(Greater.class);
            ArrayList<RequestStudent>  requestStudents= getALLStudentRequestThatThisPersonSend(student.getId());
            ArrayList<RequestGreater> requestGreaters = getALLGreaterRequestThatThisPersonSend(greater.getId());
            JsonObject jsonObjectMain = new JsonObject();
            JsonObject jsonObjectGreater = new JsonObject();
            JsonObject jsonObjectStudent = new JsonObject();
            int dd = 0;
            for (RequestStudent i : requestStudents) {
                if (((i.getHoldWorkShop().getStart().before(start) && i.getHoldWorkShop().getEnd().after(start))|| (i.getHoldWorkShop().getStart().after(start) && i.getHoldWorkShop().getStart().before(end))) &&((i.getHoldWorkShop().getHourStart().isAfter(hourStart) && i.getHoldWorkShop().getHourStart().isBefore(hourEnd)) || (i.getHoldWorkShop().getHourStart().isBefore(hourStart) && i.getHoldWorkShop().getHourEnd().isAfter(hourStart)))){
                    jsonObjectStudent.put(String.valueOf(dd),i.getHoldWorkShop().getName());
                    dd++;
                }
            }
            dd = 0;
            for (RequestGreater s : requestGreaters){
                if (((s.getHoldWorkShop().getStart().before(start) && s.getHoldWorkShop().getEnd().after(start))|| (s.getHoldWorkShop().getStart().after(start) && s.getHoldWorkShop().getStart().before(end))) &&((s.getHoldWorkShop().getHourStart().isAfter(hourStart) && s.getHoldWorkShop().getHourStart().isBefore(hourEnd)) || (s.getHoldWorkShop().getHourStart().isBefore(hourStart) && s.getHoldWorkShop().getHourEnd().isAfter(hourStart)))){
                    jsonObjectGreater.put(String.valueOf(dd),s.getHoldWorkShop().getName());
                    dd++;
                }
            }
            if (jsonObjectGreater.isEmpty() && jsonObjectStudent.isEmpty())
                return jsonObjectMain;
            else
                return jsonObjectMain.put("greater",jsonObjectGreater)
                        .put("student",jsonObjectStudent);

    }

    private ArrayList<RequestGreater> getALLGreaterRequestThatThisPersonSend(int id) {
        return dataSave.getAllGreaterRequestThatThisPersonSend(id);
    }

    private ArrayList<RequestStudent> getALLStudentRequestThatThisPersonSend(int id) {
        return dataSave.getAllStudentRequestThatThisPersonSend(id);
    }

    private ArrayList<Workshop> findALLworkShopThatThisPersonSpend(int id) {
        return dataSave.findALLworkShopThatThisPersonSpend(id);
    }

    private ArrayList<Workshop> findALLworkShopThatPrerequisiteWithThisWorkshop(int id) {
        return dataSave.findALLworkShopThatPrerequisiteWithThisWorkshop(id);
    }

    private boolean isthisMangmentOfTHisWorkShop(int id, int idWorkShop) {
        return dataSave.isthisMangmentOfTHisWorkShop(id,idWorkShop);
    }

    private void updateThisRequestInDataBase(RequestStudent requestStudent) {
        dataSave.updateThisRequestInDataBase(requestStudent);
    }

    private JsonObject seeAllRequestStudent(int workShopId) {
        return dataSave.seeAllRequestStudent(workShopId);
    }

    private RequestStudent getOneRequestStudent(int requestStudentId) {
        return dataSave.getOneRequestStudent(requestStudentId);
    }

    private void AddNewGroupStatusToDatabase(GroupStatus groupStatus) {
        dataSave.AddNewGroupStatusToDatabase(groupStatus);
    }

    private Group getOnGroupFromDataBase(int numberGroup, String groupName, int numberIdWorkShop) {
        return dataSave.getOneGroupFrommDataBase(numberGroup,groupName,numberIdWorkShop);
    }

    private RequestGreater getOneRequestGreater(Integer requestGreaterId) {
        return dataSave.getOneRequestGreater(requestGreaterId);
    }

    private void AddPersonTodataBase(Person newPerson) {
        dataSave.AddNewPersonTodataBase(newPerson);
    }

    private HoldWorkShop findThisHoldWorkShop(int id) {
        return dataSave.findThisHoldWorkShop(id);
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
        return dataSave.findPersonOfThisGreater(id);
    }

    private ArrayList<RequestGreater> findAllRequestGreater(int id) {
        return dataSave.findAllRequestGreater(id);
    }

    private ArrayList<HoldWorkShop> findWorkShophaveThisManager(int id) {
        return dataSave.findWorkShophaveThisManager(id);
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
                            .put("id",i.getId())
                            .put("Description",i.getWorkshop().getDescription());
                jsonObject.put(String.valueOf(d),jsonObject1);
                d++;
            }
        }
        return jsonObject;
    }

    private Person findPersonOfThisManagment(int id) {
        return dataSave.findPersonOfThisManagment(id);

    }
    private Person findPersonOfThisStudent(int id) {
        return dataSave.findPersonOfThisStudent(id);

    }
    private boolean deletRequestOf(int requestID) {
        return dataSave.deletRequesOf(requestID);
    }

    private boolean AddToRequestListINDataBase(RequestStudent newRequest) {
        return dataSave.AddToRequestListINDataBase(newRequestStudent);
    }
    private boolean AddToRequestListINDataBase(RequestGreater requestGreater){
        return dataSave.AddToRequestListINDataBase(newRequestGreater);
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
        return dataSave.findAllRequestStudent(id);
    }

    private Person findPersonIndatabase(String user,String pass){
        return dataSave.finPersonINdataBase(user,pass);
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
    private Person findInDataBase2(String user, String email) {
        return dataSave.findPersonIndataBase2(user,email);
    }

    private Workshop findWorkShopeInDataBase(String id, String name) {
        return new Workshop(id,name);
    }

    private void updateInPersonINdataBase(Person person) {
        dataSave.updateInPersonINdataBase(person);

    }

    private boolean searchInDataBase(String user) {
        return dataSave.searchInDataBase(user);
    }

//    private boolean AddToDataBass(Person person){
//        if (searchInDataBase(person.getUser())) {
//            System.out.println(person.toString());
//            return false;
//        }
//        else
//            addPersonTodataBase(person);
//            return true;
//
//    }


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
        return dataSave.getHoldWorkShops();
    }

    private ArrayList<Group> getALLGroupINdataBase() {
        return dataSave.getALLGroupINdataBase();
    }

    private ArrayList<GroupStatus> getALLGroupStatuseINdataBase() {
        return dataSave.getALLGroupStatuseINdataBase();
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
