package utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.*;

public class JSONReader {
	
	public static List<Room> readRoomData() {
        List<Room> rooms = new ArrayList<>();
        String filePath = "resources/rooms.json";
        
        try {
        	
        	InputStream is = new FileInputStream(filePath);
            JSONTokener tokener = new JSONTokener(is);
            JSONObject object = new JSONObject(tokener);
            JSONArray roomsArray = object.getJSONArray("rooms");
            
            // Loop over each room object in the array
            for (int i = 0; i < roomsArray.length(); i++) {
                JSONObject roomObj = roomsArray.getJSONObject(i);
                
                // Extract room data
                String roomName = roomObj.getString("name");
                String roomDescription = roomObj.getString("description");
                JSONArray itemsArray = roomObj.getJSONArray("items");
                JSONArray puzzlesArray = roomObj.getJSONArray("puzzles");
                
                // Extract items
                ArrayList<Item> items = new ArrayList<>();
                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject itemObj = itemsArray.getJSONObject(j);
                    String itemName = itemObj.getString("name");
                    String itemDescription = itemObj.getString("description");
                    Item item = new Item(itemName, itemDescription);
                    items.add(item);
                }
                
                Map<String, Item> itemMap = new HashMap<>();
                for(Item item : items) {
                    itemMap.put(item.getName(), item);
                }
                
                // Extract puzzles
                ArrayList<Puzzle> puzzles = new ArrayList<>();
                for (int k = 0; k < puzzlesArray.length(); k++) {
                    JSONObject puzzleObject = puzzlesArray.getJSONObject(k);
                    Puzzle puzzle = createPuzzleByType(puzzleObject, itemMap);
                    puzzles.add(puzzle);
                }
                
                // Extract box configuration
                JSONObject boxJson = roomObj.getJSONObject("box");
                Box roomBox = createBox(boxJson);
                
                
                // Create a Room instance
                Room room = new Room(roomName, roomDescription, items, roomBox, puzzles);
                rooms.add(room);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

	private static Box createBox(JSONObject boxJson) {
		boolean isLocked = boxJson.getBoolean("isLocked");
	    String description = boxJson.getString("description");
	    JSONArray boxItemsJson = boxJson.getJSONArray("items");
	    ArrayList<Item> boxItems = new ArrayList<>();

	    for (int i = 0; i < boxItemsJson.length(); i++) {
	        JSONObject itemJson = boxItemsJson.getJSONObject(i);
	        String itemName = itemJson.getString("name");
	        String itemDescription = itemJson.getString("description");
	        boxItems.add(new Item(itemName, itemDescription));
	    }

	    return new Box(isLocked, description, boxItems);
	}

	private static Puzzle createPuzzleByType(JSONObject puzzleObject, Map<String, Item> itemMap) {
	    String type = puzzleObject.getString("type");
	    String description = puzzleObject.getString("description");

	    switch (type) {
	        case "WORD":
	            String solution = puzzleObject.getString("solution");
	            return new WordPuzzle(description, solution);
	        case "OBJECT":
	            String requiredItemName = puzzleObject.getString("requiredItem");
	            Item requiredItem = itemMap.get(requiredItemName);
	            return new ObjectPuzzle(description, requiredItem);
	        case "MOVEMENT":
	            String item = puzzleObject.getString("item");
	            String action = puzzleObject.getString("action");
	            return new MovementPuzzle(description, item, action);
	        case "ANIMAL":
	            JSONObject tasksObject = puzzleObject.getJSONObject("tasks");
	            HashMap<String, Boolean> tasks = new HashMap<>();
	            for (String key : tasksObject.keySet()) {
	                tasks.put(key, tasksObject.getBoolean(key));
	            }
	            return new AnimalPuzzle(description, tasks);
	        default:
	            return null;
	    }
	}

}
