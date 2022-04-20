package com.example.Project1B;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/chat-history")
public class CharHistoryController {

    @EJB
    DataBaseProviderBean dataBaseProviderBean; //wstrzykniecie obiektu

    @GET
    @Produces("text/plain")//co generuje odtsaniemy zwykły tekst
    public String getChatHistory() {
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        MongoCollection<Document> mongoCollection  = dataBaseProviderBean.getMongoDatabase().getCollection("test");
        return "XXXXXX" + mongoCollection.find().first().toJson();
    }
}