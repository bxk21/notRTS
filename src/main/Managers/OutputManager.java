package main.Managers;

import main.Entity;
import main.Main;
import main.Tile;

import javax.swing.*;
import java.awt.*;

/**
 * Separate Threaded Main of Input and Output
 * Inputs are limited to every tick. To cast a spell, you must click a key one tick then click a later tick. This shouldn't be a problem as there are only 16ms per tick
 * NOTE: variables and methods of this class are not static, as there is potential for more than one instance of IO in the future.
 */
public class OutputManager extends JPanel{
	//TODO: Far future: Make keyboard and mouse input as one giant queue, keeping track of when / what order actions happened. (This shouldn't be absolutely necessary as 60 ticks/second is very fast)
	
	private JFrame frame;
	private final int BORDER = 2;
	
	
	public OutputManager(int width, int height){
		frame = new JFrame("DungeonCrawler");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//JFrame.EXIT_ON_CLOSE
		
		JPanel displayPanel = new JPanel();
//		frame.add(new JLabel(new ImageIcon("Sprites/Sprite.png")));
//		emptyLabel.setPreferredSize(new Dimension(700, 400));
		
		frame.addKeyListener(Main.inputManager);
		frame.addMouseListener(Main.inputManager);
//		frame.add(Main.inputManager);
		
		
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setVisible(true);
		
		setScreen(width, height);
		
	}
	
	
	
	public void display(){
		repaint();
	}
	
	public void paint(Graphics g){
		if (Main.IS_TESTING) {
//			System.out.println("Painting");
		}
		
		//Draw Background
		g.setColor(Color.BLACK);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		
		//Draw Map
		
		double centerX = GameManager.getPlayer().getX();
		double centerY = GameManager.getPlayer().getY();
		
		int leftTile = (int)(centerX-frame.getWidth()/(2 * Main.SCALE));
		if (leftTile < 0){
			leftTile = 0;
		}
		int rightTile = (int)(centerX+frame.getWidth()/(2 * Main.SCALE))+1;
		if (rightTile > GameManager.MAP_WIDTH){
			rightTile = GameManager.MAP_WIDTH;
		}
		int topTile = (int)(centerY-frame.getHeight()/(2 * Main.SCALE))-1;
		if (topTile < 0){
			topTile = 0;
		}
		int bottomTile = (int)(centerY+frame.getHeight()/(2 * Main.SCALE))+1;
		if (bottomTile > GameManager.MAP_HEIGHT){
			bottomTile = GameManager.MAP_HEIGHT;
		}
		//Draw Tiles
		int halfWidth = frame.getWidth()/2;
		int halfHeight = frame.getHeight()/2;
		for (int x = leftTile; x < rightTile; x++){
			for (int y = topTile; y < bottomTile; y++){
				Tile tile = GameManager.level.getTile(x, y);
				double dx = tile.getX()-GameManager.getPlayer().getX();
				double dy = tile.getY()-GameManager.getPlayer().getY();
				g.setColor(tile.getColor());
				g.fillRect((int)(Main.SCALE*dx)+(halfWidth) , (int)(Main.SCALE*dy)+(halfHeight), Main.SCALE-BORDER, Main.SCALE-BORDER);
			}
		}
		
		//Draw Entities
		for (Entity entity : GameManager.getViewable()){
			if (entity.getSprite() == null){
				System.err.println(entity + " has no sprite");
				continue;
			}
			double x = entity.getX();
			double y = entity.getY();
			if (leftTile < x && x < rightTile+1 && topTile < y && y < bottomTile+1){
				double dx = x-GameManager.getPlayer().getX();
				double dy = y-GameManager.getPlayer().getY();
				entity.getSprite().getHeight();
				g.drawImage(entity.getSprite(), (int)(Main.SCALE*dx)+(halfWidth)-(entity.getSprite().getWidth()/2), (int)(Main.SCALE*dy)+(halfHeight)-(entity.getSprite().getHeight()/2), null);
			
//				draw(entity);
				// using x and y
			}
		}
		
		topRightText(g, "EXP: " + (int)GameManager.getPlayer().countResource("Experience"));
		topLeftText(g, "Chests: " + (int)GameManager.getPlayer().countResource("Chest"));
		bottomLeftText(g, "Health: " + (int)(GameManager.getPlayer().getHealth()));
		
		//Draws "paused" over screen if paused
		if (GameManager.getPlayer().isDead()){
			centerText(g, "Game Over");
		} else if (Main.isPaused()){
			centerText(g, "Paused");
			bottomRightText(g, ("Seed: " + Main.getSeedHex() + "    Press Ctrl + S to copy to clipboard"));
		}
	}
	
	private void centerText(Graphics g, String text){
		g.setColor(Color.GRAY);
		Font font = new Font("TimesRoman", Font.BOLD, 300);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		int x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		int y = ((frame.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		g.drawString(text, x, y);
	}
	
	private void topLeftText(Graphics g, String text){
		g.setColor(Color.GRAY);
		Font font = new Font("TimesRoman", Font.BOLD, 50);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		int x = 10;
		int y = metrics.getAscent();
		g.drawString(text, x, y);
	}
	
	private void topRightText(Graphics g, String text){
		g.setColor(Color.GRAY);
		Font font = new Font("TimesRoman", Font.BOLD, 50);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		int x = frame.getWidth() - metrics.stringWidth(text) - 100;
		int y = 10 + metrics.getAscent();
		g.drawString(text, x, y);
	}
	
	private void bottomLeftText(Graphics g, String text){
		g.setColor(Color.GRAY);
		Font font = new Font("TimesRoman", Font.BOLD, 50);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		int x = 10;
		int y = frame.getHeight() - metrics.getHeight() + metrics.getAscent() - 50;
		g.drawString(text, x, y);
	}
	
	private void bottomRightText(Graphics g, String text){
		g.setColor(Color.GRAY);
		Font font = new Font("TimesRoman", Font.BOLD, 30);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		int x = frame.getWidth() - metrics.stringWidth(text) - 100;
		int y = frame.getHeight() - metrics.getHeight() + metrics.getAscent() - 50;
		g.drawString(text, x, y);
	}
	
	/**
	 * Sets the Screen Size
	 * @param width pixel width of the screen
	 * @param height pixel height of the screen
	 */
	private void setScreen(int width, int height) {
		frame.setSize(width, height);
		setSize(width, height);
		frame.setVisible(true);
	}
	
	
	public Frame getFrame() {
		return frame;
	}
}
