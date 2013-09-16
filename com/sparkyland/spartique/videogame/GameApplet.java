package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.physical.Size;
import com.sparkyland.spartique.videogame.sprite.Picture;
import com.sparkyland.spartique.gui.WButton;

import java.applet.*;
import java.awt.*;
import java.io.*;
// Properties Objects can only be loaded from the applet file system, not the server.
//import java.util.Properties;
import java.net.URL;
import java.net.MalformedURLException;

/**----------------------------------------------------------------------------
@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public abstract class GameApplet extends Applet
{
	protected URL pwd;

	public void init()
	{

		// First thing.  Get the Applet parameter for debug.
		String debug =  this.getParameter( "debug" );
		DebugLog.out.println( "Debug: " + debug );
		DebugLog.setDebug( debug );

		DebugLog.println("init()");

		String temp = getCodeBase().toString();
		String slash = "/";
//		String slash = File.separator;

		temp = temp + "com" + slash + 
			"sparkyland" + slash +
			this.getDirectory() + slash;

		DebugLog.println(temp);

		try
		{
			pwd = new URL(temp);
		}
		catch (MalformedURLException e)
		{
		}
		DebugLog.println(pwd);

		// rewrite: push this whole class down to GameApplet
		ResourceLoader.init( this, pwd );
		CSVCanvasLoader.init( this, pwd );
	}

	// Always over-ride this method.
	protected abstract String getDirectory();
	// Always over-ride this method.
	protected abstract GameCanvas getNewGameCanvas();

	public void start()
	{
		DebugLog.println("start()");
		ResourceLoader.loadStartMedia();

		GameCanvas screen = getNewGameCanvas();
		screen.start();
		screen.addMouseListener( screen );
		screen.addMouseMotionListener( screen );
		screen.removeAll();
		CSVCanvasLoader.loadCanvasFromFile(screen, "titlescreen.csv");
		this.add( screen );
	}

	public URL getPwd() { return pwd; }

	public void stop()
	{
		// rewrite: this happens when the applet loses focus.
		// we could put the pause in here.
		super.stop();
	}

	public void destroy()
	{
		// rewrite: finalize more dude.
		super.destroy();
		ResourceLoader.destroy();
	}
	public Frame getFrame()
	{ 
		Container c = getParent(); 
		while (c != null && !(c instanceof Frame)) 
		{ 
			c = c.getParent(); 
		} 
		return (Frame)c; 
	} 
}
