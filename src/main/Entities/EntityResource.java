package main.Entities;

import main.Entity;
import main.Main;

import java.lang.reflect.InvocationTargetException;

public abstract class EntityResource extends Entity {
	
//	protected int capacity;//max amount
public int quantity;//current amount
	protected int efficiency = 1;//max amount obtained at a time
	protected int mineTime = 1;//seconds to mine <efficiency>
	
	public EntityResource(int capacity) {
		super(null);
		init(capacity);
	}
	
	public EntityResource(double x, double y, int capacity) {
		super(x, y, null);
		init(capacity);
	}
	
	private void init(int capacity){
		name = "Resource";
		health = Double.MAX_VALUE;
		quantity = capacity;
		radius = .3;//TODO change when setting sprite
	}
	
	public int getMineTime(){
		return mineTime * Main.MICROSECONDS_PER_SECOND;
	}
	
	/**
	 * Obtains (Harvest/Extract/Gather/Mine) the resource as new instance of item
	 * @param amount the amount attempted
	 * @return the resource obtained
	 */
	public EntityResource get(int amount){
		
		System.out.println("Getting Resource");
		
		quantity -= amount;
		if (quantity<=0){
			quantity += amount;
			killEntity();
			return this;
		}
		if (amount == 0){
			killEntity();
			return null;
		}
		
		//creates a new resource of the same type //TODO. Currently doesn't work. Will break continuously mined Resources.
		try {
			return getClass().getDeclaredConstructor(Integer.class).newInstance(new Object[]{amount});
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		
		
		System.err.println("Resource.Get failed");
		return null;
	}
	
	public EntityResource get(){
		return get(efficiency);
	}
	
	
	public boolean isEmpty(){
		return (quantity == 0);
	}
	
	//TODO change to merge
	public void put(int added) {
		quantity += added;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
