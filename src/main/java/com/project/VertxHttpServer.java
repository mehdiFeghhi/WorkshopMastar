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
import io.vertx.ext.mongo.MongoClient;
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
        router.route(HttpMethod.POST,"/Updateperson").handler(rc ->{
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type","application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                newPerson = objectMapper.readValue(json.toString(),Person.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (AddToDataBass(newPerson)){
                JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm("HS256")
                                .setPublicKey("keyboard cat")
                                .setSymmetric(true)));

                String token = provider.generateToken(new JsonObject().put("userName",user));
                mapLogin.put(token,newPerson);
                response.end("{\"status\":1\"validation\": "+token+"}");}
            else
                response.end("{\"status\":0}");
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST,"/editPerson").handler(rc ->{
            JsonObject json = rc.getBodyAsJson();
            HttpServerResponse response = rc.response();
            response.putHeader("content-type","application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            Person newPerson = null;
            try {
                newPerson = objectMapper.readValue(json.toString(),Person.class);
                if (this.searchInDataBase(newPerson.getUser())) {
                    updateInPersonINdataBase(newPerson);
                    response.end("{\"status\":1}");
                }
                else
                    response.end("{\"status\":0}");
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
            }
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
            String Workshop = json.getString("workShop");
            try {
                newHoldWorkShop = objectMapper.readValue(Workshop, HoldWorkShop.class);
                if (this.isThisPersonIsInOneOfTheGroupOfThisWorkShop(newHoldWorkShop)) {
                    if (pay.equals("2")) {
                        if (!newHoldWorkShop.getIs_installment())
                            response.end("{\"status\":3}");
                    }
                    if (pay.equals("2")){
                        Installment payment = new Installment(newHoldWorkShop.getMoney(),"ID",newHoldWorkShop.getPayMoneyInHowTimes());
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
            } catch (IOException e) {
                e.printStackTrace();
                response.end("{\"status\":0}");
            }
        });
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/forgetUserName").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            this.user = json.getString("user");
            this.email = json.getString("email");
            HttpServerResponse response = rc.response();
            response.putHeader("content-type", "application/json");
            final String codeValidation ;
            if((newPerson = findInDataBase2(user,email)) != null) {
                codeValidation = make_Password(9);
                OurEmail ourEmail = new OurEmail();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ourEmail.sendMail(email,codeValidation);
                    }
                });
                t.start();
                response.end("{\"status\": 1 }");
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

        httpServer
                .requestHandler(router::accept)
                .listen(5000);

        }

    private boolean deletRequestOf(int requestID) {
    }

    private boolean deletRequestOf(Person newPerson, String workShopHandlerID) {
       return true;
    }

    private boolean AddToRequestListINDataBase(RequestStudent newRequestStudent) {
        return true;
    }

    private Object findWorkShopeInDataBase(String id, HoldWorkShop newHoldWorkShop, RoleOfWorkShape ourType) {
    }

    private boolean isThisPersonIsInOneOfTheGroupOfThisWorkShop(HoldWorkShop newWorkshop) {
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


    static String make_Password(int len) {
        System.out.println("Generating password using random() : ");
        System.out.print("Your new password is : ");

        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";


        String values = Capital_chars + numbers ;

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
