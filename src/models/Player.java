package models;
import core.MessageDispatcher;
import utils.Priority;
import utils.PriorityMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represents the player character in the Escape Room game. Represents the player character with a manageable inventory in the game.
 *
 * <p>
 * Utilizes the Observer pattern to notify interested parties (e.g., GUI components)
 * whenever the inventory changes, allowing for dynamic updates to the inventory display.
 * Observers can be added or removed to subscribe or unsubscribe from inventory updates.
 * </p>
 * @author Sarah Yack
 */
public class Player {
	private final ArrayList<Item> inventory;
	private final List<InventoryObserver> observers = new ArrayList<>();
	MessageDispatcher dispatch = MessageDispatcher.getInstance();
	
	public Player() {
		inventory = new ArrayList<>();
	}

	/**
	 * Adds an item to the player's inventory.
	 * @param item The item to add.
	 */
	public void addToInventory(Item item) {
		inventory.add(item);
		item.addToInventory();
		notifyObservers();
	}

	/**
	 * Removes an item from the player's inventory.
	 * @param item The item to remove.
	 */
	public void removeFromInventory(Item item) {
		inventory.remove(item);
		item.removeFromInventory();
		notifyObservers();
	}

	/**
	 * Displays the name and description of each item in the inventory.
	 */
	public void showInventory() {
		if (hasItemsInInventory()) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "Items In Your Inventory: " + "\n"));
			for (Item item : inventory) {
				dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, item.getName() + " - " + item.getDescription() + "\n"));
			}
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "There are no items in your inventory." + "\n"));
		}
	}

	/**
	 * Finds an item in the player's inventory by its name.
	 * @param itemName The name of the item.
	 * @return The item if found, or null if not found.
	 */
	public Item findItemInInventory(String itemName) {
			for (Item item : inventory) {
				if (item.getName().equalsIgnoreCase(itemName)) {
					return item;
				}
			}
		return null;
	}

	/**
	 * Checks if the player has any items in their inventory.
	 * @return True if the player has items in their inventory, false otherwise.
	 */
	public boolean hasItemsInInventory() {
        return !inventory.isEmpty();
	}

	/**
	 * Drops unnecessary items from the player's inventory.
	 * <p>
	 * <b>Note: This method is to be overhauled or removed</b>.
	 */
	public void dropUnnecessaryItems() {
        for (Item item : inventory) {
        	if (!item.needed) {
        		removeFromInventory(item);
        	}
        }
    }

	/**
	 * Gets the player's inventory.
	 * @return The player's inventory.
	 */
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	/**
	 * Adds an inventory observer to the list of observers.
	 * @param observer An inventory observer.
	 */
	public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

	/**
	 * Removes an inventory observer from the list of observers.
	 * @param observer An inventory observer.
	 */
    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

	/**
	 * Notifies all observers of an inventory change.
	 */
    private void notifyObservers() {
        for (InventoryObserver observer : observers) {
            observer.inventoryChanged(this);
        }
    }
}
