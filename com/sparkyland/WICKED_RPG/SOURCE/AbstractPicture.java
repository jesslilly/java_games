
import java.awt.Graphics;

// This is an interface to WCanvas.
interface AbstractPicture 
{
	void paint(Graphics g);
	void update();
	int getLayer();
	String getName();
	int getX();
	int getY();
	boolean getSolid();
	boolean isClipped();
}
