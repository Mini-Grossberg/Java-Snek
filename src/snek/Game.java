package snek;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

class GamePanel extends JPanel implements ActionListener{
	final Color PANEL = new Color(0xEBF5F7);				// Colour for the Game Area
	final Color SNAKE = new Color(0xA5D8C4);				// Colour for the Snake
	final Color FOOD = new Color(0xFFC0CB);					// Colour for the Food
	final Color TEXT = new Color(0x606060);					// Colour for the Text
	
	final int PANELSIZE = 900; 								// Size of the total playable area in pixels.
	final int SNAKEPIXEL = 60; 								// Size of the snake in pixels.
	final int GRIDPIXEL = PANELSIZE/SNAKEPIXEL;				// Number of grid boxes.
	final int MAX_SNAKE_LENGTH = GRIDPIXEL * GRIDPIXEL;		// Victory length - maximum size of the snake.
	
	final int[] x = new int[MAX_SNAKE_LENGTH];				// X Coordinates of the Snake.
	final int[] y = new int[MAX_SNAKE_LENGTH];				// Y Coordinates of the Snake.
	
	int snakeLength = 5;									// Initial Length of the Snake.
	
	static final int UP = -2;								// Direction -- UP.
	static final int DOWN = 2;								// Direction -- DOWN.
	static final int RIGHT = 1;								// Direction -- RIGHT.
	static final int LEFT = -1;								// Direction -- LEFT.
	
	private final Random random = new Random();
	private int foodX;										// X Coordinate of the Food.
	private int foodY;										// Y Coordinate of the Food.
	
	boolean directionChangeFlag = false;					// Used this to avoid immediate changing of direction allowing for the snake to change directions illegally.
	
	Timer timer;
	
	final int DELAY = 100;
	
	// -- Speed/FPS Control --
	private double moveCounter = 0;
	private double currentSpeed = 3;
	
	// -- Direction Control --
	static int direction = RIGHT;
	
	boolean running = false;
	
	// Run game -- Constructor --
	public GamePanel() {
		this.setBackground(PANEL);
		
		Dimension d = new Dimension(PANELSIZE, PANELSIZE);
		this.setPreferredSize(d);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
		
		for (int i = 0; i < snakeLength; i++) {
			x[i] = GRIDPIXEL/2 - i;
			y[i] = GRIDPIXEL/2;
		}
		
		setupKeyBindings();
		startGame();
	}
	
	// -- Keybinds --
	private void setupKeyBindings() {
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();
		
		KeyStroke keyW = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
		KeyStroke keyA = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
		KeyStroke keyS = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
		KeyStroke keyD = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0);
		
		inputMap.put(keyW, "moveUp");
		inputMap.put(keyA, "moveLeft");
		inputMap.put(keyS, "moveDown");
		inputMap.put(keyD, "moveRight");
		
