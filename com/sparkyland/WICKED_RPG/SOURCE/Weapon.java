
import java.awt.Image;

abstract class Weapon extends WSprite
{
	String type;
	Direction direction;
	Coordinate location;

	public Weapon (int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], go wicked, int freq, int vx, int vy,
					String t, Direction dir
					)
	{
		super(x, y, w, h, s, l, n, f, wicked, freq, vx, vy);
		type = t;
		direction = dir;
	}
	public Weapon ()
	{
		this (0, 0, 0, 0, false, 1, "name", null, null, 0, 0, 0, "type", null );
	}

	// Setters & Getters.
	// ---------
	public void setType ( String type ) { this.type = type; }
	public void setDirection ( Direction direction ) { this.direction = direction; }
	public void setLocation ( Coordinate location )
	{
		this.location = location;
		locx = location.getX();
		locy = location.getY();
	}
	public String getType ( ) { return type; }
	public Direction getDireciton ( ) { return direction; }
	public Coordinate getLocation ( ) { return location; }
}
