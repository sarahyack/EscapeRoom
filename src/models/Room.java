package models;

import java.util.ArrayList;

/**
 * A class representing a room in the Escape Room game. Rooms can contain items, puzzles, and boxes.
 * Room attributes are name, description, locked door status, items, room box, and puzzles.
 * @author Sarah Yack
 */
public class Room {
	private final String name;
	private final String description;
	private boolean hasLockedDoor = true;
	private final ArrayList<Item> roomContents;
	public Box roomBox;
	private final ArrayList<Puzzle> roomPuzzles;
	
	public Room(String name, String description, ArrayList<Item> contents, Box currentBox, ArrayList<Puzzle> puzzles) {
		this.name = name;
		this.description = description;
		this.roomContents = contents;
		this.roomBox = currentBox;
		this.roomPuzzles = puzzles != null ? puzzles: new ArrayList<>();
	}

	/**
	 * Finds an item in the room by its name
	 * @param itemName String name of the item
	 * @return Item object
	 */
	public Item findItemByName(String itemName) {
		for (Item item: roomContents) {
			if (item.getName().equalsIgnoreCase(itemName)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Finds a puzzle in the room by its name
	 * @param puzzleName String name of the puzzle
	 * @return Puzzle object
	 */
	public Puzzle findPuzzleByName(String puzzleName) {
		// TODO: Refactor with the Puzzle Class to have an identifier or name
		for (Puzzle puzzle: roomPuzzles) {
			if (puzzle.getName().equalsIgnoreCase(puzzleName)) {
				return puzzle;
			}
		}
		return null;
	}

	/**
	 * Checks if the room has a locked door
	 * @return boolean
	 */
	public boolean hasLockedDoor() {
		return hasLockedDoor;
	}

	/**
	 * Toggles the locked door status
	 * @param state boolean
	 */
	public void toggleLocked(boolean state) {
		hasLockedDoor = state;
	}

	/**
	 * Adds an item to the room
	 * @param item Item object
	 */
	public void addItem(Item item) {
		roomContents.add(item);
	}

	/**
	 * Removes an item from the room
	 * @param item Item object
	 */
	public void removeItem(Item item) {
		roomContents.remove(item);
	}

	/**
	 * Returns a prefabricated description of the room.
	 * <b>Note: This method is to be overhauled</b>
	 */
	public void getDescription() {
		// TODO: Refactor to comply with MessageDispatcher pattern
		System.out.println(this.name);
		System.out.println(this.description);
		if (this.roomContents.isEmpty()) {
			System.out.println("There are no items in the Room.");
		} else {
			System.out.print("Items in Room: ");
			System.out.println(this.roomContents);
		}
	}

	/**
	 * Returns the items in the room
	 * @return ArrayList<Item>
	 */
	public ArrayList<Item> getItems() {
		return roomContents;
	}

	/**
	 * Returns the puzzles in the room
	 * @return ArrayList<Puzzle>
	 */
	public ArrayList<Puzzle> getPuzzles() {
		return roomPuzzles;
	}

	/**
	 * Adds a puzzle to the room
	 * @param puzzle Puzzle object
	 */
	public void addPuzzle(Puzzle puzzle) {
		roomPuzzles.add(puzzle);
	}

	/**
	 * Returns whether all puzzles in the room have been solved
	 * @return boolean
	 */
	public boolean isAllPuzzlesSolved() {
		for (Puzzle puzzle : roomPuzzles) {
			if (!puzzle.isSolved) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the name of the room
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the contents of the room
	 * @return String
	 */
	public String getItemList() {
		return roomContents.toString();
	}

	/**
	 * Returns whether the room has puzzles
	 * @return boolean
	 */
	public boolean hasPuzzles() {
	    return !this.roomPuzzles.isEmpty();
	}

	/**
	 * Prints out the puzzles in the room
	 * <b>Note: This method is to be overhauled</b>
	 */
	public void showPuzzles() {
		// TODO: Refactor to comply with MessageDispatcher pattern
	    for (Puzzle puzzle : roomPuzzles) {
	        System.out.println("- " + puzzle.getDescription());
	    }
	}

	/**
	 * Returns whether all Puzzles in the room have been solved, whether the box is locked, and if the door is locked
	 * @return boolean
	 */
	public boolean isRoomCompleted() {
	    if (!isAllPuzzlesSolved()) {
	        return false;
	    }

	    if (roomBox != null && roomBox.getLocked()) {
	        return false;
	    }

        return !hasLockedDoor();
    }

}
