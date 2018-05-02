package main.Tiles;

import main.Tile;

import java.awt.*;

public class TileStairLanding extends Tile {
	public TileStairLanding(int x, int y) {
		super(x, y);
		blocksVision = false;
		blocksGround = false;
		blocksAir = false;
		character = 'L';
		string = "Landing";
		color = Color.BLUE;
	}
}
