package com.sparkyland.spartique.physical;

import com.sparkyland.spartique.common.DebugLog;

import java.io.Serializable;

public class Coordinate implements Serializable, Cloneable
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
	public Coordinate( Coordinate coordinate )
	{
		this( coordinate.getX(), coordinate.getY() );
	}
	public void clone( Coordinate coordinate )
	{
		this.x = coordinate.getX();
		this.y = coordinate.getY();
	}
	public void adjust( int distance, Direction direction )
	{
		this.adjust( distance, direction.getIntDirection() );
	}
	public void adjust( int distance, int intDirection )
	{
		//DebugLog.println("It's gonna work now: adjust!" );
		switch ( intDirection )
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
	public boolean equals( Coordinate coordinate )
	{
		return ( this.x == coordinate.getX() && this.y == coordinate.getY() );
	}
	public String toString()
	{
		return ( super.toString() + " x: " + x + " y: " + y );
	}
	public void translate( int multiplier )
	{
		this.x = x * multiplier;
		this.y = y * multiplier;
	}
	
	// Setters & Getters.
	// -----------------
	public void setX ( int x ) { this.x = x; }
	public void setY ( int y ) { this.y = y; }
	public int getX () { return this.x;	}
	public int getY () { return this.y; }
}
