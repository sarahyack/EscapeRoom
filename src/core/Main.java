package core;

import java.util.Scanner;

import commands.CommandParser;

import java.util.List;

import models.*;
import utils.JSONReader;

public class Main {


	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    
	    Player player = new Player();
	    CommandParser parser = new CommandParser();
	    
	    List<Room> allRooms = JSONReader.readRoomData();
	    int currentRoomIndex = 0;
	    
	    System.out.println("Welcome to Escape Room!");
	    boolean firstTime = true;
	    
	    while (true) {
	    	Room currentRoom = allRooms.get(currentRoomIndex);
	    	
	        if (firstTime) {
	            System.out.println("You are in:");
	            currentRoom.getDescription();
	            firstTime = false;
	        }
	        
	        System.out.print("> ");
	        String input = scanner.nextLine();
	        
	        if ("quit".equalsIgnoreCase(input)) {
	            break;
	        } else {
	            parser.parseCommand(input, player, currentRoom);
	            
	            if (currentRoom.isAllPuzzlesSolved()) {
	                System.out.println("Congratulations, you've completed the room!");
	                // Move to the next room
	                currentRoomIndex++;
	                if (currentRoomIndex >= allRooms.size()) {
	                    System.out.println("You have escaped all rooms! Game over!");
	                    break;
	                } else {
	                    // Setup for the next room
	                    currentRoom = allRooms.get(currentRoomIndex);
	                    currentRoom.getDescription();
	                }
	            }
	        }
	    }
	    
	    scanner.close();
	    System.out.println("Game Over! Thanks for playing!");
	}


}
