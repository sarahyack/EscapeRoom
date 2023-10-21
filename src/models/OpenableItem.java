package models;

import java.util.ArrayList;

public class OpenableItem extends Item {
	public ArrayList<Item> contents;
	public boolean isOpen = false;

	public OpenableItem(String name, String description, ArrayList<Item> boxContents) {
		super(name, description);
		this.contents = boxContents;
	}
	
	public Item open(Player player) {
		for (Item item : contents) {
			player.addToInventory(item);
		}
		return null;
	}
	
	public void toggleOpen() {
		if (isOpen == false) {
			isOpen = true;
		} else {
			isOpen = true;
		}
	}
}
