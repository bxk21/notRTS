package main.Entities;

import main.Actions.ActionAttackProjectile;
import main.Actions.ActionMine;
import main.Actions.ActionMove;
import main.Actions.ActionMoveDash;
import main.Entity;
import main.Location;
import main.Main;
import main.Managers.GameManager;
import main.Timing;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public final class EntityPC extends EntityMortal {
	
//	private int dashTimer = 0;
//	private final double dashMultiplier = 5;
//	private final int dashDuration = 30; // Half second
//	private final int dashCD = 120; // Two seconds
	public final double MAX_HEALTH = 300;
	
	public EntityPC(double x, double y) {
		super(x, y, "Player");
		moves = true;
		name = "Player";
		maxSpeed = 5;
		health = MAX_HEALTH;
		attackRadius = 10;
		setSprite("BlueCircle.bmp");
	}
	
//	public void tick(int time) {}
	
	/**
	 * Gets input
	 * @param mouse World Location pointed at by mouse
	 * @param input Key inputs
	 */
	public void receiveInput(int[] mouse, HashMap<Integer, Boolean> input){
		//Finish previous actions
		if (!Timing.checkTimings(timings)){
			return;
		}
		
//		System.out.println("Receiving Inputs");
//		for (int key : input.keySet()){
//			if (input.get(key)){
//				System.out.println("Key " + key);
//			}
//		}
		
		/* Interpret Inputs */
		if (input.get(KeyEvent.VK_E)){
			interact();
//			System.out.println("Interaction");
			return;
		}
		double vert = 0, horiz = 0;
		if (input.get(KeyEvent.VK_W)){
			vert--;
		}
		if (input.get(KeyEvent.VK_S)){
			vert++;
		}
		if (input.get(KeyEvent.VK_A)){
			horiz--;
		}
		if (input.get(KeyEvent.VK_D)){
			horiz++;
		}
		if (vert != 0 && horiz != 0){
			final double diag = Math.sqrt(2);
			vert /= diag;
			horiz /= diag;
		}
		
//		System.out.println("move: " + horiz + ", " + vert);
		
		/* Enact Inputs */
		
		//Attack
		if (input.get(Main.inputManager.MOUSE_LEFT_NUMBER) && !Timing.exists("Basic Attack", timings)){
			timings.add(new Timing("Basic Attack", Main.sysTime(), 0, 0, .2));
//			System.out.println("Basic Attack At " + Main.gameManager.pointingAt(mouse[0], mouse[1]));
			actionQueue.addLast(new ActionAttackProjectile(this, Main.gameManager.pointingAt(mouse[0], mouse[1], false)));
		}
		//Move
//		if ()
		if (vert != 0 || horiz != 0){
			if (actionQueue.peekFirst() instanceof ActionMove){
				actionQueue.pollFirst();
			}
			if (input.get(KeyEvent.VK_SPACE) && !Timing.exists("Dash", timings)){
//				ArrayList<Double> timing = new ArrayList<>();
//				timing.add(5.0);
				
				ActionMoveDash dash = new ActionMoveDash(this, new Location(this.getX() + horiz*3, this.getY() + vert*3));
				actionQueue.addLast(dash);
				timings.add(new Timing("Dash", Main.sysTime(), 0, dash.estimatedTime(), 5));
//				vert*=dashMultiplier;
//				horiz*=dashMultiplier;
			
			} else {
				actionQueue.addLast(new ActionMove(this, new Location(this.getX() + horiz, this.getY() + vert)));
			}
		} else {
			if (actionQueue.peekFirst() instanceof ActionMove){
				actionQueue.pollFirst();
			}
		}
		
	}
	
	private void interact() {
		for (Entity entity : GameManager.getEntities()){
			if (Location.directDistanceEdge(this, entity) < 1 && entity instanceof EntityResource && !((EntityResource) entity).isEmpty() && !Timing.exists("Interact", timings)){
				System.out.println("Interacting");
				timings.add(new Timing("Interact", Main.sysTime(), 0, .5, .5));
				
				actionQueue.addLast(new ActionMine(this, (EntityResource)entity));
				return;
			}
		}
	}
	
	public void heal(double heal) {
		health += heal;
		if (health > MAX_HEALTH){
			health = MAX_HEALTH;
		}
	}
	
	public void reset() {
		health = MAX_HEALTH;
		actionQueue.clear();
		timings.clear();
	}
}
