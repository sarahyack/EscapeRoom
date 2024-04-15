package commands;

import java.util.Arrays;

import java.util.ArrayList;

import core.MessageDispatcher;
import models.*;
import utils.Priority;
import utils.PriorityMessage;

public class CommandParser {
	private final MessageDispatcher dispatch = MessageDispatcher.getInstance();
	private void pickUpItem(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.addToInventory(item);
			currentRoom.removeItem(item);
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "Cannot pick up non-existent item.\n"));
		}
	}
	
	private void useItem(Player player, Item item) {
		if (item != null) {
			item.use();
			if (!item.needed) {
				player.removeFromInventory(item);
			}
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "Cannot use non-existent item.\n"));
		}
	}
	
	private void examine(Item item) {
		if (item != null) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, item.getName() + " - " + item.getDescription() + "\n"));
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "Cannot examine non-existent item.\n"));
		}
		
	}
	
	private void drop(Player player, Item item, Room currentRoom) {
		if (item != null) {
			player.removeFromInventory(item);
			currentRoom.addItem(item);
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "You dropped: " + item.getName() + "\n"));
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "Cannot drop non-existent item.\n"));
		}
	}
	
	private void look(Room room) {
		dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "You are in " + room.getName() + "\n"));
	    room.getDescription();
	    if (room.hasLockedDoor()) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "The door is locked.\n"));
	    } else {
	        dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "The door is unlocked.\n"));
	    }
	    if (room.hasPuzzles() && !room.isAllPuzzlesSolved()) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "There's a puzzle here: \n"));
			room.showPuzzles();
	    } else if (room.isAllPuzzlesSolved()) {
	    	dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "All puzzles are solved.\n"));
	    }
		dispatch.createPriorityMessage(new PriorityMessage(Priority.LOW, "What would you like to do?\n"));
	}
	// TODO: Need to implement this >
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

		dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "List of Commands:\n"));
		for (String command : commands) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, command + "\n"));
		}
	}
	
	private void solveWordPuzzle(Puzzle puzzle, String[] parts, Player player, Room currentRoom) {
	    if (parts.length > 1) {
	        puzzle.trySolve(parts[1], player, currentRoom);
	    } else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "You need to provide an answer to solve the puzzle.\n"));
	    }
	}
	
	private boolean determineIfDone(Room currentRoom) {
		if (currentRoom.isAllPuzzlesSolved()) {
			currentRoom.roomBox.setAccessible(true);
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "The Box is now Accessible.\n"));
	    	if (currentRoom.roomBox.getLocked()) {
	    		currentRoom.roomBox.toggleLockBox(false);
	    	}
		}

        return currentRoom.isRoomCompleted();
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
				dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "This item can't be opened or closed.\n"));
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
				dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "There is no puzzle here to solve.\n"));
	    		return;
	    	} else {
	    		for (Puzzle puzzle : puzzles) {
		    		if (puzzle.isSolved()) {
						dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "This puzzle is already solved.\n"));
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
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "Invalid command: " + command + "\n"));
	    }
	}

}
