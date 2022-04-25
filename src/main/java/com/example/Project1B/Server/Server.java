package com.example.Project1B.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private final MongoChatLogDAO mongoChatLogDAO;

    public Server(int port) throws IOException {
        mongoChatLogDAO = new MongoChatLogDAO();
        System.out.println("[SERVER-LOG] Server starting...");
        serverSocket = new ServerSocket(port);
        System.out.println("[SERVER-LOG] Server socket started...");
        while (true)
        {
            System.out.println("[SERVER-LOG] Waiting for new connection...");
            Socket client = serverSocket.accept();
            System.out.println("[SERVER-LOG] Client connected [external address:" + client.getInetAddress() + "]");
            Connection connection = new Connection(client, mongoChatLogDAO);
            connections.add(connection);
            new Thread(connection).start();
        }
    }
}
