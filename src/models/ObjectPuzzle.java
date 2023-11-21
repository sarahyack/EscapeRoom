package models;

public class ObjectPuzzle extends Puzzle {
	private final String requiredItem;
	
	public ObjectPuzzle(String description, String requiredItem) {
		super(description, PuzzleType.OBJECT);
		this.requiredItem = requiredItem;
	}
	
	@Override
	public boolean trySolve(Player player, Room currentRoom) {
		Item item = player.findItemInInventory(requiredItem);
		if (item != null) {
			isSolved = true;
			player.removeFromInventory(item);
        }
        return isSolved;
    }

	@Override
	public boolean trySolve(String attempt, Player player, Room currentRoom) {
		return false;
	}

}
