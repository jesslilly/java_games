package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.ResourceLoader;

import java.awt.Image;
import java.awt.Graphics;

/////////////////////////////////////////////////////////////////
public class Picture extends SimplePicture
{
	protected Image frames[];			// Only 1 for a Picture! no size.  Arrays have one already. (length)
	protected int frame;

	public Picture(int x, int y, int w, int h,  boolean s, int l, String n, Image f[], DisplayCanvas canvas)
	{
		super(x, y, w, h, s, l, n, canvas);

//		frames = f;
		frames = new Image[f.length];
		System.arraycopy(f, 0, frames, 0, f.length );
		frame = 0; // always 0 for this Class.
	}
	public Picture( CSVTokenizer tokenizer )
	{
		// int x, int y, int w, int h,  boolean s, int l,
		// String n, Image f[], DisplayCanvas canvas
		super( tokenizer );
		int beginFrame = 0;
		int endFrame = 0;
		if ( tokenizer.numberOfTokens() > 9 )
		{
			beginFrame = tokenizer.getIntAt(9);
			endFrame = tokenizer.getIntAt(10);
		}
		DebugLog.println( "frames : " + beginFrame + " " + endFrame );
		this.frames = ResourceLoader.getPictures( tokenizer.getStringAt(8), beginFrame, endFrame );
		this.canvas = null;
		frame = 0; // always 0 for this Class.
	}

	public Picture()
	{
		super();
		frame = 0;
		frames = null;
	}

	public Picture clonePicture()
	{
		// rewrite:Unit too.
		return new Picture ( locx, locy, width, height, solid, layer, name, frames, canvas);
	}

	public void paint (Graphics g)
	{
		try
		{
			g.drawImage(frames[frame], locx, locy, width, height, canvas);
		}
		catch ( Exception e )
		{
			DebugLog.println( "Can't Paint - " + e );
		}
	}
	public void setFrames( Image f[] )
	{
		frames = new Image[f.length];
		System.arraycopy(f, 0, frames, 0, f.length );
	}
	//getFrames
	//getFrame
	//setFrame
}