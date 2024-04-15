package models;

import core.MessageDispatcher;
import utils.Priority;
import utils.PriorityMessage;

/**
 * The unique key that opens the door.
 */
public class Key extends Item {
	MessageDispatcher dispatch = MessageDispatcher.getInstance();
	public Key(String name, String description) {
		super(name, description);
	}

	/**
	 * Checks if the player has the key and opens the door if they do. If not, prints an error message.
	 * @param player Player object
	 * @param currentRoom Room object
	 */
	public void setHasIt(Player player, Room currentRoom) {
        hasIt = currentRoom.roomBox.isAccessible() && !currentRoom.roomBox.getLocked();
	}

	/**
	 * Unlocks the door if the player has the key. If not, prints an error message.
	 * @param currentRoom Room object
	 * @param player Player object
	 */
	public void unlockDoor(Room currentRoom, Player player) {
		if (hasIt) {
			currentRoom.toggleLocked(false);
			Item key = player.findItemInInventory(this.getName());
			player.removeFromInventory(key);
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "You unlocked the door.\n"));
		} else {
			dispatch.createPriorityMessage(new PriorityMessage(Priority.NORMAL, "You do not have the key.\n"));
		}
	}

}
