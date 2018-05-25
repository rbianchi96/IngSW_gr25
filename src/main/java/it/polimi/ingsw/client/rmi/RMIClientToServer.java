package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientInterface;
import it.polimi.ingsw.paramsloader.NetParamsLoader;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.server.rmi.RMIServerInterface;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RMIClientToServer implements ServerInterface {
	private RMIServerInterface server;
	private RMIClient client;
	private Timer pingTimer;
	private Timer reconnectTimer;
	private String sessionNickname;
	public RMIClientToServer(ClientInterface client, String ip, String serverName) {
		try {
			this.client = new RMIClient(client);	//RMIClient to send to server used to receive responses
			server = (RMIServerInterface)Naming.lookup("rmi://" + ip + "/" + serverName);
			pingTimer();
		} catch(NotBoundException e) {
			e.printStackTrace();
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(RemoteException e) {
			e.printStackTrace();
		}

	}

	// Timer to ping the server set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void pingTimer(){
		pingTimer = new Timer();
		pingTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ping();
			}
		}, 500, 2500);
	}

	// ping the RMI Server
	private boolean ping(){
		try {
			server.ping();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			pingTimer.cancel();
			// LOST CONNECTION
			System.out.println("Connection lost with RMI Server!");
			reconnectTimer();
			return false;
		}
	}

	// Timer to ping the server set with a delay of 500 milliseconds, repeat every 2 and half minutes
	private void reconnectTimer(){
		reconnectTimer = new Timer();
		reconnectTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				reconnectPing();
			}
		}, 500, 2500);
	}

	private boolean reconnectPing(){
		try {
			server.ping();
			//Notify the client is back online and will try to restore the connection with the server
			System.out.println("You are back online, trying to restore the connection with RMI Server...");
			reconnectTimer.cancel();
			pingTimer();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			// Still can't get to server
			return false;
		}
	}

	@Override
	public void login(String username) {
		try {
			server.login(username, client);
			sessionNickname = username;
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logout() {

	}
}
