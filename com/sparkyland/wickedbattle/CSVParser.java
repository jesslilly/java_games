package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.SpinVector;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.military.Objective;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.Layer;
import com.sparkyland.spartique.physical.Coordinate;

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
The CSVParser reads map files.
Basically a big old parser.
It will fill a WPCanvas with objects specified from a file.


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
public class CSVParser 
{
	protected static String line, tempString;
	protected static WickedBattleCanvas WPCanvas;
	protected static Applet applet;
	protected static Vector prototypes;
	protected static SpinVector scenarios;
	protected static CSVTokenizer tokenizer;
	protected static URL pwd;

	// rewrite
	public static final int UNIT_SIZE = 36;

	// static initializer.
	static
	{
		scenarios = new SpinVector();
		tokenizer = new CSVTokenizer();
	}

	public static void init(Applet applet, URL pwd)
	{
		DebugLog.println( "CSVParser.init():" + applet );
		CSVParser.applet = (Applet)applet;
		CSVParser.pwd = pwd;
	}

	public static SpinVector getScenarios() { return CSVParser.scenarios; }

	public static SpinVector loadScenarios()
	{

		try
		{
			URL fileURL = new URL( pwd, "data/scenarios.csv");
			DebugLog.println("Open this: " + fileURL);
			BufferedReader in = new BufferedReader( new InputStreamReader( fileURL.openStream() ) );

			String line;
			Scenario scenario;
			while ( true )
			{
				line = in.readLine();
				
				DebugLog.println("Process line -|-: " + line );
				if (line == null)
					break;

				if (line.startsWith("#",0))
				{
					// Ignore line.
				}
				else
				{
					
					tokenizer.tokenize( line );
					if (tokenizer.getStringAt(0).equals("Scenario"))
					{
						scenario = new Scenario( tokenizer, applet );
						if ( scenario instanceof Scenario )
						{
							scenarios.addElement( scenario ); 
						}
					}
					else if (tokenizer.getStringAt(0).equals("UnitPrototype"))
					{
						// UnitPrototype,1,0,infantry,1,1,4,4,2
						DebugLog.println( "UnitPrototype - " + tokenizer );
						WPArmy.setUnitPrototype( tokenizer, applet );
					}
				}
			}
			in.close();
		} 
		catch(MalformedURLException e)
		{
			DebugLog.println("URLException:"+e);
		} 
		catch(IOException e)
		{
			DebugLog.println("IOException:"+e);
		}
		return getScenarios();
	}

