package com.example.Project1B.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class Connection implements Runnable {

    private final MongoChatLogDAO chatLogDao;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private Users users;

    public Connection(Socket socket, MongoChatLogDAO mongoChatLogDAO) {
        this.users = Users.getInstance();
        this.chatLogDao = mongoChatLogDAO;
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line;
                line = reader.readLine();
                action(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(Operation.RECEIVE_MESSAGE + ":" + message);
        writer.flush();
        System.out.println("[SERVER-LOG] RESPONSE SEND -> " + message);
    }

    public void sendFile(byte[] bytes) {

    }

    public byte[] readFile() {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // line - [login]:[hasło]:[typoperacji]:[arg1]:[arg2]:[arg...n]
    public void action(String line) throws IOException {
        System.out.println("[SERVER-LOG] REQUEST RECEIVED -> " + line);
        String data[] = line.split(":");

        switch (Operation.valueOf(data[2])) {
            case LOGIN:
                if (users.tryLogin(this, data[0], data[1])) {
                    sendMessage(Operation.LOGIN_OK.toString());
                } else {
                    sendMessage(Operation.LOGIN_FAILED.toString());
                }
                break;

            case SEND_MESSAGE:
                if (users.checkLogin(data[0], data[1])) {
                    //wysylanie do innych userow
                    Users.Entity targetEntity = users.getEntityById(data[4]);
                    targetEntity.sendMessage(data[3]);
                    boolean isGroup = targetEntity instanceof Users.Group;
                    chatLogDao.saveMessage(new MessageDTO(data[0], data[4], data[3], isGroup));
                    sendMessage(Operation.SEND_MESSAGE_OK.toString());
                } else {
                    sendMessage(Operation.SEND_MESSAGE_FAILED.toString());
                }
                break;

            case FILE_TRANSFER:
                if (users.checkLogin(data[0], data[1])) {
                    //users.getEntityById(data[4]).dataTransfer(readFile());
                    //sendMessage(Operation.FILE_TRANSFER_OK.toString());
                } else {
                    sendMessage(Operation.FILE_TRANSFER_FAILED.toString());
                }
                break;

            case GET_HISTORY:
                if (users.checkLogin(data[0], data[1])) {
                    String from = data[0];
                    String to = data[3];

                    Users.Entity entity = users.getEntityById(to);
                    List<MessageDTO> messages = (entity instanceof Users.Group) ? chatLogDao.getGroupMessages(to) : chatLogDao.getPrivateMessages(from, to);
                    messages.stream()
                            .map(MessageDTO::toString)
                            .forEach(this::sendMessage);

                    sendMessage(Operation.GET_HISTORY_OK.toString());
                } else {
                    sendMessage(Operation.GET_HISTORY_FAILED.toString());
                }
                break;

            case JOIN_GROUP:
                if (users.checkLogin(data[0], data[1])) {
                    users.getGroupById(data[3]).AddUser(Users.getInstance().getUserById(data[0]));
                    sendMessage(Operation.JOIN_GROUP_OK.toString());
                } else {
                    sendMessage(Operation.JOIN_GROUP_FAILED.toString());
                }
                break;

            case LOGOUT:
                if (users.checkLogin(data[0], data[1])) {
                    users.logout(data[0]);
                    sendMessage(Operation.LOGOUT_OK.toString());
                } else {
                    sendMessage(Operation.LOGOUT_FAILED.toString());
                }
                break;

        }
    }
}
