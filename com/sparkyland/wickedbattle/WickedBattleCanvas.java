package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.videogame.GridGameCanvas;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.*;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.ResourceLoader;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

// rewrite: a lot of this functionality maybe should be pushed to other classes.
public class WickedBattleCanvas extends GridGameCanvas implements KeyListener, FocusListener
{
	protected Unit selectedUnit, selectedEnemy;
	protected WickedBattle mainProgram;
	protected WPBattle battle;
	// rewrite: use active army all over the place.
	protected WPArmy activeArmy;
	protected boolean practiceMode, inputEnabled;

	public WickedBattleCanvas(WickedBattle mainProgram, Dimension size, Size gridRowsCols )
	{
		super(mainProgram, size, gridRowsCols);
		this.mainProgram = mainProgram;
		this.addKeyListener(this);
		addFocusListener(this);
		selectedUnit = selectedEnemy = null;
		practiceMode = false;
		this.activeArmy = null;
		this.inputEnabled = true;

		// rewrite: Hard Code.
		offScreenG.setFont( new Font( "SansSerif", Font.BOLD, 14 ) );
	}
	public void makeScenario( Scenario scenario )
	{
		// rewrite: machine specific "/".
		this.battle = new WPBattle( scenario.getName(), mainProgram, scenario.getArmy1(), scenario.getArmy2(), this );
		this.activeArmy = battle.getPlayerArmy(1);
		// rewrite: much cooler if it was "load media for quick attack castle battle".
		ResourceLoader.loadStartMedia();
		CSVParser.loadCanvasFromFile( this, scenario.getMapFile() );

		mainProgram.getGameScreen().setWhosTurnLabel( activeArmy.getName() );
		try
		{
			updateUnitStats( selectedUnit );
		}
		catch ( Exception exc )
		{
			DebugLog.println( "Selected Unit must be set in config: " + exc );
		}

	}
	public void add(AbstractPicture pict)
	{
		super.add( pict );
		// If it is a unit, add it to the battle.  The battle will keep track of statistics.
		if ( pict instanceof Unit )
		{
			Unit unit = (Unit)pict;
			battle.add( unit );
			if ( unit.isSelected() )
			{
				this.setSelectedUnit( unit );
			}
		}
    }	

	public void update( Graphics g )
	{
		super.update(g);
	}

	// KeyListener Interface
	public void keyPressed ( KeyEvent event )
	{
		//DebugLog.println( "KeyEvent: " + event.toString() );
		if ( ! inputEnabled )
		{
			// rewrite: EARLY RETURN!!!!!!!!!!!!!!!!!!!!!!!!
			DebugLog.println( "All input is disabled." );
			return;
		}
		
		int intKey = event.getKeyCode();
		char charKey = (char)intKey;

		if ( Direction.isValidDirection( intKey ) )
		{
			Direction directionPressed = new Direction(intKey);

			DebugLog.println( "User hit " + directionPressed + " arrow." );

			// rewrite: unit.move1Space(direction)
			this.move1Space( selectedUnit, directionPressed );

			// Only auto select next for Human player.  Otherwise, it will skip a unit in AI.
			if ( ! selectedUnit.hasMP() )
			{
				selectNextUnit();
			}
		}
		else
		{
			switch (charKey)
			{

				case KeyEvent.VK_N : // (N)ext Unit.
				{
					selectNextUnit();
					break;
				}
				case KeyEvent.VK_F : // (F)inish Turn.
				case KeyEvent.VK_SPACE :
				{
					finishTurn();
					break;
				}
				case KeyEvent.VK_TAB :
				{
					DebugLog.println( "User hit a tab." );
					mainProgram.getGameScreen().getPauseButton().setState( true );
				}
			}

		}
	}
	public void keyReleased ( KeyEvent e )
	{
//		DebugLog.println( "KeyEvent " + e.toString() );
	}
	// rewrite: Move all the 'paused stuff' to a lower class and make it optional.
	public void keyTyped ( KeyEvent e )
	{
//		DebugLog.println( "KeyEvent " + e.toString() );
	}
	public void focusGained( FocusEvent e )
	{
		DebugLog.println( this.getClass().toString() + e +  e.getComponent());
		super.focusGained( e );
		// rewrite: pass the pause button into this class.  Current Screen is dangerous.
		mainProgram.getGameScreen().getPauseButton().setState( false );
	}
	public void focusLost( FocusEvent e )
	{
		DebugLog.println( this.getClass().toString() + e +  e.getComponent());
		super.focusLost( e );
	}

