//package v12;

import java.applet.Applet;
import java.net.*;
import java.io.*;
import java.lang.String;

// temporary imports for prtototyping./
import java.util.Vector;
import java.awt.*;

/*----------------------------------------------------------------------------------------------------
The MapReader reads map files.
Basically a big old parser.
It will fill a gameCanvas with objects specified from a file.


BIG idea.
It would be nice to have a single instance of a ... say ... grass object to use over and over.
The problem is that the grass has a locx and locy, so we would need to have the game canvas keep 
track of the loc of this single grass instance.  (grass != marijuana)
Further review:
I think I said this because I wanted grass objects to take up less memory.  The thing is, I think
that the grass is already sharing one common image among all instances.  (Via implicit pointers)  The
image is not copied.  All the objects have a pointer to the image.  Now each one DOES need 
distinct coordinates.  It tracks that already.  OK.

----------------------------------------------------------------------------------------------------*/
class MapReader 
{
	String line, tempString;
	WCanvas gameCanvas;
	go wicked;
	Loader loader;
	Vector prototypes;

	public MapReader(WCanvas c, Loader l, go wicked)
	{
		line = " ";
		tempString = " ";
		gameCanvas = c;
		this.wicked = wicked;
		loader = l;
		prototypes = new Vector();
	}
	public void createPrototypes()
	{
		SimplePicture red = new SimplePicture(0, 0, 20, 20, false, Layer.BOTTOM, "red", wicked);
		FileDialog f = new FileDialog( new Frame(), "Create Prototype", FileDialog.SAVE );
		f.show();
		String fileName = f.getFile();
		if (fileName != null)
		{
			try
			{
				FileOutputStream fos = new FileOutputStream( fileName );
				TextObjectOutputStream out = new TextObjectOutputStream( fos );
				out.writeObject( red );
				out.writeBoolean( true );
				out.writeInt( 200 );
				out.flush();
				out.close();
			}
			catch ( IOException e)
			{
				System.out.println(e);
			}
		}
	}

	// YO THIS TAKES TIME!!!!!!!!!!!!!
	public void loadCanvasFromFile(String fileName)
	{
		try
		{
			URL fileURL = new URL(fileName);
			System.out.println("Open this: " + fileURL);
			BufferedReader in = new BufferedReader(new InputStreamReader( fileURL.openStream() ) );
			//DataInputStream in = new DataInputStream( new FileInputStream( new File(fileURL.toString())) );

			line = "HI";
			while (line != null)
			{
				line = in.readLine();
				if (line == null)
				{
					break;
				}

				if (line.startsWith("#",0))
				{
					// Ignore line.
				}
				else if (line.startsWith("%",0))
				{
					// 0 indicates a FILL.
					System.out.println("GOT A Variable.");
					this.processVariables();
				}
				else
				{
					// Got a good line of data.
					//System.out.println("Good " + line + ".");
					this.processLine();
				}
			}
			in.close();
		} 
		catch(MalformedURLException e){
			System.out.println("URLException:"+e); } 
		catch(IOException e){
			System.out.println("IOException:"+e); }

	}

	// These methods will process a given line of data.
	// ------------------------------------------------

	private void processVariables()
	{
		// System.out.println("+" + line.substring(1,5).trim() + "+");
		String variable = line.substring(1,5).trim();
		if (variable.equals("FILL"))
		{
			// Fill the screen with this stuff.
			// With a fill, you give the coordinates of the upper left
			// and the coordinates of the lower right..
			String fill = getFill().trim();
			//System.out.println("+" + fill + "+");
			if (fill.equals("Picture"))
			{
				int w = getWidth();
				int h = getHeight();
				boolean s = getSolid();
				int l = getLayer();
				String n = getName();
				int frame = getFrame1();
				
				for (int ix = getX(); ix <= w; ix = ix + 20)
				{
					for (int iy = getY(); iy <= h; iy = iy + 20)
					{
						//System.out.println("Fill" + n + ix + iy + w + h);
						gameCanvas.add(new Picture(ix, iy, 20, 20, s, l,
								n, loader.getPictures(frame, frame), wicked ));
					}
				}
			}
			else if (fill.equals("AnimatedPi"))
			{
				int w = getWidth();
				int h = getHeight();
				boolean s = getSolid();
				int l = getLayer();
				String n = getName();
				int fr1 = getFrame1();
				int fr2 = getFrame2();
				int freq = getFrequency();
				
				for (int ix = getX(); ix <= w; ix = ix + 20)
				{
					for (int iy = getY(); iy <= h; iy = iy + 20)
					{
						//System.out.println("Fill" + n + ix + iy + w + h);
						gameCanvas.add(new AnimatedPicture(ix, iy, 20, 20, s, l,
								n, loader.getAnimatedPictures(fr1, fr2), wicked, freq ));
					}
				}
			}
		}
		if (variable.equals("MAP"))
		{
			System.out.println("NextMap#2 is: " + line.substring(18,30));
			gameCanvas.setNextMaps(
				line.substring(5,17).trim(),
				line.substring(18,30).trim(),
				line.substring(31,43).trim(),
				line.substring(44,56).trim());
		}
	}


