package com.sparkyland.wickedbattle;

import java.awt.Container;

/**----------------------------------------------------------------------------
class CardScreen.
Implemented by mainScreen, practiceSetupScreen, gameScreen, etc.
Interface for ScreenManager.

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

// rewrite: there is some dupe code in the sublcasses.  Not sure what to do about it.
public class CardScreen extends Container
{
	// Set the next screen by having a reference to the Screen manager
	// and calling screenManager.setNextScreen();
	protected WickedBattle mainProgram;
	protected ScreenManager screenManager;

	public CardScreen( WickedBattle mainProgram, ScreenManager screenManager )
	{
		this.screenManager = screenManager;
		this.mainProgram = mainProgram;
	}

	public void init() { ; }

	public void start()
	{
		this.doLayout();
		this.repaint();
		//list();
	}
}
