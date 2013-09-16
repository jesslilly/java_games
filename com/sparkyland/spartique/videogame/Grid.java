package com.sparkyland.spartique.videogame;


import com.sparkyland.spartique.videogame.sprite.SimplePicture;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.physical.Size;

import java.awt.Color;
import java.awt.Graphics;

/////////////////////////////////////////////////////////////////
public class Grid extends SimplePicture
{
	protected Size squareSize;

	public Grid(int x, int y, int w, int h, int l, Size squareSize, DisplayCanvas canvas)
	{
		super(x, y, w, h, false , l,  "grid", canvas);
		this.squareSize = squareSize;
	}

	public void paint (Graphics g)
	{
		int numCols = ( width / squareSize.getWidth()  );
		int numRows = ( height / squareSize.getHeight()  );
		g.setColor( Color.black );

		int tempX = 0;
		int tempY = locy + height;
		for ( int horzIndex = 1 ; horzIndex < numCols ; horzIndex++ )
		{
			tempX = horzIndex * squareSize.getWidth();
			g.drawLine( tempX , locy , tempX, tempY);
		}
		tempX = locx + width;
		tempY = 0;
		for ( int vertIndex = 1 ; vertIndex < numRows ; vertIndex++ )
		{
			tempY = vertIndex * squareSize.getHeight();
			g.drawLine( locx , tempY , tempX, tempY);
		}
	}
}