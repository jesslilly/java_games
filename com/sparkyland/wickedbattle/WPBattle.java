package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.military.Battle;

/**----------------------------------------------------------------------------
Class WPBattle

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
rewrite: This class should override a more general WPBattle in the 'common' code as well should army.
rewrite: I see tons of dupe code here because of player 1, player 2.
-----------------------------------------------------------------------------*/

public class WPBattle extends Battle
{
	protected WickedBattleCanvas wpCanvas;

	public WPBattle ( String name, WickedBattle mainProgram, String army1, String army2, WickedBattleCanvas wpCanvas )
	{
		// rewrite: hard code.
		super( name, 2 /* 2 Players */ );
		this.wpCanvas = wpCanvas;
		this.add( new WPArmy( army1, mainProgram, wpCanvas ) );
		this.add( new WPArmy( army2, mainProgram, wpCanvas ) );
	}
	public WPArmy getEnemyArmy( WPArmy army )
	{
		WPArmy enemyArmy = (WPArmy)getArmy( 2 );
		if ( army.equals( enemyArmy ) )
			enemyArmy = (WPArmy)getArmy( 1 );
		return enemyArmy;
	}
	public void add( Unit unit )
	{
		 unit.getArmy().add( unit );
	}
	public void remove( Unit unit )
	{
		if ( unit.isDead() )
		{
			unit.getArmy().incrementCasualties();
			unit.getArmy().remove( unit );
		}
	}
	public boolean decided()
	{
		boolean finished = false;
		if ( super.decided() )
		{
			finished = true;
		}
		return finished;
	}

	public WPArmy getPlayer1Army() { return (WPArmy)getArmy( 1 ); }
	public WPArmy getPlayer2Army() { return (WPArmy)getArmy( 2 ); }
	public WPArmy getPlayerArmy( int playerNum )
	{
		DebugLog.println( "getPlayerArmy " + playerNum + "." );
		return (WPArmy)getArmy( playerNum );
	}
//	public void setPlayer2Army( WPArmy f ) { this.player2Army = f; }
//	public void setPlayer1Army( WPArmy f ) { this.player1Army = f; }
}
