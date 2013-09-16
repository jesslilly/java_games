package com.sparkyland.spartique.videogame.cards;

import com.sparkyland.spartique.gui.WButton;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.DebugLog;

import java.awt.Image;

/////////////////////////////////////////////////////////////////
public class Card extends WButton
{
	public Card(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], GameCanvas canvas, int freq, int vx, int vy)
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy, "");
	}
}