//package com.example.Project1B;
//
//import com.example.Project1B.Server.Connection;
//import lombok.SneakyThrows;
//import lombok.extern.java.Log;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.ejb.ActivationConfigProperty;
//import javax.ejb.MessageDriven;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//
//@MessageDriven(activationConfig = {
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
//        @ActivationConfigProperty(propertyName = "destination", propertyValue = "Messages")
//})
//@Log
//public class MessagingService implements MessageListener {
//
//    private Connection connection;
//
//    @SneakyThrows
//    @Override
//    public void onMessage(Message messList) {
//        var st = connection.sendMessage(m);
//        log.info("New message: " + onMessage(message););
//    }
//
//    @PostConstruct
//    public void postConstruct(){
//        log.info(getClass().getSimpleName() + ": postConstruct");
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        log.info(getClass().getSimpleName() + ": preDestroy");
//    }
//
//
//}
