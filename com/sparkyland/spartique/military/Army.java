package com.sparkyland.spartique.military;

import com.sparkyland.spartique.common.DebugLog;
/**----------------------------------------------------------------------------
Class Army

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public abstract class Army
{
	protected String name;
	protected int size, casualties;
	protected Objective objective;

	public Army ( String name )
	{
		this.name = name;
		this.size = 0;
		this.casualties = 0;
		this.objective = new Objective(Objective.SURVIVAL);
	}

	public String getName() { return this.name; }

	public boolean equals( Army otherArmy )
	{
		return this.name.equals( otherArmy.getName() );
	}
	public void setObjective( Objective obj ) { this.objective =  obj; }

	protected abstract int size();

	public boolean failedObjective()
	{
		// Objective.SURVIVAL:
		return  (this.size() <= 0);
	}

	public boolean isDead() { return casualties == size; }

	public int getSize() { return size; }
	public void setSize( int num ) { this.size = num; }

	public void incrementCasualties() { casualties++; }
	public int getCasualties() { return casualties; }
	public void setCasualties( int num ) { this.casualties = num; }

	public int getStrength() { return size - casualties; }

	public String toString() { return ( name + " army" ); }

}
