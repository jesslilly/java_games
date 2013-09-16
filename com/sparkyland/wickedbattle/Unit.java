package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.TurnObject;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.*;
import com.sparkyland.spartique.videogame.DisplayCanvas;

import java.awt.Image;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.String;

/**----------------------------------------------------------------------------
Class Unit

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

// rewrite: make a Unit Class to push down into spartique.  Make a WPUnit with deviations from that class.
public class Unit extends BoundedSprite implements Cloneable, Selectable, TurnObject
{
	private static int availability;
	public static final Size defaultSize = new Size( 36, 36 );// rewrite: should be set as default size of gridSquare.
	public static final int NONE = 0, ARCHER = 1;
	public static final int 
		AI0_NONE = 0, 
		AI1_DONTMOVE = 1, 
		AI2_DEFENDLOC = 2,
		AI3_KILLUNITTYPE = 3,
		AI4_DONOTHING = 4,
		AI5_RANDOMMOVE = 5;
	protected int aiNumber;

	// rewrite: make HP a double and display as an int.
	protected int HP, currentMP, attack, defense;
	protected int maxMP;
	protected int rate; // 6 for horsy, 3 for infantry, etc.
	protected WPArmy army;
	protected boolean selected, exploding, hasCounterAttack;
	protected int specialAttack;	// rewrite: specialAttack object.  Array instead of int.
	protected Applet applet;
	AnimatedPicture explosion;

	// rewrite: rate -> speed.
	public Unit ( int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], DisplayCanvas canvas, int freq, int vx, int vy,
					Applet applet,
					int specialAttack, int HP, int rate, 
					int attack, int defense, int maxMP )
	{
		super(x, y, w, h, s, l, n, f, canvas, freq, vx, vy);

		this.army = null;
		this.specialAttack = specialAttack;
		this.HP = HP;
		this.maxMP = maxMP;
		this.currentMP = maxMP;
		this.selected = false;
		this.exploding = false;
		this.hasCounterAttack = true;
		this.applet = applet;
		this.aiNumber = Unit.AI0_NONE;
		this.rate = rate;
		this.attack = attack;
		this.defense = defense;
		Image i[] = new Image[2];
		i[0] = applet.getImage( WickedBattle.getPWD(), "images/spark1.gif" );
		this.explosion = new AnimatedPicture( 0, 0, 36, 36, false, Layer.TOP, "explosion", i, canvas, 1 );
	}

	public void init( CSVTokenizer csv )
	{
		this.locx = csv.getIntAt(1);
		this.locy = csv.getIntAt(2);
		this.selected = csv.getBooleanAt(5);
		this.aiNumber = csv.getIntAt(6);
		this.HP = csv.getIntAt(7);
	}

	public Unit cloneUnit()
	{
		/* rewrite: with real clone, see coordinate and size.
		locx = unit.getX();
		locy = unit.getY();
		width = unit.getWidth();
		height = unit.getHeight();
		solid = unit.getSolid();
		layer = unit.getLayer();
		name = unit.getName();
		frames = unit.getFrames();
		canvas = unit.get
		frequency = unit.get
		vx = unit.get
		vy = unit.get
		applet = unit.get
		army = unit.get
		specialAttack = unit.get
		HP = unit.get
		rate = unit.get
		*/
		return new Unit ( locx, locy, width, height, solid, layer, name, frames, canvas, frequency, vx, vy, applet, specialAttack, HP, rate, attack, defense, maxMP );
	}

	public void update()
	{
		super.update();
		if ( exploding )
			explosion.update();
	}

	public void paint(Graphics g)
	{
		int blackbox = 16;
		super.paint(g);
		g.setColor( Color.black );
		if ( HP < 10 )
			blackbox = 8;
		g.fillRect( locx, locy + 24, blackbox, 12);
		g.setColor( Color.white );
		g.drawString( String.valueOf(HP), locx, locy + height );
		if (selected /*&& ( step % 2 == 0 )*/ )
		{
			//black blue cyan darkGray gray green lightGray magenta orange pink red white yellow
			g.setColor(Color.yellow);
//			Rectangle clipRect = getClipRect();
//			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			g.drawRect( locx, locy, width, height);
		}
		if (exploding)
		{
			explosion.setX( this.locx );
			explosion.setY( this.locy );
			explosion.paint( g );
			if ( explosion.isLifeDone() )
				exploding = false;
		}
	}
	public void die ()
	{
		this.HP = 0;
		DebugLog.println( name + " unit has died!");

		if ( this.selected )
		{
			DebugLog.println("Selcted Unit has died!");
			setSelected( false );
		}
		((WickedBattleCanvas)canvas).getBattle().remove( this );
		((WickedBattleCanvas)canvas).remove( this );
		DebugLog.println("Sound effect: Bleh!");
	}

	public void goDirection()
	{
		Coordinate coord = new Coordinate();
		coord.adjust( rate, direction );
		setVelocity( coord );
	}

