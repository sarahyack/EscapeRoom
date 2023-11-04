package commands;

import java.util.Arrays;

import java.util.ArrayList;

// import models.Box;
import models.Item;
import models.Key;
import models.OpenableItem;
import models.Player;
import models.Puzzle;
import models.Room;
import models.WordPuzzle;

public class CommandParser {
	
	private void pickUpItem(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.addToInventory(item);
			currentRoom.removeItem(item);
		} else {
			System.out.println("Cannot pick up non-existent item.");
		}
	}
	
	private void useItem(Player player, Item item) {
		if (item != null) {
			item.use();
			if (item.needed == false) {
				player.removeFromInventory(item);
			}
		} else {
			System.out.println("Cannot use non-existent item.");
		}
	}
	
	private void examine(Item item) {
		if (item != null) {
			System.out.print(item.getName() + " - ");
			System.out.println(item.getDescription());
		} else {
			System.out.println("Cannot examine a non-existent item.");
		}
		
	}
	
	private void drop(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.removeFromInventory(item);
			currentRoom.addItem(item);
			System.out.println("You dropped: " + item);
		} else {
			System.out.println("Cannot drop non-existent item.");
		}
	}
	
	private void look(Room room) {
	    System.out.print("You are in ");
	    room.getDescription();
	    if (room.hasLockedDoor()) {
	        System.out.println("The door is locked.");
	    } else {
	        System.out.println("The door is unlocked.");
	    }
	    if (room.hasPuzzles()) {
	        System.out.println("There's a puzzle here: ");
	        room.showPuzzles();
	    }
	    System.out.println("What would you like to do?");
	}
	//Need to implement this >
	public void openClose(Player player, OpenableItem item) {
		item.open(player);
		item.needed = false;
		item.toggleOpen();
	}
	
	public void openBox(Player player, Room currentRoom) {
		currentRoom.roomBox.open(player, currentRoom);
	}
	
	public void pushPull() {
		
	}
	
	public void turn() {
		
	}
	
	public void checkInventory(Player player) {
		System.out.println("Items In Your Inventory:");
		player.showInventory();
	}
	
	public void showHelp() {
		String[] commands = {
				"pick up [item] - Pick up an Item in the Room",
				"use [item] - Use an Item from Inventory",
				"drop [item] - Drop Item into Room",
				"examine [item] - Examine Item",
				"solve [solution]- Solve a Puzzle",
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
	
	private void solveWordPuzzle(Puzzle puzzle, String[] parts, Player player, Room currentRoom) {
	    if (parts.length > 1) {
	        puzzle.trySolve(parts[1], player, currentRoom);
	        if (puzzle.isSolved() == true) {
	        	currentRoom.roomBox.setAccessible(true);
	        	System.out.println("The Box is now Accessible.");
	        	if (currentRoom.roomBox.getLocked() == true) {
		    		currentRoom.roomBox.toggleLockBox(false);
		    	}
	        }
	    } else {
	        System.out.println("You need to provide an answer to solve the puzzle.");
	    }
	}

	private void solveOtherPuzzle(Puzzle puzzle, Player player, Room currentRoom) {
	    puzzle.trySolve(player, currentRoom);
	    if (puzzle.isSolved() == true) {
	    	currentRoom.roomBox.setAccessible(true);
	    	if (currentRoom.roomBox.getLocked() == true) {
	    		currentRoom.roomBox.toggleLockBox(false);
	    	}
	    }
	}

	
	public void parseCommand(String input, Player player, Room currentRoom) {
		System.out.println("Parsing command: " + input);
		String[] parts = input.split(" ");
	    String command = parts[0].toLowerCase();

	    String[] itemlessCommands = {"help", "look", "inventory", "solve"};
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
	    	item = player.findItemInInventory(objectName);
	    	if (item instanceof Key) {
	    		((Key) item).unlockDoor(currentRoom, player);
	    	} else {
	    		useItem(player, item);
	    	}
	    	
	    } else if ("drop".equals(command)) {
	    	item = player.findItemInInventory(objectName);
	    	drop(player, item, currentRoom);
	    } else if ("look".equals(command)) {
	    	look(currentRoom);
	    } else if ("open".equalsIgnoreCase(command) && "box".equalsIgnoreCase(objectName)) {
	    	openBox(player, currentRoom);
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
	    	System.out.println("Trying to solve a puzzle...");
	    	ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
	    	if (puzzles == null || puzzles.isEmpty()) {
	    		System.out.println("There is no puzzle here to solve.");
	    		return;
	    	} else {
	    		System.out.println("Found " + puzzles.size() + " puzzle(s).");
	    		for (Puzzle puzzle : puzzles) {
		    		if (puzzle.isSolved()) {
		    			System.out.println("This puzzle is solved.");
		    			continue;
		    		}
		    		
		    		if (puzzle instanceof WordPuzzle) {
		    			System.out.println("This is a WordPuzzle.");
		    			solveWordPuzzle(puzzle, parts, player, currentRoom);
		    		} else {
		    			System.out.println("This is a different type of puzzle.");
		    			solveOtherPuzzle(puzzle, player, currentRoom);
		    		}
		    	}
	    	}
	    }
	    else {
	        System.out.println("Invalid command: " + command);
	    }
	}

}
