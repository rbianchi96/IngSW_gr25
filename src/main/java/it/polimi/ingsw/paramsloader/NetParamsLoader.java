package it.polimi.ingsw.paramsloader;

import javax.json.JsonObject;
import java.io.Reader;

public class NetParamsLoader extends ParamsLoader {
	public static final String FILE_NAME = "netParams.json";

	private int socketServerPort;

	private String rMIServerName;
	private int rMIServerPort;

	/**Constructor
	 *
	 * @param file
	 */
	public NetParamsLoader(Reader file) {
		super(file);

		socketServerPort = root.getJsonObject("socketServer").getInt("port");

		JsonObject rMIParams = root.getJsonObject("RMIServer");
		rMIServerName = rMIParams.getString("name");
		rMIServerPort = rMIParams.getInt("port");
	}

	/**
	 *
	 * @return the number of the port of the server
	 */
	public int getSocketServerPort() {
		return socketServerPort;
	}

	/**
	 *
	 * @return the name of the RMI server
	 */
	public String getRMIServerName() {
		return rMIServerName;
	}

	/**
	 *
	 * @return the port of the RMI server
	 */
	public int getRMIServerPort() {
		return rMIServerPort;
	}
}
