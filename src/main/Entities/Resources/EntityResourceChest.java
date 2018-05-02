package main.Entities.Resources;

import main.Entities.EntityResource;

public class EntityResourceChest extends EntityResource {
	public EntityResourceChest(double x, double y) {
		this(x, y, 1);
	}
	
	public EntityResourceChest(double x, double y, int amount) {
		super(x, y, amount);
		radius = .4;
		efficiency = 1;
		mineTime = 1;
		name = "Chest";
		setSprite("Chest.bmp");
		blocking = true;
	}
}
