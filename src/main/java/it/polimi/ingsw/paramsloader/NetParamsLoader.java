package it.polimi.ingsw.paramsloader;

import javax.json.JsonObject;

public class NetParamsLoader extends ParamsLoader {
	private int socketServerPort;

	private String rMIServerName;
	private int rMIServerPort;

	public NetParamsLoader(String fileName) throws NullPointerException {
		super(fileName);

		socketServerPort = root.getJsonObject("socketServer").getInt("port");

		JsonObject rMIParams = root.getJsonObject("RMIServer");
		rMIServerName = rMIParams.getString("name");
		rMIServerPort = rMIParams.getInt("port");
	}

	public int getSocketServerPort() {
		return socketServerPort;
	}

	public String getRMIServerName() {
		return rMIServerName;
	}

	public int getRMIServerPort() {
		return rMIServerPort;
	}
}
