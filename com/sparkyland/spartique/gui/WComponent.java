package com.sparkyland.spartique.gui;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.videogame.sprite.BoundedSprite;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;

import java.awt.Image;

/**----------------------------------------------------------------------------
Class WComponent

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public abstract class WComponent extends BoundedSprite
{

	public WComponent
		(int x, int y, int w, int h, boolean s, int l, String n, Image f[],
		GameCanvas canvas, int freq, int vx, int vy )
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
	}

	public WComponent( CSVTokenizer tokenizer )
	{
		super( tokenizer );
	}
}
