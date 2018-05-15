package it.polimi.ingsw.socketserver;

import it.polimi.ingsw.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    Controller controller;
    private ArrayList<SocketClientHandler> ClientHandlers;
    private int port;
    public SocketServer(int port,Controller controller) {
        this.port = port;
        this.controller = controller;

    }

    public void StartServer() {
        ServerSocket serverSocket=null;
        try {

            serverSocket = new ServerSocket(port);
            System.out.println("Socket Server ready");
            while (!false) {
                try {
                    Socket socket = serverSocket.accept();
                    SocketClientHandler socketClientHandler = new SocketClientHandler(socket, controller);
                    Thread t = new Thread(socketClientHandler);
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


    }
}


