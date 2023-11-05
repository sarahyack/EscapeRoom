package utils;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONReader {

	public JSONReader() {
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject readJsonFromFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, perhaps return null or throw a custom exception
        }
        return new JSONObject(content);
    }

}
