package commands;

import java.util.Arrays;

import java.util.ArrayList;

import models.*;

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
	    if (room.hasPuzzles() && room.isAllPuzzlesSolved() == false) {
	        System.out.println("There's a puzzle here: ");
	        room.showPuzzles();
	    } else if (room.isAllPuzzlesSolved() == true) {
	    	System.out.println("There are no more puzzles to solve.");
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
		player.showInventory();
	}
	
	public void showHelp() {
		String[] commands = {
				"look - Get a description of the Room",
				"help - Display list of Commands",
				"inventory - Display the Contents of your Inventory",
				"pick up [item] - Pick up an Item in the Room",
				"use [item] - Use an Item from Inventory",
				"drop [item] - Drop Item into Room",
				"examine [item] - Examine Item",
				"solve [solution]- Solve a Puzzle",
				"open [item or box] - Open an Openable Item or the Box",
				"quit - Exit Game"
		};
		
		System.out.println("List of Commands:");
		for (String command : commands) {
			System.out.println(command);
		}
	}
	
	private void solveWordPuzzle(Puzzle puzzle, String[] parts, Player player, Room currentRoom) {
	    if (parts.length > 1) {
	        puzzle.trySolve(parts[1], player, currentRoom);
	    } else {
	        System.out.println("You need to provide an answer to solve the puzzle.");
	    }
	}
	
	private boolean determineIfDone(Room currentRoom) {
		if (currentRoom.isAllPuzzlesSolved()) {
			currentRoom.roomBox.setAccessible(true);
			System.out.println("The Box is now Accessible.");
	    	if (currentRoom.roomBox.getLocked() == true) {
	    		currentRoom.roomBox.toggleLockBox(false);
	    	}
		}
		
		if (currentRoom.isRoomCompleted()) {
			return true;
		} else {
			return false;
		}
	}

	
	public void parseCommand(String input, Player player, Room currentRoom) {
		String[] parts = input.split(" ");
	    String command = parts[0].toLowerCase();

	    String[] itemlessCommands = {"help", "look", "inventory", "solve"};
	    String[] multiWordCommands = {"pick up"};
	    
	    String combinedCommand = String.join(" ", Arrays.copyOfRange(parts, 0, Math.min(parts.length, 2))).toLowerCase();
	    
	    int objectNameIndex = (Arrays.asList(multiWordCommands).contains(combinedCommand)) ? 2 : 1;
	    String objectName = (parts.length > objectNameIndex) ? parts[objectNameIndex] : null;
	    
	    Item item = null;
	    
	    
	    if (!Arrays.asList(itemlessCommands).contains(command) || Arrays.asList(multiWordCommands).contains(combinedCommand)) {
	        item = currentRoom.findItemByName(objectName);
	        if (item == null && "key".equals(objectName) && currentRoom.hasLockedDoor()) {
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
	    	if (item == null) {
	    		item = currentRoom.findItemByName(objectName);
	    	}
	    	examine(item);
	    } else if ("inventory".equals(command)) {
	    	checkInventory(player);
	    } else if ("solve".equals(command)) {
	    	ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
	    	if (puzzles == null || puzzles.isEmpty()) {
	    		System.out.println("There is no puzzle here to solve.");
	    		return;
	    	} else {
	    		for (Puzzle puzzle : puzzles) {
		    		if (puzzle.isSolved()) {
		    			System.out.println("This puzzle is solved.");
		    			continue;
		    		}
		    		
		    		if (puzzle instanceof WordPuzzle) {
		    			solveWordPuzzle(puzzle, parts, player, currentRoom);
		    			determineIfDone(currentRoom);
		    			break;
		    		} else if (puzzle instanceof ObjectPuzzle){
		    			puzzle.trySolve(player, currentRoom);
		    			determineIfDone(currentRoom);
		    			break;
		    		} else if (puzzle instanceof MovementPuzzle) {
		    			String action = (parts.length > 1) ? parts[1] : "invalid";
		    			puzzle.trySolve(action, player, currentRoom);
		    			determineIfDone(currentRoom);
		    			break;
		    		} else if (puzzle instanceof AnimalPuzzle) {
		    			System.out.println("Implement this.");
		    		}
		    	}
	    	}
	    }
	    else {
	        System.out.println("Invalid command: " + command);
	    }
	}

}
