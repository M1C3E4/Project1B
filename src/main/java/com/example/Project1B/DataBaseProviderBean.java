package com.example.Project1B;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


import javax.annotation.PostConstruct;
import javax.ejb.*;

@Singleton//jedna instancja
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)//w obrębie kontenera działa

public class DataBaseProviderBean {

    private MongoDatabase mongoDatabase = null;

    @PostConstruct//uruchamia się zawsze jako pierwsza(zaraz po konstruktorze)
    public void init(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        mongoDatabase = mongoClient.getDatabase("test");// ta linijka == nasze pole

    }

    @Lock(LockType.READ)//ta metoda jest tylko do odczytu info dla kompilatora
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
