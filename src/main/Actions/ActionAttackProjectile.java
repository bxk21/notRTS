package main.Actions;

import main.Action;
import main.Entities.EntityProjectile;
import main.Entity;
import main.Location;
import main.Managers.GameManager;

public class ActionAttackProjectile extends Action {
	public ActionAttackProjectile(Entity actor, Location actee) {
		super(actor, actee);
		preAction = null;
		timeLeft = 0;// (int) actor.getAttackSpeed()* Main.MICROSECONDS_PER_SECOND
		name = "AttackProjectile";
	}
	
	protected void onStart() {
		GameManager.addNonEntity(new EntityProjectile(actor, actee));
	}
	
	protected boolean act(int time) {
		return false;
	}
	
	protected boolean condition() {
		return actor.getAttackRadius() != 0;
	}
	
	protected void onEnd() {
	
	}
	
	protected boolean prerequisite() {
		return true;
	}
	
	protected Action defaultAction() {
		return null;
	}
}
