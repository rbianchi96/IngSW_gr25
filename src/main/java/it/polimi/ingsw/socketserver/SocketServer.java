package it.polimi.ingsw.socketserver;

import it.polimi.ingsw.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    Controller controller;
    private int port;
    public SocketServer(int port,Controller controller) {
        this.port = port;
        this.controller = controller;
    }

    public void StartServer() {
        ServerSocket serverSocket=null;
        try {

            serverSocket = new ServerSocket(port);

            while (!false) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread t = new Thread(new SocketClientHandler(socket, controller));
                    t.start();
                } catch (IOException e) {
                    break; // Server socket closed
                }
            }

        } catch (Exception e) {
            if(serverSocket != null && !serverSocket.isClosed()){
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.err.println(e.getMessage()); // port not available
            return;
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("Socket Server ready");

    }
}


