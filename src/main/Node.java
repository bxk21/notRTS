package main;

import main.Managers.GameManager;

import java.util.ArrayList;

public class Node {
	public int x, y;
	public Node goal;//all nodes in a set have the same goal
	public Location parent;
	public double costF, currentCostG, absoluteDistanceH;
	public ArrayList<Node> neighbors;
	ArrayList<Node> nodes;
	
	public Node(ArrayList<Node> nodes, int x, int y, Location goal){
		this.x = x;
		this.y = y;
		this.goal = getNode((int) goal.x, (int)goal.y);
		this.nodes = nodes;
		
		absoluteDistanceH = Location.directDistanceEdge(new Location(x, y), goal);
		getNeighbors(true);
	}
	
	public Node(ArrayList<Node> nodes, int x, int y, Node goal) {
		this.x = x;
		this.y = y;
		this.goal = goal;
		this.nodes = nodes;
		
		absoluteDistanceH = Location.directDistanceEdge(new Location(x, y), new Location(goal.x, goal.y));
		getNeighbors(true);
	}
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private ArrayList<Node> getNeighbors(boolean passableOnly) {
		neighbors.clear();
		for (int xLoc = x-1; xLoc<=x+1; xLoc++){//In the 3x3 area around (x,y)
			for (int yLoc = y-1; yLoc<=y+1; yLoc++){
				if( !passableOnly || !GameManager.level.getTile(xLoc, yLoc).blocksGround){//Is not blocking
					neighbors.add(getNode(xLoc, yLoc));
				}
			}
		}
		return neighbors;
	}
	
	public Node getNode(int x, int y){
		for (Node node : nodes){
			if (node.x==x && node.y==y) {
				return node;
			}
		}
		Node node = new Node(x, y);
		nodes.add(node);
		return node;
	}
	
}

