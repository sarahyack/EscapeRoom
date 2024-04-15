package core;

import commands.CommandParser;
import models.InventoryObserver;
import models.Item;
import models.Player;
import models.Room;
import utils.Priority;
import utils.PriorityMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Handles all UI elements for the game as well as the game logic and output.
 */
public class GameTerminal implements InventoryObserver {
    private final CommandParser parser;
    private final Player player;
    private final List<Room> allRooms;
	private final MessageDispatcher dispatch = MessageDispatcher.getInstance();
	private Timer dispatchTimer = null;
    private int currentRoomIndex = 0;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField inputField;
	private final JPanel iconGridPanel = new JPanel(new GridLayout(0, 6));

    public GameTerminal(CommandParser parser, Player player, List<Room> allRooms2) {
        this.parser = parser;
        this.player = player;
        this.allRooms = allRooms2;
        
        initialize();
        postWelcomeMessage();
        timedDispatch(false);
    }

	/**
	 * Initializes the JFrame to have a text area and input field. The layout is set to be a BorderLayout with a North, West, South, and Center panel.
	 */
	private void initialize() {
    	frame = new JFrame("Game Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		JPanel centerPanel = new JPanel();
    	centerPanel.setLayout(new BorderLayout());
    	
    	textArea = new JTextArea();
    	setUpTextArea();
		dispatch.setTextArea(textArea);
    	JScrollPane scrollPane = new JScrollPane(textArea);
    	centerPanel.add(scrollPane, BorderLayout.CENTER);
    	
    	JPanel bottomPanel = new JPanel();
    	bottomPanel.setLayout(new BorderLayout());
    	
    	inputField = new JTextField();
    	setUpInputField();
    	bottomPanel.add(inputField, BorderLayout.CENTER);
    	
    	JPanel eastPanel = new JPanel();
    	eastPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel westPanel = getWestPanel();

		JPanel northPanel = new JPanel();
    	northPanel.setLayout(new BorderLayout());
    	
    	JButton hintButton = new JButton("Get Hint");
    	northPanel.add(hintButton, BorderLayout.WEST);
    	
    	iconGridPanel.setBackground(Color.BLACK);
    	updateInventoryDisplay();
    	northPanel.add(iconGridPanel, BorderLayout.CENTER);
    	
    	frame.add(northPanel, BorderLayout.NORTH);
    	frame.add(centerPanel, BorderLayout.CENTER);
    	frame.add(westPanel, BorderLayout.WEST);
    	frame.add(bottomPanel, BorderLayout.SOUTH);
    	frame.setVisible(true);
    }

	/**
	 * Sets up the west panel with 64x64 repeated stone images.
	 * @return JPanel with 64x64 stone images
	 */
	private JPanel getWestPanel() {
		JPanel westPanel = new JPanel() {
			private Image stoneImage;

			{
				try {
					stoneImage = ImageIO.read(new File("resources/Stone.png"));
				} catch (IOException e) {
					System.out.println("No File to Read.");
				}
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (stoneImage != null) {
					for (int x = 0; x < getWidth(); x += stoneImage.getWidth(this)) {
						for (int y = 0; y < getHeight(); y += stoneImage.getHeight(this)) {
							g.drawImage(stoneImage, x, y, this);
						}
					}
				}
			}
		};
		int preferredWidth = 64;
		westPanel.setPreferredSize(new Dimension(preferredWidth, westPanel.getHeight()));
		westPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		return westPanel;
	}

	/**
	 * Sets up the text area for the game.
	 */
	private void setUpTextArea() {
    	textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(new Color(0, 255, 65));
    }

	/**
	 * Sets up the input field for the game. Adds an action listener to handle user input.
	 */
	private void setUpInputField() {
    	
    	inputField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0, 255, 65));
        inputField.setCaretColor(Color.WHITE);
        inputField.addActionListener(e -> {
            String text = inputField.getText();
			PriorityMessage message = new PriorityMessage(Priority.HIGH, "> " + text + "\n");
			dispatch.createPriorityMessage(message);
            inputField.setText("");

            handleCommand(text);
        });
    }

	/**
	 * Sends the first message to the player.
	 */
    private void postWelcomeMessage() {
		dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "Welcome to Escape Room!\n"));
        Room currentRoom = allRooms.get(currentRoomIndex);
		dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "You are in the " + currentRoom.getName() + ".\n"));
        currentRoom.getDescription();
    }   

	/**
	 * Handles the user input and dispatches messages to the player.
	 * @param command The user input
	 */
    private void handleCommand(String command) {

        if (command.equalsIgnoreCase("quit")) {
			dispatch.dispatchMessages();
			timedDispatch(true);
        	frame.dispose();
        } else {
        	Room currentRoom = allRooms.get(currentRoomIndex);
        	parser.parseCommand(command, player, currentRoom);
        	
        	if (currentRoom.isRoomCompleted()) {
				dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "Congratulations, you've completed the room!\n"));
                // player.dropUnnecessaryItems();
                currentRoomIndex++;
                if (currentRoomIndex >= allRooms.size()) {
					dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "You have escaped all rooms!\n"));
					dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "Game Over! Thanks for playing!\n"));
					dispatch.dispatchMessages();
					timedDispatch(true);
                    int delay = 5000;
                    Timer timer = new Timer(delay, e -> frame.dispose());
                    timer.setRepeats(false);
                    timer.start();
                    return;
                } else {
                    // Setup for the next room
                    currentRoom = allRooms.get(currentRoomIndex);
					dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "You have moved to the next room.\n"));
					dispatch.createPriorityMessage(new PriorityMessage(Priority.HIGH, "You are in the " + currentRoom.getName() + ".\n"));
                    // textArea.append("You are in \n");
                    currentRoom.getDescription();
                }
            }
        }
    }

	/**
	 * Starts or stops the dispatch timer.
	 * @param endGame If true, stops the timer. If false, starts the timer.
	 */
	private void timedDispatch(boolean endGame) {
		if (dispatchTimer == null) {
			dispatchTimer = new Timer(100, e -> {
				dispatch.dispatchMessages();
			});
			dispatchTimer.setRepeats(true);
		}
		if (!endGame) {
			if (!dispatchTimer.isRunning()) {
				dispatchTimer.start();
			}
		} else {
			dispatchTimer.stop();
			dispatchTimer = null;
		}
	}

	/**
	 * Resets and Updates the inventory display.
	 */
	private void updateInventoryDisplay() {
    	SwingUtilities.invokeLater(() -> {
    		iconGridPanel.removeAll();
        	
        	for (Item item : player.getInventory()) {
        	    ImageIcon icon = new ImageIcon("resources/ItemIcon.png");
        	    JLabel iconLabel = new JLabel(icon);
        	    iconLabel.addMouseListener(new MouseAdapter() {
        	        @Override
        	        public void mouseClicked(MouseEvent e) {
        	        	item.use();
        	        }
        	        
        	        @Override
        	        public void mouseEntered(MouseEvent e) {
        	        	iconLabel.setToolTipText(item.toString());
        	        }
        	    });
        	    iconGridPanel.add(iconLabel);
        	}
        	
        	iconGridPanel.revalidate();
            iconGridPanel.repaint();
    	});
    	
    }

	/**
	 * Implements the Observer interface to update the inventory display.
	 * @param player The Player Object
	 */
    @Override
    public void inventoryChanged(Player player) {
        updateInventoryDisplay();
    }
}
