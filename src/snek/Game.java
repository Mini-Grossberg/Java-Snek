package snek;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GamePanel extends JPanel {
	final Color PANEL = new Color(0x383b3d);
	final int PANELSIZE = 900;
	
	public GamePanel() {
		this.setBackground(PANEL);
		
		Dimension d = new Dimension(PANELSIZE, PANELSIZE);
		this.setPreferredSize(d);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
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