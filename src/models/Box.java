package models;

import java.util.ArrayList;

public class Box {
	private boolean isAccessible = false;
	private boolean isLocked;
	private Key key = new Key("Key", "A shiny golden key");
	private Item item1;
	private Item item2;
	
	public Box(boolean isLocked, String boxDescription, ArrayList<Item> boxItems) {
		this.isLocked = isLocked;
		this.item1 = boxItems.get(0);
		this.item2 = boxItems.get(1);
	}
	
	public void setAccessible(boolean state) {
		isAccessible = state;
	}
	
	public boolean isAccessible() {
		return isAccessible;
	}
	
	public void toggleLockBox(boolean state) {
		isLocked = state;
	}
	
	public boolean getLocked() {
		return isLocked;
	}
	
	public Item open(Player player, Room currentRoom) {
		if (isAccessible == true) {
			player.addToInventory(key);
			System.out.println("The Key has been added to your inventory!");
			player.addToInventory(item1);
			System.out.println(item1.getName() + " has been added to your inventory.");
			player.addToInventory(item2);
			System.out.println(item2.getName() + " has been added to yout inventory.");
		} else {
			System.out.println("The Box is Inaccessible.");
		}
		return null;
	}
}
