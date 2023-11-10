package core;

import javax.swing.SwingUtilities;

import commands.CommandParser;

import java.util.List;

import models.*;
import utils.JSONReader;

public class Main {


	public static void main(String[] args) {
        Player player = new Player();
        CommandParser parser = new CommandParser();
        List<Room> allRooms = JSONReader.readRoomData();

        SwingUtilities.invokeLater(() -> {
            GameTerminal terminal = new GameTerminal(parser, player, allRooms);
            terminal.setVisible(true);
        });
	}


}
