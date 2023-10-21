package models;

public class Item {
	private String name;
	private String description;
	protected boolean hasIt = false;
	public boolean needed = true;
	
	public Item(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public void addToInventory() {
		hasIt = true;
		System.out.println("You added " + name + " to your Inventory.");
	}
	
	public void use() {
		if (hasIt) {
			System.out.println("You used" + name + ".");
			needed = false;
		} else {
			System.out.println("You don't have " + name + " yet.");
		}
	}
	
	public void removeFromInventory() {
		hasIt = false;
		System.out.println(name + " has been removed from your Inventory.");
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return name + ": " + description;
	}
}
