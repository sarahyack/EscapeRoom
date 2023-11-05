package models;

import java.util.ArrayList;

public class Room {
	private String name;
	private String description;
	private boolean hasLockedDoor = true;
	private ArrayList<Item> roomContents;
	public Box roomBox;
	private ArrayList<Puzzle> roomPuzzles;
	
	public Room(String name, String description, ArrayList<Item> contents, Box currentBox, ArrayList<Puzzle> puzzles) {
		this.name = name;
		this.description = description;
		this.roomContents = contents;
		this.roomBox = currentBox;
		this.roomPuzzles = puzzles != null ? puzzles: new ArrayList<>();
	}
	
	public Item findItemByName(String itemName) {
		for (Item item: roomContents) {
			System.out.println("Checking item: " + item.getName());
			System.out.println("Comparing: " + item.getName() + " with " + itemName);
			if (item.getName().equalsIgnoreCase(itemName)) {
				return item;
			}
		}
		return null;
	}

	public boolean hasLockedDoor() {
		return hasLockedDoor;
	}
	
	public void toggleLocked(boolean state) {
		hasLockedDoor = state;
	}
	
	public void addItem(Item item) {
		roomContents.add(item);
	}
	
	public void removeItem(Item item) {
		roomContents.remove(item);
	}
	
	public void getDescription() {
		System.out.println(this.name);
		System.out.println(this.description);
		System.out.print("Items in Room: ");
		System.out.println(this.roomContents);
	}
	
	public ArrayList<Puzzle> getPuzzles() {
		return roomPuzzles;
	}
	
	public void addPuzzle(Puzzle puzzle) {
		roomPuzzles.add(puzzle);
	}
	
	public boolean isAllPuzzlesSolved() {
		for (Puzzle puzzle : roomPuzzles) {
			if (!puzzle.isSolved) {
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public String getItemList() {
		return roomContents.toString();
	}
	
	public boolean hasPuzzles() {
	    return !this.roomPuzzles.isEmpty();
	}

	public void showPuzzles() {
	    for (Puzzle puzzle : roomPuzzles) {
	        System.out.println("- " + puzzle.getDescription());
	    }
	}

}
