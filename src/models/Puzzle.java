package models;

public abstract class Puzzle {
	enum PuzzleType {
		WORD, OBJECT, MOVEMENT, ANIMAL
	}
	private String description;
	protected String hint;
	protected boolean isSolved;
	private PuzzleType type;
	
	public Puzzle(String description, PuzzleType puzzleType) {
		this.description = description;
		this.isSolved = false;
		this.type = puzzleType;
		
	}
	
	public String getDescription() {
		return description;
	}
	
	public PuzzleType getType() {
		return type;
	}
	
	public void puzzleInfo() {
		System.out.println(getDescription());
		System.out.print("Puzzle Type: ");
		System.out.println(getType());
	}
	
	public boolean isSolved() {
		return isSolved;
	}
	
	public abstract boolean trySolve(String attempt, Player player, Room currentRoom);
	
	public boolean trySolve(Player player, Room currentRoom) {
		return false;
	}
		
	public void setHint(String puzzleHint) {
		hint = puzzleHint;
	}
	
	public String getHint() {
		return hint;
	}
}
