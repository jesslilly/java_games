package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.physical.Size;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.ResourceLoader;

import java.applet.*;
import java.awt.*;
import java.io.*;
// Properties Objects can only be loaded from the applet file system, not the server.
//import java.util.Properties;
import java.net.URL;
import java.net.MalformedURLException;

/**----------------------------------------------------------------------------
Class WickedBattle is the main Applet for WICKED BATTLE.

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class WickedBattle extends Applet /*implements Runnable, KeyListener*/
{
	// rewrite: already has a size from applet.
	private Size size;
	private URL pwd;
	private ScreenManager screenManager;
	private static URL PWD;

	public void init()
	{

		// First thing.  Get the Applet parameter for debug.
		String debug =  this.getParameter( "debug" );
		DebugLog.out.println( "Debug: " + debug );
		DebugLog.setDebug( debug );

		DebugLog.println("init()");

		size = new Size(400,400);

		// These color and font values will not propigate to all of the widgets.
		//3/20/2002setBackground( Color.black );
		//3/20/2002setForeground( Color.white );
		setFont( new Font( "SansSerif", Font.PLAIN, 11 ) );

		String temp = getCodeBase().toString();

		// rewrite
		//temp = temp.substring(0, temp.indexOf("/source")) + "/";
		//temp = temp + "wickedbattle/";
		
		String slash = "/";
//		String slash = File.separator;

		temp = temp + "com" + slash + 
			"sparkyland" + slash +
			"wickedbattle" + slash;

		DebugLog.println(temp);
		try
		{
			pwd = new URL(temp);
		}
		catch (MalformedURLException e)
		{
		}
		DebugLog.println(pwd);

		PWD = pwd;

		this.setLayout(new BorderLayout());

		ResourceLoader.init( this, pwd );
		CSVParser.init( this, pwd );
	}

	public void start()
	{
		DebugLog.println("start()");

		screenManager = new ScreenManager( this );

		screenManager.showScreen( new MainScreen( this, screenManager ) );
	}

	public GameScreen getGameScreen() { return (GameScreen)screenManager.getCurrentScreen(); }
	public ScreenManager getScreenManager() { return screenManager; }

	public Size getSize(int i) {return size;}
	public URL getPwd() { return pwd; }

	public static URL getPWD() { return PWD; }

	public void validateTree()
	{
		super.validateTree();
	}

	public void stop()
	{
		// rewrite: this happens when the applet loses focus.
		super.stop();
	}

	public void destroy()
	{
		// rewrite: finalize more dude.
		super.destroy();
		ResourceLoader.destroy();
	}

}
