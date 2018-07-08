package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.controller.Controller;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    Controller controller;
    private int port;

    /**Constructor
     *
     * @param port of the server
     * @param controller
     */
    public SocketServer(int port, Controller controller) {
        this.port = port;
        this.controller = controller;
    }

    public void startServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Socket server started on port " + port);
            while (true) {
                    Socket socket = serverSocket.accept();
                    socket.setKeepAlive(true);
                    Thread t = new Thread(new SocketClientHandler(socket, controller));
                    t.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try
            {
                serverSocket.close();
            }
            catch(Exception ex)
            {}
        }
    }
}