/*
	public void specialAttack()
	{
		//DebugLog.println("SpecialAttack");

		Direction specialAttackDirection = army.getDirectionOfBattle();
		// rewrite: enemy
		WPArmy enemyArmy = ((WickedBattleCanvas)canvas).getEnemyArmy( this.army );
		Unit enemy = ((WickedBattleCanvas)canvas).getClosestEnemy( this.getGridCoordinate(), specialAttackDirection, enemyArmy );
		if ( enemy != null )
		{
			enemy.decrementHP( 1.0 );
		}
	}
	*/

	public boolean hasSpecialAttack( )
	{
		return specialAttack > Unit.NONE;
	}
	public void stopMoving()
	{
		super.stopMoving();
	}

	public void setSpecialAttack( int attack )	{ this.specialAttack = attack; }
	public int getSpecialAttack() { return this.specialAttack; }
	public void setSelected( boolean selected )
	{
		this.selected = selected;
	}
	public void meleeAttack( Unit defendingUnit, boolean allowCounterAttack )
	{
		
		AbstractPicture terrain = ((WickedBattleCanvas)canvas).getPictureBelow( defendingUnit );
		double defenseModifier = 1.0;
		if ( terrain instanceof Terrain )
		{
			defenseModifier = ((Terrain)terrain).getDefenseModifier();
		}
		this.currentMP = 0;
		double defendingUnitDamage = 
			( this.HP * this.attack ) /
			( defendingUnit.getHP() * defendingUnit.getDefense() * defenseModifier ) + 1;
		defendingUnit.decrementHP( defendingUnitDamage );
		if ( allowCounterAttack && defendingUnit.hasCounterAttack() )
		{
			defendingUnit.setHasCounterAttack( false );
			defendingUnit.meleeAttack( this, false );
		}
	}
	private void explode()
	{
		// rewrite: explosion can get added many times.
		// also, explosion does not move with unit.
		this.exploding = true;
		explosion.setLife(5);
	}
	public void decrementHP( double damage )
	{
		this.HP = this.HP - (int)damage;
		DebugLog.println( this + " takes " + damage + " damage." );
		//DebugLog.println( " HP: " + HP + " isDead " + isDead() );
		if ( this.isDead() )
			this.die();
		else
			this.explode();
	}

	public boolean isSelected() { return this.selected; }
	public int getHP() { return this.HP; }
	public void setHP( int HP ) { this.HP = HP; }
	public void setArmy( WPArmy army ) { this.army = army; }
	public WPArmy getArmy() { return this.army; }
	public void setAvailability( int availability ) { this.availability = availability; }
	public int getAvailability() { return this.availability; }
	public Coordinate getLoc() { return new Coordinate(locx, locy ); }
	public boolean isDead() { return this.HP <= 0; }
	public int getRate() { return rate; }
	public void setRate( int r )
	{
		// rewrite: Lots of values should be decimals instead of int.
		// Valid rates are positive and negative: 1, 2, 3, 4, 6, 9, 12, 16.
		this.rate = r;
	}
	public int getMP() { return currentMP; }
	public int getMaxMP() { return maxMP; }
	public void setMP( int MP ) { this.currentMP = MP; }
	public boolean hasMP() { return currentMP > 0; }
	public boolean doneWithTurn() { return ! this.hasMP(); }
	public void replenish() 
	{ 
		this.currentMP = maxMP;
		this.hasCounterAttack = true;
	}
	public String getUnitInfo() 
		{ return attack + "-" + defense + "-" + currentMP + "/" + maxMP + " " + name; }
	public int getAttack() { return this.attack; }
	public int getDefense() { return this.defense; }
	public boolean hasCounterAttack() { return hasCounterAttack; }
	public void setHasCounterAttack( boolean a ) { this.hasCounterAttack = a; }
	public void setAINumber( int aiNumber ) {this.aiNumber = aiNumber; }
	public int getAINumber() { return this.aiNumber; }
	public void finishTurn() { this.currentMP = 0; }

	public void setAttack( int attack ) { this.attack = attack; }
	public void setDefense( int defense ) { this.defense = defense; }
	public void setMaxMP( int mp ) { this.maxMP = mp; }
	public void setDisplayCanvas( DisplayCanvas c )
	{
		super.setDisplayCanvas( c );
		this.explosion.setDisplayCanvas( c );
	}
}