	// YO THIS TAKES TIME!!!!!!!!!!!!!
	public static void loadCanvasFromFile(WickedBattleCanvas WPCanvas, String fileName)
	{
		CSVParser.WPCanvas = WPCanvas;

		try
		{
			URL fileURL = new URL( pwd, "data/" + fileName );
			DebugLog.println("Open this: " + fileURL);
			BufferedReader in = new BufferedReader(new InputStreamReader( fileURL.openStream() ) );

			line = "Initialized";
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
					// % Variable
					DebugLog.println("GOT A Variable.");
					CSVParser.processVariables();
				}
				else
				{
					// Got a good line of data.
					//DebugLog.println("Good " + line + ".");
					CSVParser.processLine();
				}
			}
			in.close();
		} 
		catch(MalformedURLException e){
			DebugLog.println("URLException:"+e); } 
		catch(IOException e){
			DebugLog.println("IOException:"+e); }

	}

	// These methods will process a given line of data.
	// ------------------------------------------------

	private static void processVariables()
	{
		tokenizer.tokenize( line );
		String variable = tokenizer.getStringAt(0);
		tokenizer.removeTokenAt(0);

		DebugLog.println("==+" + variable + "+");
		if (variable.equals("%FILL"))
		{

			String fill = fill = tokenizer.getStringAt(0);

			// rewrite: maybe there is a cooler OO way to do this.
			Picture prototype = new Picture();
			if (fill.equals("Picture"))
			{
				prototype = new Picture( tokenizer );
			}
			else if (fill.equals("AnimatedPicture"))
			{
				prototype = new AnimatedPicture( tokenizer );
			}
			else if (fill.equals("Terrain"))
			{
				prototype = new Terrain( tokenizer );
			}

			int startX = prototype.getX();
			int endX = prototype.getWidth();
			int startY = prototype.getY();
			int endY = prototype.getHeight();

			for (int ix = startX; ix <= endX; ix = ix + UNIT_SIZE)
			{
				for (int iy = startY; iy <= endY; iy = iy + UNIT_SIZE)
				{
					
					Picture pict2 = new Picture();;

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
					WPCanvas.add(pict2);
				}
			}
		}
	}


	private static void processLine()
	{
		tokenizer.tokenize( line );

		if (tokenizer.getStringAt(0).equals("Music"))
		{
			DebugLog.println( "CSVParser load and loop " + tokenizer.getStringAt(1) );
			ResourceLoader.loadAndLoopMusic( tokenizer.getStringAt(1) );
		}
		else if (tokenizer.getStringAt(0).equals("Picture"))
		{
			WPCanvas.add( new Picture( tokenizer ) );
		}
		else if (tokenizer.getStringAt(0).equals("AnimatedPicture"))
		{
			WPCanvas.add( new AnimatedPicture( tokenizer ) );
		}
		else if (tokenizer.getStringAt(0).equals("Terrain"))
		{
			WPCanvas.add( new Terrain( tokenizer ) );
		}
		else if (tokenizer.getStringAt(0).equals("Sprite"))
		{
			WPCanvas.add( new Sprite( tokenizer ) );
		}
		else if (tokenizer.getStringAt(0).equals("Objective"))
		{
			Objective obj = new Objective( tokenizer );
			WPArmy wpa = (WPArmy)WPCanvas.getBattle().getArmy( tokenizer.getIntAt(4) );
			wpa.setObjective( obj );
		}
		else if (tokenizer.getStringAt(0).equals("Unit"))
		{
			// Unit,108,324,1,0,false,5,infantry,30
			// record,x,y,player #,unit #,selected,AI,name,HP
			WPArmy wpArmy = (WPArmy)WPCanvas.getBattle().getArmy( tokenizer.getIntAt(3) );
			Unit newUnit = wpArmy.makeUnit( tokenizer.getIntAt(4) );
			newUnit.init( tokenizer );
			WPCanvas.add( newUnit );
		}/*
		else if (tokenizer.getStringAt(0).equals("UnitPrototype"))
		{
			// UnitPrototype,1,0,infantry,1,1,4,4,2
			DebugLog.println( "UnitPrototype - " + tokenizer );
			WPArmy wpArmy = (WPArmy)WPCanvas.getBattle().getArmy( tokenizer.getIntAt(1) );
			wpArmy.setUnitPrototype( tokenizer );
		}*/
	}
	// The following methods will get variables from the current line.
	//----------------------------------------------------------------
	private static int getX()
	{
		int x = 0;
		tempString = line.substring(16,19);
		if (!tempString.startsWith(" "))
			x = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("Xis defaulted!");
		return x;
	}
	private static int getY()
	{
		int y = 0;
		tempString = line.substring(20,23);
		if (!tempString.startsWith(" "))
			y = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("Y is defaulted!");
		return y;
	}
	private static int getWidth()
	{
		int w = UNIT_SIZE;
		tempString = line.substring(24,27);
		if (!tempString.startsWith(" "))
			w = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("W is defaulted!");
		return w;
	}
	private static int getHeight()
	{
		int h = UNIT_SIZE;
		tempString = line.substring(28,31);
		if (!tempString.startsWith(" "))
			h = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("h is defaulted!");
		return h;
	}
	private static boolean getSolid()
	{
		boolean solid = false;
		tempString = line.substring(32,37);
		if (!tempString.startsWith(" "))
		{
			if (tempString.trim().equals("true"))
				solid = true;
		}
		else
			DebugLog.println("solid is defaulted!");
		return solid;
	}
	private static int getLayer()
	{
		int l = 1;
		tempString = line.substring(38,39);
		if (!tempString.startsWith(" "))
			l = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("layer is defaulted!");
		return l;
	}
	private static String getName()
	{
		String name = "none";
		tempString = line.substring(40,52);
		if (!tempString.startsWith(" "))
			name = tempString;
		else
			DebugLog.println("name is defaulted!");
		return name;
	}
	private static int getFrame1()
	{
		int fr1 = 0;
		tempString = line.substring(53,56);
		if (!tempString.startsWith(" "))
			fr1 = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("fr1 is defaulted!");
		return fr1;
	}
	private static int getFrame2()
	{
		int fr2 = 0;
		tempString = line.substring(57,60);
		if (!tempString.startsWith(" "))
			fr2 = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("fr2 is defaulted!");
		return fr2;
	}
	private static int getFrequency()
	{
		int fre = 1;
		tempString = line.substring(66,69);
		if (!tempString.startsWith(" "))
			fre = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("fre is defaulted!");
		return fre;
	}
	private static int getVX()
	{
		int vx = 0;
		tempString = line.substring(70,73);
		if (!tempString.startsWith(" "))
			vx = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("vx is defaulted!");
		return vx;
	}
	private static double getDefenseMod()
	{
		double mod = 1.0;
		tempString = line.substring(70,73);
		if (!tempString.startsWith(" "))
			mod = ( new Double (tempString.trim() ) ).doubleValue();
		else
			DebugLog.println("mod is defaulted!");
		return mod;
	}
	private static int getVY()
	{
		int vy = 0;
		tempString = line.substring(74,77);
		if (!tempString.startsWith(" "))
			vy = Integer.parseInt(tempString.trim());
		else
			DebugLog.println("vy is defaulted!");
		return vy;
	}
	private static String getFill()
	{
		String fill = " ";
		tempString = line.substring(5,15);
		if (!tempString.startsWith(" "))
			fill = tempString.trim();
		else
			DebugLog.println("There is no fill type!");
		return fill;
	}
}


