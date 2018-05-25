package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.server.ServerInterface;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class SocketClient extends Socket implements ServerInterface {
	private PrintWriter out;
	private Scanner in;
    private Timer reconnectTimer;
	private ClientInterface client;
    private Socket socket;
    private String sessionNickname;
    private String sessionID;
	public SocketClient(Socket socket, ClientInterface client) {
		this.client = client;
		this.socket = socket;
		socketReceiverCreation();
	}

    private void socketReceiverCreation(){
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());

            SocketClientReceiver receiver = new SocketClientReceiver(this, in);	//Create the receiver
            if (reconnectTimer != null) {
                reconnectTimer.cancel();
                //Notify the client is back online and will try to restore the connection with the server
                System.out.println("You are back online, trying to restore the connection with Socket Server...");
                restoreSession();
            }
            else
                reconnectTimer = null;
            Thread t = new Thread(receiver);
            t.start();	//Start the receiver
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    protected void pingReconnection(){
	    // NOTIFY Trying to reconnect...
        pingTimer();
    }
	void decode(String message) {
		String[] msgVector = message.split("#");    //Split message
		switch(msgVector[0]) {
			case "login_response":
				client.loginResponse(msgVector[1], msgVector[2]);
                if (msgVector[1]=="success"){
                    sessionID= msgVector[2];
                }
				break;
			case "not_logged":
				client.notLoggedYet(msgVector[1]);
				break;
			case "suspended_user":
				client.notifySuspendedUser(msgVector[1]);
				break;
			case "new_user":
				client.notifyNewUser(msgVector[1]);
				break;
			case "players_list":
				client.sendPlayersList(Arrays.copyOfRange(msgVector, 1, msgVector.length));
            case "reconnect_response":
                client.notifyReconnectionStatus(Boolean.getBoolean(msgVector[1]),msgVector[2]);

			default:
				break;
		}
	}

	@Override
	public void login(String username) {
		out.println("login#" + username);
		out.flush();
		sessionNickname = username;
	}

	@Override
	public  void logout() {
		out.println("logout");
		out.flush();
		reconnectTimer.cancel();
	}

    // Timer to attempt the creation of new socket connection set with a delay of 500 milliseconds, repeat every 2 and half minutes
    private void pingTimer(){
        reconnectTimer = new Timer();
        reconnectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                socketReceiverCreation();
            }
        }, 500, 2500);
    }
    private void restoreSession(){
        out.println("reconnect#" + sessionID + "#" + sessionNickname);
        out.flush();
    }
}