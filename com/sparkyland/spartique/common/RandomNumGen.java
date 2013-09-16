package com.sparkyland.spartique.common;

import java.util.Random;

public class RandomNumGen 
{
	private static Random random;

	// static initializer.
	static
	{
	
		// Set the seed value to get a consistant batch of random numbers.
		//this.random = new Random(1317L);
		RandomNumGen.random = new Random();
	}
	public static int rollXSidedDie( int numSides )
	{
		try
		{
			int result = Math.abs( random.nextInt() % numSides ) + 1;
			DebugLog.println( "RandomNumGen rollXsidedDie: " + result );
			return result;
		}
		catch ( Exception e )
		{
			return 0;
		}
	}

}
