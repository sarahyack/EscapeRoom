package core;

import java.util.Scanner;

import commands.CommandParser;

import java.util.ArrayList;
import java.util.List;

import models.*;

public class Main {
	
	public static Room setupRoom(String roomName, String roomDescription, 
						            List<String> itemNames, List<String> itemDescriptions,
						            ArrayList<Item> boxItems,
						            boolean boxLocked, String boxDescription, 
						            String puzzleDescription, String puzzleSolution) {
			// Initialize items
			ArrayList<Item> items = new ArrayList<>();
			for (int i = 0; i < itemNames.size(); i++) {
				Item item = new Item(itemNames.get(i), itemDescriptions.get(i));
				items.add(item);
			}
			
			// Initialize box
			Box box = new Box(boxLocked, boxDescription, boxItems);
			
			// Initialize puzzle
			Puzzle puzzle = new WordPuzzle(puzzleDescription, puzzleSolution);
			
			// Initialize room
			Room room = new Room(roomName, roomDescription, items, box, puzzle);
			
			return room;
			}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Player player = new Player();
		ArrayList<Item> room1Contents = new ArrayList<Item>();
		Item teacup = new Item("Teacup", "a cup for tea.");
		Item tube = new Item("Tube", "a roll of metal, probably useless.");
		Box box1 = new Box(false, teacup, tube);
		Item gear = new Item("Gear", "A single little gear");
		room1Contents.add(gear);
		Puzzle riddlePuzzle = new WordPuzzle("I require a pinkie's agility from one born into nobility", "Teacup");
		Room room1 = new Room("Room 1", "Locked Room with a Box.", room1Contents, box1, riddlePuzzle);

		
		CommandParser parser = new CommandParser();
		
		System.out.println("Welcome to Escape Room!");
		boolean firstTime = true;
		
		while (true) {
			if (firstTime) {
				System.out.println("You are in:");
				room1.getDescription();
				firstTime = false;
			}
			
			System.out.print("> ");
			String input = scanner.nextLine();
			
			if ("quit".equalsIgnoreCase(input)) {
				break;
			} else {
				parser.parseCommand(input, player, room1);
			}
		}
		
		scanner.close();
		System.out.println("Game Over! Thanks for playing!");
		
	}

}
