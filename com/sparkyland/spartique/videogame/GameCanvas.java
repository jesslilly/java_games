package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.videogame.sprite.SimpleText;
import com.sparkyland.spartique.videogame.sprite.AbstractPicture;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.gui.Mouseable;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Checkbox;
import java.awt.Point;
import java.applet.Applet;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.util.Vector;

public class GameCanvas extends DisplayCanvas 
	implements MouseListener, MouseMotionListener, FocusListener
{
	protected SimpleText message;

	// rewrite: use spinvector.
	protected Vector mouseables;
	private Mouseable currentMouseable, previousMouseable;

	public GameCanvas(Applet mainProgram, Dimension size)
	{
		super( mainProgram, size);

		// A class like applet must call these methods.
		//addMouseListener(this);
		//addFocusListener(this);
		message = new SimpleText( 0, 200, 3, "Paused.  Click to resume.", this, 14 );
		mouseables = new Vector();
		currentMouseable = previousMouseable = null;
	}

	// -----------------begin mouse-----------------------------------

    public void add(AbstractPicture pict)
	{
		super.add( pict );
		if ( pict instanceof Mouseable )
		{
			DebugLog.println("Adding " + pict + " to mouseables list.(" + mouseables.size() + ")" );
			this.mouseables.addElement( pict );
		}
	}
	// rewrite: implement remove for mouseables.
	public void removeAll()
	{
		super.removeAll();
		mouseables.removeAllElements();
	}
	public boolean hasMouseableAt( Point p )
	{
		boolean hasMouseable = false;
		// rewrite: really, this should loop through layer 9 objects b4 layer 8, etc.
		// (do higher objects first ).
		for ( int i	= 0; i < mouseables.size(); i++ )
		{
			if ( ((Mouseable)mouseables.elementAt(i)).contains( p ) )
			{
				hasMouseable = true;
				previousMouseable = currentMouseable;
				currentMouseable = ((Mouseable)mouseables.elementAt(i));
			}
		}
		//DebugLog.println( currentMouseable + " <- " + previousMouseable );
		return hasMouseable;
	}
	public Mouseable getCurrentMouseable()
	{
		return currentMouseable;
	}
	public void mouseEntered( MouseEvent e )
	{
	}
	public void mouseExited(MouseEvent e)
	{
		if ( currentMouseable != null)
		{
			currentMouseable.mouseExited();
		}
		currentMouseable = previousMouseable = null;
	}
	public void mousePressed( MouseEvent e )
	{
		requestFocus();
		DebugLog.println("mousePressed(" + e + ")");
		if ( currentMouseable != null )
		{
			currentMouseable.mousePressed();
		}
	}
	public void mouseReleased(MouseEvent e) 
	{
	
		DebugLog.println("mouseReleased(" + e + ")");

		if ( currentMouseable != null )
		{
			currentMouseable.mouseReleased();
		}
	}
	public void mouseMoved(MouseEvent e) 
	{
		//DebugLog.println("mouseMoved(" + e + ")");
		if ( this.hasMouseableAt( e.getPoint() ) )
		{
			currentMouseable.mouseEntered();
		}
		else
		{
			currentMouseable = null;
		}
		if ( previousMouseable != currentMouseable 
			&& previousMouseable != null )
		{
			previousMouseable.mouseExited();
			previousMouseable = null;
		}
	}
	public void mouseDragged(MouseEvent e) 
	{
		this.mouseMoved( e );
	}
	public void mouseClicked(MouseEvent e) 
	{
		requestFocus();
		DebugLog.println("mouseClicked(" + e + ")");

		if ( currentMouseable != null )
		{
			currentMouseable.mouseClicked();
		}
	}


	// -----------------end mouse-----------------------------------



	// rewrite: ghost out when you lose focus.  Say, 'pasued click to resume'.    
	public void focusGained( FocusEvent e )
	{
		//DebugLog.println( "GameCanvas" + e + e.getComponent());
		this.remove( message );
		setPaused( false );
	}
	public void focusLost( FocusEvent e )
	{
		//DebugLog.println( "GameCanvas" + e + e.getComponent() );
		// pause right away.
		setPaused( true );
		// then add the message.
		this.add( message );
		// then repaint, so we get the message indeffinately.
		// It would be nice if we could repaint, but if you hold down tab
		// while you move an archer in, I think we get a deadlock.
		/*
		// rewrite:  We could either change some syncronized crap or
		// instead of trying to display a paused message on the canvas,
		// we could make a checkbox abd a button fo paused.  the check box would be disabled,
		// and the button would pause/resume based on the value of the check box.  That way
		the game canvas can update the checkbox when we lose focus.  cuz before when 
		we were clicking the checkbox, it would go check / uncheck really fast.
		Another option is to draw on the damn thing without doing an add.
		Or maybe when we change the whole canvas paint scheme, we can do it more easily.
		*/
		// this.repaint();
	}
// -----------------------------------  END EVENT PROCESSING ----------------------------------------------------

	public boolean isFocusTraversable() { return true; }

}





