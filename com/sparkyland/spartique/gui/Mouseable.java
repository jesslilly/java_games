package com.sparkyland.spartique.gui;

import java.awt.Point;

// Mouseable objects are manipulated by the gamecanvas.
// They can be clicked or hovered over.

public interface Mouseable 
{
	void mouseClicked( );
	void mouseEntered( );
	void mouseExited( );
	void mousePressed( );
	void mouseReleased( );
//	void mouseDragged( );
//	void mouseMoved( );
	boolean contains(Point p);
	boolean wasClicked();
	String getActionCommand();
}
