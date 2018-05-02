package main.Entities.Resources;

import main.Entities.EntityResource;

public class EntityResourceStairs extends EntityResource {
	
	public EntityResourceStairs(int forward) {
		super(1);
	}
	
	public EntityResourceStairs(double x, double y, int forward) {
		super(x, y, forward);
		
		
		if (quantity > 0) {
			name = "Stairs Down";
			setSprite("StairsDown.bmp");
			efficiency = 1;
		} else {
			name = "Stairs Up";
			setSprite("StairsUp.bmp");
			efficiency = 0;
		}
	}
	
	public boolean direction() {
		return quantity > 0;
	}
}