		actionMap.put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GamePanel.direction != DOWN && !directionChangeFlag) {
					GamePanel.direction = UP;
					directionChangeFlag = true;
				}
			}
		});
		
		actionMap.put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GamePanel.direction != UP && !directionChangeFlag) {
					GamePanel.direction = DOWN;
					directionChangeFlag = true;
				}
			}
		});
		
		actionMap.put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GamePanel.direction != RIGHT && !directionChangeFlag) {
					GamePanel.direction = LEFT;
					directionChangeFlag = true;
				}
			}
		});
		
		actionMap.put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GamePanel.direction != LEFT && !directionChangeFlag) {
					GamePanel.direction = RIGHT;
					directionChangeFlag = true;
				}
			}
		});
	}
	
	// -- Check if the snake is running into anything --
	public void checkCollisions() {
		// -- Collision with Self -- 
		for (int i = snakeLength; i > 0; i--) {
			if ((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
		}
		
		// -- Collision with Walls --
		if (x[0] < 0) {
			running = false;
		}
		else if (y[0] < 0) {
			running = false;
		}
		else if (x[0] > GRIDPIXEL) {
			running = false;
		}
		else if (x[0] > GRIDPIXEL) {
			running = false;
		}
		
		// -- Game Over --
		if (!running) stopGame();
	}
	
	// -- Assign new food spawn locations -- 
	public void initFood() {
		foodX = random.nextInt(GRIDPIXEL-2);
		foodY = random.nextInt(GRIDPIXEL-2);
	}
	
	// -- Check if the food is spawning inside the snake --
	public boolean checkInitFood(int a, int b) {
		for (int i = 0; i < snakeLength; i++) {
			if (a == x[i] && b == y[i]) return false;
		}return true;
	}
	
	// -- Main Game Loop --
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			moveCounter += 0.2;
			
			if (moveCounter >= currentSpeed || currentSpeed == 0) {
				move();
				checkFood();
				checkCollisions();
				
				directionChangeFlag = false;
				
				moveCounter = 0;
			}
		}
		
		repaint();
	}
	
	// -- Used to change the location of the snake every frame -- 
	public void move() {
		for (int i = snakeLength; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		switch(direction) {
		case RIGHT:
			x[0] = x[0] + 1;
			break;
		case LEFT:
			x[0] = x[0] - 1;
			break;
		case UP:
			y[0] = y[0] - 1;
			break;
		case DOWN:
			y[0] = y[0] + 1;
			break;
		}
	}

	// -- checks if the food is eaten by the snake --
	public void checkFood() {
		if ((x[0] == foodX && y[0] == foodY)) {
			snakeLength++;
			currentSpeed /= 1.5;
			while (!checkInitFood(foodX, foodY)) initFood();
		}
	}
	
	// -- Paint the graphics --
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(running) {
			spawnFood(g);
			drawSnake(g);
		}
		
		else {
			gameOver(g);
		}
		
		Toolkit.getDefaultToolkit().sync(); // GODSEND -- syncs the painting of the graphics with the timer --
	}
	
	// -- Draw the snake --
	public void drawSnake(Graphics g) {
		g.setColor(SNAKE);
		
		for (int i = 0; i < snakeLength; i++)	{
			g.fillRect(x[i] * SNAKEPIXEL, y[i] * SNAKEPIXEL, SNAKEPIXEL, SNAKEPIXEL);
		}
	}
	
	// -- Make the food --
	public void spawnFood(Graphics g) {
		g.setColor(FOOD);
			
		g.fillRect(foodX * SNAKEPIXEL, foodY * SNAKEPIXEL, SNAKEPIXEL, SNAKEPIXEL);
	}
	
	
	// Run Game -- Initial Settings --
	public void startGame() {
		initFood();
		running = true;
		
		if(timer == null) {
			timer = new Timer(DELAY, this);
		}
		timer.start();
	}
	
	// -- Continues the game of paused in between -- 
	public void runGame() {
		running = true;
		if (timer != null) {
			timer.start();
		}
	}
	
	// -- Pauses/Stops the game --
	public void stopGame() {
		running = false;
		if (timer != null) {
			timer.stop();
		}
	}
	
	// -- Resets everything to scratch and runs the game --
	public void resetGame() {
		stopGame();
		snakeLength = 5;
		for (int i = 0; i < snakeLength; i++) {
			x[i] = GRIDPIXEL / 2 - i;
			y[i] = GRIDPIXEL / 2;
		}
		
		currentSpeed = 3;
		moveCounter = 0;
		
		direction = RIGHT;
		directionChangeFlag = false;
		
		startGame();
		repaint();
	}
	
	// GAME OVER
	public void gameOver(Graphics g) {
		String message = "Game Over!\n\nScore: " + (snakeLength - 5);
		
		g.setColor(TEXT);
		g.setFont(new Font("Garamond", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		// Center the text:
		g.drawString(message, 
				(PANELSIZE - metrics.stringWidth(message)) / 2, PANELSIZE / 2);
	}
}

// -- Read Key Pressed EVENT --
class KeyPressed extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			if (GamePanel.direction != GamePanel.RIGHT) { 
				GamePanel.direction = GamePanel.LEFT; 
			}break; 
		case KeyEvent.VK_D:
			if (GamePanel.direction != GamePanel.LEFT) { 
				GamePanel.direction = GamePanel.RIGHT; 
			}break; 
		case KeyEvent.VK_W:
			if (GamePanel.direction != GamePanel.DOWN) { 
				GamePanel.direction = GamePanel.UP; 
			}break; 
		case KeyEvent.VK_S:
			if (GamePanel.direction != GamePanel.UP) { 
				GamePanel.direction = GamePanel.DOWN; 
			}break; 
		}
	}
}

