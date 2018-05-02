package main.Entities;

import main.Actions.ActionAttack;
import main.Managers.GameManager;

public class EntityCharger extends EntityEnemy {
	
	public EntityCharger(double x, double y) {
		super(x, y, "Enemy");
		name = "Charger";
		attackRadius = .01;
		maxSpeed = 3;
//		windUp = 5;// default
//		coolDown = 55;// default
		visionRadius = 10;// default
		attackSpeed = 1;
		setSprite("RedCircle.bmp");
	}
	
	public void tick(int time){
		super.tick(time);
		if (aggroed && actionQueue.isEmpty()){
			actionQueue.offerFirst(new ActionAttack(this, GameManager.getPlayer()));
		}
	}
	
}
