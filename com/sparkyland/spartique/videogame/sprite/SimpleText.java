package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.DisplayCanvas;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

// prototyping.
import java.io.Serializable;

/////////////////////////////////////////////////////////////////
public class SimpleText extends SimplePicture
{
	protected int pointSize;

	public SimpleText(int x, int y, int l, String n, DisplayCanvas canvas, int pointSize )
	{
		super(x, y, 0, 0, false, l, n, canvas);
		this.pointSize = pointSize;
	}
	public void paint( Graphics g )
	{
		g.setColor( Color.red );
		g.setFont( new Font( canvas.getFont().getName(), Font.BOLD, pointSize ) );
		g.drawString( name, locx, locy );
	}
	public void setMessage( String message ) { this.name = message; }

}