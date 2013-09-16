import java.io.Serializable;

class Coordinate implements Serializable
{
	private int x, y;

	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public Coordinate()
	{
		this(0,0);
	}
	public void adjust( int distance, Direction direction )
	{
		int intKey = direction.getIntDirection();
		switch (intKey)
		{
			case Direction.RIGHT:
				x = x + distance; break;
			case Direction.LEFT:
				x = x - distance; break;
			case Direction.UP:
				y = y - distance; break;
			case Direction.DOWN:
				y = y + distance; break;
		}
	}

	// Setters & Getters.
	// -----------------
	public void setX ( int x ) { this.x = x; }
	public void setY ( int y ) { this.y = y; }
	public int getX () { return this.x;	}
	public int getY () { return this.y; }
}
