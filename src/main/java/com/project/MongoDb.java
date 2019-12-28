package com.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public class MongoDb {
    MongoClient client;
    public MongoDb(MongoClient client){
        this.client = client;
    }

    public void findThisPerson(String user, String pass, Handler<AsyncResult<List<JsonObject>>> handler) {
        JsonObject find = null;
        JsonObject document = new JsonObject().put("user",user)
                                            .put("pass",pass)
                                            .put("_id",user);
        this.client.find("Persons",document,handler);

    }
    public void AddPersonMongo(Person person,Handler<AsyncResult<List<JsonObject>>> handler) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String AsStringinFormatPerson = objectMapper.writeValueAsString(person);
        JsonObject jsonObject = new JsonObject(AsStringinFormatPerson);
        jsonObject = jsonObject.put("_id",person.getUser());
        this.client.insert("Persons",jsonObject,res ->{

            if (res.succeeded()) {
                String id = res.result();
                System.out.println("Inserted book with id " + id);
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
