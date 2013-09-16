package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.SpinVector;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.military.Objective;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.Layer;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.gui.*;

import java.applet.Applet;
import java.net.*;
import java.io.*;
import java.lang.String;

// temporary imports for prtototyping./
import java.util.Vector;
import java.util.StringTokenizer;
import java.awt.*;
import java.net.URL;

/*----------------------------------------------------------------------------------------------------
The CSVCanvasLoader reads csv map files.
Basically a big old parser.
It will fill a canvas with objects specified from a file.


BIG idea.
It would be nice to have a single instance of a ... say ... grass object to use over and over.
The problem is that the grass has a locx and locy, so we would need to have the game canvas keep 
track of the loc of this single grass instance.  (grass != marijuana)
Further review:
I think I said this because I wanted grass objects to take up less memory.  The thing is, I think
that the grass is already sharing one common image among all instances.  (Via implicit pointers)  The
image is not copied.  All the objects have a pointer to the image.  Now each one DOES need 
distinct coordinates.  It tracks that already.  OK.

// rewrite: amke this a static class like resourceloader.
----------------------------------------------------------------------------------------------------*/
public class CSVCanvasLoader 
{
	// Temporary work variables:
	protected static String TEMPSTRING;
	protected static Vector PROTOTYPES;

	// Member variables:
	protected static URL PWD;
	protected static CSVTokenizer TOKENIZER;
	protected static Applet APPLET;

	// rewrite
	public static final int UNIT_SIZE = 36;

	// static initializer.
	static
	{
		TOKENIZER = new CSVTokenizer();
	}

	public static void init(Applet applet, URL pwd)
	{
		DebugLog.println( "CSVCanvasLoader.init():" + applet );
		CSVCanvasLoader.APPLET = applet;
		CSVCanvasLoader.PWD = pwd;
	}


	// YO THIS TAKES TIME!!!!!!!!!!!!!
	public static void loadCanvasFromFile(DisplayCanvas canvas, String fileName)
	{

		try
		{
			URL fileURL = new URL( PWD, "data/" + fileName );
			DebugLog.println("Open this: " + fileURL);
			BufferedReader in = new BufferedReader(new InputStreamReader( fileURL.openStream() ) );

		// rewrite: use ResourceLoader.getFileContents(), then process the vector.
			String line = "Initialized";
			while (line != null)
			{
				line = in.readLine();
				if (line == null)
				{
					break;
				}

				if (line.startsWith("#",0))
				{
					// Ignore LINE.
				}
				else if (line.startsWith("%",0))
				{
					// % Variable
					DebugLog.println("GOT A Variable.");
					CSVCanvasLoader.processVariables( canvas, line );
				}
				else
				{
					// Got a good LINE of data.
					//DebugLog.println("Good " + LINE + ".");
					CSVCanvasLoader.processLine( canvas, line );
				}
			}
			in.close();
		} 
		catch(MalformedURLException e){
			DebugLog.println("URLException:"+e); } 
		catch(IOException e){
			DebugLog.println("IOException:"+e); }

	}

	// These methods will process a given LINE of data.
	// ------------------------------------------------

	protected static void processVariables( DisplayCanvas canvas, String line )
	{
		TOKENIZER.tokenize( line );
		String variable = TOKENIZER.getStringAt(0);
		TOKENIZER.removeTokenAt(0);

		DebugLog.println("==+" + variable + "+");
		if (variable.equals("%FILL"))
		{

			String fill = TOKENIZER.getStringAt(0);

			// rewrite: maybe there is a cooler OO way to do this.
			Picture prototype = new Picture();
			if (fill.equals("Picture"))
			{
				prototype = new Picture( TOKENIZER );
			}
			else if (fill.equals("AnimatedPicture"))
			{
				prototype = new AnimatedPicture( TOKENIZER );
			}
			else if (fill.equals("Terrain"))
			{
				prototype = new Terrain( TOKENIZER );
			}

			int startX = prototype.getX();
			int endX = prototype.getWidth();
			int startY = prototype.getY();
			int endY = prototype.getHeight();

			for (int ix = startX; ix <= endX; ix = ix + UNIT_SIZE)
			{
				for (int iy = startY; iy <= endY; iy = iy + UNIT_SIZE)
				{
					
					Picture pict2 = new Picture();

					// rewrite: maybe there is a cooler OO way to do this.
					if (fill.equals("Picture"))
					{
						pict2 = prototype.clonePicture();
					}
					else if (fill.equals("AnimatedPicture"))
					{
						pict2 = ((AnimatedPicture)prototype).cloneAnimatedPicture();
					}
					else if (fill.equals("Terrain"))
					{
						pict2 = ((Terrain)prototype).cloneTerrain();
					}

					pict2.setX(ix);
					pict2.setY(iy);
					pict2.setWidth(UNIT_SIZE);
					pict2.setHeight(UNIT_SIZE);
					//DebugLog.println("Fill" + n + ix + iy + w + h);
					canvas.add(pict2);
				}
			}
		}
	}


	protected static void processLine( DisplayCanvas canvas, String line )
	{
		TOKENIZER.tokenize( line );

		if (TOKENIZER.getStringAt(0).equals("Music"))
		{
			DebugLog.println( "CSVCanvasLoader load and loop " + TOKENIZER.getStringAt(1) );
			ResourceLoader.loadAndLoopMusic( TOKENIZER.getStringAt(1) );
		}
		else if (TOKENIZER.getStringAt(0).equals("IncludeCSVFile"))
		{
			DebugLog.println( "CSVCanvasLoader load this file too " + TOKENIZER.getStringAt(1) );
			CSVCanvasLoader.loadCanvasFromFile(canvas, TOKENIZER.getStringAt(1));
		}
		else if (TOKENIZER.getStringAt(0).equals("ColorBox"))
		{
			canvas.add( new ColorBox( TOKENIZER ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("Picture"))
		{
			canvas.add( new Picture( TOKENIZER ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("WLabel"))
		{
			canvas.add( new WLabel( TOKENIZER, canvas ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("WButton"))
		{
			canvas.add( new WButton( TOKENIZER, canvas ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("AnimatedPicture"))
		{
			canvas.add( new AnimatedPicture( TOKENIZER ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("Terrain"))
		{
			canvas.add( new Terrain( TOKENIZER ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("Sprite"))
		{
			canvas.add( new Sprite( TOKENIZER ) );
		}
		else if (TOKENIZER.getStringAt(0).equals("BoundedSprite"))
		{
			BoundedSprite sprite = new BoundedSprite( TOKENIZER );
			Rectangle rect = new Rectangle();
			rect.x = TOKENIZER.getIntAt(14);
			rect.y = TOKENIZER.getIntAt(15);
			rect.width = TOKENIZER.getIntAt(16);
			rect.height = TOKENIZER.getIntAt(17);
			sprite.setBounded( true );
			sprite.setBounds( rect );
			sprite.setReaction( TOKENIZER.getIntAt(18) );
			canvas.add( sprite );
		}
		// rewrite: perhaps another way to implement this is to have an
		// action to remove a picture and then add another one?
		else if (TOKENIZER.getStringAt(0).equals("WMultipleChoice"))
		{
			canvas.add( new WMultipleChoice( TOKENIZER, canvas ) );
		}
		else
		{
			DebugLog.println( "Unknown action " + TOKENIZER.getStringAt(0) );
		}
	}
}


