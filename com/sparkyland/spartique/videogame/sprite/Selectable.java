package com.sparkyland.spartique.videogame.sprite;

import java.awt.Graphics;

public interface Selectable 
{
	void paint(Graphics g);
	void setSelected( boolean selected );
	boolean isSelected();
}
