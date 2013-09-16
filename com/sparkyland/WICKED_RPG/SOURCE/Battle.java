/*
BASIC OVERVIEW: All Mortals may attack/counter once per 'round'
All Mortals may be damaged mulitple times per round.
A Round is when the hero attacks.

Add all of the mortals to the battle.

Hero may attack one or more enemies depending on his weapon.
Hero Attacks. All input is disabled.

All enemies that were attacked get a turn To Counter attack.
Enemies sense they are attacked.
	If hitpoints has decreased and countered = false
	then set Mortal.turn = Battle.turns++;
*/

import java.util.Vector;

class Battle 
{
	public static int CURRENT_TURN;
	Vector contestants;
	int round;						// which round are we on?
	int turn;						// which turn of the round?
	// turn may be obsolete by CURRENT_TURN?
	int turns;						// The total number of turns for a round.
	WCanvas gameCanvas;

	public Battle (WCanvas gameCanvas) 
	{
		cleanBattle();
		contestants = new Vector();
		this.gameCanvas = gameCanvas;
	}
	public static boolean isItMyTurn( int turn )
	{
		return (turn == CURRENT_TURN && CURRENT_TURN != 0);
	}

	public void add(AbstractPicture pict)
	{
		if (pict instanceof Monster)
		{
			contestants.addElement(pict);
		}
	}
    public void removeAll()
	{
		while ( contestants.size() > 0 )
		{
			contestants.removeElementAt(0);
		}
		cleanBattle();
    }
	public void newRound()
	{
		round++;
		CURRENT_TURN = turn = 0;
		turns = 0;
		for (int i = 0 ; i < contestants.size() ; i++ )
		{
			((Monster)contestants.elementAt(i)).setCountered( false );
		}
	}
	public void takeTurn()
	{
		turn++;
		CURRENT_TURN = turn;
		turns++;
	}
	public void commenceRound()
	{
		turn++;
		for ( ; turn <= turns ; turn++ )
		{
			CURRENT_TURN = turn;

			/*
			First Enemy that is attacked counters first.
				Mortal Battle.whosTurnIsIt()
				Mortal.counter()
					countered = true;
					turn = 0;
			First Enemy counters and may attack other enemies.
			*/
			System.out.println(this);
			Mortal activePlayer = whosTurnIsIt();
			if ( activePlayer instanceof Monster )
			{
				((Monster)activePlayer).startAttack(gameCanvas.getHero() ); 
			}
		}
		finishRound();
	}
	private void finishRound()
	{
		// Hero.input enabled.
	}
	private Mortal whosTurnIsIt()
	{
		int i;
		Mortal currentPlayer = null;
		for (i = 0; i < contestants.size(); i++) {
			currentPlayer = (Mortal)contestants.elementAt(i);
			//System.out.println("Searching:" + currentPlayer);
			if ( currentPlayer instanceof Monster &&
				turn == ((Monster)currentPlayer).getTurn() )
			{
				break;
			}
		}
		System.out.println("Got:" + currentPlayer);
		return currentPlayer;
	}
	public int addTurn()
	{
		turns = turns + 1;
		return turns;
	}
	private void cleanBattle()
	{
		round = 0;
		CURRENT_TURN = turn = 0;
		turns = 0;
	}
	// ---------------------------------------------------------
	public String toString()
	{
		return 
			"Battle.  Round: " + round + "\n"
			+ " Turn: " + turn + " Turns: " + turns + "\n"
			+ " Num of Contestants: " + contestants.size()
			;
	}
	// Setters and Getters.
	// ---------------------------------------------------------
	public int getRound() { return round; }
	public int getTurn() { return turn; }
	public int getTurns() { return turns; }
}
