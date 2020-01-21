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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    Grader_Request newGraderRequest = null;
    DataSave dataSave = null;
//    JsonObject jsonMongo = new JsonObject().put("host","127.0.0.1")
//            .put("port","27017")
//            .put("username","mehdi")
//            .put("password","3339539")
//            .put("db_name","Workshopes");
    Map<String,Person> mapLogin = new HashMap<String,Person>();
    Map<Integer,ValidationProperty> mapValidtionCode = new HashMap<>();
    public VertxHttpServer (){
        if (SaveFIle.loadHashMap("mapLogin123.ser") != null)
            this.mapLogin = SaveFIle.loadHashMap("mapLogin123.ser") ; // this property save all person be login in data base
        if (SaveFIle.loadHashMap("mapValiditionCode.ser") != null)
            this.mapValidtionCode = SaveFIle.loadHashMap("mapValiditionCode.ser"); // this property save all token that send to person In data base
        dataSave = new DataSave();
    }
    @Override
    public void start() throws Exception {
//        SaveFIle.saveHashMap("mapLogin123.ser",null);
//        SaveFIle.saveHashMap("mapValiditionCode.ser",null);
//        SaveFIle.saveArrayListInFile("workshopsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("holdWorkShopsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("groupsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("requestsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("requirmentsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("personsArrayList.ser",null);
//        SaveFIle.saveArrayListInFile("groupStatus.ser",null);
//        String string = "January 2, 2022";
//        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
//        String string2 = "February 12, 2022";
//        DateFormat format1 = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
//        LocalTime time = LocalTime.of(10,45,00);
//        LocalTime time2 = LocalTime.of(12,30,00);
//        Person mehdi = new Person("mehdi","feghhi","1378-12-16","mf1378mf","1850427933","1850427933","0937837990","mf1378mf@yahoo.com");
//        Addmin addmin = new Addmin(AdminType.General);
//        mehdi.addToArrayListOfRole(addmin);
//        Workshop workshopOfs = new Workshop("Math","ArrahehMishaved");
//        workshopOfs.setId(0);
//        AddNewWorkShopTOdataBase(workshopOfs);
//
//        AddPersonTodataBase(mehdi);
//        AddPersonTodataBase(new Person("Ramin","Roshan","1378-10-27","ramin153","12345678","2560443090","09397021876","raminrowshan153@gmail.com"));
//        addNewHoldWorkShop(new HoldWorkShop(time,time2,format.parse(string),format1.parse(string),"python",0,null,workshopOfs,true, (long) 10000000));
//        dataSave.saveInFile();
        Vertx vertx = Vertx.vertx() ;

       // MongoClient client = MongoClient.createShared(vertx,jsonMongo) ;
       // MongoDb MyDataBase = new MongoDb(client);
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);



        ///////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                 //
        router.route(HttpMethod.POST, "/login").handler(rc -> {                                    //
            JsonObject json = rc.getBodyAsJson();                                                     //
            this.user = json.getString("user");                                                  //
            this.pass = json.getString("pass");                                                  //
            System.out.println(json.toString());                                                      //
            HttpServerResponse response = rc.response();                                              //
            response.putHeader("content-type", "application/json");                            //
//            MyDataBase.findThisPerson(user,pass,res ->{                                             //
//                try {                                                                               //
//                    newPerson = makePerson(res.result());                                           //
//                } catch (IOException e) {                                                           //
//                   response.end("{\"status\":3}");                                                  // whit this code one person can login to by it's user
//                    e.printStackTrace();                                                            // and pass and get a token to do it's work
//                }                                                                                   //
//            });                                                                                     //
        newPerson = findPersonIndatabase(this.user,this.pass);                                        //
        Addmin admin3 = new Addmin();                                                                 //
        if (newPerson != null){                                                                       //
                JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()                         //
                        .addPubSecKey(new PubSecKeyOptions()                                          // // make new token to this person
                                .setAlgorithm("HS256")                                                //
                                .setPublicKey("keyboard cat")                                         //
                                .setSymmetric(true)));                                                //
                                                                                                      //
                String token = provider.generateToken(new JsonObject().put("userName",user));         //
                mapLogin.put(token,newPerson);                                                        //
                if(!newPerson.getIs_Active())                                                         //
                    response.end("{\"status\":13");//this person Can't activity                    //
                if(newPerson.is_this_role_in_our_person(admin3)){                                     //
                    SaveFIle.saveHashMap("mapLogin123.ser", (HashMap) this.mapLogin);        //
                    SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);//
                    response.end("{\"status\": 100 ,\"validation\": "+token+"}");                        //;//this person is Admin amd must seen new page from another person
                }                                                                                           //
                else {                                                                                      //
                    SaveFIle.saveHashMap("mapLogin123.ser", (HashMap) this.mapLogin);              //
                    SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);//
                    response.end("{\"status\": 1 ,\"validation\": " + token + "}");                     //
                }                                                                                          //
            }                                                                                              //
            else                                                                                           //
                response.end("{\"status\":0}");                                                         //
        });                                                                                                //
        /////////////////////////////////////////////////////////////////////////////////////////////////////





        //whit this codes I show a personlity and it's property to his or she loggine and have token just it's password
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                                                                                                    //
        router.route().handler(BodyHandler.create());                                                               //
        router.route(HttpMethod.POST, "/person").handler(rc -> {                                                 //
            JsonObject json = rc.getBodyAsJson();                                                                   //
            ObjectMapper objectMapper = new ObjectMapper();                                                         //
                                                                                                                    //
            String token = json.getString("token");                                                            //
            System.out.println(json.toString());                                                                    //
            HttpServerResponse response = rc.response();                                                            //
            response.putHeader("content-type", "application/json");                                          //
            if (mapLogin.containsKey(token)) {                                                                      //
                String jason = null;                                                                                //
                try {                                                                                               //
                    Person myperson4 = mapLogin.get(token).clone();                                                 //
                    jason = objectMapper.writeValueAsString(myperson4);                                             //
                } catch (JsonProcessingException e) {                                                               //
                    System.out.println("make mistack");                                                             //
                    response.end("{\"status\":2}");                                                              //
                    e.printStackTrace();                                                                            //
                }                                                                                                   //
                response.end("{\"status\":1,\"person\":"+jason+"}");                                             //
            }                                                                                                       //
            else {                                                                                                  //
                response.end("{\"status\":0}");                                                                  //
            }                                                                                                       //
                                                                                                                    //
        });                                                                                                         //
                                                                                                                    //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////









        //with this code I Logged person in maplogin   and remove it's token in this array
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                 //
        router.route(HttpMethod.GET, "/logOut").handler(rc -> {                                                    //
            JsonObject json = rc.getBodyAsJson();                                                                     //
                                                                                                                      //
            System.out.println("ya ali");                                                                             //
            String token = json.getString("token");                                                              //
            System.out.println(json.toString());                                                                      //
            HttpServerResponse response = rc.response();                                                              //
            response.putHeader("content-type", "application/json");                                            //
            if (mapLogin.containsKey(token)) {                                                                        //
                updateInPersonINdataBase(mapLogin.get(token));                                                        //
                mapLogin.remove(token);                                                                               //
                SaveFIle.saveHashMap("mapLogin123.ser", (HashMap) this.mapLogin);                             //
                SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);               //
                response.end("{\"status\":1}");                                                                    //
            }                                                                                                         //
            else {                                                                                                    //
                response.end("{\"status\":0}");                                                                    //
            }                                                                                                         //
                                                                                                                      //
        });                                                                                                           //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////







        //with this code I edite person in my WorkShop
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                 //
        router.route(HttpMethod.POST,"/editPerson").handler(rc ->{                                                 //
            JsonObject json = rc.getBodyAsJson();                                                                     //
            HttpServerResponse response = rc.response();                                                              //
            response.putHeader("content-type","application/json");                                             //
            ObjectMapper objectMapper = new ObjectMapper();                                                           //
            if (mapLogin.containsKey(json.getString("token"))){                                                  //
                newPerson = null;                                                                                     //
                try {                                                                                                 //
                    newPerson = objectMapper.readValue(json.getJsonObject("person").toString(), Person.class);        //
                    if (this.searchInDataBase(newPerson.getUser())) {                                                 //
                        updateInPersonINdataBase(newPerson);                                                          //
                        response.end("{\"status\":1}");                                                            //
                    } else                                                                                            //
                        response.end("{\"status\":0}");                                                            //
                } catch (IOException e) {                                                                             //
                    e.printStackTrace();                                                                              //
                    response.end("{\"status\":0}");                                                                //
                }                                                                                                     //
            }else                                                                                                     //
                response.end("{\"status\":0}");                                                                    //
        });                                                                                                           //
     ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////






     ////////////////////////////requestGreater

     ////////////////////////////////////////////   ////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                 //
        router.route(HttpMethod.POST,"/requestGrader").handler(rc -> {                                             //
                    JsonObject json = rc.getBodyAsJson();                                                             //
                    HttpServerResponse response = rc.response();                                                      //
                    response.putHeader("content-type", "application/json");                                    //
                    ObjectMapper objectMapper = new ObjectMapper();                                                   //
                    String token = json.getString("token");                                                      //
                    String massage = json.getString("massage");                                                  //
                    int id = json.getInteger("workShopHandler");                                                 //
                    if (mapLogin.containsKey(token)) {                                                                //
                        newPerson = mapLogin.get(token);                                                              //
                    }                                                                                                 //
                    else {                                                                                            //
                        response.end("{\"status\":0}");                                                            //
                        return;                                                                                       //
                    }                                                                                                 //
                    newHoldWorkShop = findThisHoldWorkShop(id);                                                       //
                    if (newHoldWorkShop == null) {                                                                    //
                         response.end("{\"status\":0}");                                                           //
                        return;                                                                                       //
                    }                                                                                                 //
                    else {                                                                                            //
                        boolean canSendRequest = true;                                                                //
                        Grader grader = (Grader) newPerson.findOurType("2");                                   ////
                        JsonObject json2 = new JsonObject();                                                          ////////////////////////////////////////////////////////////////////////////////
                        json2 = PersonHoldWorkShopThatHaveInThisTime(newPerson, newHoldWorkShop.getStart(), newHoldWorkShop.getEnd(), newHoldWorkShop.getHourStart(), newHoldWorkShop.getHourEnd());//
                        if (!json2.isEmpty()) {                                                                                ///////////////////////////////////////////////////////////////////////
                            response.end("{\"status\":5,\"workShopMustSpend\":" + json2 + "}");//have this WorkShopInThisTime//
                            return;                                                                                             //
                        }                                                                                             //
                        // bayad tozihat ye chiz ezafeh konam                                                         //
                        if (AddToRequestListINDataBase(new Grader_Request(massage,newHoldWorkShop,grader))) {         //
                            dataSave.saveInFile();
                            response.end("{\"status\":1}");                                                        //
                            return;                                                                                   //
                        } else {
                            response.end("{\"status\":2");// this person request before                            //
                            return;                                                                                   //
                        }                                                                                             //
                    }                                                                                                 //
        });                                                                                                           //
                                                                                                                      //
                                                                                                                      //
                                                                                                                      //
                                                                                                                      //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////







        ///////////////////////////////////////requestStudent
        //if person have request to workShope that request for it befor it's request be baan
        // also if if person be delete from data base or it's time have commen from the before request it's be it's request be baan to
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                                     //
        router.route(HttpMethod.POST,"/requestStudent").handler(rc ->{                                                                 //
            JsonObject json = rc.getBodyAsJson();                                                                                         //
            HttpServerResponse response = rc.response();                                                                                  //
            response.putHeader("content-type","application/json");                                                                 //
            ObjectMapper objectMapper = new ObjectMapper();                                                                               //
            String token = json.getString("token");                                                                                  //
            String massage = json.getString("massage");                                                                              //
            String pay = json.getString("pay");                                                                                      //
            int id = json.getInteger("workShopHandler");                                                                             //
            if (mapLogin.containsKey(token))                                                                                              //
                 newPerson = mapLogin.get(token);                                                                                         //
            else                                                                                                                          //
                response.end("{\"status\":0}");                                                                                        //
                                                                                                                                          //
            newHoldWorkShop = findThisHoldWorkShop(id);                                                                                   //
            if (newHoldWorkShop == null) {                                                                                                 //
                response.end("{\"status\":0}");
                return;//
            }
            else {                                                                                                                        //
                boolean canSendRequest = true;                                                                                            //
                Student student = (Student) newPerson.findOurType("1");                                                            //
                dataSave.saveInFile();                                                                                                    ///////////
                ArrayList<Workshop> workshopPrerequisites = findALLworkShopThatPrerequisiteWithThisWorkshop(newHoldWorkShop.getWorkshop().getId());//
                JsonObject jsonObject = new JsonObject();                                                                                 //
                ArrayList<String>titleMain = null;                                                                                        //
                ArrayList<String>titleStudent = null;                                                                                     //
                if (!workshopPrerequisites.isEmpty()){                                                                                    //
                    ArrayList<Workshop> workshopsStudentSpend = findALLworkShopThatThisPersonSpend(student.getId());                      //
                    for (Workshop i :workshopPrerequisites){                                                                              //
                        titleMain.add(i.getTitle());                                                                                      //
                    }                                                                                                                     //
                    for (Workshop d :workshopsStudentSpend){                                                                              //
                        titleStudent.add(d.getTitle());                                                                                   //
                    }                                                                                                                     //
                    int f = 0;                                                                                                            //
                    for (String s : titleMain){                                                                                           //
                        if (!titleStudent.contains(s)){                                                                                   //
                            canSendRequest = false;                                                                                       //
                            jsonObject.getString(String.valueOf(f),s);                                                                    //
                            f++;                                                                                                          //
                        }                                                                                                                 //
                    }                                                                                                                     //
                    if(!canSendRequest){                                                                                                  //
                        response.end("{\"status\":5,\"workShopMustSpend\":"+jsonObject+"}");//must spend this workshop befor
                        return;                                                                                                           //
                    }                                                                                                                     //
                                                                                                                                          //
                }                                                                                                                         //
                JsonObject json2 = new JsonObject();                                                                                      /////////////////////////////////////////////////
                json2 = PersonHoldWorkShopThatHaveInThisTime(newPerson,newHoldWorkShop.getStart(),newHoldWorkShop.getEnd(),newHoldWorkShop.getHourStart(),newHoldWorkShop.getHourEnd());//
                if (!json2.isEmpty()) {                                                                                                    /////////////////////////////////////////////
                    response.end("{\"status\":5,\"workShopMustSpend\":" + json2 + "}");                                                  //
                    return;                                                                                                                 //have this WorkShopInThisTime
                }                                                                                                                           //
                if (!this.isThisPersonIsInOneOfTheGroupOfThisWorkShop(newHoldWorkShop, student.getId())) {                                  //
                    if (pay.equals("2")) {                                                                                                  //
                        if (!newHoldWorkShop.getIs_installment()) {                                                                         //
                            response.end("{\"status\":3}");
                            return;
                        }                                                                                                                   //
                    }                                                                                                                       //
                    if (pay.equals("2")) {                                                                                                  //
                        Installment payment = new Installment(newHoldWorkShop.getMoney(), newHoldWorkShop.getPayMoneyInHowTimes());         /////
                        newRequestStudent = new RequestStudent(massage, newHoldWorkShop, (Student) newPerson.findOurType("1"), payment);//
                    } else {                                                                                                                   //
                        Pay payment = new Pay(newHoldWorkShop.getMoney(), newHoldWorkShop.getIs_installment());                                //
                        newRequestStudent = new RequestStudent(massage, newHoldWorkShop, (Student) newPerson.findOurType("1"), payment);//
                    }                                                                                                                          //
                    // bayad tozihat ye chiz ezafeh konam                                                                                      //
                    if (AddToRequestListINDataBase(newRequestStudent)) {
                        dataSave.saveInFile();                                                                                                 //
                        response.end("{\"status\":1}");
                        return;                                                                                                                //
                    } else {                                                                                                                   //
                        response.end("{\"status\":2}");// this person request before
                        return;
                    }                                                                                                                          //
                }                                                                                                                              //
                response.end("{\"status\":0}");                                                                                             //
            }                                                                                                                                  //
                                                                                                                                               //
        });                                                                                                                                    //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////














        ///////////////////////////////////////sendEmail
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                                                                         //
        router.route(HttpMethod.POST, "/sendEmail").handler(rc -> {                                                                                                        //
            JsonObject json = rc.getBodyAsJson();                                                                                                                             //
            this.user = json.getString("setUser");                                                                                                                       //
            this.email = json.getString("setEmailAddress");                                                                                                              //
            HttpServerResponse response = rc.response();                                                                                                                      //
            response.putHeader("content-type", "application/json");                                                                                                    //
            final String codeValidation;                                                                                                                                      //
            if (json.getString("IN").equals("1")) {                 //have account or have not;                                                                          //
                if ((newPerson = findPersonByUser(user)) != null) {                                                                                                           //
                    codeValidation = make_Password(6);                                                                                                                   //
                    OurEmail ourEmail = new OurEmail();                                                                                                                       //
                    new Thread(new Runnable() {                                                                                                                               //
                        @Override                                                                                                                                             //
                        public void run() {                                                                                                                                   //
                            System.out.println("akdjvxnskjdvn");                                                                                                              //
                            if (ourEmail.sendMail(email, codeValidation)) //if email send succesful we add to our list of valicationCods string validation                    //
                                mapValidtionCode.put(mapValidtionCode.size()+1,new ValidationProperty(new Date(),user,codeValidation));                                    //
                        }                                                                                                                                                     //
                    }).start();                                                                                                                                               //
                    SaveFIle.saveHashMap("mapLogin123.ser", (HashMap) this.mapLogin);                                                                                //
                    SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);                                                                  //
                    response.end("{\"status\": 1 }");                                                                                                                      //
                } else                                                                                                                                                        //
                    response.end("{\"status\":0}");                                                                                                                        //
            }                                                                                                                                                                 //
            else {                                                                                                                                                            //
                if ((newPerson = findPersonByUser(user)) != null){                                                                                                            //
                    response.end("{\"status\":0}");                                                                                                                        //
                }                                                                                                                                                             //
                else {                                                                                                                                                        //
                    codeValidation = make_Password(6);                                                                                                                   //
                    OurEmail ourEmail = new OurEmail();                                                                                                                       //
                    Thread t = new Thread(new Runnable() {                                                                                                                    //
                        @Override                                                                                                                                             //
                        public void run() {                                                                                                                                   //
                            if (ourEmail.sendMail(email, codeValidation))                                                                                                     //
                                mapValidtionCode.put(mapValidtionCode.size() + 1, new ValidationProperty(new Date(), user, codeValidation));                               //
                                                                                                                                                                              //
                        }                                                                                                                                                     //
                    });                                                                                                                                                       //
                    t.start();                                                                                                                                                //
                    SaveFIle.saveHashMap("mapLogin123.ser", (HashMap) this.mapLogin);                                                                                //
                    SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);                                                                  //
                    response.end("{\"status\": 1 }");                                                                                                                     //
                }                                                                                                                                                            //
            }                                                                                                                                                                //
        });                                                                                                                                                                  //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////










        //////////////////request Student cancel from student

        /////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                          //
        router.route(HttpMethod.POST,"/requestStudentCancelFromStudent").handler(rc -> {    //
            JsonObject json = rc.getBodyAsJson();                                              //
            HttpServerResponse response = rc.response();                                       //
            response.putHeader("content-type", "application/json");                     //
            ObjectMapper objectMapper = new ObjectMapper();                                    //
            String token = json.getString("token");                                       //
            int requestID = json.getInteger("WorkShopID");                                //
            if (mapLogin.containsKey(token))                                                   //
                newPerson = mapLogin.get(token);                                               //
            else                                                                               //
                response.end("{\"status\":0}");                                             //
            if(deletRequestOf(requestID))                                                      //
                response.end("{\"status\":1}");                                             //
            else                                                                               //
                response.end("{\"status\":0}");                                             //
        });                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////






        ///////////////////////////////this request remove token that was after 500000 secends times

        /////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                  //
        router.route(HttpMethod.GET, "/afterFiveHundred").handler(rc -> {                           //
            JsonObject json = rc.getBodyAsJson();                                                      //
            HttpServerResponse response = rc.response();                                               //
            response.putHeader("content-type", "application/json");                             //
            Date date = new Date();                                                                    //
            for(int i = 0 ;i <=  mapValidtionCode.size() ; i++){                                       //
                if((mapValidtionCode.get(i).date.getTime()-date.getTime()) > 500000)                   //
                    mapValidtionCode.remove(i);                                                        //
            }                                                                                          //
            SaveFIle.saveHashMap("maplagin123", (HashMap) this.mapLogin);                     //
            SaveFIle.saveHashMap("mapValiditionCode", (HashMap) this.mapValidtionCode);       //
            response.end("{\"status\":1}");                                                        //
        });                                                                                           //
                                                                                                      //
        ///////////////////////////////////////////////////////////////////////////////////////////////













        ///person stutus in hold workshop
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                        //
        router.route(HttpMethod.POST,"/PersonStatusInHoldWorkShop").handler(rc -> {                       //
            JsonObject json = rc.getBodyAsJson();                                                            //
            HttpServerResponse response = rc.response();                                                     //
            response.putHeader("content-type", "application/json");                                   //
            ObjectMapper objectMapper = new ObjectMapper();                                                  //
            String token = json.getString("token");                                                     //
            String username1 = json.getString("user");                                                  //
            int workShopID = json.getInteger("WorkShopID");                                             //
            if (mapLogin.containsKey(token))                                                                 //
                newPerson = mapLogin.get(token);                                                             //
            else                                                                                             //
                response.end("{\"status\":0}");                                                           //
            String number = situationInThisHoldWorkshop(workShopID,username1);                               //
            response.end("{\"status\":"+number+"}");                                                      //
        });                                                                                                  //
                                                                                                             //
        //////////////////////////////////////////////////////////////////////////////////////////////////////









        ///see allHistory Of this Grader
        //with this a[i we can see all history of one grader that have one request to work shop
        //if this person was managment of this workshope
        ////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                 //
        router.route(HttpMethod.POST,"/seeAllHistoryOfThisGrader").handler(rc ->{                  //
            JsonObject json = rc.getBodyAsJson();                                                     //
            HttpServerResponse response = rc.response();                                              //
            String token = json.getString("token");                                              //
            if(mapLogin.containsKey(token))                                                           //
                newPerson = mapLogin.get(token);                                                      //
            else {                                                                                    //
                response.end("{\"status\":0}");                                                    //
            }                                                                                         //
            Managment managment = new Managment();                                                    //
            if(!newPerson.is_this_role_in_our_person(managment)){                                     //
                response.end("{\"status\":0}");                                                    //
            }                                                                                         //
            managment = (Managment) newPerson.findOurType("3");                                //
            if (!isthisMangmentOfTHisWorkShop(managment.id,json.getInteger("IdWorkShop")))       //
                response.end("{\"status\":3}");//permissionDenaid                                  //
                                                                                                      //
            JsonObject HistoryOfHim = findHistoryOfGreater(json.getInteger("idGreater"));        //
            response.end("{\"status\":1\"information\":"+HistoryOfHim+"}");                        //
        });                                                                                           //
                                                                                                      //
        ///////////////////////////////////////////////////////////////////////////////////////////////





        //////////////see all recent work shop
        ///////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                            //
        router.route(HttpMethod.GET,"/seeAllRecentWorkShop").handler(rc ->{   //
            JsonObject json = seeAllRecentWorkShop();                            //
            HttpServerResponse response = rc.response();                         //
            response.end("{\"status\":1,\"seeAllRecent\":"+json+"}");         //
        });                                                                      //
        ///////////////////////////////////////////////////////////////////////////










        ////////see all workshop in data save
        ////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                   //
        router.route(HttpMethod.GET,"/seeAllWorkShop").handler(rc ->{                //
            JsonObject json = seeAllWorkShop();                                         //
            HttpServerResponse response = rc.response();                                //
            response.end("{\"status\":1,\"seeAllRecent\":"+json+"}");                //
        });                                                                             //
                                                                                        //
        // /////////////////////////////////////////////////////////////////////////////




        ///////////see all holder workshop that excuteing
        //////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                               //
        router.route(HttpMethod.GET,"/seeAllHolderWorkShop").handler(rc ->{      //
            JsonObject json = seeAllHolderWorkShop();                               //
            HttpServerResponse response = rc.response();                            //
            response.end("{\"status\":1,\"seeAllRecent\":"+json+"}");            //
        });                                                                         //
        //////////////////////////////////////////////////////////////////////////////









        //////////////////////////////////////////////////////////////////check validation code
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                //
        router.route(HttpMethod.POST,"/CheckValidationCode").handler(rc -> {                                      //
            JsonObject json = rc.getBodyAsJson();                                                                    //
            HttpServerResponse response = rc.response();                                                             //
            response.putHeader("content-type", "application/json");                                           //
            String validCode = json.getString("ValidationCode");//getValidationCode                             //
            this.user = json.getString("user");                                                                 //
            for (int d = mapValidtionCode.size();d > 0 ; d--) {                                                      //
                if (mapValidtionCode.get(d).token.equals(validCode) && mapValidtionCode.get(d).user.equals(user)) {  //
                    JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()                                    //
                            .addPubSecKey(new PubSecKeyOptions()                                                     //
                                    .setAlgorithm("HS256")                                                           //
                                    .setPublicKey("keyboard cat")                                                    //
                                    .setSymmetric(true)));                                                           //
                                                                                                                     //
                    String token = provider.generateToken(new JsonObject().put("userName", user));                   //
                    mapLogin.put(token, new Person(user,email));                                                     //
                    SaveFIle.saveHashMap("maplagin123.ser", (HashMap) this.mapLogin);                       //
                    SaveFIle.saveHashMap("mapValiditionCode.ser", (HashMap) this.mapValidtionCode);         //
                    response.end("{\"status\":1 ,\"token\":" + token + "}");                                     //
                }                                                                                                   //
            }                                                                                                       //
            response.end("{\"status\":0}");                                                                      //
        });                                                                                                         //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////






        //////////////////////////////////////////////////////////////////////////////forgeetPass
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                                                                                       //
        router.route(HttpMethod.POST,"/forgetPass").handler(rc -> {                                                                                                      //
            JsonObject json = rc.getBodyAsJson();                                                                                                                           //
            HttpServerResponse response = rc.response();                                                                                                                    //
            response.putHeader("content-type", "application/json");                                                                                                  //
            ObjectMapper objectMapper = new ObjectMapper();                                                                                                                 //
                                                                                                                                                                            //
            if (json.containsKey("token") && mapLogin.containsKey(json.containsKey("token"))) {                                                                             //
                if (findInDataBase2(json.getJsonObject("person").getString("setUser"), json.getJsonObject("person").getString("setEmailAddress")) != null) {      //
                    try {                                                                                                                                                   //
                        newPerson = objectMapper.readValue(json.getJsonObject("person").toString(),Person.class);                                                           //
                    } catch (IOException e) {                                                                                                                               //
                        response.end("{\"status\":0}");                                                                                                                  //
                        e.printStackTrace();                                                                                                                                //
                    }                                                                                                                                                       //
                    updateInPersonINdataBase(newPerson);                                                                                                                    //
                    response.end("{\"status\":1}");                                                                                                                      //
                } else                                                                                                                                                      //
                    response.end("{\"status\":0}");                                                                                                                      //
            }                                                                                                                                                               //
            else                                                                                                                                                            //
                response.end("{\"status\":0}");                                                                                                                          //
        });                                                                                                                                                                  //
        //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////













        ////////////////////////////////////////////////////
        //signUp
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                               //
        router.route(HttpMethod.POST,"/signUp").handler(rc -> {                                                  //
            JsonObject json = rc.getBodyAsJson();                                                                   //
            HttpServerResponse response = rc.response();                                                            //
            response.putHeader("content-type", "application/json");                                          //
            ObjectMapper objectMapper = new ObjectMapper();                                                         //
            if (json.containsKey("token") && mapLogin.containsKey(json.getString("token"))){                   //
                newPerson = mapLogin.get(json.getString("token"));                                             //
                try {                                                                                               //
                    newPerson = objectMapper.readValue(json.getJsonObject("person").toString(),Person.class);       //
                    mapLogin.put(json.getString("token"),newPerson);                                           //
                    AddPersonTodataBase(newPerson);                                                                 //
                    dataSave.saveInFile();                                                                          //
                    response.end("{\"status\":1}");                                                              //
                                                                                                                    //
                } catch (IOException e) {                                                                           //
                    System.out.println("this person can't make");                                                   //
                    response.end("{\"status\":0}");                                                              //
                    e.printStackTrace();                                                                            //
                }                                                                                                   //
            }                                                                                                       //
            else                                                                                                    //
                response.end("{\"status\":0}");                                                                  //
        });                                                                                                         //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////




        ////request student canscel from student
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                             //
        router.route(HttpMethod.POST,"/requestStudentCancelFromStudent").handler(rc -> {                       //
                    JsonObject json = rc.getBodyAsJson();                                                         //
                    HttpServerResponse response = rc.response();                                                  //
                    response.putHeader("content-type", "application/json");                                //
                    ObjectMapper objectMapper = new ObjectMapper();                                               //
                    String token = json.getString("token");                                                  //
                    int requestID = json.getInteger("WorkShopID");                                           //
                    if (mapLogin.containsKey(token)) {                                                            //
                        newPerson = mapLogin.get(token);                                                          //
                    }                                                                                             //
                        else                                                                                      //
                        response.end("{\"status\":0}");                                                        //
                    if(deletRequestOf(requestID))                                                                 //
                        response.end("{\"status\":1}");                                                        //
                    else                                                                                          //
                        response.end("{\"status\":0}");                                                        //
                });                                                                                               //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////





        ////////////see all history of this grader

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                         //
        router.route(HttpMethod.POST,"/seeAllHistoryOfThisGrader").handler(rc ->{                          //
           JsonObject json = rc.getBodyAsJson();                                                              //
           HttpServerResponse response = rc.response();                                                       //
           String token = json.getString("token");                                                       //
           if(mapLogin.containsKey(token))                                                                    //
               newPerson = mapLogin.get(token);                                                               //
           else {                                                                                             //
               response.end("{\"status\":0}");                                                             //
           }                                                                                                  //
           Managment managment = new Managment();                                                             //
            if(!newPerson.is_this_role_in_our_person(managment)){                                             //
                response.end("{\"status\":0}");                                                            //
            }                                                                                                 //
            managment = (Managment) newPerson.findOurType("3");                                        //
            if (!isthisMangmentOfTHisWorkShop(managment.id,json.getInteger("IdWorkShop")))               //
                response.end("{\"status\":3}");//permissionDenaid                                          //
                                                                                                              //
            JsonObject HistoryOfHim = findHistoryOfGreater(json.getInteger("idGreater"));                //
            response.end("{\"status\":1\"information\":"+HistoryOfHim+"}");                                //
        });                                                                                                   //
        //////////////////////////////////////////////////////////////////////////////////////////////////////








        /////////////////////////////////////////////////see all greater request one work shop
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());                                                        //
        router.route(HttpMethod.POST,"/seeAllGraderRequestOneWorkShop").handler(rc ->{                    //
            JsonObject jsonObject = rc.getBodyAsJson();                                                      //
            HttpServerResponse response = rc.response();                                                     //
            String token =  jsonObject.getString("token");                                              //
                                                                                                             //
            if(mapLogin.containsKey(token)) {
                newPerson = mapLogin.get(token);
            }
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop"))) {
                response.end("{\"status\":3}");//permissionDenaid
                return;
            }
            JsonObject AllGreaterRequest = seeAllRequestGreater(jsonObject.getInteger("IdWorkShop"));

            response.end("{\"status\":1,\"information\":"+AllGreaterRequest.toString()+"}");

            /////////////////////////////////////////////////////////////////////////////////////////





        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllStudentRequestOneWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");

            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid

            JsonObject allRequestStudnet = seeAllRequestStudent(jsonObject.getInteger("WorkShopId"));
            if (allRequestStudnet == null)
                response.end("{\"status\":3}");//we haven't any Student Request
            response.end("{\"status\":1\"information\":"+allRequestStudnet+"}");

        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////















        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/seeAllHoldWorkShopThatManagmentHave").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");

            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
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
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            if (requestStudent.getPay() instanceof Installment){
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



        ///////////////////////////////////////////////////////////////////////





        ///////////////////////////////////////////////////////////////////////////
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
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            if (requestStudent.getPay() instanceof Installment){
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



        /////////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////////////////////
        //////rejectRequest for grader
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/RejectRequestGrader").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            Grader_Request graderRequest = new Grader_Request();
            if((graderRequest = getOneRequestGreater(jsonObject.getInteger("RequestGraderId")))== null) {
                response.end("{\"status\":0}");
                return;
            }
            if (graderRequest.getHoldWorkShop().getId() != jsonObject.getInteger("IdWorkShop")){
                response.end("{\"status\":3}");
                return;
            }
            if (!graderRequest.getAccetply().equals(Accetply.Reject)) {
                graderRequest.setAccetply(Accetply.Reject);
                dataSave.saveInFile();
                response.end("{\"status\":1}");
                return;
            }
            response.end("{\"status\":0}");
            return;

        });

        ///////////////////////////////////////////////////////////////////////////////////






        //////////////////////////////////////////////////////////////////////////////////////////////



        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AcceptRequestGreater").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            int numberGroup = jsonObject.getInteger("GroupNumber");
            String groupName = jsonObject.getString("GroupName");
            int numberIdWorkShop = jsonObject.getInteger("IdGroup");
            GroupG groupG;
            if((groupG = getOnGroupFromDataBase(numberGroup,groupName,numberIdWorkShop))== null)
                    response.end("{\"status\":0}");

            Grader_Request graderRequest;
            if((graderRequest = getOneRequestGreater(jsonObject.getInteger("RequestGreaterId")))== null)
                response.end("{\"status\":0}");
            if (newGraderRequest.getAccetply() != Accetply.Accept) {
                newGraderRequest.setAccetply(Accetply.Accept);
                GroupStatus groupStatus = new GroupStatus(groupG, graderRequest.getGrader());
                AddNewGroupStatusToDatabase(groupStatus);
                response.end("{\"status\":1}");
            }
            response.end("{\"status\":0}");

        });






        ////////////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AcceptRequestStudent").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            RequestStudent requestStudent;
            if((requestStudent = getOneRequestStudent(jsonObject.getInteger("RequestStudentId")))== null)
                response.end("{\"status\":0}");
            if(requestStudent.getPay().isPayComplite()) {
                int numberGroup = jsonObject.getInteger("GroupNumber");
                String groupName = jsonObject.getString("GroupName");
                int numberIdWorkShop = jsonObject.getInteger("IdGroup");
                GroupG groupG;
                if((groupG = getOnGroupFromDataBase(numberGroup, groupName, numberIdWorkShop))== null)
                    response.end("{\"status\":0}");
                if (requestStudent.getAccetply() != Accetply.Accept) {
                    requestStudent.setAccetply(Accetply.Accept);
                    GroupStatus groupStatus = new GroupStatus(groupG, requestStudent.getStudent());
                    AddNewGroupStatusToDatabase(groupStatus);
                    response.end("{\"status\":1}");
                }
                response.end("{\"status\":0}");
            }
            else
                response.end("{\"status\":3}");//this person don't pay it's payment Complite
        });




        //////////////////////////////////////////////////////////////////////////////////////////






        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewGroup").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            ObjectMapper objectMapper = new ObjectMapper();
            GroupG groupG = new GroupG();
            JsonObject object43 = new JsonObject();
            object43 = jsonObject.getJsonObject("Group");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenaid
            groupG = new GroupG(object43.getString("name"),object43.getString("head"),object43.getInteger("number"));
            HoldWorkShop holdWorkShop = findThisHoldWorkShop(jsonObject.getInteger("IdWorkShop"));
            groupG.setHoldWorkShop(holdWorkShop);
            if(AddNewGroupTodatabase(groupG)) {
                response.end("{\"status\":1}");
            }
            response.end("{\"status\":0}");
        });




        //////////////////////////////////////////////////////////////////////////////////////////AbC form


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewABCForm").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
            }
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            JsonArray qustionABC = jsonObject.getJsonArray("question");
            ArrayList<String> stringArrayList = new ArrayList<String>();
            for (int i = 0 ; i < qustionABC.size();i++){
                stringArrayList.add(qustionABC.getString(i));
            }
            AbsForm absFormOfMe = new AbsForm(stringArrayList);
            if(AddNewAbCForm(absFormOfMe))
                response.end("{\"status\":1,\"id_number\":"+absFormOfMe.getNumber()+"}");
            response.end("{\"status\":0}");
        });





        ////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////makeNewForm
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewForm").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            int id_number_AbcForm = jsonObject.getInteger("id_number");
            int id_HoldWorkShop = jsonObject.getInteger("idHoldWorkShop");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            Managment managment = new Managment();
            if(!newPerson.is_this_role_in_our_person(managment)){
                response.end("{\"status\":0}");
            }
            managment = (Managment) newPerson.findOurType("3");
            if (!isthisMangmentOfTHisWorkShop(managment.id,jsonObject.getInteger("IdWorkShop")))
                response.end("{\"status\":3}");//permissionDenai
            AbsForm absFormOfMe = findAbcFormFromDataBaseById(id_number_AbcForm);
            if (absFormOfMe == null){
                response.end("{\"status\":0}");
            }
            HoldWorkShop holdWorkShop = findThisHoldWorkShop(id_HoldWorkShop);
            Form form1 = new Form(absFormOfMe,holdWorkShop);
            if(SaveFormInDataBase(form1))
                response.end("{\"status\":1}");
            response.end("{\"status\":0}");
        });


        //////////////////////////////////////////////////////////////





        /////////////////////////////////////////////////////see all abc form


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.GET,"/seeAllabcform").handler(rc ->{
            JsonObject json = null;
            HttpServerResponse response = rc.response();
            try {
                json = seeAllABCformInDataBase();
            } catch (JsonProcessingException e) {
                response.end("{\"status\":0}");
                e.printStackTrace();
            }

            response.end("{\"status\":1,\"seeAllRecent\":"+json+"}");
        });




        //////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////Answer to Question

        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AnswerToQuestion").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            String EachForm = jsonObject.getString("EachForm");
            String user = jsonObject.getString("user_Form_TO");
            int IdFormfullItQuestion = jsonObject.getInteger("Id_Form_full_It_Question");

            Form form = findFromInDataBaseBYID(IdFormfullItQuestion);
            JsonArray answ = jsonObject.getJsonArray("answer");
            ArrayList<String> stringArrayList = new ArrayList<String>();
            for (int i = 0 ; i < answ.size();i++){
                stringArrayList.add(answ.getString(i));
            }
            Grader grader = new Grader();
            Grader grader3;
            Student student = new Student();
            Student student3;
            Managment managment;
            GroupStatus groupStatus1;
            GroupStatus groupStatus3;
            GroupG groupG1 = null;
            GroupG groupG2;
            boolean firstCondition = false;
            boolean secendCondition = false;
            boolean thirdCondition  = false;
            Person person2 = findPersonByUser(user);
            ArrayList<GroupStatus> groupStatus2 = getALLGroupStatuseINdataBaseOfThisWorkShope(form.holdWorkShop.getId());
            if (EachForm.equals("1")){//grader for student
                if (newPerson.is_this_role_in_our_person(grader)){
                    grader = (Grader) newPerson.findOurType("2");
                    if(person2.is_this_role_in_our_person(student)){
                        student = (Student) person2.findOurType("1");
                        for(GroupStatus g : groupStatus2){
                            if(g.getRoleOfWorkShape() instanceof Student){
                                student3 = (Student) g.getRoleOfWorkShape();
                                if (student.getId() == student3.getId()){
                                    firstCondition = true;
                                    groupG1 = g.getGroupG();
                                }
                            }
                            if (g.getRoleOfWorkShape() instanceof Grader){
                                grader3 = (Grader) g.getRoleOfWorkShape();
                                if (grader3.getId() == grader.getId()) {
                                    secendCondition = true;
                                    groupG2 = g.getGroupG();
                                    if (groupG1 != null) {
                                        if (groupG1.getId() == groupG2.getId())
                                            thirdCondition = true;
                                    }
                                }
                            }
                        }
                        if (firstCondition && secendCondition && thirdCondition) {
                            Qualifition qualifition = new Qualifition(stringArrayList, grader,student,form);
                            if (addQualififtionTodataBase(qualifition))
                                response.end("{\"status\":1}");
                            else
                                response.end(("{\"status\":0}"));
                        }
;

                    }
                    else {
                        System.out.println("This Person Havent this Role for give massage");
                        response.end("{\"status\":0}");
                    }
                }
                else{
                    System.out.println("THIS PERSON HAVENT THIS ROLE");
                    response.end("{\"status\":0}");
                }
            }
            if (EachForm.equals("2")) {// Student for grader
                if (newPerson.is_this_role_in_our_person(student)) {
                    student = (Student) newPerson.findOurType("1");
                    if (person2.is_this_role_in_our_person(grader)) {
                        for (GroupStatus s : groupStatus2) {
                            if (s.getRoleOfWorkShape() instanceof Grader) {
                                grader = (Grader) s.getRoleOfWorkShape();
                                for (GroupStatus g : groupStatus2) {
                                    if (g.getRoleOfWorkShape() instanceof Grader) {
                                        grader3 = (Grader) g.getRoleOfWorkShape();
                                        if (grader3.getId() == grader.getId()) {
                                            firstCondition = true;
                                            groupG1 = g.getGroupG();
                                        }
                                    }
                                    if (g.getRoleOfWorkShape() instanceof Student) {
                                        student3 = (Student) g.getRoleOfWorkShape();
                                        if (student3.getId() == grader.getId()) {
                                            secendCondition = true;
                                            groupG2 = g.getGroupG();
                                            if (groupG1 != null) {
                                                if (groupG1.getId() == groupG2.getId()) {
                                                    thirdCondition = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (firstCondition && secendCondition && thirdCondition) {
                                    Qualifition qualifition = new Qualifition(stringArrayList, grader, student, form);
                                    if (addQualififtionTodataBase(qualifition))
                                        response.end("{\"status\":1}");
                                    else
                                        response.end(("{\"status\":0}"));
                                }
                            }
                        }
                    } else {
                        System.out.println("THIS PERSON HAVENT THIS ROLE");
                        response.end("{\"status\":0}");
                    }
                } else {
                    System.out.println("THIS PERSON HAVENT THIS ROLE");
                    response.end("{\"status\":0}");
                }
            }
            if (EachForm.equals("3")){// student for managment

            }
            if (EachForm.equals("4")){// Managment for Grader

            }
            if (EachForm.equals("5")){// grader for managment

            }
        });










        ///////////////////////////////////////////////////////////////////////////////////






        ////////////////////////////////////////////////////////////////////see answer of each person to their formes








        ////////////////////////////////////////////////////////////////////////////////////
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
                return;
            }
            Addmin addmin2 = new Addmin();
            if(!newPerson.is_this_role_in_our_person(addmin2)){
                response.end("{\"status\":0}");
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Workshop workshop = null;
            try {
                workshop = objectMapper.readValue(jsonObject.getJsonObject("WorkShop").toString(),Workshop.class);
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
                return;
            }
            if(!AddNewWorkShopTOdataBase(workshop)) {
                response.end("{\"status\":0}");
                return;
            }
            ArrayList<Workshop>workshopsPrerequisite = findThisWorkShop(jsonObject.getJsonArray("WorkShopPrerequisite"));
            for(Workshop i: workshopsPrerequisite){
                AddNewReqirmentsToDataBase(new Requirments(i,workshop, Relation.Prerequisite));
            }
            ArrayList<Workshop>workshopsNeed = findThisWorkShop(jsonObject.getJsonArray("WorkShopNeed"));
            for (Workshop i: workshopsNeed){
                AddNewReqirmentsToDataBase(new Requirments(i,workshop,Relation.TheNeed));
            }
            dataSave.saveInFile();
            response.end("{\"status\":1}");
        });




        /////////////////////////////////////////////////////////////////////////////////////






        ////////////////////////////////////////////////////////////////////////////////Make form



        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/MakeNewHoldWorkShop").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                response.end("{\"status\":0}");
                return;
            }
            Addmin addmin3 = new Addmin();
            if(!newPerson.is_this_role_in_our_person(addmin3)){
                response.end("{\"status\":0}");
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Person person = findPersonByUser(jsonObject.getString("user"));
            if(person == null) {
                response.end("{\"status\":4}");//can't find this person in dataBase
                return;
            }
            HoldWorkShop holdWorkShop = new HoldWorkShop();
            try {
                JsonObject jsonObject1 = jsonObject.getJsonObject("HoldWorkShop");
                holdWorkShop.setPayMoneyInHowTimes(jsonObject1.getInteger("payMoneyInHowTimes"));
                holdWorkShop.setName(jsonObject1.getString("name"));
                holdWorkShop.setIs_installment(jsonObject1.getBoolean("is_installment"));
                holdWorkShop.setMoney(jsonObject1.getLong("money"));
                holdWorkShop.setPayMoneyInHowTimes(jsonObject1.getInteger("payMoneyInHowTimes"));
                holdWorkShop.setStart(new SimpleDateFormat("yyyy/MM/dd").parse(jsonObject1.getString("start")));
                holdWorkShop.setEnd(new SimpleDateFormat("yyyy/MM/dd").parse(jsonObject1.getString("end")));
                holdWorkShop.setHourEnd(LocalTime.parse(jsonObject1.getString("hourEnd")));
                holdWorkShop.setHourStart(LocalTime.parse((jsonObject1.getString("hourStart"))));

            } catch (ParseException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
                return;
            }
            Workshop workshop = findWorkShopeInDataBase(jsonObject.getInteger("idWorkShop"));
            if (workshop == null){
                response.end("{\"status\":0}");
                return;
            }
            holdWorkShop.setManagment((Managment) person.findOurType("3"));
            holdWorkShop.setWorkshop(workshop);
            if(!addNewHoldWorkShop(holdWorkShop)) {
                response.end("{\"status\":0}");
                return;
            }
            dataSave.saveInFile();
            response.end("{\"status\":1}");
            return;
        });
        /////////////////////////////////////////////////////////////////////////////






        /////////////////////////////////////////////////////////admin see all person


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AdminSeeAllPerson").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = new JsonObject();
            Addmin addmin4 = new Addmin();
            if(mapLogin.containsKey(token)) {
                newPerson = mapLogin.get(token);
            }
            else {
                response.end("{\"status\":0}");
                return;
            }
            if(!newPerson.is_this_role_in_our_person(addmin4)){
                response.end("{\"status\":0}");
                return;
            }
            ArrayList<Person> persons = allPersonIndataBase();
            int dd = 0;
            Addmin addmin3 = (Addmin)newPerson.findOurType("0");
            for(Person i : persons){
                if(!i.is_this_role_in_our_person(addmin4)|| (addmin3.getAdminType() == AdminType.General)) {
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
            response.end("{\"status\":1,\"information\":"+json+"}");
            return;
        });

        ////////////////////////////////////////////////////////////////////////////////













        /////////////////////////////////////////////////////////////////////// student of one group by user greater


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/studentOfOneGroupByUSerGreater").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            int idHoldWorkShope = jsonObject.getInteger("IdWorkShop");
            ArrayList<GroupStatus>groupStatusArrayList = getALLGroupStatuseINdataBaseOfThisWorkShope(idHoldWorkShope);
            JsonObject json = null;
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else {
                System.out.println("we have'nt this person in mplogin ");
                response.end("{\"status\":0}");
            }
            Grader grader = new Grader();
            if(!newPerson.is_this_role_in_our_person(grader)){
                System.out.println("this person have'nt any greateries");
                response.end("{\"status\":0}");
            }
            GroupG groupG = null;
            for (GroupStatus i : groupStatusArrayList){
                if (i.getGroupG().getHead().equals(newPerson.getUser()))
                        groupG = i.getGroupG();
            }
            if (groupG == null){
                System.out.println("this person can't see person of each of groupG");
                response.end("{\"status\":0}");
            }
            int dd = 0;
            Student student = null;
            Student student1 = null;
            ArrayList<Person>persons = findAllPersonOfThisHOldWorkShop(idHoldWorkShope);
            for(Person i : persons) {
                for (GroupStatus s : groupStatusArrayList) {
                    if (s.getRoleOfWorkShape().equals(Student.class)) {
                        student = (Student) s.getRoleOfWorkShape();
                        student1 = (Student) i.findOurType("1");
                        if (student1.getId() == student.getId()) {
                            JsonObject jsonObject1 = new JsonObject().put("setId", i.getId())
                                    .put("setGender", i.getGender())
                                    .put("setUser", i.getUser())
                                    .put("setName", i.getName())
                                    .put("setNationalCode", i.getNationalCode())
                                    .put("setLastName", i.getLastName())
                                    .put("setDate_birthday", i.getDate_birthday())
                                    .put("setActivity", i.getIs_Active());
                            json.put(String.valueOf(dd), jsonObject1);
                            dd++;
                        }
                    }
                }
            }
            response.end("{\"status\":1,\"information\":"+json+"}");
        });
    //////////////////////////////////////////////////////////////////////////////////////////////////////////













        ///////////////////////////////////////////////////////////////////////////////Admin change person activity


        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/AdminChangePersonActivity").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = null;
            Addmin addmin4 = new Addmin();
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(addmin4)){
                response.end("{\"status\":0}");
            }
            Addmin addmin2 = (Addmin)newPerson.findOurType("0");
            Person person = findPersonIndataBase(json.getString("user"),json.getString("nationCode"));
            if(person == null)
                response.end("{\"status\":0}");
            if(!person.is_this_role_in_our_person(addmin4) || addmin2.getAdminType() == AdminType.General){
                boolean activity = json.getBoolean("activity");
                person.setIs_Active(activity);
                response.end("{\"status\":1}");
            }
            response.end("{\"status\":13}");//Don't Access
        });



        /////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////// logout all person in System
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/LogoutAllPersonInSystem").handler(rc ->{
            JsonObject jsonObject = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            String token =  jsonObject.getString("token");
            JsonObject json = null;
            Addmin addmin3 = new Addmin();
            if(mapLogin.containsKey(token))
                newPerson = mapLogin.get(token);
            else
                response.end("{\"status\":0}");
            if(!newPerson.is_this_role_in_our_person(addmin3)){
                response.end("{\"status\":0}");
            }
            Addmin addmin4 = (Addmin)newPerson.findOurType("0");
            String Level = jsonObject.getString("Level");
            List keys = new ArrayList(mapLogin.keySet());
            if(Level.equals("AllPerson")){
                if (addmin4.getAdminType() == AdminType.General) {
                    mapLogin.clear();
                    mapLogin.put(jsonObject.getString("token"), newPerson);
                }
            }
            else{
                Addmin addmin5 = new Addmin();
                for (int i = 0 ; i < keys.size();i++){
                    if (!mapLogin.get(keys.get(i)).is_this_role_in_our_person(addmin5))
                        mapLogin.remove(keys.get(i));
                }

            }
            response.end("{\"status\":Ok}");
        });
        ///////////////////////////////////////////////////////////////////////////////////



        httpServer
                .requestHandler(router::accept)
                .listen(5000);

        }

    private JsonObject seeAllHolderWorkShop() {
        ArrayList<HoldWorkShop> holdWorkShop = getALLHoldWorkShop();
        int d = 0;
        Date date = new Date();
        JsonObject jsonObject = new JsonObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        for(HoldWorkShop i : holdWorkShop){
                JsonObject jsonObject1 = new JsonObject();
                if (i.getManagment() != null) {
                    Person person = findPersonOfThisManagment(i.getManagment().id);
                    jsonObject1.put("NameWorkShop", i.getName())
                            .put("Management", person.getName() + "  " + person.getLastName())
                            .put("DateStart", dateFormat.format(i.getStart()).toString())
                            .put("DateEnd", dateFormat.format(i.getEnd()).toString())
                            .put("HourStart",i.getHourStart().toString())
                            .put("HourEnd",i.getHourEnd().toString())
                            .put("Money", i.getMoney())
                            .put("IsInstallment", i.getIs_installment())
                            .put("Title", i.getWorkshop().getTitle())
                            .put("id", i.getId())
                            .put("Description", i.getWorkshop().getDescription());
                    jsonObject.put(String.valueOf(d), jsonObject1);
                    d++;
                } else {
                    jsonObject1.put("NameWorkShop", i.getName())
                            .put("Management", "Unknown")
                            .put("DateStart", dateFormat.format(i.getStart()).toString())
                            .put("DateEnd", dateFormat.format(i.getEnd()).toString())
                            .put("HourStart",i.getHourStart().toString())
                            .put("HourEnd",i.getHourEnd().toString())
                            .put("Money", i.getMoney())
                            .put("IsInstallment", i.getIs_installment())
                            .put("Title",i.getWorkshop().getTitle())
                            .put("id", i.getId())
                            .put("Description", i.getWorkshop().getDescription());
                    jsonObject.put(String.valueOf(d), jsonObject1);
                    d++;

                }
            }
        return jsonObject;
    }


    private JsonObject seeAllWorkShop() {
        ArrayList<Workshop> workshopArrayList = dataSave.getWorkshops();
        int d = 0;
        Date date = new Date();
        JsonObject jsonObject = new JsonObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        for(Workshop i : workshopArrayList){
            JsonObject jsonObject1 = new JsonObject();
                jsonObject1.put("Title", i.getTitle())
                        .put("id", i.getId())
                        .put("Description", i.getDescription());
                jsonObject.put(String.valueOf(d), jsonObject1);
                d++;
        }
        return jsonObject;
    }

    private boolean addQualififtionTodataBase(Qualifition qualifition) {
        return true ;
    }

    private Form findFromInDataBaseBYID(int idFormfullItQuestion) {
        return null;
    }

    private JsonObject seeAllABCformInDataBase() throws JsonProcessingException {
        return dataSave.seeAllABCformInDataBase();
    }

    private boolean SaveFormInDataBase(Form form1) {
        return dataSave.SaveFormInDataBase(form1);
    }

    private AbsForm findAbcFormFromDataBaseById(int id_number_abcForm) {
        return dataSave.findAbcFormFromDataBaseById(id_number_abcForm);
    }

    private boolean AddNewAbCForm(AbsForm absFormOfMe) {
        return  dataSave.AddNewAbCForm(absFormOfMe);
    }

    private ArrayList<Person> findAllPersonOfThisHOldWorkShop(int idHoldWorkShope) {
        ArrayList<GroupStatus> groupStatusArrayList = getALLGroupStatuseINdataBaseOfThisWorkShope(idHoldWorkShope);
        Student student = null;
        ArrayList<Person> personArrayList = new ArrayList<Person>();
        Person person = null;
        for (GroupStatus i : groupStatusArrayList){
            if (i.getRoleOfWorkShape().getClass().equals(Student.class)){
                student = (Student) i.getRoleOfWorkShape();
                person = findPersonOfThisStudent(student.getId());
                personArrayList.add(person);
            }
        }
        return  personArrayList;
    }
    private String situationInThisHoldWorkshop(int workShopID, String username1) {
        HoldWorkShop holdWorkShop = findThisHoldWorkShop(workShopID);
        Person person = findPersonByUser(username1);
        Managment managment = null;
        Grader grader = null;
        Student student = null;
        Grader grader1 = new Grader();
        Student student1 = new Student();
        Grader_Request graderRequest = null;
        RequestStudent requestStudent = null;
        grader = (Grader) person.findOurType("2");
        student = (Student) person.findOurType("1");
        Managment managment2 = new Managment();
        if (person.is_this_role_in_our_person(managment2)) {
            managment = (Managment) person.findOurType("3");
            if (holdWorkShop.getManagment() != null) {
                if (holdWorkShop.getManagment().id == managment.id)
                    return "managment";
            }
        }
        ArrayList<Requests>  allRequestArrayList = dataSave.seeAllRequestArrayList(workShopID);
        ArrayList<GroupStatus> groupStatusArrayListr = getALLGroupStatuseINdataBaseOfThisWorkShope(workShopID);

        for(Requests i : allRequestArrayList){
            if(i instanceof RequestStudent){
                requestStudent = (RequestStudent) i;
                if (requestStudent.getStudent().getId() == student.id) {
                    if(requestStudent.getAccetply().equals(Accetply.Accept)){
                        for(GroupStatus s : groupStatusArrayListr){
                            if(s.getRoleOfWorkShape() instanceof Student) {
                                student1 = (Student) s.getRoleOfWorkShape();
                                if (student1.id == student.getId()){
                                    if(s.getStatues()>10)
                                        return "PsssStudent";
                                    else if(s.getStatues()<10)
                                        return "FailStudent";
                                    else if (s.getStatues() == -1)
                                        return "AcceptStudent_PandingMark";
                                }
                            }
                        }
                        return "weHaveMistack";
                    }
                    if (requestStudent.getAccetply().equals(Accetply.Reject)){
                        return "rejectStudent";
                    }
                    else if (requestStudent.getAccetply().equals(Accetply.Pending)){
                        return "PendingRequest";
                    }
                }
            }
            if (i instanceof Grader_Request){
                graderRequest = (Grader_Request) i;
                if (graderRequest.getGrader().getId() == grader.getId()){
                    if (graderRequest.getAccetply().equals(Accetply.Accept)) {
                        for (GroupStatus s : groupStatusArrayListr) {
                            if (s.getGroupG().getHead().equals(person.getUser()))
                                return "AcceptHeadGrader";
                        }
                        return "AcceptGrader";
                    }
                    if (graderRequest.getAccetply().equals(Accetply.Reject))
                        return "RejectGrader";
                    if (graderRequest.getAccetply().equals(Accetply.Pending))
                        return "PendingGrader";
                }
            }
        }
        return "haveNoActivity";
    }

    private ArrayList<GroupStatus> getALLGroupStatuseINdataBaseOfThisWorkShope(int WorkShopId) {
        return dataSave.getALLGroupStatuseINdataBaseOfThisWorkShope(WorkShopId);
    }

    private boolean AddNewGroupTodatabase(GroupG groupG) {
        return  dataSave.AddNewGroupTOdatabase(groupG);
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
            Student student = (Student)newPerson.findOurType("1");
            Grader grader = (Grader)newPerson.findOurType("2");
            ArrayList<RequestStudent>  requestStudents= getALLStudentRequestThatThisPersonSend(student.getId());
            ArrayList<Grader_Request> graderRequests = getALLGreaterRequestThatThisPersonSend(grader.getId());
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
            for (Grader_Request s : graderRequests){
                if (((s.getHoldWorkShop().getStart().before(start) && s.getHoldWorkShop().getEnd().after(start))|| (s.getHoldWorkShop().getStart().after(start) && s.getHoldWorkShop().getStart().before(end))) &&((s.getHoldWorkShop().getHourStart().isAfter(hourStart) && s.getHoldWorkShop().getHourStart().isBefore(hourEnd)) || (s.getHoldWorkShop().getHourStart().isBefore(hourStart) && s.getHoldWorkShop().getHourEnd().isAfter(hourStart)))){
                    jsonObjectGreater.put(String.valueOf(dd),s.getHoldWorkShop().getName());
                    dd++;
                }
            }
            if (jsonObjectGreater.isEmpty() && jsonObjectStudent.isEmpty())
                return jsonObjectMain;
            else
                return jsonObjectMain.put("grader",jsonObjectGreater)
                        .put("student",jsonObjectStudent);

    }

    private ArrayList<Grader_Request> getALLGreaterRequestThatThisPersonSend(int id) {
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

    private GroupG getOnGroupFromDataBase(int numberGroup, String groupName, int numberIdWorkShop) {
        return dataSave.getOneGroupFrommDataBase(numberGroup,groupName,numberIdWorkShop);
    }

    private Grader_Request getOneRequestGreater(Integer requestGreaterId) {
        return dataSave.getOneRequestGreater(requestGreaterId);
    }

    private void AddPersonTodataBase(Person newPerson) {
        dataSave.AddNewPersonTodataBase(newPerson);
    }

    private HoldWorkShop findThisHoldWorkShop(int id) {
        return dataSave.findThisHoldWorkShop(id);
    }

    private JsonObject seeAllRequestGreater(int id) {
        ArrayList<Grader_Request> graderRequests = findAllRequestGreater(id);
        JsonObject jsonObject = new JsonObject();
        int d = 0;
        for (Grader_Request i : graderRequests){
            JsonObject jsonObject1 = new JsonObject().put("idGreater",i.getGrader().getId());
            Person person = findPersonOfThisGreater(i.getGrader().getId());
            jsonObject1.put("name",person.getName());
            jsonObject1.put("lastName",person.getLastName());
            jsonObject1.put("userName",person.getUser());
            jsonObject1.put("id_requerst",i.getId());
            jsonObject1.put("state",i.getAccetply().toString());
            jsonObject.put(String.valueOf(d),jsonObject1);
            d++;
        }
        return jsonObject;
    }

    private Person findPersonOfThisGreater(int id) {
        return dataSave.findPersonOfThisGreater(id);
    }

    private ArrayList<Grader_Request> findAllRequestGreater(int id) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        for(HoldWorkShop i : holdWorkShop){
            if(i.getStart().after(date)) {
                JsonObject jsonObject1 = new JsonObject();
                if (i.getManagment() != null) {
                    Person person = findPersonOfThisManagment(i.getManagment().id);
                    jsonObject1.put("NameWorkShop", i.getName())
                            .put("Management", person.getName() + "  " + person.getLastName())
                            .put("DateStart", dateFormat.format(i.getStart()).toString())
                            .put("DateEnd", dateFormat.format(i.getEnd()).toString())
                            .put("HourStart",i.getHourStart().toString())
                            .put("HourEnd",i.getHourEnd().toString())
                            .put("Money", i.getMoney())
                            .put("IsInstallment", i.getIs_installment())
                            .put("Title", i.getWorkshop().getTitle())
                            .put("id", i.getId())
                            .put("Description", i.getWorkshop().getDescription());
                    jsonObject.put(String.valueOf(d), jsonObject1);
                    d++;
                } else {
                    jsonObject1.put("NameWorkShop", i.getName())
                            .put("Management", "Unknown")
                            .put("DateStart", dateFormat.format(i.getStart()).toString())
                            .put("DateEnd", dateFormat.format(i.getEnd()).toString())
                            .put("HourStart",i.getHourStart().toString())
                            .put("HourEnd",i.getHourEnd().toString())
                            .put("Money", i.getMoney())
                            .put("IsInstallment", i.getIs_installment())
                            .put("Title",i.getWorkshop().getTitle())
                            .put("id", i.getId())
                            .put("Description", i.getWorkshop().getDescription());
                    jsonObject.put(String.valueOf(d), jsonObject1);
                    d++;

                }
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
        return dataSave.AddToRequestListINDataBase(newRequest);
    }
    private boolean AddToRequestListINDataBase(Grader_Request graderRequest){
        return dataSave.AddToRequestListINDataBase(graderRequest);
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

    private Workshop findWorkShopeInDataBase(int id, String name) {
        return dataSave.findworkShopById(id);
    }
    private Workshop findWorkShopeInDataBase(int id){
        return dataSave.findworkShopById(id);
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
                if(i.getRoleOfWorkShape().getClass() == Grader.class){
                    Grader grader =(Grader)i.getRoleOfWorkShape();
                    if (grader.getId() == id){
                        GroupG groupG = i.getGroupG();
                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.put("NameWorkShop",i.getGroupG().getHoldWorkShop().getName());
                        jsonObject1.put("Title",i.getGroupG().getHoldWorkShop().getWorkshop().getTitle());
                        Person person = findPersonOfThisManagment(i.getGroupG().getHoldWorkShop().getId());
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

    private ArrayList<GroupG> getALLGroupINdataBase() {
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
