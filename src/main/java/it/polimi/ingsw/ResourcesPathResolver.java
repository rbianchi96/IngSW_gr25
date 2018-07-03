package it.polimi.ingsw;

import java.io.*;

public class ResourcesPathResolver {
	/**
	 * Get a file by a specified path.
	 * @param path the path of the resources folder
	 * @param fileName the file path relative to the resources folder
	 * @return a Reader for the requested file
	 * @throws FileNotFoundException if not exists a file with the given path
	 */
	public static Reader getResourceFile(String path, String fileName) throws FileNotFoundException {
		if(path == null) {	//Built-in resources
			InputStream iS = ResourcesPathResolver.class.getClassLoader().getResourceAsStream(fileName);

			if(iS == null) throw new FileNotFoundException();

			return new InputStreamReader(iS);
		} else {
			return new InputStreamReader(new FileInputStream(path + fileName));
		}
	}
}
