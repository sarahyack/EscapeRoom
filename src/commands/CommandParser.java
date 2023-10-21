package commands;

import java.util.Arrays;

import models.Box;
import models.Item;
import models.OpenableItem;
import models.Player;
import models.Puzzle;
import models.Room;
import models.WordPuzzle;

public class CommandParser {
	
	public void pickUpItem(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.addToInventory(item);
			currentRoom.removeItem(item);
		} else {
			System.out.println("Cannot pick up non-existent item.");
		}
	}
	
	public void useItem(Player player, Item item) {
		if (item != null) {
			item.use();
			if (item.needed == false) {
				player.removeFromInventory(item);
			}
		} else {
			System.out.println("Cannot use non-existent item.");
		}
	}
	
	public void examine(Item item) {
		if (item != null) {
			System.out.print(item.getName() + " - ");
			System.out.println(item.getDescription());
		} else {
			System.out.println("Cannot examine a non-existent item.");
		}
		
	}
	
	public void drop(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.removeFromInventory(item);
			currentRoom.addItem(item);
			System.out.println("You dropped: " + item);
		} else {
			System.out.println("Cannot drop non-existent item.");
		}
	}
	
	public void look(Room room) {
		System.out.println("Locked: " + room.hasLockedDoor());
		room.getDescription();
	}
	
	public void openClose(Player player, OpenableItem item) {
		item.open(player);
		item.needed = false;
		item.toggleOpen();
	}
	
	public void openClose(Player player, Box box, Room currentRoom) {
		box.open(player, box, currentRoom);
	}
	
	public void pushPull() {
		
	}
	
	public void turn() {
		
	}
	
	public void checkInventory(Player player) {
		player.showInventory();
	}
	
	public void showHelp() {
		String[] commands = {
				"pick up [item] - Pick up an Item in the Room",
				"use [item] - Use an Item from Inventory",
				"drop [item] - Drop Item into Room",
				"examine [item] - Examine Item",
				"look - Get a description of the Room",
				"open [item or box] - Open an Openable Item or the Box",
				"quit - Exit Game",
				"help - Display list of Commands",
				"inventory - Display the Contents of your Inventory"
		};
		
		System.out.println("List of Commands:");
		for (String command : commands) {
			System.out.println(command);
		}
	}

	
	public void parseCommand(String input, Player player, Room currentRoom) {
		System.out.println("Parsing command: " + input);
		String[] parts = input.split(" ");
	    String command = parts[0].toLowerCase();

	    String[] itemlessCommands = {"help", "look", "inventory"};
	    String[] multiWordCommands = {"pick up"};
	    
	    String combinedCommand = String.join(" ", Arrays.copyOfRange(parts, 0, Math.min(parts.length, 2))).toLowerCase();
	    
	    int objectNameIndex = (Arrays.asList(multiWordCommands).contains(combinedCommand)) ? 2 : 1;
	    String objectName = (parts.length > objectNameIndex) ? parts[objectNameIndex] : null;
	    
	    Item item = null;
	    
	    System.out.println("Command: " + command);
	    System.out.println("Combined Command: " + combinedCommand);
	    
	    if (!Arrays.asList(itemlessCommands).contains(command) || Arrays.asList(multiWordCommands).contains(combinedCommand)) {
	        item = currentRoom.findItemByName(objectName);
	        System.out.println("Found item: " + item);
	        if (item == null && "key".equals(objectName) && currentRoom.hasLockedDoor()) {
	        	System.out.println("Attempting to find item...");
	        	item = player.findItemInInventory(objectName);
	        }
	    }
	    
	    if (Arrays.asList(multiWordCommands).contains(combinedCommand)) {
	        command = combinedCommand;
	    }

	    if ("pick up".equals(command)) {
	        pickUpItem(player, item, currentRoom);
	    } else if ("use".equals(command)) {
	        useItem(player, item);
	    } else if ("drop".equals(command)) {
	    	item = player.findItemInInventory(objectName);
	    	drop(player, item, currentRoom);
	    } else if ("look".equals(command)) {
	    	look(currentRoom);
	    } else if ("open".equals(command) || "close".equals(command)) {
	    	if (item != null && item instanceof OpenableItem) {
	    		((OpenableItem) item).open(player);
	    		((OpenableItem) item).toggleOpen();
	    	} else {
	    		System.out.println("This item can't be opened or closed.");
	    	}
	    } else if ("help".equalsIgnoreCase(command)) {
	    	showHelp();
	    } else if ("examine".equalsIgnoreCase(command)) {
	    	item = player.findItemInInventory(objectName);
	    	examine(item);
	    } else if ("inventory".equals(command)) {
	    	checkInventory(player);
	    } else if ("solve".equals(command)) {
	    	Puzzle puzzle = currentRoom.getPuzzle();
	    	if (puzzle != null) {
	    		if (puzzle instanceof WordPuzzle) {
	    			if (parts.length > 1) {
	    				puzzle.trySolve(parts[1], player, currentRoom);
	    			} else {
	    				System.out.println("You need to provide an answer to solve the puzzle.");
	    			}
	    		} else {
	    			puzzle.trySolve(player, currentRoom);
	    		}
	    	} else {
	    		System.out.println("There is no puzzle here to solve.");
	    	}
	    }
	    else {
	        System.out.println("Invalid command: " + command);
	    }
	}

}
