package com.sparkyland.spartique.gui;

import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.videogame.SpriteHolder;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.SpinVector;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.videogame.sprite.Picture;
import com.sparkyland.spartique.videogame.sprite.AbstractPicture;
import com.sparkyland.spartique.physical.Coordinate;
import com.sparkyland.spartique.videogame.CSVCanvasLoader;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;
import java.awt.Dimension;

/////////////////////////////////////////////////////////////////
public class WMultipleChoice extends DisplayCanvas implements AbstractPicture
{
	//protected WButton nextButton, previousButton;
	// choices can be any AbstractPicture.
	protected SpinVector choices;

	protected WMultipleChoice buddy;
	protected String name;

	/*
	public WMultipleChoice(int x, int y, int w, int h,  boolean s, int l, String n, Image f[], DisplayCanvas canvas)
	{
		super(x, y, w, h, s, l, n, f, canvas);
	}*/

	// Constructor from CSV Tokenizer.
	public WMultipleChoice( CSVTokenizer tokenizer, DisplayCanvas canvas )
	{
		super(canvas.getApplet(), new Dimension(10,10) );
		//WMultipleChoice,deckName,deck-choice.csv,null
		this.name = tokenizer.getStringAt(1);
		this.choices = new SpinVector();
		String buddyName = tokenizer.getStringAt(3);
		if ( ! "null".equals(buddyName) )
		{
			this.buddy = (WMultipleChoice)canvas.get( buddyName );
		}
		loadChoicesFromFile( tokenizer.getStringAt(2) );
		this.choices.pointToFirstObject();
	}

	public void setChoices( SpinVector choices )
	{
		this.choices = choices;
	}

	public void loadChoicesFromFile( String fileName )
	{
		// We may want to use CSVCanvasLoader to load up this Spin Vector.
		CSVCanvasLoader.loadCanvasFromFile(this, fileName);
		//Vector choiceVector = ResourceLoader.getFileContents( "data", fileName );
		//this.choices = new SpinVector( choiceVector );
		//String imageName = (String)choices.getSelectedObject();
		//this.frames = ResourceLoader.getPictures( imageName, 0, 0 );
	}

	// rewrite: when I implement clipping, clip here.
	public void next()
	{
		this.choices.pointToNextObject();
		//DebugLog.println( "WMultipleChoice next " + imageName );
		//this.frames = ResourceLoader.getPictures( imageName, 0, 0 );
		if ( buddy != null )
		{
			buddy.next();
		}
	}

	public void previous()
	{
		this.choices.pointToPreviousObject();
		//DebugLog.println( "WMultipleChoice previous " + imageName );
		//this.frames = ResourceLoader.getPictures( imageName, 0, 0 );
		if ( buddy != null )
		{
			buddy.previous();
		}
	}

	public AbstractPicture getChoice()
	{
		return (AbstractPicture)choices.getSelectedObject();
	}
	
	// begin SpriteHolder interface implementation.
	public void add(AbstractPicture pict)
	{
		this.choices.addElement( pict );
	}
    public void remove(AbstractPicture pict)
	{
		this.choices.removeElement( pict );
	}
    public void removeAll()
	{
		this.choices.removeAllElements();
	}
	// end SpriteHolder interface implementation.

	// begin AbstractPicture interface implementation.
	public void paint(Graphics g)
	{
		((AbstractPicture)choices.getSelectedObject()).paint(g);
	}
	public void update()
	{
		((AbstractPicture)choices.getSelectedObject()).update();
	}
	public int getLayer()
	{
		return ((AbstractPicture)choices.getSelectedObject()).getLayer();
	}
	public String getName()
	{
		//return ((AbstractPicture)choices.getSelectedObject()).getName();
		return this.name;
	}
	public String toString()
	{
		return ((AbstractPicture)choices.getSelectedObject()).toString();
	}
	public int getX()
	{
		return ((AbstractPicture)choices.getSelectedObject()).getX();
	}
	public int getY()
	{
		return ((AbstractPicture)choices.getSelectedObject()).getY();
	}
	public Coordinate getLoc()
	{
		return ((AbstractPicture)choices.getSelectedObject()).getLoc();
	}
	public boolean isSolid()
	{
		return ((AbstractPicture)choices.getSelectedObject()).isSolid();
	}
	public boolean isClipped()
	{
		return ((AbstractPicture)choices.getSelectedObject()).isClipped();
	}
	public Coordinate getGridCoordinate()
	{
		return ((AbstractPicture)choices.getSelectedObject()).getGridCoordinate();
	}
	public void setGridCoordinate( Coordinate gridCoordinate )
	{
		((AbstractPicture)choices.getSelectedObject()).setGridCoordinate( gridCoordinate );
	}
	// end AbstractPicture interface implementation.


}