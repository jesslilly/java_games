package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.physical.Layer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.applet.Applet;
import java.util.Vector;
// DisplayCanvas is intended for adding sprites and stuff, but no user input.

public class DisplayCanvas extends Canvas implements Runnable, SpriteHolder
{
	private static final int REFRESH_SLEEP = 50;    // in ms 20fps.	Refresh Rate.
	Thread animation;
	boolean suspended;

// I would like to support things besides applets.  Maybe an applet can have a main...
	protected Applet mainProgram;
	// rewrite: make infinity layers.  We could have a sorted vector and every element
	// would esentially have a different layer because of the painting order.
	// Or we could have a vector of vectors.  Main vector=layer, sub vector=picture.
	protected Vector layer1, layer2, layer3;
	protected Graphics offScreenG; // 2buf
	protected Image offScreenImage; // 2buf
	protected byte timer;

	public DisplayCanvas(Applet mainProgram, Dimension size)
	{
		super();
		this.setSize( size );

		this.mainProgram = mainProgram;
		suspended = false;
		timer = 0;
		layer1 = new Vector();
		layer2 = new Vector();
		layer3 = new Vector();

		offScreenImage = mainProgram.createImage( getSize().width, getSize().height ); // 2buf make offscreen buffer
		offScreenG = offScreenImage.getGraphics(); // 2buf

	}
/*3/18/2002
	public void init()
	{
		DebugLog.println("Does dispcanvas.init even get called?");
	}
	*/

	public void start()
	{
		animation = new Thread(this);
		animation.setName("animation");
		if (animation != null)
		{
			animation.start();   // run() method starts now.  Body of thread.
		}
	}

	public void setPaused( boolean pause )
	{
		setSuspended( pause );
	}

	// rewrite.
	public void run() 
	{ 
		Thread thisThread = Thread.currentThread();
		while (animation == thisThread)  // threadAPI
		{ // universal animation loop.  paint-update-sleep.
			repaint();
			try 
			{
				animation.sleep (REFRESH_SLEEP); // start threadAPI
				if (suspended == true)
				{
					synchronized(this)
					{
						while (suspended && animation == thisThread)
							//DebugLog.println("Animation in busy sleep loop waiting on:" + Thread.currentThread());
							animation.sleep ( 500 ); // half a second.

							// With java 1.0 event API, wait() worked.
							// Now, with 1.1 API, the EventQueue thread is not allowed to notify animation.
							//wait();
					}// stop threadAPI
				}
			}
			catch (Exception exc)
			{
			};
		}
	}

    public void paint(Graphics g)
	{

		this.clearScreen(offScreenG);
		int i;
		for (i = 0; i < (layer1.size()); i++) {
			((AbstractPicture)layer1.elementAt(i)).paint(offScreenG);
		}
		for (i = 0; i < (layer2.size()); i++) {
			((AbstractPicture)layer2.elementAt(i)).paint(offScreenG);
		}
		for (i = 0; i < (layer3.size()); i++) {
			((AbstractPicture)layer3.elementAt(i)).paint(offScreenG);
		}

		// Create a clipped structure to hold all objects that get clipped.
		// Loop through them here and use drawImage(im,0,0,getclip,getclip,this);
		//2/19/2002g.drawImage(offScreenImage,0,0,mainProgram);
		g.drawImage(offScreenImage,0,0,this);
    }

	private void clearScreen( Graphics graphics )
	{
		// You may not want to clearScreen when clipping is implemented.
		// You can use the old screen over again.
		graphics.setColor(Color.black);
		graphics.fillRect(0,0,getSize().width, getSize().height );  // clear buffer
	}

	// Called implicitly with DisplayCanvas.repaint().
    public void update(Graphics g)
	{
		if ( timer < 99 )
			timer++;
		else
			timer = 0;

		// Update all of the little guys.
		int i;
		for (i = 0; i < layer1.size(); i++) {
			((AbstractPicture)layer1.elementAt(i)).update();
		}
		for (i = 0; i < layer2.size(); i++) {
			((AbstractPicture)layer2.elementAt(i)).update();
		}
		for (i = 0; i < layer3.size(); i++) {
			((AbstractPicture)layer3.elementAt(i)).update();
		}

		paint(g);
	}
	
	public Image getMapImage()
	{
		ImageProducer producer = offScreenImage.getSource();
		Image temp = createImage(producer);
		return temp;
	}


