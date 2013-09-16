package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.common.DebugLog;

import com.sparkyland.spartique.physical.Direction;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;

import java.awt.*;

public class BoundedSprite extends Sprite
{
	static public final int STOP = 1, BOUNCE = 2, WARP = 3;

	protected boolean bounded;
	protected Rectangle bounds;
	protected int reaction;

	public BoundedSprite(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], DisplayCanvas canvas, int freq, int vx, int vy)
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		bounded = false;
		bounds = new Rectangle();
		reaction = STOP;
	}
	public BoundedSprite()
	{
		bounded = false;
	}

	public BoundedSprite( CSVTokenizer tokenizer )
	{
		super( tokenizer );
	}

	public void update()
	{
		super.update();

		if ( bounded && isMoving() && isOutOfBounds() )
		{
			//DebugLog.println("BoundedSprite is out of bounds.");
			react();
		}
	}

	// rewrite: duplicate code.  See react.  The same checks.
	private boolean isOutOfBounds()
	{ 
		return ( locx <= bounds.x
		|| ( locx + width ) >= ( bounds.x + bounds.width )
		|| locy <= bounds.y
		|| ( locy + height ) >= ( bounds.y + bounds.height ) );
	}

	public void react ()
	{
		switch ( reaction )
		{
			case STOP :
			{
				this.stopMoving();
				break;
			}
			case BOUNCE :
			{
				if (locx <= bounds.x)
				{
					vx = -vx;
				}
				else if ( ( locx + width ) >= bounds.width)
				{
					vx = -vx;
				}
				else if (locy <= bounds.y)
				{
					vy = -vy;
				}
				else if ( ( locy + height ) >= bounds.height)
				{
					vy = -vy;
				}
				break;
			}
			case WARP :
			{
				if ( locx <= bounds.x )
				{
					locx =  bounds.width;
				}
				else if ( locx >= bounds.width )
				{
					locx = bounds.x;
				}
				else if ( locy <= bounds.y )
				{
					locy = bounds.height;
				}
				else if ( locy >= bounds.height )
				{
					locy =  bounds.y;
				}
				break;
			}
			// rewrite: Implement WARP
			
		
		}
	}
	// rewrite: with radius parameter. 1 assumed.
	public void setRadiusBounds ( )
	{	
		this.setBounds( new Rectangle( locx - this.width, locy - this.height, this.width * 3, this.height * 3 ) );
	}

	public boolean isBounded() { return bounded; }
	public void setBounded( boolean bounded ) { this.bounded = bounded; }
	public Rectangle getBounds() { return bounds; }
	public void setBounds ( Rectangle bounds ) { this.bounds = bounds; bounded = true; }
	public int getReaction() { return reaction; }
	public void setReaction ( int reaction ) { this.reaction = reaction; }
}