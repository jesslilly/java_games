package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.TurnVector;
import com.sparkyland.spartique.common.RandomNumGen;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.Direction;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.military.Army;
import com.sparkyland.spartique.military.Objective;

import java.awt.Image;
import java.applet.Applet;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**----------------------------------------------------------------------------
Class WPArmy

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class WPArmy extends Army
{
	protected static Hashtable armyPrototypes;

	protected Unit unitType[];
	protected final int numUnitTypes = 3;
	protected WickedBattle mainProgram;
	protected ArmyStatsPanel armyStatsPanel;
	protected Direction directionOfBattle;
	protected WickedBattleCanvas wpCanvas;
	protected TurnVector units;

	// static initializer.
	static
	{
		armyPrototypes = new Hashtable();
	}

	public WPArmy ( String name, WickedBattle mainProgram, WickedBattleCanvas wpCanvas )
	{
		super( name );
		this.wpCanvas = wpCanvas;
		this.mainProgram = mainProgram;
		unitType = new Unit[numUnitTypes];
		this.units = new TurnVector();

		Vector unitPrototypes = (Vector)armyPrototypes.get( name );
		DebugLog.println( "Load prototypes for " + name );

		for ( int i = 0; i < unitType.length; i++)
		{
			DebugLog.println( "i " + i );
			DebugLog.println( "u " + (Unit)unitPrototypes.elementAt(i) );
			unitType[i] = (Unit)unitPrototypes.elementAt(i);
			DebugLog.println( "j " + unitType[i] );
		}


	}
	public static void setUnitPrototype( CSVTokenizer tokenizer, Applet applet )
	{
		Image f[] = ResourceLoader.getImage( tokenizer.getStringAt(4), tokenizer.getIntAt(5), tokenizer.getIntAt(6) );
		Unit prototype = new Unit ( 0,0,36,36,true,2,tokenizer.getStringAt(4),f,null,3,0,0,applet,Unit.NONE,99, 6, 99, 99, 99 );
		// UnitPrototype,1,0,infantry,1,1,4,4,2
		prototype.setAttack( tokenizer.getIntAt(7) );
		prototype.setDefense( tokenizer.getIntAt(8) );
		prototype.setMaxMP( tokenizer.getIntAt(9) );

		// Prototype Unit is ready.
		String armyName = tokenizer.getStringAt(2);
		if ( ! armyPrototypes.containsKey( armyName ) )
		{
			armyPrototypes.put( armyName, new Vector(3) );
		}
		/*else
		{
			((Vector)armyPrototypes.get( armyName )).addElement( prototype );
		}*/
		((Vector)armyPrototypes.get( armyName )).addElement( prototype );

		DebugLog.println( "static prototype added " + prototype );
	}
	/*
	public void setUnitPrototype( CSVTokenizer tokenizer )
	{
		Image f[] = ResourceLoader.getPictures( tokenizer.getStringAt(3), tokenizer.getIntAt(4), tokenizer.getIntAt(5) );
		Unit prototype = new Unit ( 0,0,36,36,true,2,"name",f,wpCanvas,3,0,0,mainProgram,Unit.NONE,99, 6, 99, 99, 99 );
		// UnitPrototype,1,0,infantry,1,1,4,4,2
		prototype.setAttack( tokenizer.getIntAt(6) );
		prototype.setDefense( tokenizer.getIntAt(7) );
		prototype.setMaxMP( tokenizer.getIntAt(8) );

		unitType[tokenizer.getIntAt(2)] = prototype;
	}*/

	public Unit makeRandomUnit()
	{
		// rewrite: In the future, use the Availability of the unit.
		return makeUnit( RandomNumGen.rollXSidedDie( numUnitTypes ) - 1 );
	}

	public Unit makeUnit(int unitNumber )
	{
		Unit newUnit = unitType[unitNumber].cloneUnit();
		newUnit.setArmy( this );
		newUnit.setDisplayCanvas( wpCanvas );
		return newUnit;
	} 
	public void setDirecionOfBattle( Direction directionOfBattle )	{ this.directionOfBattle = directionOfBattle; }
	public Direction getDirectionOfBattle() { return this.directionOfBattle; }

	public void setVisualDisplay( ArmyStatsPanel armyStatsPanel /* updatable */)
	{
		this.armyStatsPanel = armyStatsPanel;
	}

	public void incrementCasualties()
	{
		super.incrementCasualties();
		DebugLog.println( name + " has " + casualties + " casualties.");
		if ( armyStatsPanel != null )
		{
			armyStatsPanel.update();
		}
	}
	public void add( Unit newUnit )
	{
		units.addElement( newUnit );
	}
	public Unit getNextUnit( int direction )
	{
		return (Unit)units.getNextObject();
	}
	public Unit getFirstUnit()
	{
		Unit firstUnit;
		units.pointToFirstObject();
		firstUnit = (Unit)units.getSelectedObject();
		return firstUnit;
	}
	public void replenishUnits()
	{
		DebugLog.println("--------replenish MP----------");
		DebugLog.println("--------replenish Counter----------");
		for ( int i = 0; i < (units.size()); i++)
		{
			((Unit)units.elementAt(i)).replenish();
		}
	}
	public void remove( Unit unit )
	{
		units.removeElement( unit );
	}

	public boolean failedObjective()
	{
		boolean failedObjective = super.failedObjective();
		if ( objective.getObjective() == Objective.DEFEND_COORDINATES
			&& !failedObjective )
		{
			//enemy wpCanvas.getEnemyArmy( this );
			// See jml boo jml below for how to (get enemy unit at). make a function.
//			wpCanvas.getPieceAt
			DebugLog.println( "Jess left off here." );
			failedObjective = false;
		}
		return  failedObjective;
	}

	public void doAI( )
	{

		wpCanvas.setInputEnabled( false );
		units.setCircular( false );

		Unit currentAIUnit;
		for ( units.pointToFirstObject(); 
			( units.getSelectedObject() != null && units.size() > 0 ); 
			units.pointToNextObject() )
		{
			currentAIUnit = (Unit)units.getSelectedObject();
			wpCanvas.setSelectedUnit( currentAIUnit );

			DebugLog.println( "Do AI for " + currentAIUnit.getName() );

			// rewrite: Use the timer class to wait so this doesn't happen so fast.

			switch ( currentAIUnit.getAINumber() )
			{
			case Unit.AI0_NONE :
				break;
			case Unit.AI1_DONTMOVE :
				attackAdjacentUnit( currentAIUnit );
				break;
			case Unit.AI2_DEFENDLOC :
				attackAdjacentUnit( currentAIUnit );
				break;
			case Unit.AI3_KILLUNITTYPE :
				break;
			case Unit.AI4_DONOTHING :	
				break;
			case Unit.AI5_RANDOMMOVE :
				// Don't forget that move will attack if it bumps into an enemy.
				// rewrite: can call in a loop because it takes time for the animation 
				// to work.  See move unit for PATH idea.
				// WARNING: This is not an issue right now.  It was fixed in WPCanvas.
				// If you move/attack, then we auto select the next unit, 
				// then we get the next
				// unit AGAIN in this loop which skips a unit.
				wpCanvas.move1Space( currentAIUnit, Direction.getRandomDirection() );
				break;
			}
			currentAIUnit.finishTurn();
		}
		wpCanvas.finishTurn();
		wpCanvas.setInputEnabled( true );
	}
	protected void attackAdjacentUnit( Unit currentAIUnit )
	{
		Coordinate selectedGridCoordinate = new Coordinate( currentAIUnit.getGridCoordinate( ) );
		Coordinate adjacentGridCoordinate = new Coordinate();
		AbstractPicture adjacentPiece = null;

		for ( Direction adjacentDirection = new Direction( Direction.UP );
		adjacentDirection.getIntDirection() < Direction.NUM_DIRECTIONS;
		adjacentDirection.incrementDirection() )
		{
			adjacentGridCoordinate.clone( selectedGridCoordinate );
			adjacentGridCoordinate.adjust( 1, adjacentDirection );
			DebugLog.println( "AI Looking for enemies at: " + adjacentGridCoordinate );
			adjacentPiece = wpCanvas.getPieceAt( adjacentGridCoordinate );
			// jml boo jml
			if ( adjacentPiece instanceof Unit ) // instanceof handles null.
			{
				Unit adjacentUnit = (Unit)adjacentPiece;
				WPArmy enemyArmy = wpCanvas.getEnemyArmy( currentAIUnit.getArmy() );
				if ( adjacentUnit.getArmy().equals( enemyArmy ) )
				{
					DebugLog.println( currentAIUnit + " AI attacks " + adjacentUnit );
					currentAIUnit.meleeAttack( adjacentUnit, true );
					// rewrite: Make a unit that does Attack all around it.
					// To do that, remove this break;
					break;
				}
			}
		}
	}

	public int size() { return units.size(); }

	public static Hashtable getArmyPrototypes() { return armyPrototypes; }
	public static Enumeration getArmyNames() { return armyPrototypes.keys(); }

}
