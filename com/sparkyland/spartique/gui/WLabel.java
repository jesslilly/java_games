package com.sparkyland.spartique.gui;

import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.videogame.GameCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.DebugLog;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

/////////////////////////////////////////////////////////////////
public class WLabel extends WComponent
{
	protected String label;
	private int labelX, labelY;

	public WLabel
		(int x, int y, int w, int h, boolean s, int l, String n, Image f[],
		GameCanvas canvas, int freq, int vx, int vy )
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		this.label = name;
		this.calculateLabelPosition();
	}

	// rewrite: point size, font, text color, background color
	public WLabel( CSVTokenizer tokenizer, DisplayCanvas canvas )
	{
		super( tokenizer );
		//font = tokenizer.getStringAt(14);
		this.label = tokenizer.getStringAt(15);
		//textColor = tokenizer.getStringAt(16);
		//backgroundColor = tokenizer.getStringAt(17);
		this.canvas = canvas;  // (Canvas is needed in calculateLabelPosition ).
		this.calculateLabelPosition();
	}
	protected void calculateLabelPosition()
	{
		FontMetrics fontMetrics = canvas.getFontMetrics( WButton.FONT );
		int labelWidth = fontMetrics.stringWidth( label );
		labelX = locx + ( width / 2 ) - ( labelWidth / 2 );
		labelY = locy + ( height / 2 ) + ( WButton.POINT_SIZE / 2 );
	}

	public void paint (Graphics g)
	{
		g.setColor( Color.red );
		g.setFont( WButton.FONT );
		g.drawString( label, labelX, labelY );
	}

	public String getLabel() { return this.label; }


}