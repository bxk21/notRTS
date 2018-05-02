package main.Entities.Resources;

import main.Entities.EntityResource;

public class EntityResourceExperience extends EntityResource {
	public EntityResourceExperience(int quantity) {
		super(quantity);
		mineTime = 0;
		name = "Experience";
	}
}
