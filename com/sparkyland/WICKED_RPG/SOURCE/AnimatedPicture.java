//package v12;
import java.awt.Image;
import java.awt.Graphics;
import java.applet.Applet;
import java.awt.Rectangle;

/////////////////////////////////////////////////////////////////
public class AnimatedPicture extends Picture {
	boolean animating;
	int frequency, step, lifeSpan, life;
	static boolean CLIP = true;

	public AnimatedPicture(int x, int y, int w, int h, boolean s, int l, String n, Image f[], go wicked, int freq) 
	{
		super(x,y,w,h,s,l,n,f,wicked);
		frequency = freq;
		animating = true;
		step = 0;
		lifeSpan = 0;
		life = 0;
	}
	public AnimatedPicture() 
	{
		super();
		frequency = step = lifeSpan = life = 0;
		animating = false;
	}

	public void update() 
	{
		if (lifeSpan > 0)
		{
			life++;
			if (life >= lifeSpan)
			{
				wicked.getGameCanvas().remove(this);
				life = 0;
			}
		}
		if (animating = true
		&& frequency > 0)
		{
			// Check the value of step first thing, so we dont get a step of 26
			// divided by a frequency of 1 when frequency gets changed from a 
			// high number to a low one.
			if (step >= (frequency * frames.length))
			{
				step = 0;
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
		// Allow null images for blink affect.
		if (frames[frame] != null)
		{
			super.paint(g);
		}
	}
	public void paint (Graphics g, int frameNumber)
	{
		g.drawImage(frames[frameNumber], locx + GX, locy + GY, wicked);
	}

	// Setters and Getters
	// -------------------------------------
	public void setFrequency(int freq) {
		if ((freq <= 0) || (freq >= 10))
			frequency = 1;
		else 
			frequency = freq;
	}
	public int getLifeSpan () { return lifeSpan; }
	public void setLifeSpan ( int l ) { lifeSpan = l; }
	public int getStep () { return step; }
	public void setStep ( int s ) { step = s; }
	public int getLife () { return life; }
	public void setLife ( int l ) { life = l; }
	public int getFrequency () { return frequency; }
	public boolean isClipped() { return AnimatedPicture.CLIP; }
	public Rectangle getClipRect()
	{
		return new Rectangle ( locx, locy, width -1, height -1);
	}
}