package models;

import java.util.List;

public class Box {
	public boolean isAccessible = false;
	public boolean isLocked;
	public Key key;
	public Item item1;
	public Item item2;
	
	public Box(boolean isLocked, String boxDescription, List<Item> boxItems) {
		this.isLocked = isLocked;
		this.item1 = boxItems[0];
		this.item2 = boxItems[1];
	}
	
	public void setAccessible(boolean state) {
		if (state == true) {
			isAccessible = true;
		} else {
			isAccessible = false;
		}
	}
	
	public Item open(Player player, Box currentBox, Room currentRoom) {
		if (isAccessible == true) {
			player.addToInventory(key);
			player.addToInventory(item1);
			player.addToInventory(item2);
		} else {
			System.out.println("The Box is Inaccessible.");
		}
		return null;
	}
}
