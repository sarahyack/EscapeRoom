package models;

public class ObjectPuzzle extends Puzzle {
	private Item requiredItem;
	
	public ObjectPuzzle(String description, Item requiredItem) {
		super(description, PuzzleType.OBJECT);
		this.requiredItem = requiredItem;
	}
	
	@Override
	public boolean trySolve(Player player, Room currentRoom) {
		// TODO Auto-generated method stub
		if (player.findItemInInventory(requiredItem.getName()) != null) {
			isSolved = true;
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
