package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.CSVTokenizer;

import java.awt.Graphics;
import java.awt.Color;
import java.util.StringTokenizer;

// prototyping.
import java.io.Serializable;

/////////////////////////////////////////////////////////////////
// No need to extend object.  All classes do by default.
public class SimplePicture implements AbstractPicture, Serializable
{
	// Even though this GVX approach works, It might be easier to take care of in DisplayCanvas
	// by funking with this stmt:		g.drawImage(offScreenImage,0,0,mainProgram);
	// static int GX, GY, GVX, GVY;
	static boolean CLIP = false; // rewrite: I could use an interface for ClippedObject
	// rewrite: OK, another thing about clipping.  An object is not a clipped object or not.
	// sometimes it will clip other times not.  For example, an animated graphic only needs
	// to be clipped when it changes.
	protected int locx, locy, width, height, layer;
	protected boolean solid;
	protected String name;
	protected transient DisplayCanvas canvas;	// do not serialize canvas.
	protected Coordinate location;
	protected Coordinate gridCoordinate;

	// parameter order is important for CSVParser.
	public SimplePicture(int x, int y, int w, int h, boolean s, int l, String n, DisplayCanvas canvas)
	{
		locx = x;
		locy = y;
		width = w;
		height = h;
		solid = s;
		layer = l;
		name = n;
		this.canvas = canvas;
		location = new Coordinate (x,y);
		gridCoordinate = new Coordinate();
	}
	// SimplePicture(X=120, 180 20  20  false 1 red                  this
	// SIMPLEPICTURE( X=120, Y=180, WIDTH=20, HEIGHT=20, LAYER=1, SOLID=TRUE, NAME=Door, COLOR=black)

	// XML beginning idea:  If the tag is <AnimatedPicture>,
	// then create an AnimatedPicture Object from the default constructor.
	// Then call AnimatedPicture.initFromXML().  These init from XML methods
	// would call super to that all data would be filled in from the super classes.
	// call the methods toXML and fromXML.
	/*public void initFromXML( String xmlString, SimplePicture pict )
	{
		//if (xmlString.contains("x="))
		{
			//pict.setX( xmlString.getParameter( "x" ); 
		}
		StringTokenizer parameterGrabber = new StringTokenizer( xmlString, "," );
		DebugLog.println("Problem constructing from a String.");
	}*/
	public SimplePicture( CSVTokenizer tokenizer )
	{
		this.locx = tokenizer.getIntAt(1);
		this.locy = tokenizer.getIntAt(2);
		this.width = tokenizer.getIntAt(3);
		this.height = tokenizer.getIntAt(4);
		this.solid = tokenizer.getBooleanAt(5);
		this.layer = tokenizer.getIntAt(6);
		this.name = tokenizer.getStringAt(7);
	}
	public SimplePicture()
	{
		locx = locy = width = height = 0;
		layer = 1;
		solid = false;
		name = "null";
	}
	public void paint (Graphics g)
	{
		this.setColor( g );
		g.fillRect(locx, locy, width, height);
	}
	protected void setColor( Graphics g )
	{
		g.setColor(Color.red);
	}
	public void update()
	{
	}

	// Setters
	// -------
	public void setX( int x) { locx = x; }
	public void setY( int y) { locy = y; }
	public void setName ( String name )	{ this.name = name; }
	public void setLayer(int l) { layer = l; }
	public void setLocation( Coordinate l ) { location = l; }
	public void setSize( Coordinate size ) { width = size.getX(); height = size.getY(); }
	public void setHeight( int height ) { this.height = height; }
	public void setWidth( int width ) { this.width = width; }
	public void setDisplayCanvas( DisplayCanvas c ) { this.canvas = c; }

	// Getters
	// -------
	public boolean isSolid() { return solid; }
	public int getLayer() { return layer; }
	public String getName() { return name; }
	public String toString() { return name; }
	public boolean isDead() { return false; }
	public int getX() { return locx; }
	public int getY() { return locy; }
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public boolean isClipped() { return SimplePicture.CLIP; }
	public Coordinate getLoc() { return new Coordinate( locx, locy); }	// rewrite: Just use x and y.  ( It makes life easier in here.) Make methods and constructors using Coordinate and Rectangle!
	public DisplayCanvas getDisplayCanvas() { return canvas; }
	public Coordinate getSize() { return new Coordinate( width, height ); }
	public Coordinate getGridCoordinate() { return gridCoordinate; } 
	public void setGridCoordinate( Coordinate gridCoordinate ) { this.gridCoordinate = gridCoordinate; } 
}