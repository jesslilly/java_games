//package v12;
import java.awt.Graphics;
import java.applet.Applet;
import java.awt.Color;
import java.util.StringTokenizer;

// prototyping.
import java.io.Serializable;

/////////////////////////////////////////////////////////////////
// Maybe it could extend Object some day?  Perhaps.  I'm not sure why.
// It might help, cause things retruned from an array or a vector are objects.  Then I don't have to cast.
public class SimplePicture implements AbstractPicture, Serializable {
	static int GX, GY, GVX, GVY;
	static boolean CLIP = false;
	protected int locx, locy, width, height, layer;
	protected boolean solid;
	protected String name;
	protected transient go wicked;	// do not serialize wicked.
	protected Coordinate location;

	// parameter order is important for MapReader.
	public SimplePicture(int x, int y, int w, int h, boolean s, int l, String n, go wicked) {
		locx = x;
		locy = y;
		width = w;
		height = h;
		solid = s;
		layer = l;
		name = n;
		this.wicked = wicked;
		location = new Coordinate (x,y);
	}
	// SimplePicture(X=120, 180 20  20  false 1 red                  this
	// SIMPLEPICTURE( X=120, Y=180, WIDTH=20, HEIGHT=20, LAYER=1, SOLID=TRUE, NAME=Door, COLOR=black)
	public SimplePicture( String parameters, go wicked )
	{
		StringTokenizer parameterGrabber = new StringTokenizer( parameters, "," );
		System.out.println("Problem constructing from a String.");
	}
	public SimplePicture() {
		locx = locy = width = height = 0;
		layer = 1;
		solid = false;
		name = "null";
	}
	public void paint (Graphics g) {
		g.setColor(Color.red);
		g.fillRect(locx + GX, locy + GY, width, height);
	}
	public void update() {
	}
	static void updateGlobals ()
	{
		GX = GX + GVX;
		GY = GY + GVY;
	}

	// Setters
	// -------
	public void setX( int x) { locx = x; }
	public void setY( int y) { locy = y; }
	static void setGX(int x) { GX = x; }
	static void setGY(int y) { GY = y; }
	static void setGVX(int gvx) { GVX = gvx; }
	static void setGVY(int gvy) { GVY = gvy; }
	public void setName ( String name )	{ this.name = name; }
	public void setLayer(int l) { layer = l; }
	public void setLocation( Coordinate l ) { location = l; }

	// Getters
	// -------
	public boolean getSolid() { return solid; }
	public int getLayer() { return layer; }
	public String getName() { return name; }
	public boolean isDead() { return false; }
	public int getX() { return locx; }
	public int getY() { return locy; }
	static int getGVX() { return GVX; }
	static int getGVY() { return GVY; }
	static int getGX() { return GX; }
	static int getGY() { return GY; }
	public boolean isClipped() { return SimplePicture.CLIP; }
	public Coordinate getLocation() { return location; }
	public go getWicked() { return wicked; }
}