package main.Entities;

import main.Entity;

public abstract class EntityMortal extends Entity {
	
	EntityMortal(double x, double y, String team){
		super(x, y, team);
		blocking = true;
	}
}
