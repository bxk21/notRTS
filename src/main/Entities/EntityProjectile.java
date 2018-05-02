package main.Entities;

import main.Actions.ActionMove;
import main.Entity;
import main.Location;
import main.Managers.GameManager;

public class EntityProjectile extends Entity {
	
	private Entity owner;
	
	public EntityProjectile(Entity owner, Location target, double attackDamage) {
		super(owner.x, owner.y, owner.getTeam());
		this.owner = owner;
		flying = true;
		name = "Projectile";
		maxSpeed = 10;
		actionQueue.addLast(new ActionMove(this, target, true));
		radius = .125;// half of .25, which is 1/4 of 64 pixels, which is 16 pixels
		this.attackDamage = attackDamage;
		setSprite("Projectile.bmp");
		
		//Set distance
		double xd = target.x - owner.x;
		double yd = target.y - owner.y;
		double d = Math.sqrt(xd*xd+yd*yd);
		double ratio = owner.getAttackRadius()/d;
		double newXD = xd * ratio;
		double newYD = yd * ratio;
		target.x = owner.x + newXD;
		target.y = owner.y + newYD;
	}
	
	public EntityProjectile(Entity actor, Location actee) {
		this(actor, actee, actor.attackDamage);
	}
	
	
	public void tick(int time) {
		super.tick(time);
		for (Entity entity: GameManager.getEntities()){
			if (this.enemyOf(entity)){
				if (Location.isTouching(this, entity)){
					owner.damage(entity, attackDamage);
					killNonEntity();
					return;
				}
				
			}
		}
		if (actionQueue.isEmpty()){
			killNonEntity();
		}
	}
}
