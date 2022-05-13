package com.example.Project1B;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "Messages")
})
@Log
public class MessagingService implements MessageListener {

    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        var someData = message.getBody(SomeData.class);
        log.info("New message: " + someData.getData());
    }

    @PostConstruct
    public void postConstruct(){
        log.info(getClass().getSimpleName() + ": postConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info(getClass().getSimpleName() + ": preDestroy");
    }


}
