package models;

import java.util.ArrayList;

public class Room {
	private String name;
	private String description;
	public boolean hasLockedDoor = true;
	private ArrayList<Item> roomContents;
	public Box roomBox;
	private Puzzle puzzle;
	
	public Room(String name, String description, ArrayList<Item> contents, Box currentBox, Puzzle puzzle) {
		this.name = name;
		this.description = description;
		this.roomContents = contents;
		this.roomBox = currentBox;
		this.puzzle = puzzle;
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
	
	public Puzzle getPuzzle() {
		return puzzle;
	}
}