	// Add methods.
	// Only add Objects that know how to paint and update themselves.
	// Add at a certain layer please.  layers 1, 2, 3.  Including background.
	// Object's added should have a unique name. Checking is too much work.
	// Add objects of the same name, but if you try to get them,
	// you will always get the first object added at the lowest layer.

	// There is also nothing to prevent you from adding 2 objects at the same x,y
	// and the same layer. (BUT YOU SHOULDN'T !!!!!)
	//-----------------------------------------------------------
    public void add(AbstractPicture pict) {
//		addToClipTracker( pict );
		switch (pict.getLayer()) {
		case 1:
			layer1.addElement(pict);
			break;
		case 2:
			layer2.addElement(pict);
			break;
		case 3:
			layer3.addElement(pict);
			break;
		}
    }	
	// Remove methods. 
	//-----------------------------------------------------------

    public void remove(AbstractPicture pict)
	{
		if ( pict != null )
			{
			switch (pict.getLayer()) {
			case 1:
				layer1.removeElement(pict);
				break;
			case 2:
				layer2.removeElement(pict);
				break;
			case 3:
				layer3.removeElement(pict);
				break;
			}
		}
	}

    public void removeAll()
	{
		while ( 0 < layer1.size() ) {
			layer1.removeElementAt(0);
		}
		while ( 0 < layer2.size() ) {
			layer2.removeElementAt(0);
		}
		while ( 0 < layer3.size() ) {
			layer3.removeElementAt(0);
		}
    }
	// Get Methods
	//------------------------------------
	public AbstractPicture get( String name )
	{
		// Check for name in layer2 first.
		int i;
		for (i = 0; i < layer2.size(); i++) {
			if ( name.equals(((AbstractPicture)layer2.elementAt(i)).getName()) ) {
				return (AbstractPicture)layer2.elementAt(i);
			}
		}
		for (i = 0; i < layer3.size(); i++) {
			if ( name.equals(((AbstractPicture)layer3.elementAt(i)).getName()) ) {
				return (AbstractPicture)layer3.elementAt(i);
			}
		}
		for (i = 0; i < layer1.size(); i++) {
			if ( name.equals(((AbstractPicture)layer1.elementAt(i)).getName()) ) {
				return (AbstractPicture)layer1.elementAt(i);
			}
		}
		DebugLog.println(name + " isn't in here.");
		return null;
	}

	public AbstractPicture getPictAt( Coordinate xy, int l )
	{
		// rewrite: This code is crap, you should allow different layers.
		return getPictAt( xy.getX(), xy.getY(), Layer.MIDDLE );
	}

	public AbstractPicture getPictAt( int x, int y, int l )
	{
		int i;
		// rewrite: Eliminate dupe code with an array of vectors.  layer[0].elementAt(i), etc.
		// Or- One sorted vector.  Everything is draw one at a time anyway.
		// Eeeeew, this code looks really slow too.
		switch (l) {
		case 1:		
			for (i = 0; i < layer1.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer1.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		case 2:
			for (i = 0; i < layer2.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer2.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		case 3:
			for (i = 0; i < layer3.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer3.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		default:
			DebugLog.println("Nice try jerk!  Use a real layer.");
		}
		return null;
	}
	// Must call with layer 1->3
	public boolean isSolidAt(int locx, int locy, int layer)
	{

		boolean solid = false;
		AbstractPicture pict = getPictAt( locx, locy, layer );
		if (pict != null)
		{
			solid = pict.isSolid();
		}
		//DebugLog.println("solid at layer" + layer + " at " + (locx) + " " + (locy) + " = " + solid);
		return solid;

	}
	public Graphics getOffScreenG()
	{
		return this.offScreenG;
	}

// --------------- Clipping -----------------------------------------
	private void addToClipTracker ( AbstractPicture pict )
	{
		if (pict.isClipped())
		{
			DebugLog.println("Add " + pict.getName() + " to the clipTrakcer.");
		}
	}

	// Getters

	// Component: public Dimension getSize()
	// Component: public void setSize( Dimension d )
	public boolean isPaused() { return suspended; }
	public Applet getApplet() { return mainProgram; }
	public static int getRefreshPause() { return REFRESH_SLEEP; }

	public boolean isSuspended() { return suspended; }
	public void setSuspended(boolean suspended) { this.suspended = suspended; }


}





