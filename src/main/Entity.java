package main;

import main.Entities.EntityResource;
import main.Entities.Resources.EntityResourceStairs;
import main.Managers.GameManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * General purpose "entity"
 * Is a valid target
 */
public abstract class Entity extends Matter {
	
	protected String name;
	public LinkedBlockingDeque<Action> actionQueue = new LinkedBlockingDeque<>();
	protected boolean flying = false;
	public double friction = 0.25;
	public double direction;
	protected double visionRadius = 5;
	protected double attackRadius = 1;
	public double attackDamage = 25;
	protected double health = 100;
	public double maxSpeed = 2;
	int windUp = 5, coolDown = 55;
	protected ArrayList<Timing> timings = new ArrayList<>();
	protected ArrayList<EntityResource> resources = new ArrayList<>();
	private String team;
	private static final double SLIDE_PERCENTAGE = 1.01;//important to keep entities from grinding against each other.
	
	protected double attackSpeed = 1;
	
	public Entity(double x, double y, String team) {
		super(x,y);
		direction = 0.0;
		moves = true;
		radius = .375;//Makes for .75 diameter = 48/64 pixels
		this.team = team;
	}
	
	public Entity(String team) {
		super();
		this.team = team;
	}
	
	/**
	 * Simulates the entity
	 * @param time the amount of time moved
	 */
	public void tick(int time){
//		System.out.println(this + " Acting: " + actionQueue.peekFirst());
		if (actionQueue.peekFirst() != null && actionQueue.peekFirst().tick(time)) {
			if (actionQueue.size() == 1 && actionQueue.peekFirst().defaultAction() != null) {// if action list is otherwise empty and current action has a default action, then replace action with it's default
				System.out.println("Action " + actionQueue.peekFirst());
				actionQueue.offerFirst(actionQueue.pollFirst().defaultAction());
				System.out.println("Defaulting to " + actionQueue.peekFirst());
			} else {
				//			System.out.println("Removing action: " + actionQueue.peekFirst());
				actionQueue.pollFirst();
			}
		}
	}
	
	/**
	 * Updates sprite
	 * @param timeLeft
	 */
	public void updateSprite(int timeLeft) {
		// TODO: Future work
	}
	
	public double[] attemptMove(double toX, double toY, double fromX, double fromY) {
		return attemptMove(toX, toY, fromX, fromY, 0);
	}
	
	/**
	 * Attempts to move to a location.
	 * Checks against colliding walls and flattens against them.
	 * Recursive for multiple collisions and reattempts.
	 * @param toX Target Location x
	 * @param toY Target Location y
	 * @param fromX Source Location x
	 * @param fromY Source Location y
	 * @return decided-upon end location
	 */
	public double[] attemptMove(double toX, double toY, double fromX, double fromY, int attempts) {
		attempts++;
		if(attempts>20){
//			System.err.println("Movement Failed: " + this);
			return new double[]{fromX, fromY};
		}
		if (flying){
			return new double[]{toX,toY};
		}
//		System.out.println("Attempting to move to:  (" + toX + " " + toY + ") from (" + fromX + " " + fromY + ")");
		int xi = (int) toX;
		int yi = (int) toY;
//		System.out.println("My square is:  (" + xi + " " + yi + ")");
		int[] checkX;
		int[] checkY;
		if (toX - xi < radius) {//check left
			//System.out.println("check left");
			checkX = new int[]{xi, xi - GameManager.BLOCK_SIZE};
		} else if (toX - xi > GameManager.BLOCK_SIZE - radius) {//check right
			//System.out.println("check right");
			checkX = new int[]{xi, xi + GameManager.BLOCK_SIZE};
		} else {//check self only
			//System.out.println("check self");
			checkX = new int[]{xi};
		}
		if (toY - yi < radius) {//check up
			//System.out.println("check up");
			checkY = new int[]{yi, yi - GameManager.BLOCK_SIZE};
		} else if (toY - yi > GameManager.BLOCK_SIZE - radius) {//check down
			//System.out.println("check down");
			checkY = new int[]{yi, yi + GameManager.BLOCK_SIZE};
		} else {//check self only
			//System.out.println("check self");
			checkY = new int[]{yi};
		}
		for (int squareX : checkX) {
			for (int squareY : checkY) {
				if (!GameManager.level.spaceAt(squareX, squareY).blocksGround()) {//skip non-ground blocking blocks
					continue;
				}
				
				if (squareX == xi && squareY == yi) {//If the move brings the center into a block, reattempt at lower speed.
					//TODO: adjust for radius
					final double ratio = 1;//percent of radius the velocity is moved back by. Set to 1 because if the center was in the block, moving by a radius, an edge will still touch the block
					// double vx = x - fromX;
					// int dirVX = (int) vx/Math.abs(vx);
					// x -= radius*ratio*dirVX;
					// x = (fromX*ratio+x*invRatio);
					// y = (fromY*ratio+y*invRatio);
					
					double vx = toX - fromX;
					double vy = toY - fromY;
					double v = Math.sqrt(vx * vx + vy * vy);//diagonal velocity
					double deltaV = (v - radius * ratio) / v;//new velocity
					//double deltaV = v2/v;//ratio for new velocity
					toX -= vx * (1 - deltaV);
					toY -= vy * (1 - deltaV);
					return attemptMove(toX, toY, fromX, fromY, attempts);
				}
				
				
				double deltaX = toX - (squareX + GameManager.BLOCK_SIZE_HALF);//delta from center
				int dirX = (int) (deltaX / Math.abs(deltaX));// +1 or -1 depending on direction
				double deltaY = toY - (squareY + GameManager.BLOCK_SIZE_HALF);
				int dirY = (int) (deltaY / Math.abs(deltaY));
				
				if (squareX == xi) {//vertical
					if (toY > squareY + GameManager.BLOCK_SIZE_HALF) {//up
						//adjust y to lower wall
						toY = squareY + GameManager.BLOCK_SIZE + radius;
					} else {//down
						//adjust y to upper wall
						toY = squareY - radius;
					}
					return attemptMove(toX, toY, fromX, fromY, attempts);
				}
				if (squareY == yi) {//horizontal
					if (toX > squareX + GameManager.BLOCK_SIZE_HALF) {//left
						//adjust x to right wall
						toX = squareX + GameManager.BLOCK_SIZE + radius;
					} else {//right
						//adjust x to left wall
						toX = squareX - radius;
					}
					return attemptMove(toX, toY, fromX, fromY, attempts);
				}
				
				double deltaFromCornerX = deltaX - GameManager.BLOCK_SIZE_HALF * dirX;
				double deltaFromCornerY = deltaY - GameManager.BLOCK_SIZE_HALF * dirY;
				double delta = Math.sqrt(deltaFromCornerY * deltaFromCornerY + deltaFromCornerX * deltaFromCornerX);//distance to corner
				if (delta < radius) {//diagonal
//					System.out.println(".\t\t\tHits Corner");
					toX += deltaFromCornerX * (radius / delta - 1);
					toY += deltaFromCornerY * (radius / delta - 1);
					return attemptMove(toX, toY, fromX, fromY, attempts);
				}
			}
		}
		
		//Temporarily move entity for collision testing
		double oldX = x;
		double oldY = y;
		x = toX;
		y = toY;
		for (Entity entity : GameManager.getEntities()){
			if (entity.isBlocking() && Location.isTouching(this, entity)){
//				System.out.println(this + " is touching " + entity);
				double idealDistance = radius + entity.radius;
//				System.out.println("Ideal Distance: " + idealDistance);
				double dx = x - entity.x;
				double dy = y - entity.y;
				double actualDistance = Math.sqrt(dx*dx + dy*dy);
//				System.out.println("Actual Distance: " + actualDistance);
				double ratio = SLIDE_PERCENTAGE * idealDistance / actualDistance;
//				System.out.println("Ratio: " + ratio);
				double newX = entity.x + (dx * ratio);
				double newY = entity.y + (dy * ratio);
//				System.out.println("New Distance: " + (newX - x)+ ", " + (newY - y));
				
				x = oldX;
				y = oldY;
				return attemptMove(newX, newY, fromX, fromY, attempts);
			}
		}
		return new double[]{toX, toY};
	}
	
