package main.Entities;

import main.Entities.Resources.EntityResourceExperience;
import main.Location;
import main.Managers.GameManager;

public abstract class EntityEnemy extends EntityMortal {
	
	boolean aggroed = false;
	
	EntityEnemy(double x, double y, String team){
		super(x, y, team);
		resources.add(new EntityResourceExperience(10));
	}
	
	public void tick(int time){
		super.tick(time);
		if (!aggroed && Location.directDistanceEdge(this, GameManager.getPlayer()) < visionRadius){
			aggroed = true;
//			System.out.println(this + " Aggroed");
		}
		
	}
	
}
