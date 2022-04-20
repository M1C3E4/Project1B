package com.example.Project1B.Server;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable{

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;

    public Connection(Socket socket)
    {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            String line;
            try {
                line = reader.readLine();
                action(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }

    public void sendData(byte[] data)
    {
        try
        {
            dataOutput.write(data.length);
            dataOutput.write(data);
            dataOutput.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // line - [login]:[hasło]:[typoperacji]:[arg1]:[arg2]:[arg...n]
    public void action(String line)
    {
        System.out.println("[SERVER-LOG] MESSAGE RECEIVED : " + line);
        String data[] = line.split(":");

        switch (Operation.valueOf(data[2]))
        {
            case LOGIN:
                if(Users.getInstance().tryLogin(this, data[0], data[1]))
                {
                    sendMessage(Operation.LOGIN_OK.toString());
                }
                else
                {
                    sendMessage(Operation.LOGIN_FAILED.toString());
                }
                break;

            case SEND_MESSAGE:
                if(Users.getInstance().checkLogin(data[0], data[1]))
                {
                    //wysylanie do innych userow
                    Users.getInstance().getEntityById(data[4]).sendMessage(data[3]);
                    sendMessage(Operation.SEND_MESSAGE_OK.toString());
                }
                else
                {
                    sendMessage(Operation.SEND_MESSAGE_FAILED.toString());
                }
                break;

            case FILE_TRANSFER:
                if(Users.getInstance().checkLogin(data[0], data[1]))
                {

                }
                else
                {
                    sendMessage(Operation.FILE_TRANSFER_FAILED.toString());
                }
                break;

            case GET_HISTORY:
                if(Users.getInstance().checkLogin(data[0], data[1]))
                {

                }
                else
                {
                    sendMessage(Operation.GET_HISTORY_FAILED.toString());
                }
                break;
        }
    }
}