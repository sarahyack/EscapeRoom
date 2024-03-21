package core;

import commands.CommandParser;
import models.InventoryObserver;
import models.Item;
import models.Player;
import models.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameTerminal implements InventoryObserver {
	// TODO: Refactor to comply with MessageDispatcher pattern
    private final CommandParser parser;
    private final Player player;
    private final List<Room> allRooms;
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
        
    }
    
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
		MessageDispatcher.getInstance().setTextArea(textArea);
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

	private void setUpTextArea() {
    	textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(new Color(0, 255, 65));
    }
    
    private void setUpInputField() {
    	
    	inputField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0, 255, 65));
        inputField.setCaretColor(Color.WHITE);
        inputField.addActionListener(e -> {
            String text = inputField.getText();
            textArea.append("> " + text + "\n");
            inputField.setText("");

            handleCommand(text);
        });
    }

    private void postWelcomeMessage() {
        textArea.append("Welcome to Escape Room!\n");
        Room currentRoom = allRooms.get(currentRoomIndex);
        textArea.append("You are in:\n");
        currentRoom.getDescription();
    }   

    private void handleCommand(String command) {

        if (command.equalsIgnoreCase("quit")) {
        	frame.dispose();
        	return;
        } else {
        	Room currentRoom = allRooms.get(currentRoomIndex);
        	parser.parseCommand(command, player, currentRoom);
        	
        	if (currentRoom.isRoomCompleted()) {
                textArea.append("Congratulations, you've completed the room!\n");
                // player.dropUnnecessaryItems();
                currentRoomIndex++;
                if (currentRoomIndex >= allRooms.size()) {
                    textArea.append("You have escaped all rooms!\n");
                    textArea.append("Game Over! Thanks for playing!\n");
                    int delay = 5000;
                    Timer timer = new Timer(delay, e -> frame.dispose());
                    timer.setRepeats(false);
                    timer.start();
                    return;
                } else {
                    // Setup for the next room
                    currentRoom = allRooms.get(currentRoomIndex);
                    textArea.append("You have moved to the next room.\n");
                    // textArea.append("You are in \n");
                    currentRoom.getDescription();
                }
            }
        }
    }
    
    public void updateInventoryDisplay() {
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

    @Override
    public void inventoryChanged(Player player) {
        updateInventoryDisplay();
    }
}
