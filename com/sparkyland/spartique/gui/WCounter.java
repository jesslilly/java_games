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
public class WCounter extends WLabel
{
	protected int counter;

	public WCounter
		(int x, int y, int w, int h, boolean s, int l, String n, Image f[],
		GameCanvas canvas, int freq, int vx, int vy )
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		label = "0";
		counter = 0;
	}

	public void setCounter( int number )
	{
		this.counter = number;
		this.label = String.valueOf( counter );
	}
	public void increment()
	{
		this.label = String.valueOf( ++counter );
	}
	public void decrement()
	{
		this.label = String.valueOf( --counter );
	}


}