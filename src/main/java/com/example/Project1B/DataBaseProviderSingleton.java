package com.example.Project1B;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.annotation.PostConstruct;

//Zwykły wzorzec projektowy singleton;

public class DataBaseProviderSingleton {

    private static MongoDatabase mongoDatabase = null;

    public static MongoDatabase createMongoDatabase(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        return mongoClient.getDatabase("test");// ta linijka == nasze pole
    }


    public synchronized static MongoDatabase getMongoDatabase() { //static dlatego chcemy sie odwolywac bez tworzenia nowej instancji klasy
        if(mongoDatabase == null){
            mongoDatabase = createMongoDatabase();
        }
        return mongoDatabase;
    }
}
