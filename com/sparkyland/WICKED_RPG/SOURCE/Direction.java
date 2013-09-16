import java.awt.event.KeyEvent;

class Direction 
{
	public final static int
		RIGHT = 0,
		LEFT = 1, 
		UP = 2,
		DOWN = 3;

	int intDirection;
	// Constructors
	// -----------------------------------------------------------
	public Direction()
	{
		this( Direction.DOWN ); // No negatives!
	}
	public Direction(int intKey)
	{
		setIntDirection( intKey );
		//System.out.println("dire: " + intKey);
	}

	// Class methods.
	// ----------------------------------------------------------
	static int heading( int vx, int vy )
	{
		int d;
		int x = Math.abs(vx);
		int y = Math.abs(vy);

		if (x > y)
		{
			if (vx < 0)
			{
				d = Direction.LEFT;
			}
			else
			{
				d = Direction.RIGHT;
			}
		}
		else
		{
			if (vy < 0)
			{
				d = Direction.UP;
			}
			else
			{
				d = Direction.DOWN;
			}
		}
		return d;
	}
	static int heading( Coordinate c )
	{
		return heading( c.getX(), c.getY() );
	}
	static int getIntDirection (int intKey)
	{	
		if (Direction.isValidDirection( intKey ))
		{
			// This check is necessary because we may pass in a different key.
			//System.out.println("Valid Directional key.");
		}
		int intDirection = intKey; 
		switch (intKey)
		{
			case KeyEvent.VK_RIGHT :
				intDirection = RIGHT; break;
			case KeyEvent.VK_LEFT :
				intDirection = LEFT; break;
			case KeyEvent.VK_UP :
				intDirection = UP; break;
			case KeyEvent.VK_DOWN :
				intDirection = DOWN; break;
		}
		return intDirection;
	}

	static boolean isValidDirection( int intKey )
	{
		// This check is necessary because we may pass in a different key.
		boolean valid = false;
		if (intKey >= 0 && intKey <= 3)
		{
			valid = true;
		}
		switch (intKey)
		{
			case KeyEvent.VK_RIGHT :
			case KeyEvent.VK_LEFT :
			case KeyEvent.VK_UP :
			case KeyEvent.VK_DOWN :
			{
				valid = true;
				break;
			}
		}
		return valid;
	}

	public Direction opposite()
	{
		int opposite = DOWN;
		switch (intDirection)
		{
			case RIGHT :
				opposite = LEFT; break;
			case LEFT :
				opposite = RIGHT; break;
			case UP :
				opposite = DOWN; break;
			case DOWN :
				opposite = UP; break;
		}
		return new Direction( opposite );
	}

	public void setIntDirection (int intKey) 
	{
		this.intDirection = Direction.getIntDirection(intKey);
	}
	public int getIntDirection() { return this.intDirection; }

}
