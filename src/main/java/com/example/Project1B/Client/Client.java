package com.example.Project1B.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String address;
    private int port;

    private Receiver reader;
    private Sender sender;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;

    private String login, password;

    public Client(String address, int port) throws IOException {
        this.address = address;
        this.port = port;

        socket = new Socket(address, port);

        try {
            reader = new Receiver(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            sender = new Sender(new PrintWriter(new OutputStreamWriter(socket.getOutputStream())));
            new Thread(reader).start();
            new Thread(sender).start();
            dataOutput = new DataOutputStream(socket.getOutputStream());
            dataInput = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        mainMenu();
    }

    public void mainMenu(){
        Scanner scanner = new Scanner(System.in);
        int choose = 0;
        while (true) {
            System.out.println("********  USER MENU  ********");
            System.out.println("1. LOGIN");
            System.out.println("2. SEND MESSAGE");
            System.out.println("3. GET HISTORY");
            System.out.println("4. LOGOUT");
            System.out.println("5. EXIT");
            System.out.println("********-------------********");
            choose = scanner.nextInt();

            Operation operation = Operation.NULL;
            switch (choose)
            {
                case 1:
                    sendLogin();
                    break;
                case 2:
                    enterChat();
                    break;
                case 3:
                    operation = Operation.GET_HISTORY;
                    break;
                case 4:
                    operation = Operation.FILE_TRANSFER;
                    break;
                case 5:
                    operation = Operation.LOGOUT;
                    break;
                case 6:
                    System.out.println("Closing app...");
                    return;
            }
        }
    }

    /*public void chatMenu(){
        int choose = 0;
        while (true) {
            System.out.println("********  CHAT MENU  ********");
            System.out.println("1. ENTER DIRECT MESSAGE");
            System.out.println("2. ENTER GROUP");
            System.out.println("3. GO BACK");
            System.out.println("********-------------********");
            Operation operation = Operation.NULL;
            switch (choose)
            {
                case 1:
                case 2:
                    enterChat();
                    break;
                case 3:
                    operation = Operation.GET_HISTORY;
                    return;
            }
            //action(operation);
        }
    }*/

    public void enterChat(){
        String id, mess;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj nazwe użytkownika/grupy do której chcesz dołączyć:");
        id = scanner.next();
        System.out.print("Podaj wiadomość:");
        mess = scanner.next();
        sendMessage(mess, id);
    }

    public void sendLogin(){
        Scanner scanner = new Scanner(System.in);
        login = scanner.next();
        password = scanner.next();
        sender.output(login + ":" + password + ":" + Operation.LOGIN);
    }

    public void sendMessage(String message, String id){
        sender.output(login + ":" + password + ":" + Operation.SEND_MESSAGE + ":" + message + ":" + id);
    }

    public void sendLogout(){
        sender.output(login + ":" + password + ":" + Operation.LOGOUT);
    }

    public void sendFile(){

    }

    class Sender implements Runnable {

        private PrintWriter writer;
        private String toSend = null;

        public Sender(PrintWriter writer)
        {
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (toSend != null) {
                        writer.println(toSend);
                        writer.flush();
                        toSend = null;
                    }
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void output(String message){
            sender.toSend = message;
        }
    }

    class Receiver implements Runnable {

        private BufferedReader reader;

        public Receiver(BufferedReader reader){
            this.reader = reader;
        }

        @Override
        public void run() {
            while (true) {
                String line;
                try {
                    line = reader.readLine();
                    input(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void input(String message){
            System.out.println("RESPONSE -> " + message);
        }
    }
}