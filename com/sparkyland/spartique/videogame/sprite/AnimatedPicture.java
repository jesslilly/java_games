package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.DebugLog;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Rectangle;

/////////////////////////////////////////////////////////////////
public class AnimatedPicture extends Picture {
	protected boolean animating, animateOnce;
	protected int frequency, step, life;
	static boolean CLIP = true;

	public AnimatedPicture(int x, int y, int w, int h, boolean s, int l, String n, Image f[], DisplayCanvas canvas, int freq) 
	{
		super(x,y,w,h,s,l,n,f,canvas);
		frequency = freq;
		animating = true;
		animateOnce = false;
		step = 0;
		life = 0;
	}
	public AnimatedPicture() 
	{
		super();
		frequency = step = 0;
		life = 0;
		animating = false;
	}
	public AnimatedPicture( CSVTokenizer tokenizer )
	{
		super( tokenizer );
		this.frequency = tokenizer.getIntAt(11);
		DebugLog.println( "frequency" + frequency );
		animating = true;
		step = 0;
		life = 0;
	}
	// rewrite: this needs to be rewrit in a major way.
	public AnimatedPicture cloneAnimatedPicture()
	{
		return new AnimatedPicture ( locx, locy, width, height, solid, layer, name, frames, canvas, frequency);
	}
	public void update() 
	{

		if (life > 0)
		{
			life--;
			if ( this.isLifeDone() )
				canvas.remove( this );
		}
		if ( ( animating || animateOnce )
		&& frequency > 0)
		{
			// Check the value of step first thing, so we dont get a step of 26
			// divided by a frequency of 1 when frequency gets changed from a 
			// high number to a low one.
			if (step >= (frequency * frames.length))
			{
				step = 0;
				animateOnce = false;
			}
			frame = (int) ( step / frequency );  // truncated
			step++;
		}
		else 
		{
			frame = 0;
		}
	}
	public void paint (Graphics g)
	{
		// Allow null images for blink effect.
		// rewrite: push this down, so a null image does not throw an exception for any picture.
		if (frames[frame] != null)
		{
			super.paint(g);
		}
	}
	public void paint (Graphics g, int frameNumber)
	{
		g.drawImage(frames[frameNumber], locx, locy, canvas);
	}
	public void animateOnce() { this.animateOnce = true; }

	// Setters and Getters
	// -------------------------------------
	public void setFrequency(int freq) {
		if ((freq <= 0) || (freq >= 10))
			frequency = 1;
		else 
			frequency = freq;
	}
	public void setAnimating( boolean a ) { this.animating = a; }
	public boolean isLifeDone ()	{ return (life <= 0);	}
	public int getStep () { return step; }
	public void setStep ( int s ) { step = s; }
	public void incrementStep() { step++; }
	public int getLife () { return life; }
	public void setLife ( int l ) { life = l; }
	//public void setFrame ( int f ) { this.frame = f; /* rewrite: check frame first. */};
	public int getFrequency () { return frequency; }
	public boolean isClipped() { return AnimatedPicture.CLIP; }
	public Rectangle getClipRect()
	{
		return new Rectangle ( locx, locy, width -1, height -1);
	}
}