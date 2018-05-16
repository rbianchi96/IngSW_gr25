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

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Socket Server ready");
            while (!false) {
                try {

                    Socket socket = serverSocket.accept();
                    System.out.println("New Client connected: " + socket.getLocalAddress().toString());
                    Thread t = new Thread(new SocketClientHandler(socket, controller));
                    t.start();
                } catch (IOException e) {
                    break; // entrerei qui se serverSocket venisse chiuso
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
    }
    public ArrayList<SocketClientHandler> getClientHandlers() {
        return ClientHandlers;
    }
}


