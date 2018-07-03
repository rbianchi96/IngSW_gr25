package it.polimi.ingsw.paramsloader;

import javax.json.JsonObject;
import java.io.Reader;

public class NetParamsLoader extends ParamsLoader {
	public static final String FILE_NAME = "netParams.json";

	private int socketServerPort;

	private String rMIServerName;
	private int rMIServerPort;

	public NetParamsLoader(Reader file) {
		super(file);

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
