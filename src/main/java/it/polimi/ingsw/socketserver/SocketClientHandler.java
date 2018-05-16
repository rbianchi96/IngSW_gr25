package it.polimi.ingsw.socketserver;

import it.polimi.ingsw.Controller;
import java.io.IOException;
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
    public void run(){
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            out.println("Connection Established");
            out.flush();
            while (true) {
                String line = in.nextLine();
                if (line.equals("close")) {
                    break;
                }else {
                    decode(line);
                }
            }
            in .close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void decode(String line){
        String[] request = line.split("#");
        if (request[0]!=null) {
            switch (request[0]) {
                case "login": {
                   if ( controller.getLobby().login(request[1])){
                        out.println("Welcome");
                        out.flush();
                        System.out.println("Client " + request[1] + " logged!");}
                   else {
                       out.println("Login failed");
                       out.flush();
                   }
                }
                    break;
                default:
                    break;
            }
        }
    }
    private String encode(String... args){
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg +" ");
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }
}
