package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.ResourceLoader;

import com.sparkyland.spartique.videogame.sprite.Picture;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.awt.*;
import java.applet.Applet;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.CSVTokenizer;
import java.awt.event.ItemListener;

/**----------------------------------------------------------------------------
Class Scenario

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class Scenario
{

	protected short number; // 16 bit: -32768 to 32767
	protected String name, difficulty, objective, description, mapFile, imageName, army1, army2;
	protected Picture picture;
	protected Panel panel;
	protected DisplayCanvas canvas;
	protected Choice player1Army, player2Army, player2, player1;
/*
	public Scenario( short number, String name, String difficulty,
		String imageName, String objective, String description, 
		String mapFile, Applet applet )
	{
		this.number = number;
		this.name = name;
		this.difficulty = difficulty;
		this.objective = objective;
		this.description = description;
		this.mapFile = mapFile;

		player1Army = new Choice( );
		player2Army = new Choice( );
		player2 = new Choice( );
		player1 = new Choice( );
		makePanel( applet );
	}*/
	public Scenario( CSVTokenizer tokenizer, Applet applet )
	{
		//record type,scenario number,Name,difficulty,picture,objective,
		// description,csv map file,army1,army2
		this.number = tokenizer.getShortAt(1);
		this.name = tokenizer.getStringAt(2);
		this.difficulty = tokenizer.getStringAt(3);
		this.imageName = tokenizer.getStringAt(4);
		this.objective = tokenizer.getStringAt(5);
		this.description = tokenizer.getStringAt(6);
		this.mapFile = tokenizer.getStringAt(7);
		// rewrite: only 2 armies.
		this.army1 = tokenizer.getStringAt(8);
		this.army2 = tokenizer.getStringAt(9);

		makePanel( applet );

		DebugLog.println( "XPG9:" + this );
	}

	public String toString()
	{
		return "Scenario," + number + "," + name;
	}

	private void makePanel( Applet applet )
	{
		this.panel = new Panel();
		panel.setLayout(new BorderLayout());

		Panel scenarioPanel = new Panel();
		Panel player1Panel = new Panel();
		Panel player2Panel = new Panel();
		Panel middlePanel = new Panel();
		Panel bottomPanel = new Panel();
		scenarioPanel.setLayout(new BorderLayout());
		player1Panel.setLayout(new GridLayout(1,3));
		player2Panel.setLayout(new GridLayout(1,3));
		middlePanel.setLayout(new GridLayout(3,1));
		bottomPanel.setLayout(new BorderLayout());

		canvas = new DisplayCanvas( applet, new Dimension( 400, 200 ) );
		Image tempArray[] = new Image[1];
		tempArray[0] = ResourceLoader.getImage( this.imageName );
		this.picture = new Picture(0, 0, 400, 200, false, 1, "scenario", tempArray, canvas);
		canvas.add( picture );


		player1 = new Choice( );
		player1.add( "Human" );
		player1.add( "Computer" );
		player1.select( "Human" );
		player1.setEnabled( false );

		player2 = new Choice( );
		player2.add( "Human" );
		player2.add( "Computer" );
		player2.select( "Computer" );
		player2.setEnabled( false );
		
		player1Army = new Choice( );
		player2Army = new Choice( );
		Enumeration armyNames = WPArmy.getArmyNames();
		while ( armyNames.hasMoreElements() )
		{
			String name = (String)armyNames.nextElement();
			player1Army.add( name );
			player2Army.add( name );
		}
		DebugLog.println( "Default to " + army1 + " vs " + army2 );
		player1Army.select( army1 );
		player1Army.setEnabled( true );
		player2Army.select( army2 );
		player2Army.setEnabled( true );

		scenarioPanel.add( "West", new Label( "Scenario " + number + ": " + name ) );
		scenarioPanel.add( "Center", new Label( "Objective: " + objective ) );
		scenarioPanel.add( "East", new Label( "Difficulty: " + difficulty ) );

		player1Panel.add( new Label( "Player 1" ) );
		player1Panel.add( player1 );
		player1Panel.add( player1Army );

		player2Panel.add( new Label( "Player 2" ) );
		player2Panel.add( player2 );
		player2Panel.add( player2Army );

		middlePanel.add( scenarioPanel );
		middlePanel.add( player1Panel );
		middlePanel.add( player2Panel );



		DebugLog.println( "XPG987" );

		TextArea textArea = new TextArea( description, 7, 7, TextArea.SCROLLBARS_VERTICAL_ONLY );
		textArea.setEditable( false );

		bottomPanel.add( "North", middlePanel );
		bottomPanel.add( "Center", textArea );

		panel.add( "North", canvas );
		panel.add( "Center", bottomPanel );
	}


	public Panel getPanel( ItemListener container ) 
	{
		player1Army.addItemListener( container );
		player2Army.addItemListener( container );
		player2.addItemListener( container );
		player1.addItemListener( container );
		// panel.invalidate();
		// Funny, but this is necessary.
		// The panel wont draw the second time,
		// if the size has already been determined.
		panel.setSize(0,0);
		//panel.list();
		return this.panel;
	}

	public Choice getPlayer1ArmyChoice() { return player1Army; }
	public Choice getPlayer2ArmyChoice() { return player2Army; }
	public void setArmy1( String army ) { this.army1 = army; }
	public void setArmy2( String army ) { this.army2 = army; }
	public String getMapFile() { return this.mapFile; }
	public String getName() { return this.name; }
	public String getArmy1() { return this.army1; }
	public String getArmy2() { return this.army2; }

}



