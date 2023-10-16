package commands;

import models.Box;
import models.Item;
import models.Player;
import models.Room;

public class CommandParser {
	
	public void pickUpItem(Player player, Item item) {
		player.addToInventory(item);
	}
	
	public void useItem(Player player, Item item) {
		item.use();
		if (item.needed == false) {
			player.removeFromInventory(item);
		}
	}
	
	public void examine(Item item) {
		System.out.print(item.getName() + " - ");
		System.out.println(item.getDescription());
	}
	
	public void drop() {
		
	}
	
	public void look(Room room) {
		
	}
	
	public void openClose(Item item) {
		
	}
	
	public void openClose(Box box) {
		
	}
	
	public void pushPull() {
		
	}
	
	public void turn() {
		
	}
	
	public void checkInventory(Player player) {
		player.showInventory();
	}
	
	public void parseCommand(String input, Player player, Room currentRoom) {
		String[] parts = input.split(" ");
		String command = parts[0];
		String objectName = parts[1];
		
		Item item = currentRoom.findItemByName(objectName);
		
		if (item == null && "key".equals(objectName) && currentRoom.hasLockedDoor) {
			item = player.findItemInInventory(objectName);
		}
		
		if (item == null) {
			System.out.println("That item doesn't exist in this room or your inventory.");
		}
		
		if ("pick".equals(command)) {
			pickUpItem(player, item);
		} else if ("use".equals(command)) {
			useItem(player, item);
		} else {
			System.out.println("Invalid command: " + command);
		}
	}
}
