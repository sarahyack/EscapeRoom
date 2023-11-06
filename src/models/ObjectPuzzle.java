package models;

public class ObjectPuzzle extends Puzzle {
	private String requiredItem;
	
	public ObjectPuzzle(String description, String requiredItem) {
		super(description, PuzzleType.OBJECT);
		this.requiredItem = requiredItem;
	}
	
	@Override
	public boolean trySolve(Player player, Room currentRoom) {
		// TODO Auto-generated method stub
		Item item = player.findItemInInventory(requiredItem);
		if (item != null) {
			isSolved = true;
			player.removeFromInventory(item);
			return isSolved;
		} else {
			return isSolved;
		}
	}

	@Override
	public boolean trySolve(String attempt, Player player, Room currentRoom) {
		// TODO Auto-generated method stub
		return false;
	}

}
