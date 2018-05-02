package main.Tiles;

import main.Tile;

import java.awt.*;

public class TileChest extends Tile {
	
	public TileChest(int x, int y){
		super(x, y);
		blocksVision = false;
		blocksGround = false;
		blocksAir = false;
		character = 'C';
		string = "Chest";
		color = Color.YELLOW;
	}
}