// -- BASIC WINDOW AND MAIN FUNCTION --
public class Game {
	final Color BG = new Color(0x606060);					// Colour of the Background
	final Color BUTTON_PANEL_COLOR = new Color(0xb0b0b0);	// Colour of the Button Panel
	final Color BUTTON_TEXT_COLOR = new Color(0x404040);	// Colour of the Button Text
	
	GamePanel playableBG = new GamePanel();
	
	public Game() {
		screenInit();
	}
	
	// -- Initialize Window --
	private void screenInit() {
		// Initialization
		JFrame frame = new JFrame();
		
		// Playable Background
		JPanel center = new JPanel();
		center.setBackground(BG);
		center.setLayout(new GridBagLayout());
		
		center.add(playableBG);
		frame.getContentPane().add(center, BorderLayout.CENTER);
				
		//Screen Settings
		frame.setSize(1600, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JPanel controlPanel = createControlPanel(frame);
		frame.getContentPane().add(controlPanel, BorderLayout.EAST);
		
		// Colour setting
		frame.getContentPane().setBackground(BG);
		
		frame.setVisible(true);
	}
	
	// -- Control Buttons -- 
	private JPanel createControlPanel(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 1, 10, 20));
		
		panel.setBackground(BUTTON_PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50 ,50 ,50 ,50));
		
		final Color BUTTON_COLOR = new Color(0xFFFFFF); // White buttons on pink panel
        final Font BUTTON_FONT = new Font("Garamond", Font.BOLD, 20);
		
		// --- Play/Pause Button ---
        JButton playPause = new JButton("Play");
        playPause.setFont(BUTTON_FONT);
        playPause.setBackground(BUTTON_COLOR);
        playPause.setForeground(BUTTON_TEXT_COLOR);
		
		playPause.addActionListener(e -> {
			if (playableBG.running) {
				playableBG.stopGame();
				playPause.setText("Play");
			}else {
				playableBG.runGame();
				playPause.setText("Stop");
			}
		});
		
		// --- Quit Button ---
        JButton quit = new JButton("Quit");
        quit.setFont(BUTTON_FONT);
        quit.setBackground(BUTTON_COLOR);
        quit.setForeground(BUTTON_TEXT_COLOR);
        
		quit.addActionListener(e -> {
			frame.dispose();
			System.exit(0);
		});
		
		// --- Restart Button ---
        JButton restart = new JButton("Restart");
        restart.setFont(BUTTON_FONT);
        restart.setBackground(BUTTON_COLOR);
        restart.setForeground(BUTTON_TEXT_COLOR);
		
		restart.addActionListener(e -> {
			playableBG.resetGame();
		});
		
		panel.add(playPause);
		panel.add(quit);
		panel.add(restart);
		
		// Add empty space fillers (set them to the panel background color)
        for(int i = 0; i < 7; i++) { // Changed to 7 to account for 3 buttons
            JLabel filler = new JLabel("");
            filler.setBackground(BUTTON_PANEL_COLOR);
            panel.add(filler); 
        }
		
		return panel;
	}
	
	// -- MAIN FUNCTION --
	public static void main(String args[]) {
		SwingUtilities.invokeLater(() -> {
			new Game();
		});
	}
}