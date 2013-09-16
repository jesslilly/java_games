package com.sparkyland.spartique.common;

import java.util.Vector;

public class TurnVector extends SpinVector
{
	// Constructor.  Default selectedObject to the first one.
	public TurnVector( int intialCapacity, int cpacityIncrement )
	{
		super( intialCapacity, cpacityIncrement );
		circular = true;
	}
	public TurnVector( int intialCapacity )
	{
		super( intialCapacity );
	}
	public TurnVector()
	{
		super();
	}


	public void addElement( TurnObject turnObject )
	{
		super.addElement( turnObject );
	}
	
	public Object getNextObject()
	{

		// selectedObject has not changed yet.  
		// We are deciding what the next one should be.
		TurnObject prevObject = (TurnObject)selectedObject;
		int noInfiniteLoop = 0;
		
		do
		{
			//prevObject = (TurnObject)selectedObject;

			// This method sets selectedObject.
			pointToNextObject();

			if ( selectedObject == prevObject || selectedObject == null )
			{
				// We went all the way through back to the same selected object.
				DebugLog.println( "No other object to select." );
				break;
			}
			if (noInfiniteLoop>100)
			{
				// rewrite: put in an ERROR file on the server, so I know if this is 
				// happening to people.
				DebugLog.println( "Hey Jess, this is an issue, dog!" );
				break;
			}
			noInfiniteLoop++;
		}
		while ( ((TurnObject)selectedObject).doneWithTurn() );

		return getSelectedObject();
	}
}
