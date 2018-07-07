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
		if(path != null) {
			try {
				InputStreamReader external = new InputStreamReader(new FileInputStream(path + fileName));

				return external;
			} catch(FileNotFoundException e) {
				System.out.println(fileName + " in use defined folder not found, using built-in resource file.");
				return getResourceFile(null, fileName);	//Return built-in resource
			}
		} else {	//Built-in resources
			InputStream iS = ResourcesPathResolver.class.getClassLoader().getResourceAsStream(fileName);

			if(iS == null) throw new FileNotFoundException();

			return new InputStreamReader(iS);
		}
	}
}
