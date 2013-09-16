package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.CSVTokenizer;

import java.awt.Graphics;
import java.awt.Color;

public class ColorBox extends SimplePicture
{
	// There is no 'clear'.  Think about it.  You just don't add a ColorBox...
	protected Color color;

	public ColorBox(int x, int y, int w, int h, boolean s, int l, String n, DisplayCanvas canvas, Color color)
	{
		super(x, y, w, h, s, l, n, canvas);
		this.color = color;
	}
	public ColorBox( CSVTokenizer tokenizer )
	{
		super( tokenizer );
		int r = tokenizer.getIntAt(8);
		int g = tokenizer.getIntAt(9);
		int b = tokenizer.getIntAt(10);
		this.color = new Color( r, g, b );
	}
	protected void setColor( Graphics g )
	{
		g.setColor(this.color);
	}

	// Setters
	// -------
	public void setColor( Color color ) { this.color = color; }

	// Getters
	// -------
	public Color getColor() { return this.color; }
}