	// Search the 4 squares around the selectedUnit and adjust HP.
	public void makeAEKills( Unit triggerUnit )
	{
		// rewrite: selectedGridCoordinate could be null;
		Coordinate selectedGridCoordinate = new Coordinate( triggerUnit.getGridCoordinate( ) );
		Coordinate adjacentGridCoordinate = new Coordinate();
		Direction adjacentDirection = new Direction();
		AbstractPicture adjacentPiece = null;
//		DebugLog.println( "MakeAEKills");
		for ( int i = 0; i <= 3; i++ )
		{
//			DebugLog.println( "i = " + i);
			adjacentGridCoordinate.clone( selectedGridCoordinate );
			adjacentGridCoordinate.adjust( 1, adjacentDirection );
//			DebugLog.println( "GC" + adjacentGridCoordinate.getX() + " " + adjacentGridCoordinate.getY() );
			adjacentPiece = getPieceAt( adjacentGridCoordinate );
			if ( adjacentPiece instanceof Unit ) // instanceof handles null.
			{
//				DebugLog.println( "Got a Unit");
				Unit adjacentUnit = (Unit)adjacentPiece;
				WPArmy enemyArmy = getEnemyArmy( triggerUnit.getArmy() );
				if ( adjacentUnit.getArmy().equals( enemyArmy ) )
				{
					triggerUnit.decrementHP(1);
					adjacentUnit.decrementHP(1);
				}
			}
			adjacentDirection.incrementDirection();
		}
	}

	public void remove( AbstractPicture pict )
	{
		super.remove( pict );
		if ( pict instanceof Unit )
		{
			if ( battle.decided() )
			{
				// from super class message.
				int winner = battle.getWinner();
				this.message.setMessage( "Player " + winner + ": " + battle.getArmy( winner ).getName() + " victory!" );
				this.add( message );
				DebugLog.println( "Player: " + battle.getLoser() + " lost! " );
				// rewrite: no need to load the music a second time.
				if ( winner == 1 )
				{
					ResourceLoader.loadAndLoopMusic( "winbattle.mid" );
				}
				else
				{
					ResourceLoader.loadAndLoopMusic( "air.mid" );
				}
			}
		}
	}

	// rewrite: idea: you could make a PATH object that is a vector of coordinates.
	// you could give it to the unit and let it walk the path.
	public boolean move1Space( Unit unit, Direction moveDirection )
	{
		DebugLog.println( "Move unit " + moveDirection );
		boolean canMove = false;
		boolean bump = false;
		if ( ! unit.isMoving() )
		{
			if ( unit.getMP() > 0 )
			{
				if ( moveLock( unit, moveDirection ) )
				{
					// rewrite: move this stuff into unit.
					unit.setRadiusBounds( );
					unit.setDirection( moveDirection );
					unit.goDirection();
					unit.setMP( unit.getMP() - 1 );
					if ( unit == selectedUnit )
						updateUnitStats( selectedUnit );
					DebugLog.println( "Unit MP: " + unit.getMP() + " of " + unit.getMaxMP() );
					canMove = true;
				}
				else
				{
					Unit defendingUnit = checkForEnemy( unit, moveDirection );
					if ( defendingUnit != null )
					{
						// attack
						DebugLog.println( "rewrite: Sound effect! " 
							+ unit + " attacks " + defendingUnit );
						unit.meleeAttack( defendingUnit, true );
						updateUnitStats( selectedUnit );
					}
					else
					{
						bump = true;
					}
				}
			}
			else
			{
				bump = true;
			}
		}
		else
			DebugLog.println( "Can't Move Unit. Already moving!" );

		if ( bump )
		{
			DebugLog.println( "rewrite: Sound effect! BUMP!" );
			ResourceLoader.playSound( "beep" );
		}
		return canMove;
			
	}
	private Unit checkForEnemy( Unit unit, Direction moveDirection )
	{
		Unit enemyInFront = null;
		Coordinate unitGridCoordinate = new Coordinate( unit.getGridCoordinate( ) );
		// rewrite: We need enemy Army stuff here too.
		unitGridCoordinate.adjust( 1, moveDirection );
		AbstractPicture a = getPieceAt( unitGridCoordinate );
		if ( a instanceof Unit ) // instanceof handles null.
		{
			DebugLog.println( "Unit in front.");
			Unit unitInFront = (Unit)a;
			WPArmy army1, army2, enemyOfArmy1;
			army1 = unit.getArmy();
			army2 = unitInFront.getArmy();
			enemyOfArmy1 = getEnemyArmy( army1 );
			if ( enemyOfArmy1.equals( army2 ) )
			{
				enemyInFront = unitInFront;
			}
		}
		return enemyInFront;
	}

