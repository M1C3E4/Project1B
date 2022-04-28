package com.example.Project1B.Server;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.example.Project1B.DataBaseProviderSingleton;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;

public class MongoChatLogDAO {

    private static final String CHAT_LOG_COLLECTION = "chat-logs";

    public void saveMessage(MessageDTO messageDTO) {
        Objects.requireNonNull(messageDTO);
        DataBaseProviderSingleton.getMongoDatabase().getCollection(CHAT_LOG_COLLECTION).insertOne(messageDTO.toDocument());
    }

    //Wyciagamy wszystkie wiadomosci wymienione pomiedzy dwoma uzytkownikami
    public List<MessageDTO> getPrivateMessages(String from, String to) {
        Bson projection = Projections.fields(Projections.include("from", "to", "message", "isSentToGroup"), Projections.excludeId());
        MongoCursor<Document> iterator = DataBaseProviderSingleton.getMongoDatabase().getCollection(CHAT_LOG_COLLECTION)
                .find(and(eq("isSentToGroup", false),
                        or(
                                and(eq("from", to), eq("to", from)),
                                and(eq("from", from), eq("to", to))
                        )))
                .projection(projection)
                .iterator();

        List<MessageDTO> results = new ArrayList<>();
        while (iterator.hasNext()) {
            results.add(MessageDTO.ofDocument(iterator.next()));
        }
        iterator.close();
        return results;
    }

    //Wyciagamy wiadomosci ktore byly wyslane do grupy, i to konkretnie do tej podanej w parametrze
    public List<MessageDTO> getGroupMessages(String toGroup) {
        Bson projection = Projections.fields(Projections.include("from", "to", "message", "isSentToGroup"), Projections.excludeId());
        MongoCursor<Document> iterator = DataBaseProviderSingleton.getMongoDatabase().getCollection(CHAT_LOG_COLLECTION)
                .find(and(eq("isSentToGroup", true),
                        eq("to", toGroup)))
                .projection(projection)
                .iterator();

        List<MessageDTO> results = new ArrayList<>();
        while (iterator.hasNext()) {
            results.add(MessageDTO.ofDocument(iterator.next()));
        }
        iterator.close();
        return results;
    }
}
