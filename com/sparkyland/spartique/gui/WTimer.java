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
public class WTimer extends WLabel
{
	protected int minuteCounter, secondCounter, miliSecondCounter;
	protected int refreshPause;
	protected boolean timing;

	public WTimer
		(int x, int y, int w, int h, boolean s, int l, String n, Image f[],
		GameCanvas canvas, int freq, int vx, int vy )
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);
		label = "0:00.0";

		minuteCounter = secondCounter = miliSecondCounter = 0;

		// The is the length of a pause between frames in miliseconds.
		this.refreshPause = DisplayCanvas.getRefreshPause();
		this.timing = false;
	}

	public void update()
	{
		super.update();

		if (timing)
		{
			miliSecondCounter += refreshPause;

			if (miliSecondCounter >= 1000 )
			{
				miliSecondCounter -= 1000;
				secondCounter++;
			}
			if (secondCounter >= 60)
			{
				secondCounter -= 60;
				minuteCounter++;
			}
			label = minuteCounter + ":";
			if (secondCounter<10)
			{
				// Put a zero in fron of the second, example: 1:01.1.
				label += "0";
			}
			label += secondCounter + "." + (miliSecondCounter/100);
		}
	}
	public void setTiming( boolean timing ) { this.timing = timing; }

}