	private void processLine()
	{
		if (line.startsWith("SimplePicture",0))
		{						
			gameCanvas.add(new SimplePicture(getX(), getY(), 20, 20, getSolid(), getLayer(), getName(), wicked ));
		}
		if (line.startsWith("Picture",0))
		{
			int frame = getFrame1();
			gameCanvas.add(new Picture(getX(), getY(), getWidth(), getHeight(), getSolid(), getLayer(),
					getName(), loader.getPictures(frame, frame), wicked ));
		}
		if (line.startsWith("AnimatedPicture",0))
		{						
			gameCanvas.add(new AnimatedPicture(getX(), getY(), getWidth(), getHeight(), getSolid(), getLayer(),
					getName(), loader.getAnimatedPictures(getFrame1(), getFrame2()), wicked, getFrequency() ));
		}
		if (line.startsWith("WSprite",0))
		{						
			gameCanvas.add(new WSprite(getX(), getY(), getWidth(), getHeight(), getSolid(), getLayer(),
					getName(), loader.getWSprites(getFrame1(), getFrame2()), wicked, getFrequency(),
					getVX(), getVY() ));
		}
		if (line.startsWith("Mortal",0))
		{
			int frame = getFrame1();
			gameCanvas.add(new Monster(getX(), getY(), getWidth(), getHeight(), getSolid(), getLayer(),
					getName(), loader.getMortals(frame, frame), wicked ));
		}
	}
	// The following methods will get variables from the current line.
	//----------------------------------------------------------------
	private int getX()
	{
		int x = 0;
		tempString = line.substring(16,19);
		if (!tempString.startsWith(" "))
			x = Integer.parseInt(tempString.trim());
		else
			System.out.println("Xis defaulted!");
		return x;
	}
	private int getY()
	{
		int y = 0;
		tempString = line.substring(20,23);
		if (!tempString.startsWith(" "))
			y = Integer.parseInt(tempString.trim());
		else
			System.out.println("Y is defaulted!");
		return y;
	}
	private int getWidth()
	{
		int w = 20;
		tempString = line.substring(24,27);
		if (!tempString.startsWith(" "))
			w = Integer.parseInt(tempString.trim());
		else
			System.out.println("W is defaulted!");
		return w;
	}
	private int getHeight()
	{
		int h = 20;
		tempString = line.substring(28,31);
		if (!tempString.startsWith(" "))
			h = Integer.parseInt(tempString.trim());
		else
			System.out.println("h is defaulted!");
		return h;
	}
	private boolean getSolid()
	{
		boolean solid = false;
		tempString = line.substring(32,37);
		if (!tempString.startsWith(" "))
		{
			if (tempString.trim().equals("true")) // for some reason == didn't work here.
				solid = true;
		}
		else
			System.out.println("solid is defaulted!");
		return solid;
	}
	private int getLayer()
	{
		int l = 1;
		tempString = line.substring(38,39);
		if (!tempString.startsWith(" "))
			l = Integer.parseInt(tempString.trim());
		else
			System.out.println("layer is defaulted!");
		return l;
	}
	private String getName()
	{
		String name = "none";
		tempString = line.substring(40,52);
		if (!tempString.startsWith(" "))
			name = tempString;
		else
			System.out.println("name is defaulted!");
		return name;
	}
	private int getFrame1()
	{
		int fr1 = 0;
		tempString = line.substring(53,56);
		if (!tempString.startsWith(" "))
			fr1 = Integer.parseInt(tempString.trim());
		else
			System.out.println("fr1 is defaulted!");
		return fr1;
	}
	private int getFrame2()
	{
		int fr2 = 0;
		tempString = line.substring(57,60);
		if (!tempString.startsWith(" "))
			fr2 = Integer.parseInt(tempString.trim());
		else
			System.out.println("fr2 is defaulted!");
		return fr2;
	}
	private int getFrequency()
	{
		int fre = 1;
		tempString = line.substring(66,69);
		if (!tempString.startsWith(" "))
			fre = Integer.parseInt(tempString.trim());
		else
			System.out.println("fre is defaulted!");
		return fre;
	}
	private int getVX()
	{
		int vx = 0;
		tempString = line.substring(70,73);
		if (!tempString.startsWith(" "))
			vx = Integer.parseInt(tempString.trim());
		else
			System.out.println("vx is defaulted!");
		return vx;
	}
	private int getVY()
	{
		int vy = 0;
		tempString = line.substring(74,77);
		if (!tempString.startsWith(" "))
			vy = Integer.parseInt(tempString.trim());
		else
			System.out.println("vy is defaulted!");
		return vy;
	}
	private String getFill()
	{
		String fill = " ";
		tempString = line.substring(5,15);
		if (!tempString.startsWith(" "))
			fill = tempString.trim();
		else
			System.out.println("There is no filll type!");
		return fill;
	}
}


