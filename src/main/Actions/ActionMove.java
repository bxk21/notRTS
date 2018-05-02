package main.Actions;

import main.Action;
import main.Entities.EntityProjectile;
import main.Entity;
import main.Location;
import main.Main;

import java.math.BigDecimal;
import java.util.concurrent.LinkedBlockingDeque;

public class ActionMove extends Action {
	
	private boolean haltOnCollide = false;
	LinkedBlockingDeque<Location> path = new LinkedBlockingDeque<>();
	
	private int recheckTimer = Main.LOCATION_RECHECK_TIMER;
	private double range = 0;
	double speed;
	
	public ActionMove(Entity actor, Location actee) {
		super(actor, actee);
		
		timeLeft = 600*Main.MICROSECONDS_PER_SECOND;//impossibly long (10 minutes) timeout limit
		pathFind(actee);
		if (actor.flies()){
			name = "Fly";
		} else {
			name = "Walk";
		}
		speed = actor.maxSpeed;
	}
	
	public ActionMove(Entity actor, Location actee, double range){
		this(actor, actee);
		this.range = range;
	}
	
	public ActionMove(EntityProjectile actor, Location actee, boolean haltOnCollide) {
		this(actor, actee);
		this.haltOnCollide = haltOnCollide;
	}
	
	protected void onStart() {
	
	}
	
	protected void onEnd() {
	
	}
	
	/**
	 * Moves the Actor
	 * @param time amount of time passed this tick
	 * @return if the moving is finished
	 */
	protected boolean act(int time) {
		if (path.size() == 0){//should not happen. path should return somewhere else upon completion
			System.err.println("Empty Path!\n\tActor: " + actor + "\n\tActee: " + actee);
			System.exit(1);
			return true;
		}
		//Remake Path
		if (recheckTimer > 0) {
			recheckTimer--;
		} else if (recheckTimer == 0){
			if (!pathFind(actee)){//find a new path. If it fails, end actions
				return true;
			}
			if (actee.moves()){
				recheckTimer = Main.LOCATION_RECHECK_TIMER;
			} else {
				recheckTimer = -1;
			}
		}
		
		//Follow Path
		double xDeltaGoal = path.peek().x - actor.x;
		double yDeltaGoal = path.peek().y - actor.y;
		
		//change angle of actor for animations
		double theta = Math.atan2(xDeltaGoal, yDeltaGoal);
		actor.direction = theta*180/Math.PI;
		
		BigDecimal deltaGoal = BigDecimal.valueOf(Math.sqrt(xDeltaGoal * xDeltaGoal + yDeltaGoal * yDeltaGoal));
		BigDecimal timeAllotted = BigDecimal.valueOf(time).divide(BigDecimal.valueOf( Main.MICROSECONDS_PER_SECOND));
		BigDecimal deltaMax = timeAllotted.multiply(BigDecimal.valueOf(speed));
		
//		System.out.println("DeltaGoal: " + deltaGoal);
//		System.out.println("DeltaMax: " + deltaMax);
		if (deltaMax.compareTo(deltaGoal) != -1) {//if (deltaMax >= deltaGoal) //if the total possible distance is farther than the goal distance, go to the goal
			attemptMove(path.peek().x, path.peek().y, actor.x, actor.y);
			path.pollFirst();
			return (path.size() == 0);
//			double percentLeft = deltaMax.divide(deltaMax.subtract(deltaGoal), BigDecimal.ROUND_HALF_EVEN).doubleValue();
			//TODO Make act return int
//				0 = false
//			    1 = done exactly (-1?)
//				other = remaining time
			//TODO return percentLeft * time
//			return act((int)(time * percentLeft));
		}
		
		if (!deltaMax.equals(BigDecimal.ZERO)){
			BigDecimal percent = deltaMax.divide(deltaGoal, BigDecimal.ROUND_HALF_EVEN);//TODO? not sure if this is right
			double deltaPercentage = percent.doubleValue();
			double xDelta = xDeltaGoal * deltaPercentage;
			double yDelta = yDeltaGoal * deltaPercentage;
			double x = actor.x;
			double y = actor.y;
			attemptMove(actor.x + xDelta, actor.y + yDelta, actor.x, actor.y);
			
			if (haltOnCollide){
//				double[] goalLoc = {actor.x + xDelta, actor.y + yDelta};
//				double[] loc = attemptMove(actor.x + xDelta, actor.y + yDelta, actor.x, actor.y);
				
				return actor.tileOn().blocksGround();
			} else return actor.x == x && actor.y == y;
		}
		
		
//		if (Location.directDistanceEdge(actor, actee) <= range){
//			//TODO return act(time*percentLeft)
//			return true;
//		}
		return false;
//		return (Location.isTouching(actor, actee));
	}
	
	private double[] attemptMove(double toX, double toY, double fromX, double fromY) {
		double[] out = actor.attemptMove(toX, toY, fromX, fromY);
		actor.x = out[0];
		actor.y = out[1];
		return out;
	}
	
	protected boolean condition() {
		return actor.moves();
	}
	
	protected boolean prerequisite() {
		return true;
	}
	
	protected Action defaultAction() {
		return null;
	}
	
	
	/**
	 * Creates a path to the destination from the actor
	 * @param goal the destination
	 * @return Whether or not a path was found
	 */
	private boolean pathFind(Location goal) {
		if (actor.flies()){
			path.clear();
			path.add(goal);
			return true;
		} else {
			path.clear();
			//TODO: Use A* search algorithm
			path.add(goal);
			return true;
//			return false;
		}
	}
	
	public double estimatedTime() {
//		System.out.println("Path: " + Location.pathDistance(path) + " speed: " + speed);
		LinkedBlockingDeque<Location> tPath = new LinkedBlockingDeque<>(path);
		try {
			tPath.putFirst(actor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Location.pathDistance(tPath)/speed;
	}
}
