package main.Tiles;

import main.Tile;

import java.awt.*;

public class TileStairs extends Tile{
	public TileStairs(int x, int y) {
		super(x, y);
		blocksVision = false;
		blocksGround = false;
		blocksAir = false;
		character = 'S';
		string = "Stairs";
		color = Color.GREEN;
	}
}
