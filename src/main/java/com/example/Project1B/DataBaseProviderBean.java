package com.example.Project1B;

import com.example.Project1B.Server.MongoChatLogDAO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PostConstruct;
import javax.ejb.*;

@Singleton//jedna instancja
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)//w obrębie kontenera działa
public class DataBaseProviderBean {

    private MongoChatLogDAO chatLogDAO;

    @PostConstruct//uruchamia się zawsze jako pierwsza(zaraz po konstruktorze)
    public void init(){ chatLogDAO = new MongoChatLogDAO();}

    @Lock(LockType.READ)//ta metoda jest tylko do odczytu info dla kompilatora
    public MongoChatLogDAO getChatLogDAO() {
        return chatLogDAO;
    }
}
