package com.sparkyland.spartique.videogame.cards;

import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.videogame.sprite.BoundedSprite;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.DebugLog;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;

/////////////////////////////////////////////////////////////////
public class FlipCard extends Card
{
	private boolean faceUp;
	private static Image back[];
	private Image face[];
	
	// static initializer.
	static
	{
		FlipCard.back = ResourceLoader.getPictures( "backofcard", 0, 0 );
	}

	public FlipCard(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], GameCanvas canvas, int freq, int vx, int vy)
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		faceUp = false;
		face = frames;
		frames = back;
	}
	public boolean isFaceUp()
	{
		return faceUp;
	}
	public boolean isFaceDown()
	{
		return ! faceUp;
	}
	public void flip()
	{
		faceUp = ! faceUp;

		if ( faceUp )
		{
			frames = face;
		}
		else
		{
			frames = back;
		}
	}

	/*
	public void mouseReleased( )
	{
		super.mouseReleased();
		//if ( this.wasClicked() )
		//{
		//	DebugLog.println("Flip card: " + name );
		//	flip();
		//}
	}
	*/
	/*
	public void paint (Graphics g)
	{
		if ( faceUp )
		{
			super.paint(g);
		}
		else
		{
			g.drawImage(frames[frame], locx, locy, width, height, canvas);
		}
	}*/

}