package main;

import main.Entities.EntityCharger;
import main.Entities.EntityResource;
import main.Entities.Resources.EntityResourceChest;
import main.Entities.Resources.EntityResourceStairs;
import main.Managers.GameManager;
import main.Tiles.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Level {
	private Tile[][] tiles;
	private Random seed;
	private boolean loadSeeds = false;
	private int avgSize;
	private int difficulty;
	private int varSize;
	private ArrayList<Entity> entities;
	private EntityResource stairsDown;
	private EntityResource stairsUp;
	
	public Level(int height, int width, int difficulty) {
		this.difficulty = difficulty * Main.difficulty;
		seed = new Random(Main.getSeed() + difficulty);
		genLevel(height, width);
		
	}
	
	private void genLevel(int height, int width){
		tiles = new Tile[height][width];
		entities = new ArrayList<>();
		
		//Magic numbers for room generation
		avgSize = (int) (height * width / 20 * (getRandom() * .6 + .3));
		varSize = (int) (avgSize * .75);
		int numRooms = (int) ((height * width) / avgSize * 2 * Math.sqrt(difficulty+1));
		if (numRooms < 5){
			numRooms = 5;
		}
		
		
		fillLevel(false);
		ArrayList<Room> placed = placeRooms(generateRooms(numRooms));
		genCorridors(placed);
		drawCorridors(placed);
		genObjects(placed);
//		setStart(xStart, yStart);
//		fillLevel(true);
		
		System.out.println(this);
	}
	
	/**
	 * Generates a seed and saves it to a list
	 * Can be set to load previous seeds
	 * @return a seed: a double between [0,1)
	 */
	private double getRandom(){
		return seed.nextDouble();
	}
	
	public EntityResource getStairsDown(){
	    return stairsDown;
    }
    
    public EntityResource getStairsUp(){
	    return stairsUp;
    }

	
	public Tile spaceAt(int x, int y){
		return tiles[y][x];
	}
	
	/**
	 * Sets the Start location of the player
	 */
//	public void setStart(int xStart, int yStart) {
//		GameManager.getPlayer().setLocation(xStart + .5, yStart + .5);
//		
//	}
	
	private ArrayList<Room> generateRooms(int numRooms) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		for (int i = 0; i < numRooms; i++) {
			int size = (int) (varSize * (2 * getRandom() - 1) + avgSize);
			double ratio = getRandom() * .7 + .3;
			int width = (int) Math.sqrt(size / ratio);
			int height = (int) (width * ratio);
			
			if (100 *getRandom()%2==0) {//50% chance to flip dimensions
				int tmp = width;
				width = height;
				height = tmp;
			}
			rooms.add(new Room(height, width));
		}
		Collections.sort(rooms, new RoomSizeComparator());
//		if (GameManager.dbglvl > 0) {
//			System.out.println("Rooms: " + rooms.size()+" "+rooms.toString());
//		}
		return rooms;
	}
	
	/**
	 * Comparator for Room by their Size
	 */
	public class RoomSizeComparator implements Comparator<Room> {
		public int compare(Room o1, Room o2) {
			return o1.getSize() - o2.getSize();
		}
	}
	
	/**
	 * Comparator for Room by their number of Connections
	 */
	public class RoomConnectionsComparator implements Comparator<Room> {
		public int compare(Room o1, Room o2) {
			return o1.getConnections().size() - o2.getConnections().size();
		}
	}
	
	
	public ArrayList<Room> placeRooms(ArrayList<Room> rooms) {
		ArrayList<Room> placed = new ArrayList<>();
		rooms.get(0).setLoc((getWidth() / 2), (getHeight() / 2));
//		//System.out.println("FIRST ROOM IS PLACED AT:\n"
//				+ rooms.get(0).getChar() + "\n");
		
		writeRoom(rooms.get(0).getWidth(), rooms.get(0).getHeight(), rooms.get(0).getX(), rooms.get(0).getY());
		placed.add(rooms.get(0));
		
		Room prev = null;
		for (Room room : rooms) {
			
			if (prev == null) {
				prev = room;
				continue;
			}
//			//System.out.println("Current room Width & Height: "
//					+ room.getWidth() + " " + room.getHeight());
			
			double yMax = (room.getHeight() + prev.getHeight()) / 2 + 1;
			double xMax = (room.getWidth() + prev.getWidth()) / 2 + 1;
			int x, y;
//			//System.out.println("    Max X,Y " + xMax + " " + yMax);
			
			for (int tries = 0; tries < 10; tries++) {
				// theta is the angle going clockwise from the right
				double theta = getRandom() * 2 * Math.PI;
				double xDelta = Math.cos(theta);
				double yDelta = Math.sin(theta);

//				//System.out.println("    Theta slope:"
//						+ Math.abs(yDelta / xDelta) + "\n    Room slope:"
//						+ (yMax / xMax));
				
				if (Math.abs(yDelta / xDelta) > yMax / xMax) {// attach
					// vertically
					int down = (int) Math.round(yDelta / Math.abs(yDelta));
					y = (int) (prev.getY() + yMax * down);
//					//System.out.println("    Y change: " + yMax * down);
					x = (int) (prev.getX() + yMax * down * xDelta / yDelta);
//					//System.out.println("    X change: " + yMax * down * xDelta / yDelta);
					if (yMax * down * xDelta / yDelta > xMax + 1) {
						System.exit(1);
					}
				} else {// attach horizontally
					int right = (int) Math.round(xDelta / Math.abs(xDelta));
					x = (int) (prev.getX() + xMax * right);
//					//System.out.println("    X change: " + xMax * right);
					y = (int) (prev.getY() + xMax * right * yDelta / xDelta);
//					//System.out.println("    Y change: " + xMax * right * yDelta / xDelta);
					if (xMax * right * yDelta / xDelta > yMax + 1) {
						System.exit(1);
					}
				}
//				//System.out.println("    Attempting: x, y: ("
//						+ (x - room.getWidth() / 2) + " < " + (x) + " < "
//						+ (x + room.getWidth() / 2) + ", "
//						+ (y - room.getHeight() / 2) + " < " + (y) + " < "
//						+ (y + room.getHeight() / 2) + ")" + " Theta: " + theta
//						+ ", x/y Delta: (" + xDelta + ", " + yDelta + ")");
				
				// checks bounds of level
				if (x + room.getWidth() / 2 > tiles.length - 1) {
//					//System.out.println("        Out of Bounds: Right");
					continue;
				} else if (y + room.getHeight() / 2 > tiles[0].length - 1) {
//					//System.out.println("        Out of Bounds: Down");
					continue;
				} else if (x - room.getWidth() / 2 < 1) {
//					//System.out.println("        Out of Bounds: Up");
					continue;
				} else if (y - room.getHeight() / 2 < 1) {
//					//System.out.println("        Out of Bounds: Left");
					continue;
				}
//				//System.out.println("        Within Bounds: x, y: ("
//						+ (x - room.getHeight() / 2) + " _ "
//						+ (x + room.getHeight() / 2) + ", "
//						+ (y - room.getWidth() / 2) + " _ "
//						+ (y + room.getWidth() / 2) + ")");
				
				room.setLoc(x, y);
				
				boolean overlaps = false;
				for (Room b : placed) {
					if (Room.overlaps(room, b)) {
						overlaps = true;
//						//System.out.println("            OVERLAPS with: " + b.getChar());
						break;
					}
				}
				if (!overlaps) {
				
//					if (GameManager.dbglvl > 0) {
////						//System.out.println("            WRITING: " + room.getChar());
//					}
					writeRoom(room.getWidth(), room.getHeight(), room.getX(), room.getY());
					room.addConnection(prev);
					placed.add(room);
					prev = getRandomRoom(placed);
					break;
				}
			}
		}
		//System.out.println("Kept Rooms: "+placed.size());
		return placed;
	}
	
	public void genCorridors(ArrayList<Room> rooms) {
		for (Room roomA : rooms) {
			for (Room roomB : rooms) {
				if (roomA != roomB && !roomA.isConnected(roomB)) {
					double value = getRandom() * 1000 / Math.pow(rooms.size(), 1.5);
					//System.out.println(roomA.manhattanD(roomB) + " " + value);
					if (roomA.manhattanD(roomB) < value) {
						roomA.addConnection(roomB);
						//System.out.println(" Connected");
					}
				}
			}
		}
	}
	
	/**
	 * @param rooms
	 */
	private void drawCorridors(ArrayList<Room> rooms) {
		for (Room roomA : rooms) {
			for (Room roomB : roomA.getNext()) {
				Room left, right, up, down;
				if (roomA.getX() < roomB.getX()) {
					left = roomA;
					right = roomB;
				} else {
					left = roomB;
					right = roomA;
				}
				if (roomA.getY() < roomB.getY()) {
					up = roomA;
					down = roomB;
				} else {
					up = roomB;
					down = roomA;
				}
				
				if (left.getWidth() + right.getHeight() < left.getHeight() + right.getWidth()) {//Calculates if it's faster to have a 'L' or '7' shaped hallway.
					for (int x = left.getX(); x < right.getX() + 1; x++) {
						drawCorridor(x, right.getY());
					}
					for (int y = up.getY(); y < down.getY() + 1; y++) {
						drawCorridor(left.getX(), y);
					}
				} else {
					for (int x = left.getX(); x < right.getX() + 1; x++) {
						drawCorridor(x, left.getY());
					}
					for (int y = up.getY(); y < down.getY() + 1; y++) {
						drawCorridor(right.getX(), y);
					}
				}
			}
		}
	}
	
	/**
	 * Creates a TileGround at the point
	 * @param x
	 * @param y
	 */
	private void drawCorridor(int x, int y) {
		tiles[y][x] = new TileGround(x, y);
	}
	
	private void genObjects(ArrayList<Room> rooms) {
		Collections.sort(rooms, new RoomConnectionsComparator());
		ArrayList<Room> roomSet = new ArrayList<>();
		Room[] specialRooms = new Room[3 + (int) (getRandom() * (rooms.size() - 2) / 3)];
		int specialRoom = 0;
		int size = rooms.get(0).getConnections().size();
		for (Room room : rooms) {// compile dead ends
			//System.out.println(" "+room.getConnections().size());
			if (!(room.getConnections().size() == size) || room == rooms.get(rooms.size() - 1)) {
				while (!roomSet.isEmpty()) {
					//System.out.println(specialRoom);
					specialRooms[specialRoom] = roomSet.get((int) (roomSet.size() * getRandom()));
					roomSet.remove(specialRooms[specialRoom]);
					specialRoom++;
					if (specialRoom == specialRooms.length) {
						//System.out.print("Done");
						break;
					}
				}
				if (specialRoom == specialRooms.length) {
					//System.out.println(" and Done");
					break;
				}
				size = room.getConnections().size();
				
			}
			roomSet.add(room);
		}
		//System.out.println("Number of special Rooms: "+specialRooms.length);
		tiles[specialRooms[0].getY()][specialRooms[0].getX()] = new TileStairs(specialRooms[0].getX(), specialRooms[0].getY());
		stairsDown = new EntityResourceStairs(specialRooms[0].getX()+.5,specialRooms[0].getY()+.5, true);
		entities.add(stairsDown);
		
		
		tiles[specialRooms[1].getY()][specialRooms[1].getX()] = new TileStairLanding(specialRooms[1].getX(), specialRooms[1].getY());
		stairsUp = new EntityResourceStairs(specialRooms[1].getX()+.5,specialRooms[1].getY()+.5, false);
		entities.add(stairsUp);
		
//		setStart(specialRooms[1].getX(), specialRooms[1].getY());
		
		for (int i = 2; i < specialRooms.length; i++) {
			tiles[specialRooms[i].getY()][specialRooms[i].getX()] = new TileChest(specialRooms[i].getX(), specialRooms[i].getY());
			entities.add(new EntityResourceChest(specialRooms[i].getX()+.5,specialRooms[i].getY()+.5));
		}
		
		for (Room room : rooms) {
			if (room == specialRooms[0] || room == specialRooms[1]){
				continue;
			}
			int numMonsters = (int)(room.getSize() / 10 * Math.log(difficulty+3) * getRandom());
//			System.out.println("Num Monsters: " + numMonsters);
			for (int i = 0; i < numMonsters; i++) {
				entities.add(new EntityCharger(room.xLoc+ (room.getWidth() * (getRandom()-.5) * .8), room.yLoc+ (room.getHeight() * (getRandom()-.5) * .8)));
			}
		}
	}
	
	public Room getRandomRoom(ArrayList<Room> rooms) {
		return rooms.get((int) (rooms.size() * getRandom()));
	}
	
	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}
	
	/**
	 * To be used at the beginning of level generation
	 * Fills the level with walls
	 * @param walkable
	 */
	private void fillLevel(boolean walkable) {
		if (walkable) {
			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					if (tiles[y][x]==null){
						tiles[y][x] = new TileGround(x, y);
					}
				}
			}
		} else {
			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					tiles[y][x] = new TileWall(x, y);
				}
			}
		}
	}
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[1].length;
	}
	
	private void writeRoom(int width, int height, int x, int y) {
		for (int row = y - (height / 2); row < height / 2 + y; row++) {
			for (int col = x - (width / 2); col < width / 2 + x; col++) {
				tiles[row][col] = new TileGround(col, row);
			}
		}
	}
	
	/**
	 * Returns a string representation of the map, with each tile as their char representations
	 * @return
	 */
	public String toString() {
		String st = "";
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				
				
				st += tiles[i][j].getChar();
			}
			st += "\n";
		}
		return st;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	
}
