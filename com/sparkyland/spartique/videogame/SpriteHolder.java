package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.sprite.AbstractPicture;

import java.awt.Graphics;

// This is an interface to add and remove sprites.
// A SpriteHolder can add sprites, paint, and remove them
public interface SpriteHolder 
{
    void paint(Graphics g);
    void update(Graphics g);
	void add(AbstractPicture pict);
    void remove(AbstractPicture pict);
    void removeAll();
}
