package models;

import core.MessageDispatcher;
import utils.Priority;
import utils.PriorityMessage;

import java.util.ArrayList;


/**
 * A class representing a box in the Escape Room game. Box attributes are accessible, locked, and items.
 * @author Sarah Yack
 */
public class Box {
	private boolean isAccessible = false;
	private boolean isLocked;
	private boolean hasBeenOpened = false;
	private final Key key = new Key("Key", "A shiny golden key");
	private final Item item1;
	private final Item item2;
	MessageDispatcher dispatch = MessageDispatcher.getInstance();
	
	public Box(boolean isLocked, String boxDescription, ArrayList<Item> boxItems, ArrayList<Boolean> itemsNeeded) {
		this.isLocked = isLocked;
		this.item1 = boxItems.get(0);
		this.item2 = boxItems.get(1);
		this.key.needed = true;
		setItemsNeeded(itemsNeeded);
	}

	/**
	 * Sets the box to be accessible or not depending on the state
	 * @param state boolean
	 */
	public void setAccessible(boolean state) {
		isAccessible = state;
	}

	/**
	 * Checks if the box is accessible
	 * @return boolean
	 */
	public boolean isAccessible() {
		return isAccessible;
	}

	/**
	 * Toggles the lock state of the box
	 * @param state boolean
	 */
	public void toggleLockBox(boolean state) {
		isLocked = state;
	}

	/**
	 * Checks if the box is locked
	 * @return boolean
	 */
	public boolean getLocked() {
		return isLocked;
	}

	/**
	 * Sets the needed value of the item in the box to true if it is needed
	 * @param item
	 */
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
				break;
		}
	}

	/**
	 * Iterates through the itemsNeeded array and sets the needed value of the items in the box to true if it is needed
	 * @param itemsNeeded ArrayList of booleans
	 */
	public void setItemsNeeded(ArrayList<Boolean> itemsNeeded) {
		for (int i = 0; i < itemsNeeded.size(); i++) {
	        if (!itemsNeeded.get(i)) {
	            setNeeded(i);
	        }
	    }
	}

	/**
	 * Opens the box and adds the key and items to the player's inventory
	 * @param player Player object
	 * @param currentRoom Room object
	 */
	public void open(Player player, Room currentRoom) {
		if (isAccessible && !hasBeenOpened) {
			player.addToInventory(key);
			player.addToInventory(item1);
			player.addToInventory(item2);
			hasBeenOpened = true;
		} else if (hasBeenOpened) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "The Box has already been opened.\n"));
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "The Box is inaccessible.\n"));
		}
	}
}
