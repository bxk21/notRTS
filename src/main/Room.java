package main;

import java.util.ArrayList;

public class Room {

	protected ArrayList<Room> previous, next;
	protected int xLoc, yLoc, size, height, width;


	public Room(int height, int width) {
		xLoc = 0;
		yLoc = 0;
		size = height * width;
		this.height = height;
		this.width = width;
		previous = new ArrayList<Room>();
		next = new ArrayList<Room>();
		
	}
	
	public void addConnection(Room room) {
		addPrevious(room);
		room.addNext(this);
	}
	
	public void addPrevious(Room room) {
		previous.add(room);
	}
	
	public void addNext(Room room) {
		next.add(room);
	}
	
	public ArrayList<Room> getConnections(){
		ArrayList<Room> out = new ArrayList<Room>();
		out.addAll(next);
		out.addAll(previous);
		return out;
	}
	
	public boolean isConnected(Room room){
		return(previous.contains(room)||next.contains(room));
	}
	
	public ArrayList<Room> getPrevious() {
		return previous;
	}
	
	public ArrayList<Room> getNext() {
		return next;
	}
	
	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}

	public void setX(int x) {
		xLoc = x;
	}
	
	public void setY(int y) {
		yLoc = y;
	}
	
	public void setLoc(int x, int y){
		xLoc = x;
		yLoc = y;
		
		
	}
	
	public int getSize() {
		return size;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public String getInfo() {
		return  "X & Y: ("+xLoc +", " + yLoc + ") Width & Height: (" + width + ", " + height + ")"+ "Connected to " + getConnections().size();

	}

	@Override
	public String toString() {
		return "(" + width + ", " + height + ")";
	}
	
	public static boolean overlaps(Room room1, Room room2) {
		return (Math.abs(room1.getX() - room2.getX()) <=
				(room1.getWidth() + room2.getWidth())/2 &&
				Math.abs(room1.getY() - room2.getY()) <=
				(room1.getHeight() + room2.getHeight())/2);
	}
	
	/**
	 * returns the euclidian distance from this room to the given room
	 * @param room
	 * @return int distance
	 */
	public int manhattanD(Room room) {
		return Math.abs(this.getX() - room.getX())+Math.abs(this.getY() - room.getY());
	}
}
