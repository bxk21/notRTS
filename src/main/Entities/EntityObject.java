package main.Entities;

import main.Entity;

abstract class EntityObject extends Entity {
	EntityObject(double x, double y) {
		super(x, y, null);
		moves = false;
	}
	
	public EntityObject() {
		super(null);
		moves = false;
	}
}
