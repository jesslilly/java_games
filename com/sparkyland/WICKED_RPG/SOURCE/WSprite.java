//package v12;
import java.awt.Image;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.applet.Applet;
import java.awt.Color;

/////////////////////////////////////////////////////////////////
public class WSprite extends AnimatedPicture {
	int vx, vy;
	Direction direction;

	public WSprite(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], go wicked, int freq, int vx, int vy)
	{
		super(x, y, w, h, s, l, n, f, wicked, freq);
		direction = new Direction();
		setVelocity( vx, vy );
	}
	public WSprite ()
	{
		super();
		setVelocity( 0, 0 );
	}
	public void update() {
		super.update();

		updateLocation();
		// temporarily warp rectanlge.
		if (locx > 300) {
			locx = -20;
			}
		if (locx < -20) {
			locx = 300;
			}
		if (locy > 260) {
			locy = -20;
			}
		if (locy < -20) {
			locy = 260;
			}
			// warp velocity
			/* 
		if (vx > 300) {
			vx = 300;
			}
		if (vx < -300) {
			vx = -300;
			}
		if (vy > 300) {
			vy = 300;
			}
		if (vy < -300) {
			vy = -300;
			}*/
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.green);
		Rectangle clipRect = getClipRect();
		g.drawRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
	}

	public boolean clicked(int x, int y) { // is the mouse inside this sprite?
		return ((x >= locx) && (x <= (locx + width)) && (y >= locy) && (y <= (locy + height)));
	}
	public void updateLocation()
	{ 
		locx = locx + vx;
		locy = locy + vy;
	}

	public void setVelocity ( int x, int y )
	{
		vx = x;
		vy = y;
		if (vx != 0 && vy != 0)
		{
			direction.setIntDirection(Direction.heading(vx,vy));
		}
	}
	public void setVelocity ( Coordinate vel )
	{
		setVelocity ( vel.getX(), vel.getY() );
	}
	public void setVX ( int x )	{ setVelocity(x, this.vy); }
	public void setVY ( int y )	{ setVelocity(this.vx, y); }
	public int getVX () { return vx; }
	public int getVY () { return vy; }
	public void incrementStep() { step++; }
	public void setDirection( Direction d ) { this.direction = d; }
	public Direction getDirection() { return direction; }
	public boolean isAtASquare()
	{
		return (
			(vx != 0 && (locx % 20 == 0))
			||
			(vy != 0 && (locy % 20 == 0))
			);
	}
	public Rectangle getClipRect()
	{
		Rectangle clipRect = super.getClipRect();

		// Adjust x for velocity.
		if (vx < 0) // Going left
			clipRect.width = clipRect.width - vx;
		else // Going right
		{
			clipRect.x = clipRect.x - vx;
			clipRect.width = clipRect.width + vx;
		}

		// Adjust y for velocity.
		if (vy < 0) // Going up
			clipRect.height = clipRect.height - vy;
		else // Going down
		{
			clipRect.y = clipRect.y - vy;
			clipRect.height = clipRect.height + vy;
		}

		return clipRect;
	}
}