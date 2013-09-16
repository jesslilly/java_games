package com.sparkyland.spartique.common;

import java.util.Vector;

public class SpinVector extends Vector
{
	protected Object selectedObject;
	protected int selectedObjectIndex;
	protected boolean circular;

	// Constructor.  Default selectedObject to the first one.
	public SpinVector( int intialCapacity, int cpacityIncrement )
	{
		super( intialCapacity, cpacityIncrement );
		selectedObjectIndex = 0;
		circular = true;
	}
	public SpinVector( int intialCapacity )
	{
		super( intialCapacity );
		selectedObjectIndex = 0;
		circular = true;
	}
	public SpinVector()
	{
		super();
		selectedObjectIndex = 0;
		circular = true;
	}
	public SpinVector( Vector vector )
	{
		this();
		for ( int i = 0 ; i < vector.size(); i++)
		{
			this.addElement( vector.elementAt(i) );
		}
		this.pointToFirstObject();
	}

	public void setCircular( boolean circular )
	{
		this.circular = circular;
	}

	public void pointToObject( Object selectedObject )
	{
		this.selectedObject = selectedObject;
		this.selectedObjectIndex = super.indexOf( this.selectedObject );
	}
	public void pointToFirstObject()
	{
		if ( super.size() == 0 )
		{
			selectedObject = null;
		}
		else
		{
			this.selectedObjectIndex = 0;
			selectedObject = super.elementAt( selectedObjectIndex );
		}
	}

	public Object getSelectedObject()
	{
		return this.selectedObject;
	}
	public void pointToNextObject()
	{
		selectedObjectIndex++;
		if ( selectedObjectIndex >= super.size() && this.circular )
		{
			// Spin back to the begginning.
			selectedObjectIndex = 0;
		}
		// If all elements have been removed, then return null.
		if ( selectedObjectIndex < 0 || selectedObjectIndex >= super.size() )
		{
			selectedObject = null;
		}
		else
		{
			selectedObject = super.elementAt( selectedObjectIndex );
		}
		DebugLog.println( "selectedObject: " + selectedObject );
	}

	public Object getNextObject()
	{
		pointToNextObject();
		return getSelectedObject();
	}

	public void pointToPreviousObject()
	{
		selectedObjectIndex--;
		if ( selectedObjectIndex < 0 && this.circular )
		{
			// Spin back to the end.
			selectedObjectIndex = super.size() - 1;
		}
		// If all elements have been removed, then return null.
		if ( selectedObjectIndex < 0 || selectedObjectIndex >= super.size() )
		{
			selectedObject = null;
		}
		else
		{
			selectedObject = super.elementAt( selectedObjectIndex );
		}
		DebugLog.println( "selectedObject: " + selectedObject );
	}

	public Object getPreviousObject()
	{
		pointToPreviousObject();
		return getSelectedObject();
	}


	/*
	public boolean pointingToLastObject()
	{
		boolean lastObject = false;
		lastObject = ( ( super.size() -1 ) == selectedObjectIndex );
		DebugLog.println( "pointingToLastObject: " + lastObject + " ("
			+ selectedObject + " " + selectedObjectIndex + "/" + super.size() + ")" );
		return lastObject;
	}
	public boolean validPointer()
	{
		boolean valid = false;
		valid = ( selectedObjectIndex < super.size() && selectedObjectIndex >= 0 );
		DebugLog.println( "validPointer: " + valid );
		return valid;
	}*/
}
