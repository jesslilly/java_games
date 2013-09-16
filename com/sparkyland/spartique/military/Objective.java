package com.sparkyland.spartique.military;

import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.common.CSVTokenizer;

/**----------------------------------------------------------------------------
Class Objective

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class Objective
{
	// Objectives are based on what an Army has to do to win,
	// Not what an army has to do to another army.
	public final static int
		SURVIVAL = 1,
		DEFEND_COORDINATES = 2,
		WIN = 1,
		LOSE = 2,
		TIE = 3;

	protected int objective;
	protected Coordinate coordinate;

	public Objective ( int objective )
	{
		this.objective = objective;
		this.coordinate = new Coordinate();
	}
	public Objective( CSVTokenizer tokenizer )
	{
		this.objective = tokenizer.getIntAt(1);
		this.coordinate = new Coordinate( tokenizer.getIntAt(2), tokenizer.getIntAt(3) );
	}

	public void setCoordinate( Coordinate coordinate ) { this.coordinate = coordinate; }
	public int getObjective() { return objective; }
	public void setObjective( int objective ) { this.objective = objective; }

}
