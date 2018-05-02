package main.Actions;

import main.Action;
import main.Entity;
import main.Location;
import main.Managers.GameManager;

public class ActionAttackMove extends ActionMove{
	
	Entity target;
	
	public ActionAttackMove(Entity actor, Location actee) {
		super(actor, actee);
		
		if (actor.flies()){
			name = "Attack Fly";
		} else {
			name = "Attack Walk";
		}
		preAction = new ActionMove(actor, actee);
	}
	
	//TODO FIX
	protected boolean act(int time) {
		for (Entity entity : GameManager.getEntities()){
			if (actor.enemyOf(entity) && Location.directDistanceEdge(actor, entity) <= actor.getAttackRadius()){
				actor.replaceAction(new ActionAttack(actor, entity));
				break;
				//TODO: Attack closest (instead of first found in list);
			}
		}
		return false;
	}
	
	protected boolean condition() {
		return actor.moves() && actor.getAttackRadius()!=0;
	}
	
	protected boolean prerequisite() {
		for (Entity entity : GameManager.getEntities()){
			if (Location.directDistanceEdge(actor, entity) < actor.getVisionRadius()){
				target = entity;
				return true;
			}
		}
		return false;
	}
	
	protected Action defaultAction() {
		
		return new ActionAttackMove(actor, actee);
	}
}
