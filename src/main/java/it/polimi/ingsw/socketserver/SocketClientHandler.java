package it.polimi.ingsw.socketserver;

import it.polimi.ingsw.Controller;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientHandler implements Runnable{
    private Socket socket;
    Scanner in;
    Controller controller;
    PrintWriter out;
    public SocketClientHandler(Socket socket, Controller controller){
        this.controller = controller;
        this.socket = socket;
    }
    public void run(){}


}
