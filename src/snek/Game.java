package snek;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

class GamePanel extends JPanel implements ActionListener{
	final Color PANEL = new Color(0x383b3d);
	final Color SNAKE = new Color(0x84bd91);
	final Color FOOD = new Color(0xe3496f);
	
	final int PANELSIZE = 900; 								// Size of the total playable area in pixels.
	final int SNAKEPIXEL = 20; 								// Size of the snake in pixels.
	final int GRIDPIXEL = PANELSIZE/SNAKEPIXEL;				// Number of grid boxes.
	final int MAX_SNAKE_LENGTH = GRIDPIXEL * GRIDPIXEL;		// Victory length - maximum size of the snake.
	
	final int[] x = new int[MAX_SNAKE_LENGTH];				// X Coordinates of the Snake.
	final int[] y = new int[MAX_SNAKE_LENGTH];				// Y Coordinates of the Snake.
	
	int snakeLength = 5;									// Initial Length of the Snake.
	
	static final int UP = -2;								// Direction -- UP
	static final int DOWN = 2;								// Direction -- DOWN
	static final int RIGHT = 1;								// Direction -- RIGHT
	static final int LEFT = -1;								// Direction -- LEFT
	
	private final Random random = new Random();
	private int foodX;
	private int foodY;
	
	boolean first = true;
	
	Timer timer;
	int DELAY = 60;
	
	static int direction = RIGHT;
	boolean running = false;
	
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
		
		this.addKeyListener(new KeyPressed());
		this.setFocusable(true);
		this.requestFocusInWindow(true);
		
		startGame();
	}
	
	public void startGame() {
		initFood();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void initFood() {
		foodX = random.nextInt(GRIDPIXEL-2);
		foodY = random.nextInt(GRIDPIXEL-2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkFood();
		}
		
		repaint();
	}
	
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
	
	public void checkFood() {
		if ((x[0] == foodX && y[0] == foodY)) {
			snakeLength++;
			initFood();
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
		spawnFood(g);
		drawSnake(g);
	}

	public void drawGrid(Graphics g) {
		g.setColor(Color.BLACK);
		
		for (int i = SNAKEPIXEL; i < PANELSIZE; i += SNAKEPIXEL) {

			// x coord
			g.drawLine(i, 0, i, PANELSIZE);
			
			// y coord
			g.drawLine(0, i, PANELSIZE, i);
		}
	}
	
	public void drawSnake(Graphics g) {
		g.setColor(SNAKE);
		
		for (int i = 0; i < snakeLength; i++)	{
			g.fillRect(x[i] * SNAKEPIXEL, y[i] * SNAKEPIXEL, SNAKEPIXEL, SNAKEPIXEL);
		}
	}
	
	public void spawnFood(Graphics g) {
		g.setColor(FOOD);
			
		g.fillRect(foodX * SNAKEPIXEL, foodY * SNAKEPIXEL, SNAKEPIXEL, SNAKEPIXEL);
	}
}

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

public class Game {
	final Color BG = new Color(0x262829);
	
	public Game() {
		screenInit();
	}
	
	private void screenInit() {
		// Initialization
		JFrame frame = new JFrame();
		
		// Playable Background
		GamePanel playableBG = new GamePanel();
		JPanel center = new JPanel();
		center.setBackground(BG);
		center.setLayout(new GridBagLayout());
		
		center.add(playableBG);
		frame.getContentPane().add(center, BorderLayout.CENTER);
				
		//Screen Settings
		frame.setSize(1600, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Colour setting
		frame.getContentPane().setBackground(BG);
		
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(() -> {
			new Game();
		});
	}
}