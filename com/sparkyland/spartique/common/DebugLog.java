package com.sparkyland.spartique.common;

import java.io.PrintStream;

public class DebugLog 
{
	private static boolean debugOn;
	public static PrintStream out;

	// static initializer.
	static
	{
		debugOn = false;
		DebugLog.out = System.out;
	}
	public static void println( String debugMessage )
	{
		if ( debugOn )
			DebugLog.out.println( System.currentTimeMillis() + " : " + debugMessage );
	}
	public static void println( Object debugObject )
	{
		if ( debugOn )
		{
			if ( debugObject instanceof Object ) // != null
				DebugLog.println( debugObject.toString() );
			else
				DebugLog.println( "null" );
		}
	}
	public static void setDebug( boolean debug )
	{
		debugOn = debug;
	}
	public static void setDebug( String debug )
	{
		// if debug is null, equals will handle it.
		if ( "Y".equals( debug ) )
		{
			debugOn = true;
			println( "Debug On." );
		}
		else
		{
			debugOn = false;
		}
	}
}
