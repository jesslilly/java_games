package com.sparkyland.spartique.videogame.sprite;

import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.videogame.sprite.AnimatedPicture;

import java.awt.Image;

public class Terrain extends AnimatedPicture
{
	// modifier, bonus, types:
	//  .5 =  -50% for river / swamp
	// 1.5 =  +50% for hills / forest
	// 2.0 = +100% for mountains / castle
	protected double defenseModifier;

	public Terrain(int x, int y, int w, int h, boolean s, int l, String n, Image f[], DisplayCanvas canvas, int freq) 
	{
		super(x,y,w,h,s,l,n,f,canvas,freq);
		defenseModifier = 1;
	}
	public Terrain(int x, int y, int w, int h, boolean s, int l, String n, Image f[], DisplayCanvas canvas, int freq, double mod ) 
	{
		this(x,y,w,h,s,l,n,f,canvas,freq);
		this.defenseModifier = mod;
	}
	public Terrain( CSVTokenizer tokenizer )
	{
		super( tokenizer );
		this.defenseModifier = tokenizer.getDoubleAt(12);
	}
	// rewrite: this needs to be rewrit in a major way.
	public Terrain cloneTerrain()
	{
		return new Terrain ( locx, locy, width, height, solid, layer, name, frames, canvas, frequency, defenseModifier );
	}

	public void setDefenseModifier( double defenseModifier )
	{
		this.defenseModifier = defenseModifier;
	}
	public double getDefenseModifier()
	{
		return defenseModifier;
	}
	public String getDefensePercentage()
	{
		String percentageString = new String();
		int percentage = (int)( ( defenseModifier - 1 ) * 100 );
		if ( percentage > 0 )
		{
			percentageString = "+";
		}
		percentageString += percentage;
		return percentageString;
	}
}
