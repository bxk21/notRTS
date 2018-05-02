package main.Tiles;

import main.Tile;

import java.awt.*;

public class TileGround extends Tile{
	public TileGround(int x, int y) {
		super(x, y);
		blocksVision = false;
		blocksGround = false;
		blocksAir = false;
		character = ' ';
		string = "Ground";
		color = Color.LIGHT_GRAY;
	}
}
