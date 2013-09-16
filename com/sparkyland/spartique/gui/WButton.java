package com.sparkyland.spartique.gui;

import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.videogame.sprite.BoundedSprite;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.gui.Mouseable;
import com.sparkyland.spartique.videogame.CSVCanvasLoader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

/////////////////////////////////////////////////////////////////
public class WButton extends WComponent implements Mouseable
{
	// rewrite: Abstract base class with FONT for all GUI elements.
	public static Font FONT;
	public static final int POINT_SIZE = 14;

	private boolean mousePressed, mouseClicked;
	private int offsetX, offsetY;
	private String label, action;
	private Object actionArg;
	private int labelX, labelY;

	// static initializer.
	static
	{
		FONT = new Font( "SansSerif", Font.BOLD, WButton.POINT_SIZE );
	}

	public WButton
		(int x, int y, int w, int h, boolean s, int l, String n, Image f[],
		GameCanvas canvas, int freq, int vx, int vy, String label)
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		mousePressed = mouseClicked = false;
		offsetX = offsetY = 0;
		this.label = label;
		this.setBlink( false );

		calculateLabelPosition();
	}

	// rewrite: point size, font, text color, background color
	public WButton( CSVTokenizer tokenizer, DisplayCanvas canvas )
	{
		super( tokenizer );
		//font = tokenizer.getStringAt(14);
		this.label = tokenizer.getStringAt(15);
		//textColor = tokenizer.getStringAt(16);
		//backgroundColor = tokenizer.getStringAt(17);
		this.canvas = canvas;
		this.action = tokenizer.getStringAt(18);
		this.actionArg = (Object)tokenizer.getStringAt(19);

		this.convertActionArg();
		this.calculateLabelPosition();
	}
	// rewrite: dupe code in WLabel.
	protected void calculateLabelPosition()
	{
		FontMetrics fontMetrics = canvas.getFontMetrics( WButton.FONT );
		int labelWidth = fontMetrics.stringWidth( label );
		labelX = locx + ( width / 2 ) - ( labelWidth / 2 );
		labelY = locy + ( height / 2 ) + ( WButton.POINT_SIZE / 2 );
	}

	public void setBlink( boolean blink )
	{
		this.animating = blink;
	}
	public void paint (Graphics g)
	{
		try
		{
			g.drawImage(frames[frame], locx + offsetX, locy + offsetY, width, height, canvas);
		}
		catch ( Exception e )
		{
			// it might be OK to paint a null frame for a blink affect.
			//DebugLog.println( "Can't Paint - " + e );
		}

		g.setColor( Color.red );
		g.setFont( WButton.FONT );
		g.drawString( label, labelX + offsetX, labelY + offsetY );

		if ( this == ((GameCanvas)canvas).getCurrentMouseable() )
		{
			g.setColor(Color.yellow);
			g.drawRect(locx, locy, width - 1, height - 1);
		}
	}
	protected void goDown()
	{
		offsetX = offsetY = 1;
	}
	protected void goUp()
	{
		offsetX = offsetY = 0;
	}
	

	// For this widget, we are going to ignore clicks
	// and use pressed and released instead.
	// 1. Pressed and Released allows for a press-move-release
	// to count as a click if contained within this widget.
	// Clicked does not fire if that happens.
	// 2. If we use Pressed and Released along with clicked,
	// we can get 2 events fired in a short time that can cause
	// 2 clicks effectively.
	public void mouseClicked( )
	{
		mouseClicked = false;
		mousePressed = false;
	}
	public void mouseEntered(  )
	{
	}
	public void mouseExited( )
	{
		//DebugLog.println( "mouse exited!!!!!!!!!!!!!!!!!" );
		mousePressed = mouseClicked = false;
		goUp();
	}
	public void mouseDragged(  )
	{
	}
	public void mouseMoved( )
	{
	}
	public void mousePressed(  )
	{
		mousePressed = true;
		goDown();
	}
	public void mouseReleased( )
	{
		if ( mousePressed )
		{
			goUp();
			mouseClicked = true;
			performAction();
		}
		mousePressed = false;
	}
	private void convertActionArg()
	{
		if ( "next".equals(action) || "previous".equals(action) )
		{
			WMultipleChoice choice = (WMultipleChoice)canvas.get( (String)actionArg );
			actionArg = (Object)choice;
		}
	}
	private void performAction()
	{
		DebugLog.println( "WButton performAction " + action + " " + actionArg );
		if ( "ClearAndLoad".equals(action) )
		{
			canvas.removeAll();
			CSVCanvasLoader.loadCanvasFromFile(canvas, (String)actionArg);
		}
		if ( "next".equals(action) )
		{
			((WMultipleChoice)actionArg).next();
		}
		if ( "previous".equals(action) )
		{
			((WMultipleChoice)actionArg).previous();
		}
	}
	public boolean wasClicked()
	{
		DebugLog.println( this.getName() + " wasClicked() = " + mouseClicked );
		return mouseClicked;
	}
	public boolean contains( Point p )
	{
		return this.clicked( p.x, p.y );
	}
	public String getActionCommand()
	{
		return this.getName();
	}

}