package models;

// TODO: Refactor to comply with MessageDispatcher pattern

/**
 * The base class for all items in the game. Attributes are name, description, and whether it has been added to the inventory.
 */
public class Item {
	private final String name;
	private final String description;
	protected boolean hasIt = false;
	public boolean needed = true;
	
	public Item(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Adds the item to the player's inventory.
	 */
	public void addToInventory() {
		this.hasIt = true;
		System.out.println("You added " + name + " to your Inventory.");
	}

	/**
	 * Uses the item.
	 */
	public void use() {
		if (this.hasIt) {
			System.out.println("You used " + name + ".");
			needed = false;
		} else {
			System.out.println("You don't have " + name + " yet.");
		}
	}

	/**
	 * Removes the item from the player's inventory.
	 */
	public void removeFromInventory() {
		hasIt = false;
		System.out.println(name + " has been removed from your Inventory.");
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
