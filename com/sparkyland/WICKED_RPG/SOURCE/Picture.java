import java.awt.Image;
import java.awt.Graphics;
import java.applet.Applet;

/////////////////////////////////////////////////////////////////
public class Picture extends SimplePicture {
	Image frames[];			// Only 1 for a Picture! no size.  Arrays have one already.
	int frame;

	public Picture(int x, int y, int w, int h,  boolean s, int l, String n, Image f[], go wicked)
	{
		super(x, y, w, h, s, l, n, wicked);
		frames = f;
		frame = 0; // always 0 for this Class.
	}
	public Picture()
	{
		super();
		frame = 0;
		frames = null;
	}
	public void paint (Graphics g)
	{
		g.drawImage(frames[frame], locx + GX, locy + GY, wicked);
	}
}