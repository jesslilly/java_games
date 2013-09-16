package com.sparkyland.wickedbattle;

import java.util.Hashtable;
import java.awt.Container;
/**----------------------------------------------------------------------------
Class ScreenManage

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class ScreenManager
{
	private WickedBattle mainProgram;
	private CardScreen currentScreen;

	public ScreenManager( WickedBattle mainProgram )
	{
		this.mainProgram = mainProgram;


		currentScreen = null;
	}

	public void showScreen( CardScreen nextScreen )
	{
		if ( currentScreen != null )
		{
			mainProgram.remove( currentScreen );
		}

		currentScreen = nextScreen;

		currentScreen.init();
		mainProgram.add( "Center", currentScreen );
		currentScreen.start();
		mainProgram.validateTree();
	}

	public CardScreen getCurrentScreen () { return currentScreen; }


}



