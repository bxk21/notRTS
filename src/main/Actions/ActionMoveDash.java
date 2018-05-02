package main.Actions;

import main.Entity;
import main.Location;
import main.Main;

public class ActionMoveDash extends ActionMove {
	
	private final double SPEED_MULTIPLIER = 5;
	
	public ActionMoveDash(Entity actor, Location actee, double time) {
		this(actor, actee);
		timeLeft = (int) (time*Main.MICROSECONDS_PER_SECOND);
	}
	
	public ActionMoveDash(Entity actor, Location actee) {
		super(actor, actee);
		name = "Dash";
		speed = actor.maxSpeed * SPEED_MULTIPLIER;
	}
}