	public String toString(){
		return name;
	}
	
	public String getInfo() {
		return "Entity " + name + "\nLocation (" + x + ", " + y + ")";
	}
	
//	public boolean isImmobile() {
//		return (speed == 0);
//	}
	
	public boolean flies() {
		return flying;
	}
	
	
	public double getAttackDamage() {
		return attackDamage;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getVisionRadius() {
		return visionRadius;
	}
	
	public double getAttackRadius() {
		return attackRadius;
	}
	
	public int getWindUp() {
		return windUp;
	}
	
	public int getCoolDown() {
		return coolDown;
	}
	
	public void getResource(EntityResource obtained) {
		
		if (obtained instanceof EntityResourceStairs){
			GameManager.loadLevel(((EntityResourceStairs) obtained).direction());
			return;
		}
		
		boolean added = false;
		for (EntityResource resource : resources){//stacks resources if possible
			if (resource.getClass().equals(obtained.getClass())){
				resource.put(obtained.quantity);
				added = true;
			}
		}
		if (!added){
			resources.add(obtained);
		}
	}
	
	private void getResource(ArrayList<EntityResource> pack) {
		if (pack == null){
			return;
		}
		for (EntityResource obtained : pack) {
			getResource(obtained);
		}
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public void killEntity(){
		if (Main.IS_TESTING) {
			System.out.println(this + " died");
		}
		GameManager.kill(this);
	}
	
	public void killNonEntity(){
		if (Main.IS_TESTING) {
			System.out.println(this + " removed");
		}
		GameManager.killNonEntity(this);
	}
	
	public boolean enemyOf(Entity entity) {
		return (team != null) && (entity.team != null) && (team != entity.team);
	}
	
	public String getTeam(){
		return team;
	}
	
	public void replaceAction(Action action) {
		actionQueue.pollFirst();
		actionQueue.offerFirst(action);
	}
	
	/**
	 * Damage entity. If it dies, take it's resources
	 * @param entity
	 * @param damage
	 */
	public void damage(Entity entity, double damage) {
		if(Main.IS_TESTING){
//			System.out.println(this + " dealing " + damage + " damage to " + entity);
		}
		getResource(entity.takeDamage(damage));
	}
	
	public void damage(Entity entity) {
		damage(entity, attackDamage);
	}
	
	/**
	 * Take Damage
	 * If died, give all resources to killer.
	 * @param attackDamage
	 * @return
	 */
	public ArrayList<EntityResource> takeDamage(double attackDamage){
		health -= attackDamage;
		if (isDead()){
			killEntity();
			ArrayList<EntityResource> out = new ArrayList<>(resources);
			resources.clear();
			return out;
		}
		return null;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public double getAttackSpeed(){
		return attackSpeed;
	}
	
	public double countResource(String name) {
//		System.out.println(this + " Counting Resource: " + name + " from Resources of size " + resources.size());
//		if (resources.size() == 0){
//			return 0;
//		}
		
		for (EntityResource resource : resources){
//			System.out.println("\tChecking " + resource);
			if (resource.name.equals(name)){
				return resource.quantity;
			}
		}
		return 0;
	}
	
	public double getHealth() {
		return health;
	}
	
	public Tile tileOn() {
		return GameManager.level.getTile((int) x, (int) y);
	}
}
