package main.Entities.Resources;

import main.Entities.EntityResource;

public class EntityResourceStairs extends EntityResource {
	
	private boolean direction;
	
	public EntityResourceStairs(int forward) {
		super(1);
	}
	
	public EntityResourceStairs(double x, double y, boolean forward) {
		super(x, y, 1);
		
		direction = forward;
		
		if (direction) {
			name = "Stairs Down";
			setSprite("StairsDown.bmp");
			
		} else {
			name = "Stairs Up";
			setSprite("StairsUp.bmp");
		}
	}
	
	public boolean direction() {
		return direction;
	}
}
