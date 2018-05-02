package main;

import java.awt.*;

public abstract class Tile extends Location{
//	private String path;
	protected char character;
	protected String string;
	protected boolean blocksVision;
	protected boolean blocksGround;
	protected boolean blocksAir;
	protected Color color = Color.PINK;

    private Paint tilePaint;
	public Tile(int x, int y) {
		super(x, y);
		
//		path = makePath();
//		tilePaint = new Paint();

	}

//	public String makePath() {
//		String p = "src/img/Error.png";
//
//		if (this instanceof TileWall) {
//			p = "src/img/Wall.png";
//		} else if (this instanceof Tile) {
//			p = "src/img/Tile.png";
//		}
//		return p;
//	}

//	public String getPath() {
//		return path;
//	}

	public char getChar() {
		return character;
	}

	@Override
	public String toString() {
		return string;
	}
	
	/**
	 * Unimplemented Vision code
	 * @return whether or not the tile blocks vision.
	 */
	public boolean blocksVision() {
		return blocksVision;
	}
	
	/**
	 * Collision boolean for ground entities
	 * @return whether or not the tile blocks ground movement
	 */
	public boolean blocksGround() {
		return blocksGround;
	}
	
	/**
	 * Collision boolean for air entities
	 * @return whether or not the tile blocks air movement
	 */
	public boolean blocksAir() {
		return blocksAir;
	}
	
	@Override
	public boolean moves() {
		return false;
	}
	
	public Color getColor() {
		return color;
	}

//    public void draw(Canvas canvas) {
//        if (this instanceof Wall) {
//        	tilePaint.setColor(Color.RED);
//        } else if (this instanceof Landing) {
//        	tilePaint.setColor(Color.BLUE);
//        } else if (this instanceof Chest) {
//        	tilePaint.setColor(Color.YELLOW);
//        } else if (this instanceof Stairwell) {
//        	tilePaint.setColor(Color.GREEN);
//        } else if (this instanceof Tile) {
//        	tilePaint.setColor(Color.WHITE);
//        }
//        canvas.drawRect(0, 0, GameManager.SCALE, GameManager.SCALE, tilePaint);
//        tilePaint.setColor(Color.BLACK);
//        canvas.drawLine(0,0, GameManager.SCALE, 0, tilePaint);
//        canvas.drawLine(0,0, 0,GameManager.SCALE, tilePaint);
//    }
}
