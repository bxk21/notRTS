/**
 * Parent Class of all "Locations"
 * Entities and Tiles are "Locations"
 */
package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;

public class Location{
	public double x;
	public double y;
	protected boolean moves = false;
	protected double radius = 0;
	BufferedImage sprite;
	protected boolean blocking = false;
	
	public Location(){
		x = Double.MIN_VALUE;
		x = Double.MIN_VALUE;
	}
	
	public Location(double x, double y){
		this.x=x;
		this.y=y;
	}
	
	public boolean moves() {
		return moves;
	}
	
	/**
	 * Direct distance between two locations
	 * @param a first location
	 * @param b second location
	 * @return distance
	 */
	public static double directDistanceEdge(Location a, Location b){
		return directDistanceCenter(a, b) - a.getRadius() - b.getRadius();
	}
	
	/**
	 * Distance of a path
	 * @param path
	 * @return distance
	 */
	public static double pathDistance(LinkedBlockingDeque<Location> path){
		if (path.size()<2){
			return 0;
		}
		
		double distance = 0;
		
		Location at = path.peekFirst();
		for (Location to : path){
			if (at==to){
				continue;
			}
			distance += directDistanceCenter(at, to);
			at = to;
		}
		return distance;
	}
	
	/**
	 * Direct distance between two locations' centers
	 * @param a first location
	 * @param b second location
	 * @return distance
	 */
	private static double directDistanceCenter(Location a, Location b){
		return Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
	}
	
	/**
	 * Whether two locations (entities) are touching
	 * Returns false if both are the same location/entity
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isTouching(Location a, Location b, double d){
		if (a == b){
			return false;
		}
		return directDistanceEdge(a, b) <= d;
	}
	
	public static boolean isTouching(Location a, Location b) {
		return isTouching(a, b, 0);
	}
	
	//TODO: Future, implement after A* search is written
//	/**
//	 * Distance between two locations taking into account walls using A* search algorithm
//	 * @param a first location
//	 * @param b second location
//	 * @return distance
//	 */
//	public static double walkingDistance(Location a, Location b){
//		//account for diagonal distance
//		return lengthOf(aStarSearch(a,b));
//	}
	
//	/**
//	 * Total distance of locations in order
//	 * @param locations
//	 * @return distance
//	 */
//	private static double lengthOf(ArrayList<Location> locations) {
//		double distance = 0;
//		for (int i = 0; i<locations.size()-1; i++){
//			distance+= directDistanceEdge(locations.get(i),locations.get(i+1));
//		}
//		return distance;
//	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String toString() {
		return "Location at <" + x + ", " + y + ">";
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	protected void setSprite(String location){
		try {
//			URL url = new File("Sprites/"+location);
			URL url = getClass().getClassLoader().getResource("Sprites/" + location);
//			System.out.println(url);
			sprite = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected boolean isBlocking() {
		return blocking;
	}
}
