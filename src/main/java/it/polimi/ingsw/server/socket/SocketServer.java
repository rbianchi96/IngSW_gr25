package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    Controller controller;
   // private ArrayList<ClientInterface> socketClientHandlerUsers;
    private int port;

    public SocketServer(int port, Controller controller) {
        this.port = port;
        this.controller = controller;
       // this.socketClientHandlerUsers = socketClientHandlerUsers;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Socket Server ready");
            while (!false) {
                try {
                    Socket socket = serverSocket.accept();
                    socket.setKeepAlive(true);
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
}