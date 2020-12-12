package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        SocketServer server=new SocketServer(7000);
        server.start();
    }
}
