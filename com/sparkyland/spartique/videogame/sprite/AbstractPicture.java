package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.physical.Coordinate;

import java.awt.Graphics;

// This is an interface to DisplayCanvas.
public interface AbstractPicture 
{
	void paint(Graphics g);
	void update();
	int getLayer();
	String getName();
	String toString();
	int getX();
	int getY();
	Coordinate getLoc();
	boolean isSolid();
	boolean isClipped();
	Coordinate getGridCoordinate();
	void setGridCoordinate( Coordinate gridCoordinate );
}
