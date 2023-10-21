package models;

import java.util.HashMap;

public class AnimalPuzzle extends Puzzle {
	private HashMap<String, Boolean> animalTasks;

	public AnimalPuzzle(String description, HashMap<String, Boolean> tasks) {
		super(description, PuzzleType.ANIMAL);
		this.animalTasks = tasks;
	}

	@Override
	public boolean trySolve(String attempt, Player player, Room currentRoom) {
		for (String task : animalTasks.keySet()) {
			if (!animalTasks.get(task)) {
				System.out.println("The task \"" + task + "\" is not completed");
				return false;
			}
		}
		
		System.out.println("All tasks have been completed!");
		return true;
	}
	
	public void completeTask(String task) {
		if (animalTasks.containsKey(task)) {
			animalTasks.put(task, true);
		}
	}
	
	public void showTasks() {
		for (String task : animalTasks.keySet()) {
			System.out.println(task + " - " + (animalTasks.get(task) ? "Completed" : "Not Completed"));
		}
	}

}
