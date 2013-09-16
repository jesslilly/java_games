package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.physical.Direction;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;

import java.awt.*;

/////////////////////////////////////////////////////////////////
public class Sprite extends AnimatedPicture {
	protected int vx, vy;
	protected Direction direction;

	public Sprite(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], DisplayCanvas canvas, int freq, int vx, int vy)
	{
		super(x, y, w, h, s, l, n, f, canvas, freq);
		direction = new Direction();
		setVelocity( vx, vy );
	}

	public Sprite ()
	{
		super();
		stopMoving();
	}
	public Sprite( CSVTokenizer tokenizer )
	{
		super( tokenizer );
		direction = new Direction();
		setVelocity( tokenizer.getIntAt(12), tokenizer.getIntAt(13) );
	}

	public void update()
	{
		super.update();

		updateLocation();
	}

	// is the mouse inside this sprite?
	public boolean clicked(int x, int y)
	{ 
		return ((x >= locx) && (x <= (locx + width)) && (y >= locy) && (y <= (locy + height)));
	}

	public void updateLocation()
	{ 
		locx = locx + vx;
		locy = locy + vy;
	}

	public void setVelocity ( int x, int y )
	{
		vx = x;
		vy = y;
		if (vx != 0 && vy != 0)
		{
			direction.setIntDirection(Direction.heading(vx,vy));
		}
	}

	public void setVelocity ( Coordinate vel )
	{
		setVelocity ( vel.getX(), vel.getY() );
	}
	public Coordinate getVelocity ( )
	{
		return new Coordinate( vx, vy );
	}
	public void setVX ( int x )	{ setVelocity(x, this.vy); }
	public void setVY ( int y )	{ setVelocity(this.vx, y); }
	public int getVX () { return vx; }
	public int getVY () { return vy; }
	public void setDirection( Direction d ) { this.direction = d; }
	public Direction getDirection() { return direction; }
	public boolean isMoving() { return (vx != 0 || vy != 0 ); }
	public void stopMoving() { setVelocity( 0, 0 ); }

/*
	public boolean isAtASquare()
	{
		return (
			(vx != 0 && (locx % 20 == 0))
			||
			(vy != 0 && (locy % 20 == 0))
			);
	}
	*/

	public Rectangle getClipRect()
	{
		Rectangle clipRect = super.getClipRect();

		// Adjust x for velocity.
		if (vx < 0) // Going left
			clipRect.width = clipRect.width - vx;
		else // Going right
		{
			clipRect.x = clipRect.x - vx;
			clipRect.width = clipRect.width + vx;
		}

		// Adjust y for velocity.
		if (vy < 0) // Going up
			clipRect.height = clipRect.height - vy;
		else // Going down
		{
			clipRect.y = clipRect.y - vy;
			clipRect.height = clipRect.height + vy;
		}

		return clipRect;
	}
}