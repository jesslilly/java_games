package com.sparkyland.spartique.physical;

import java.awt.Dimension;

public class Size  implements Cloneable // rewrite: extend Dimension instead?
{
	private int height, width;
	static int defaultHeight, defaultWidth = 0;

	public Size(int height, int width)
	{
		this.height = height;
		this.width = width;
	}
	public Size()
	{
		this( defaultHeight, defaultWidth );
	}
	public Size( Size size )
	{
		this( size.getHeight(), size.getWidth() );
	}
	public Size( Dimension dimension )
	{
		this( dimension.height, dimension.width );
	}
	public void clone( Size size )
	{
		this.height = size.getHeight();
		this.width = size.getWidth();
	}
	public String toString()
	{
		return super.toString() + " height=" + height + " width=" + width;
	}

	// Setters & Getters.
	// -----------------
	public void setHeight ( int height ) { this.height = height; }
	public void setWidth ( int width ) { this.width = width; }
	public int getHeight () { return this.height;	}
	public int getWidth () { return this.width; }
}
