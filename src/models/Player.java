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
		for (Item item : inventory) {
			System.out.print(item.getName() + " - ");
			System.out.println(item.getDescription());
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
}
