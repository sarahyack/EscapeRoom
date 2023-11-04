package models;

public class Key extends Item {

	public Key(String name, String description) {
		super(name, description);
	}
	
	public void setHasIt(Player player, Room currentRoom) {
		if (currentRoom.roomBox.isAccessible() && !currentRoom.roomBox.getLocked()) {
			hasIt = true;
		} else {
			hasIt = false;
		}
	}
	
	public void unlockDoor(Room currentRoom, Player player) {
		if (hasIt == true) {
			currentRoom.toggleLocked(false);
			Item key = player.findItemInInventory(this.getName());
			player.removeFromInventory(key);
			System.out.println("Door has been unlocked.");
		} else {
			System.out.println("You do not have the key.");
		}
	}

}
