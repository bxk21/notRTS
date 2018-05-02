package main.Tiles;

import main.Tile;

import java.awt.*;

public class TileWall extends Tile{
	public TileWall(int x, int y) {
		super(x, y);
		blocksVision = true;
		blocksGround = true;
		blocksAir = false;
		character = 'x';
		string = "Wall";
		color = Color.BLACK;
	}
}
