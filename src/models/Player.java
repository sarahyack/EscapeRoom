package models;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private ArrayList<Item> inventory;
	private List<InventoryObserver> observers = new ArrayList<>();
	
	public Player() {
		inventory = new ArrayList<>();
	}
	
	public void addToInventory(Item item) {
		inventory.add(item);
		item.addToInventory();
		notifyObservers();
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
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (InventoryObserver observer : observers) {
            observer.inventoryChanged(this);
        }
    }
}
