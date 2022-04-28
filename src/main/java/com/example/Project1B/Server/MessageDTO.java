package com.example.Project1B.Server;

import java.util.Objects;

import org.bson.Document;

public class MessageDTO {
    private final String from;
    private final String to;
    private final String message;
    private final boolean isSentToGroup;

    public MessageDTO(String from, String to, String message, boolean isSentToGroup) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.message = Objects.requireNonNull(message);
        this.isSentToGroup = isSentToGroup;
    }

    public Document toDocument() {
        return new Document()
                .append("from", from)
                .append("to", to)
                .append("message", message)
                .append("isSentToGroup", isSentToGroup);
    }

    public static MessageDTO ofDocument(Document document) {
        return new MessageDTO(
                document.getString("from"),
                document.getString("to"),
                document.getString("message"),
                document.getBoolean("isSentToGroup"));
    }

    @Override
    public String toString() {
        return "(" + from + " -> " + to + ") " + message;
    }
}