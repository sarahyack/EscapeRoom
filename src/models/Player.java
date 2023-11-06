package models;
import java.util.ArrayList;

public class Player {
	private ArrayList<Item> inventory;
	
	public Player() {
		inventory = new ArrayList<>();
	}
	
	public void addToInventory(Item item) {
		inventory.add(item);
		item.addToInventory();
	}
	
	public void removeFromInventory(Item item) {
		inventory.remove(item);
		item.removeFromInventory();
	}
	
	public void showInventory() {
		if (hasItemsInInventory()) {
			System.out.println("Items In Your Inventory:");
			for (Item item : inventory) {
				System.out.print(item.getName() + " - ");
				System.out.println(item.getDescription());
			}
		} else {
			System.out.println("There are no items in your inventory.");
		}
	}
	
	public Item findItemInInventory(String itemName) {
			for (Item item : inventory) {
				if (item.getName().equalsIgnoreCase(itemName)) {
					return item;
				}
			}
		return null;
	}
	
	public boolean hasItemsInInventory() {
		if (inventory.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public void dropUnnecessaryItems() {
        for (Item item : inventory) {
        	if (!item.needed) {
        		removeFromInventory(item);
        	}
        }
    }
}
