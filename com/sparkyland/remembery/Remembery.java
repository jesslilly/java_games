package com.sparkyland.remembery;

import com.sparkyland.spartique.videogame.GameApplet;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.physical.Size;

import java.applet.*;

/**----------------------------------------------------------------------------
@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class Remembery extends GameApplet
{
	protected String getDirectory() { return "remembery"; }
	protected GameCanvas getNewGameCanvas() { return new RememberyCanvas( this, this.getSize(), new Size(4,4) ); }
}
