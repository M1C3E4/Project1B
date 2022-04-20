package com.example.Project1B.Client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client("localhost", 8088);
    }
}
