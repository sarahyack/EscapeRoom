package models;

public class Room {
	public String name;
	public String description;
	public boolean hasLockedDoor = true;
	
	public Room(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Item findItemByName(String itemName) {
		return null;
	}
}
