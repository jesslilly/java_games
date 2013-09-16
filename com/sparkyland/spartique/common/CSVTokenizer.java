package com.sparkyland.spartique.common;

import java.util.StringTokenizer;
import java.util.Vector;

public class CSVTokenizer extends Object
{
	protected Vector tokens;

	public CSVTokenizer()
	{
		tokens = new Vector( 20 );
	}
	public void tokenize( String csv )
	{
		StringTokenizer tokenizer = new StringTokenizer( csv, "," );

		tokens.removeAllElements();
		while ( tokenizer.hasMoreTokens() )
		{
			//DebugLog.println( "Jess adding a token." );
			tokens.addElement( tokenizer.nextToken() );
		}
	}
	public void removeTokenAt( int index )
	{
		tokens.removeElementAt( index );
		DebugLog.println( this );
	}
	public String toString()
	{
		String bobo = "CSVTokenizer:";
		for ( int i = 0; i < tokens.size(); i++)
		{
			bobo = bobo + (String)tokens.elementAt(i) + ",";
		}
		return bobo;
	}
	public int numberOfTokens() { return tokens.size(); }
	private String stringAt( int index ) { return (String)tokens.elementAt( index ); }
	public short getShortAt( int index )
	{
		try
		{
			return Short.parseShort( stringAt( index ) );
		}
		catch( Exception e )
		{
			e.printStackTrace( DebugLog.out );
			return 0;
		}
	}
	public int getIntAt( int index )
	{
		try
		{
			return Integer.parseInt( stringAt( index ) );
		}
		catch( Exception e )
		{
			e.printStackTrace( DebugLog.out );
			return 0;
		}
	}
	public String getStringAt( int index )
	{
		try
		{
			return stringAt( index );
		}
		catch( Exception e )
		{
			e.printStackTrace( DebugLog.out );
			return " ";
		}
	}
	public boolean getBooleanAt( int index )
	{
		try
		{
			//DebugLog.println( "CSV Boolean:" + stringAt( index ) + " " + stringAt( index ).equals("true") );
			return stringAt( index ).equals("true");
		}
		catch( Exception e )
		{
			e.printStackTrace( DebugLog.out );
			return false;
		}
	}
	public double getDoubleAt( int index )
	{
		try
		{
			return Double.valueOf( stringAt( index ) ).doubleValue();
		}
		catch( Exception e )
		{
			e.printStackTrace( DebugLog.out );
			return 0.0;
		}
	}
}
