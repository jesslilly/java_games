package com.sparkyland.spartique.military;

/**----------------------------------------------------------------------------
Class Battle

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class Battle
{
	protected String name;
	protected Army players[];
	protected int numCurrentPlayers, loser, winner;

	public Battle ( String name, int numCurrentPlayers )
	{
		this.name = name;
		this.numCurrentPlayers = 0;
		this.loser = -1;
		this.winner = -1;
		this.players = new Army[ numCurrentPlayers ];
	}
	public void add( Army player  )
	{
		 players[ numCurrentPlayers ] = player;
		 numCurrentPlayers ++;
	}

	public boolean decided()
	{
		boolean battleFinished = false;

/*		switch ( objective.getObjective() )
		{
		// rewrite: This only works if there are 2 players.
		case  Objective.SURVIVAL:
			if ( isSomeoneDead() )
			{
				battleFinished = true;
			}
			break;
		}*/
		if ( players[0].failedObjective() )
		{
			loser = 1;
			battleFinished = true;
		}
		if ( players[1].failedObjective() )
		{
			loser = 2;
			battleFinished = true;
		}

		return battleFinished;
	}
	
	public String getName() { return this.name; }

	public int getNumTotalPlayers() { return players.length; }
	public int getNumCurrentPlayers() { return numCurrentPlayers; }

	public Army getArmy( int playerNum ) { return players[ playerNum - 1]; }

	public int getLoser() { return loser; }
	public int getWinner()
	{
		winner = 1;
		if ( loser == 1)
		{
			winner = 2;
		}
		return winner;
	}
}
