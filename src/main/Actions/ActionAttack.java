package main.Actions;

import main.Action;
import main.Entity;
import main.Location;
import main.Main;

public class ActionAttack extends Action {
	
	private double windUp;
	private double coolDown;
	double range;
	double damage;
	
	
	/**
	 * Wrapper Action: Move then Direct Attack
	 * Get within attacking range of target and direct attack
	 * @param actor
	 * @param actee
	 */
	public ActionAttack(Entity actor, Entity actee) {
		super(actor, actee);
		timeLeft = (int) actor.getAttackSpeed() * Main.MICROSECONDS_PER_SECOND;
		this.windUp = actor.getWindUp();
		this.coolDown = actor.getCoolDown();
		this.range = actor.getAttackRadius();
		this.damage = actor.getAttackDamage();
		name = "Attack";
		preAction = new ActionMove(actor, actee, actor.getAttackRadius()+ actee.getRadius());
	}
	
	
	
	public ActionAttack(Entity actor, Entity actee, double windUp, double coolDown, double range, double damage, double attackSpeed) {
		this(actor, actee);
		this.windUp = windUp;
		this.coolDown = coolDown;
		this.range = range;
		this.damage = damage;
		timeLeft = (int) attackSpeed * Main.MICROSECONDS_PER_SECOND;
	}
	
	protected void onStart() {
		actor.damage((Entity) actee, damage);
	}
	
	protected void onEnd() {
	
	}
	
	protected boolean act(int time) {
		return false;
	}
	
	protected boolean condition() {
		return actor.getAttackRadius() != 0;
	}
	
	protected boolean prerequisite() {
		return Location.directDistanceEdge(actor, actee) < actor.getAttackRadius();
	}
	
	protected Action defaultAction() {
		return null;
	}
}
