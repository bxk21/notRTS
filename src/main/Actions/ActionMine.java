package main.Actions;

import main.Action;
import main.Entities.EntityResource;
import main.Entity;
import main.Location;

public final class ActionMine extends Action {
	
	/**
	 * Initiates the actions, setting the timer.
	 *
	 * @param actor
	 * @param actee
	 */
	public ActionMine(Entity actor, EntityResource actee) {
		super(actor, actee);
		name = "Mine";
		preAction = new ActionMove(actor, actee);
		timeLeft = actee.getMineTime();
	}
	
	protected boolean act(int time) {
		return false;
	}
	
	protected void onStart() {
	
	}
	
	protected void onEnd(){
		actor.getResource(((EntityResource) actee).get());
		actor.actionQueue.clear(); //TODO bandaid fix
	}
	
	protected boolean condition() {
		return actee instanceof EntityResource;
	}
	
	protected boolean prerequisite() {
		return Location.isTouching(actor, actee, .1);
	}
	
	protected Action defaultAction() {
		return new ActionMine(actor, (EntityResource) actee);
	}
}
