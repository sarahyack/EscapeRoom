package models;

// TODO: Refactor to comply with MessageDispatcher pattern

public class MovementPuzzle extends Puzzle {
	private String targetItem;
	private String targetAction;

	public MovementPuzzle(String description, String item, String action) {
		super(description, PuzzleType.MOVEMENT);
		this.targetItem = item;
		this.targetAction = action;
	}

	@Override
	public boolean trySolve(String action, Player player, Room currentRoom) {
		if (currentRoom.findItemByName(targetItem) != null && action.equalsIgnoreCase(targetAction)) {
			isSolved = true;
			return isSolved;
		} else {
			System.out.println("Sorry, the conditions for solving this puzzle are not met.");
			return isSolved;
		}
	}
	
	

}
