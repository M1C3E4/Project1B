package com.example.Project1B.Server;

import java.util.ArrayList;
import java.util.Random;

public class Users {

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private static Users instance = null;

    private Users(){
        User u1 = new User("USER1", "PASS1");
        User u2 = new User("USER2", "PASS2");

        users.add(u1);
        users.add(u2);
        entities.add(u1);
        entities.add(u2);
        entities.add(new Group("DEFAULT GROUP"));
    }

    public static Users getInstance()
    {
        if(instance == null)
            instance = new Users();

        return instance;
    }

    public Entity getEntityById(String id){

        for (Entity e : entities)
        {
            if(e.id.equals(id))
                return e;
        }
        return null;
    }

    public boolean checkLogin(String login, String password){
        for(User u : users)
        {
            if(u.id.equals(login) && u.password.equals(password))
            {
                return u.logged;
            }
        }
        return false;
    }

    public boolean tryLogin(Connection connection, String login, String password){
        for(User u : users)
        {
            if(u.id.equals(login) && u.password.equals(password))
            {
                u.logged = true;
                u.connection = connection;
                return true;
            }
        }
        return false;
    }

    public abstract class Entity {
        protected String id;

        public Entity(){
            createId();
        }

        public abstract void sendMessage(String message);
        public abstract void dataTransfer(byte[] data);

        public void createId(){
            id = "";
            Random random = new Random();
            for(int i = 0; i < 12; i++)
            {
                id += (char)random.nextInt(26) + 65;
            }
        }

    }

    public class User extends Entity {

        public User(String login, String password){
            this.id = login;
            this.password = password;
        }

        public User(String login, String password, boolean logged){
            this.id = login;
            this.password = password;
            this.logged = logged;
        }

        String password;
        Connection connection;
        boolean logged = false;

        @Override
        public void sendMessage(String message) {
            connection.sendMessage(message);
        }

        @Override
        public void dataTransfer(byte[] data) {
            connection.sendData(data);
        }
    }

    public class Group extends Entity {

        private ArrayList<User> usersInGroup;

        public Group(String groupName)
        {
            this.id = groupName;
            usersInGroup = new ArrayList<>();
        }

        public void AddUser(User user) {
            this.usersInGroup.add(user);
        }

        @Override
        public void sendMessage(String message) {
            for (User u:usersInGroup) {
                u.sendMessage(message);
            }
        }

        @Override
        public void dataTransfer(byte[] data) {
            for(User u:usersInGroup)
            {
                u.dataTransfer(data);
            }
        }
    }
}