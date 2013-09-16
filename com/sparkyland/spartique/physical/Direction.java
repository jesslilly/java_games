package com.sparkyland.spartique.physical;

import com.sparkyland.spartique.common.RandomNumGen;
import com.sparkyland.spartique.common.DebugLog;
import java.awt.event.KeyEvent;

public class Direction // rewwrite: extends Point...
{
	public final static int
		UP = 0,
		RIGHT = 1, 
		DOWN = 2,
		LEFT = 3,
		NUM_DIRECTIONS = 4;

	int intDirection;
	// Constructors
	// -----------------------------------------------------------
	public Direction()
	{
		this( Direction.UP ); // No negatives!
	}
	public Direction(int intKey)
	{
		setIntDirection( intKey );
		//DebugLog.println("dire: " + intKey);
	}

	// Class methods.
	// ----------------------------------------------------------
	public static int heading( int vx, int vy )
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
	public static int heading( Coordinate c )
	{
		return heading( c.getX(), c.getY() );
	}
	public static int getIntDirection (int intKey)
	{	
		if (Direction.isValidDirection( intKey ))
		{
			// This check is necessary because we may pass in a different key.
			//DebugLog.println("Valid Directional key.");
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

	public static boolean isValidDirection( int intKey )
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
	public void incrementDirection()
	{
		if ( this.intDirection >= NUM_DIRECTIONS )
			this.intDirection = UP;
		else
			this.intDirection++;
	}
	public String toString()
	{
		String directionString = "?";
		switch (intDirection)
		{
			case RIGHT :
				directionString = "Right"; break;
			case LEFT :
				directionString = "Left"; break;
			case UP :
				directionString = "Up"; break;
			case DOWN :
				directionString = "Down"; break;
		}
		return directionString;
	}
	public static Direction getRandomDirection()
	{
		return new Direction( RandomNumGen.rollXSidedDie( Direction.NUM_DIRECTIONS ) - 1 );
	}

}