	protected Unit getClosestEnemy( Coordinate inGridCoordinate, Direction direction, WPArmy enemyArmy )
	{	
		// rewrite: Is there a better way?
		// We are going to adjust the gridCoordinate in getClosestPiece, so we should really make a copy
		// and not modify what we know to be private data of the Unit class.
		Coordinate gridCoordinate = new Coordinate( inGridCoordinate );
		AbstractPicture piece = null;
		Unit unit = null;
		boolean keepLooking = true;
		while ( keepLooking )
		{
			piece = getClosestPiece( gridCoordinate, direction );
			if ( piece == null )
			{
				keepLooking = false;
			}
			else
			{
				if ( piece instanceof Unit )
				{
					unit = (Unit)piece;
					if ( unit.getArmy().equals( enemyArmy ) )
						keepLooking = false;
					else
						unit = null;
				}
			}
		}
		// May return null!
		return unit;
	}
	public WPArmy getEnemyArmy( WPArmy army )
	{
		return battle.getEnemyArmy( army );
	}
	public WPBattle getBattle() { return battle; }
	public void setBattle( WPBattle b ) { this.battle = b; }
	public void setSelectedUnit( Unit u )
	{
		if (selectedUnit != null)
			selectedUnit.setSelected(false);
		this.selectedUnit = u;	
		if (selectedUnit != null)
		{
			selectedUnit.setSelected(true);
			updateUnitStats( selectedUnit );
		}
	}
	public Unit getSelectedUnit( ) { return this.selectedUnit; }
	public void selectNextUnit()
	{
		this.setSelectedUnit( activeArmy.getNextUnit( Direction.RIGHT ) );
	}
	public void finishTurn()
	{
		// rewrite: activeArmy should go in battle class?
		// rewrite: selectedUnit should be in army.
		// rewrite: Make a dialVector Class for all the groupd that have a selected member.
		// then you could do battle.getNextArmy();

		DebugLog.println( "End of Turn for " + activeArmy );

		if ( activeArmy == battle.getPlayerArmy(1) )
		{
			activeArmy = battle.getPlayerArmy(2);
		}
		else
			activeArmy = battle.getPlayerArmy(1);
			
		DebugLog.println( "Beginning of Turn for " + activeArmy );

		activeArmy.replenishUnits();
		setSelectedUnit( activeArmy.getFirstUnit() );
		mainProgram.getGameScreen().setWhosTurnLabel( activeArmy.getName() );
		if ( activeArmy == battle.getPlayerArmy(2) )
		{
			DebugLog.println( "AI Time." );
			activeArmy.doAI();
		}
	}
	public void setInputEnabled( boolean input )
	{
		
	}
	public void updateUnitStats( Unit unit )
	{
		mainProgram.getGameScreen().setUnitStatsLabel( unit.getUnitInfo() );
/*
		Coordinate gridCoord = new Coordinate( unit.getGridCoordinate() );
		// rewrite: Assumption that the unit is a square....
		gridCoord.translate( Unit.defaultSize.getHeight() );

		// rewrite: This is all pretty lame because we should have a grid[][] thing
		// to access the terrain and pictures.
		AbstractPicture pict = getPictAt( gridCoord.getX(), gridCoord.getY(), Layer.BOTTOM );
		DebugLog.println( unit + " is on " + pict );
*/
		AbstractPicture pict = getPictureBelow( unit );

		String terrainInfo = new String();
		if ( pict instanceof Terrain )
		{
			terrainInfo += ((Terrain)pict).getDefensePercentage();
		}
		else
		{
			terrainInfo += "+0";
		}
		terrainInfo += "% Defense"; // + pict;
		DebugLog.println( "setUnitStatsLabel " + terrainInfo );
		
		mainProgram.getGameScreen().setTerrainLabel( terrainInfo );
		
	}

}