package snek;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel {
	final Color PANEL = new Color(0x383b3d);
	final Color SNAKE = new Color(0x84bd91);
	
	final int PANELSIZE = 900; 								// Size of the total playable area in pixels.
	final int SNAKEPIXEL = 20; 								// Size of the snake in pixels.
	final int GRIDPIXEL = PANELSIZE/SNAKEPIXEL;				// Number of grid boxes.
	final int MAX_SNAKE_LENGTH = GRIDPIXEL * GRIDPIXEL;		// Victory length - maximum size of the snake.
	
	final int[] x = new int[MAX_SNAKE_LENGTH];				// X Coordinates of the Snake.
	final int[] y = new int[MAX_SNAKE_LENGTH];				// Y Coordinates of the Snake.
	
	int snakeLength = 5;									// Initial Length of the Snake.
	
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
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
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