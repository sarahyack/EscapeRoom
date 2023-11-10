package core;

import javax.swing.*;


import commands.CommandParser;
import models.*;
// import core.TextAreaOutputStream;

import java.awt.*;
import java.io.PrintStream;
import java.util.List;

public class GameTerminal extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
    private JTextField inputField;
    private CommandParser parser;
    private Player player;
    private List<Room> allRooms;
    private int currentRoomIndex = 0;

    public GameTerminal(CommandParser parser, Player player, List<Room> allRooms2) {
        this.parser = parser;
        this.player = player;
        this.allRooms = allRooms2;
        createUI();
        redirectSystemStreams();
        postWelcomeMessage();
        
    }

    private void redirectSystemStreams() {
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(textArea));
        System.setOut(printStream);
        // System.setErr(printStream);
    }

    private void postWelcomeMessage() {
        textArea.append("Welcome to Escape Room!\n");
        Room currentRoom = allRooms.get(currentRoomIndex);
        textArea.append("You are in:\n");
        currentRoom.getDescription();
    }

    private void createUI() {
        // Set up the main frame
        setTitle("Escape Room Game Terminal");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(0, 0, 0));
        textArea.setForeground(new Color(0, 255, 65));

        // Make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create the input field
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0, 255, 65));
        inputField.setCaretColor(Color.WHITE);
        inputField.addActionListener(e -> {
            String text = inputField.getText();
            textArea.append("> " + text + "\n");
            inputField.setText("");

            // Here you can handle the input command
            handleCommand(text);
        });

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }    

    private void handleCommand(String command) {

        if (command.equalsIgnoreCase("quit")) {
        	dispose();
        	return;
        } else {
        	Room currentRoom = allRooms.get(currentRoomIndex);
        	parser.parseCommand(command, player, currentRoom);
        	
        	if (currentRoom.isRoomCompleted()) {
                textArea.append("Congratulations, you've completed the room!\n");
                player.dropUnnecessaryItems();
                currentRoomIndex++;
                if (currentRoomIndex >= allRooms.size()) {
                    textArea.append("You have escaped all rooms!\n");
                } else {
                    // Setup for the next room
                    currentRoom = allRooms.get(currentRoomIndex);
                    textArea.append("You have moved to the next room.\n");
                    // Assuming getDescription is modified to return a String
                    textArea.append("You are in \n");
                    currentRoom.getDescription();
                }
            }
        }
    }

    // Don't forget to include the TextAreaOutputStream class here or in its own file
    // ...
}
