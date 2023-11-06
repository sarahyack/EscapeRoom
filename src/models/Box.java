package models;

import java.util.ArrayList;

public class Box {
	private boolean isAccessible = false;
	private boolean isLocked;
	private boolean hasBeenOpened = false;
	private Key key = new Key("Key", "A shiny golden key");
	private Item item1;
	private Item item2;
	
	public Box(boolean isLocked, String boxDescription, ArrayList<Item> boxItems, ArrayList<Boolean> itemsNeeded) {
		this.isLocked = isLocked;
		this.item1 = boxItems.get(0);
		this.item2 = boxItems.get(1);
		this.key.needed = true;
		setItemsNeeded(itemsNeeded);
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
	
	public void setNeeded(int item) {
		switch (item) {
			case (0):
				item1.needed = false;
				break;
			case (1):
				item2.needed = false;
				break;
			default:
				System.out.println("Must pass either 0 or 1");
			
		}
	}
	
	public void setItemsNeeded(ArrayList<Boolean> itemsNeeded) {
		for (int i = 0; i < itemsNeeded.size(); i++) {
	        if (!itemsNeeded.get(i)) {
	            setNeeded(i);
	        }
	    }
	}
	
	public Item open(Player player, Room currentRoom) {
		if (isAccessible == true && hasBeenOpened == false) {
			player.addToInventory(key);
			player.addToInventory(item1);
			player.addToInventory(item2);
			hasBeenOpened = true;
		} else if (hasBeenOpened == true) {
			System.out.println("The Box has already been opened.");
		} else {
			System.out.println("The Box is Inaccessible.");
		}
		return null;
	}
}
