package models;

import core.MessageDispatcher;
import utils.Priority;
import utils.PriorityMessage;

/**
 * The base class for all items in the game. Attributes are name, description, and whether it has been added to the inventory.
 */
public class Item {
	private final String name;
	private final String description;
	protected boolean hasIt = false;
	public boolean needed = true;
	public static final MessageDispatcher dispatch = MessageDispatcher.getInstance();
	
	public Item(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Adds the item to the player's inventory.
	 */
	public void addToInventory() {
		this.hasIt = true;
		dispatch.createPriorityMessage(new PriorityMessage(Priority.LOW, "You added " + name + " to your Inventory.\n"));
	}

	/**
	 * Uses the item.
	 */
	public void use() {
		if (this.hasIt) {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.LOW, "You used " + name + ".\n"));
			needed = false;
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.LOW, "You don't have " + name + ".\n"));
		}
	}

	/**
	 * Removes the item from the player's inventory.
	 */
	public void removeFromInventory() {
		hasIt = false;
		dispatch.createPriorityMessage(new PriorityMessage(Priority.LOW, "You removed " + name + " from your Inventory.\n"));
	}

	/**
	 * Returns the name of the item.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of the item*
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Reforms the standard toString() method to return the name and description of the item in the form "name: description".
	 * @return String
	 */
	@Override
	public String toString() {
		return name + ": " + description;
	}
}
