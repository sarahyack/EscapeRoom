package models;

public class Key extends Item {

	public Key(String name, String description) {
		super(name, description);
	}
	
	public void setHasIt(Player player, Room currentRoom, Box box) {
		if (box.isAccessible && !box.isLocked) {
			hasIt = true;
		} else {
			hasIt = false;
		}
	}
	
	public void unlockDoor(Room currentRoom, Player player) {
		if (hasIt == true) {
			currentRoom.hasLockedDoor = false;
			Item key = player.findItemInInventory(this.getName());
			player.removeFromInventory(key);
			System.out.println("Door has been unlocked.");
		} else {
			System.out.println("You do not have the key.");
		}
	}

}
