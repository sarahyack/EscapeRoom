package models;

// TODO: Refactor to comply with MessageDispatcher pattern

public class WordPuzzle extends Puzzle {
	private String solution;

	public WordPuzzle(String description, String solution) {
		super(description, PuzzleType.WORD);
		this.solution = solution;
	}
	
	@Override
	public boolean trySolve(String attempt, Player player, Room currentRoom) {
		if (attempt.equalsIgnoreCase(solution) ) {
			isSolved = true;
			System.out.println("You solved it!");
			return isSolved;
		} else {
			System.out.println("Sorry, try again!");
			return isSolved;
		}
	}

}
