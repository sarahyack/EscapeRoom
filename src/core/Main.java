package core;

import java.util.Scanner;

import commands.CommandParser;

import java.util.ArrayList;

import models.*;

public class Main {
	
	public static ArrayList<Item> initializeItemList(String[] itemNames, String[] itemDescriptions) {
	    ArrayList<Item> itemList = new ArrayList<>();
	    for (int i = 0; i < itemNames.length; i++) {
	        String name = itemNames[i];
	        String description = itemDescriptions[i];
	        Item item = new Item(name, description);
	        itemList.add(item);
	    }
	    return itemList;
	}
	
	public static Room setupRoom(String roomName, String roomDescription, 
            String[] itemNames, String[] itemDescriptions,
            String[] boxItemNames, String[] boxItemDescriptions,
            boolean boxLocked, String boxDescription, 
            ArrayList<Puzzle> puzzles) {
			
				ArrayList<Item> roomItems = initializeItemList(itemNames, itemDescriptions);
				
				ArrayList<Item> boxItems = initializeItemList(boxItemNames, boxItemDescriptions);
				
				Box box = new Box(boxLocked, boxDescription, boxItems);
				
				Room room = new Room(roomName, roomDescription, roomItems, box, puzzles);
				
				return room;
			}


	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    
	    Player player = new Player();
	    
	    String[] roomItemNames = {"Gear"};
	    String[] roomItemDescriptions = {"A single little gear"};
	    
	    String[] boxItemNames = {"Teacup", "Tube"};
	    String[] boxItemDescriptions = {"a cup for tea.", "a roll of metal, probably useless."};
	    
	    ArrayList<Puzzle> puzzles = new ArrayList<>();
	    puzzles.add(new WordPuzzle("I require a pinkie's agility from one born into nobility", "Teacup"));
	    
	    Room room1 = setupRoom("Room 1", "Locked Room with a Box.",
	                            roomItemNames, roomItemDescriptions,
	                            boxItemNames, boxItemDescriptions,
	                            false, "This is a box.",
	                            puzzles);
	    
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
