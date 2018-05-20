package main.Managers;


import main.Entities.EntityPC;
import main.Entities.EntityResource;
import main.Entity;
import main.Level;
import main.Location;
import main.Main;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main of Game
 * (Model)
 * NOTE: variables and methods of this class are static, as there is no need for more than one instance of the game manager.
 */
public class GameManager {

	public static final int BLOCK_SIZE = 1;
	public static final double BLOCK_SIZE_HALF = BLOCK_SIZE/2;
	static final int MAP_WIDTH = 100;
	static final int MAP_HEIGHT = 100;
	
	private static EntityPC player;
	private static ArrayList<Entity> entities;//TODO: AutoSorting addEntity() method with getEntity(Class type)
	private static ArrayList<Entity> nonEntities = new ArrayList<>();
	public static Level level;
	private static int currentLevel = -1;
	private static ArrayList<Entity> killQueue = new ArrayList<>();
	
	public GameManager() {
		player = new EntityPC(-1, -1);
//		level = new Level(MAP_HEIGHT, MAP_WIDTH, currentLevel);
		loadLevel(true);
	}
	
	public static void loadLevel(boolean forward){
		if (forward){
			if (Main.IS_TESTING) {
				System.out.println("Next Level!");
			}
			currentLevel++;
//			if (seeds.size() <= currentLevel){
//				level = new Level(MAP_HEIGHT, MAP_WIDTH, currentLevel);
//				loadLevel();
//			} else {
//				level = new Level(MAP_HEIGHT, MAP_WIDTH, currentLevel, currentLevel);
//				loadLevel();
//			}
		} else {
		    if (currentLevel == 0){
		    	return;// At first level. Cannot go back more.
	        }
			if (Main.IS_TESTING) {
				System.out.println("Back a Level!");
			}
			currentLevel--;

		}
		level = new Level(MAP_HEIGHT, MAP_WIDTH, currentLevel);
		player.reset();
		entities = level.getEntities();
		
		EntityResource landing;
		if (forward) {
		    landing = level.getStairsUp();
		} else {
		    landing = level.getStairsDown();//having gone up, now at top of stairs going down.
		}
		player.setLocation(landing.getX(), landing.getY());
		entities.add(player);
	}
	
	public static void tick(int time) {
		
		long startTime = System.currentTimeMillis();
		if (Main.IS_TESTING) {
//			System.out.println("Start: " + startTime);
		}
		
		//Get Input
		getInput();
		
		
		//Entity Updates
		for (Entity entity : getViewable()) {
			entity.tick(time);
		}
		
		killQueue();
		
//		Tile currentSpace = level.spaceAt((int) player.y, (int) player.x);
//		if (currentSpace instanceof TileStairs) {
//			loadLevel(true);
//		} else if (currentSpace instanceof TileStairLanding && currentLevel > 0){
//			loadLevel(false);
//		}
		
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (Main.IS_TESTING){
//			System.out.println("Elapsed Time: " + elapsedTime);
		}
		if (elapsedTime>17){
			System.err.println("Processing too slow");
		}
	}
	
	private static void getInput() {
		int[] mouse = Main.inputManager.loadMouse();
		HashMap<Integer, Boolean> input = Main.inputManager.loadInput();
		
		
		
		
		
		
		
		
		player.receiveInput(mouse, input);
	}
	
	public static EntityPC getPlayer() {
		return player;
	}
	
	
	
	public static void kill(Entity entity) {
		killQueue.add(entity);
	}
	
	private static void killQueue(){
		for (Entity entity : new ArrayList<>(killQueue)){
			entities.remove(entity);
			if (entity==player){
				System.out.println("Game Over");
				Main.setMenu(1);
			}
		}
	}
	
	public static void addNonEntity(Entity entity) {
		nonEntities.add(entity);
	}
	
	public static ArrayList<Entity> getEntities() {
		return entities;
	}
	public static ArrayList<Entity> getNonEntities() {
		return nonEntities;
	}
	public static ArrayList<Entity> getViewable() {
		ArrayList<Entity> out = new ArrayList<>();
		out.addAll(entities);
		out.addAll(nonEntities);
		return out;
	}
	
	public static void killNonEntity(Entity entity) {
		nonEntities.remove(entity);
	}
	
	/**
	 * Obtains an entity at location
	 * @param x X location (relative to the world)
	 * @param y X location (relative to the world)
	 * @return the found entity or null if none found
	 */
	public Entity entityAt(double x, double y){
		for (Entity entity : entities){
			if (Location.isTouching(entity, new Location(x,y))){
				return entity;
			}
		}
		return null;
	}
	
	/**
	 * Gets the Location or Entity under the mouse
	 * @param mouseX
	 * @param mouseY
	 * @param getEntity whether or not to return any entities
	 * @return Location
	 */
	public Location pointingAt(int mouseX, int mouseY, boolean getEntity){
		
//		System.out.println("mouse: " + mouseX + ", " + mouseY);
		
		double x = (double)(mouseX-Main.outputManager.getFrame().getWidth()/2)/Main.SCALE + player.getX();
		double y = (double)(mouseY-Main.outputManager.getFrame().getHeight()/2)/Main.SCALE + player.getY();
		
		if (getEntity) {
			Entity entity = entityAt(x, y);
			if (entity != null) {
				return entity;
			}
		}
		return new Location(x, y);
	}
	
	
/*	public GameManager(SurfaceHolder callback, GameView gameView) {
		curTime = System.currentTimeMillis();
		
		this.holder = callback;
		this.gameView = gameView;
		
		board = new World();
		player = (Champion)board.getAgents().get(0);
		
//		cam = new Camera(0,0,0, 0);
//		cam.setFollowTarget(champ);
		
		//Log.println(Log.DEBUG,"GameManager: ","\n"+board.toString());
		
		controlXVel = 0.0;
		controlYVel = 0.0;
		controlDirection = 0.0;
		controlAttacking = false;
	}*/

//	public void setRunning(boolean b) {
//		running = b;
//	}

//	public void updateLeftControls(int xTouchStart, int yTouchStart, int xTouchCurrent, int yTouchCurrent) {
//		double angle = Math.atan2(yTouchStart - yTouchCurrent, xTouchStart - xTouchCurrent);
//		double xVector = -Math.cos(angle);
//		double yVector = -Math.sin(angle);
//
//		double controlDistance = Math.sqrt(Math.pow(yTouchStart - yTouchCurrent, 2) + Math.pow(xTouchStart - xTouchCurrent, 2));
//		if (controlDistance > MAX_CONTROL_DISTANCE) {
//			controlDistance = MAX_CONTROL_DISTANCE;
//		}
//		controlDistance -= MIN_CONTROL_DISTANCE;
//		if (controlDistance < 0) {
//			controlDistance = 0;
//		}
//
//		double ratio = MAX_SPEED / (MAX_CONTROL_DISTANCE - MIN_CONTROL_DISTANCE);
//		double speed = ratio * controlDistance / SCALE;
//
//
//		if (speed > MAX_SPEED) {
//			speed = MAX_SPEED;
//		}
//
//		controlXVel = speed * xVector / FRAMERATE;
//		controlYVel = speed * yVector / FRAMERATE;
//	}

//	public void updateRightControls(double direction, boolean attacking) {
//		controlAttacking = attacking;
//		controlDirection = direction;
//	}


}