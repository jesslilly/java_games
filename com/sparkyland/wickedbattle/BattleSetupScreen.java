package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.videogame.sprite.Picture;
import com.sparkyland.spartique.videogame.sprite.BoundedSprite;
import com.sparkyland.spartique.physical.Size;
import com.sparkyland.spartique.physical.Layer;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Integer;

/**----------------------------------------------------------------------------
Class BattleSetupScreen

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class BattleSetupScreen extends CardScreen implements ActionListener
{
	DisplayCanvas canvas;
	Panel configPanel;
	Label tag1, tag2;
	Choice numPlayer1Units, numPlayer2Units;
	Button startGameButton;

	public BattleSetupScreen ( WickedBattle mainProgram, ScreenManager screenManager )
	{
		super( mainProgram, screenManager );
		this.canvas = new DisplayCanvas( mainProgram, new Dimension( 200, 60 ) );
		this.configPanel = new Panel();

		this.tag1 = new Label( "Player 1 Units:" );
		this.numPlayer1Units = new Choice( );
		this.tag2 = new Label( "Player 2 Units:" );
		this.numPlayer2Units = new Choice( );

		this.startGameButton = new Button("Start Game");


		// rewrite: make a config or ini or xml file for the mainscreen.
		// rewrite: pretty sweet dupe code, Jess.
		numPlayer1Units.add( "70" );
		numPlayer1Units.add( "60" );
		numPlayer1Units.add( "50" );
		numPlayer1Units.add( "40" );
		numPlayer1Units.add( "30" );
		numPlayer1Units.select( "40" );
		//3/20/2002numPlayer1Units.setBackground( mainProgram.getBackground() );
		//3/20/2002numPlayer1Units.setForeground( mainProgram.getForeground() );
		numPlayer2Units.add( "70" );
		numPlayer2Units.add( "60" );
		numPlayer2Units.add( "50" );
		numPlayer2Units.add( "40" );
		numPlayer2Units.add( "30" );
		numPlayer2Units.select( "40" );
		//3/20/2002numPlayer2Units.setBackground( mainProgram.getBackground() );
		//3/20/2002numPlayer2Units.setForeground( mainProgram.getForeground() );


		startGameButton.setEnabled(true);
		//3/20/2002startGameButton.setBackground( mainProgram.getBackground() );
		// rewrite: make this button stand out some other way.
		//3/20/2002startGameButton.setForeground( Color.yellow );
		startGameButton.addActionListener( this );

		this.setLayout(new BorderLayout( ));
		
		Image im[] = new Image[1];
		im[0] = mainProgram.getImage(mainProgram.getPwd(), "images/cavalry1.gif");
		BoundedSprite eye = new BoundedSprite( 0, 24, 36, 36, false, Layer.TOP, "eye", im, canvas, 3, 3, 0 );
		eye.setBounds( new Rectangle ( 0, 0, 200, 340) ); 
		eye.setReaction( BoundedSprite.BOUNCE );
		canvas.add ( eye );
		/*
		Image f[] = new Image[1];
		f[0] = mainProgram.getImage(mainProgram.getPwd(), "images/wickedbattle.gif");
		Picture wickedbattle = new Picture( 0, 0, 200, 60, false, Layer.BOTTOM, "wickedbattle", f, canvas);
		canvas.add ( wickedbattle );*/

		configPanel.setLayout(new GridLayout(1,5));
		configPanel.add( tag1 );
		configPanel.add( numPlayer1Units );
		configPanel.add( tag2 );
		configPanel.add( numPlayer2Units );
		configPanel.add( startGameButton );

		// rewrite: Come from a file pease...  This can be a backup method in case the file doesn't show up.
		TextArea instructions = new TextArea( "How to Play\nKeys: arrow keys and <space>.\n1. Use the arrow keys to highlight a unit from the camp.\n2. Press <space> to select the unit.\n3. Use the arrow keys to move the unit up to the enemies.\n4. Press <space> again to place the unit.\n   It will attack if it is next to an enemy.\nSpecial: Archers can shoot arrows vertically!");
		//3/20/2002instructions.setBackground( mainProgram.getBackground() );
		instructions.setEditable( false );
//		instructions.setForeground( Color.white );
//		instructions.setFont( new Font( "SansSerif", Font.PLAIN, 10 ) );
		this.add( "North",canvas);
		this.add( "Center", instructions );
		this.add( "South",configPanel);

	}
	public void start()
	{
		super.start();
		this.canvas.start();
	}
	public void actionPerformed( ActionEvent e )
	{
		DebugLog.println("ActionEvent" + e);
		String label = e.getActionCommand();
		if ("Start Game".equals(label) )
		{
			int num1 = Integer.parseInt( numPlayer1Units.getSelectedItem() );
			int num2 = Integer.parseInt( numPlayer2Units.getSelectedItem() );
			DebugLog.println( "player 1 " + num1 + " numPlayer2Units: " +  num2 );

			GameScreen gameScreen = new GameScreen( mainProgram, screenManager );
			//gameScreen.getWickedBattleCanvas().makeQuickAttckCastleBattle();
			screenManager.showScreen( gameScreen );
		}
	}

}
