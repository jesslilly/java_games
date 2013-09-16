package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.ResourceLoader;
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
Class MainScreen

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class MainScreen extends CardScreen implements ActionListener
{
	DisplayCanvas canvas;
	Panel configPanel;
	Button scenarioButton, battleButton, campaignButton;

	public MainScreen ( WickedBattle mainProgram, ScreenManager screenManager )
	{
		super( mainProgram, screenManager );
		this.canvas = new DisplayCanvas( mainProgram, new Dimension( 400, 400 ) );
		this.configPanel = new Panel();

		this.scenarioButton = new Button("Scenario");
		this.battleButton = new Button("Battle");
		this.campaignButton = new Button("Campaign");

		// rewrite: make a config or ini or xml file for the mainscreen.

		scenarioButton.addActionListener( this );
		battleButton.addActionListener( this );
		campaignButton.addActionListener( this );
		battleButton.setEnabled(false);
		campaignButton.setEnabled(false);

		this.setLayout(new BorderLayout( ));
		
		Image im[] = new Image[2];
		im[0] = mainProgram.getImage(mainProgram.getPwd(), "images/eye1.gif");
		im[1] = mainProgram.getImage(mainProgram.getPwd(), "images/eye2.gif");
		BoundedSprite eye = new BoundedSprite( 0, 250, 36, 36, false, Layer.TOP, "eye", im, canvas, 3, 3, 0 );
		eye.setBounds( new Rectangle ( 0, 0, 200, 340) ); 
		eye.setReaction( BoundedSprite.BOUNCE );
		canvas.add ( eye );
		Image f[] = new Image[1];
		f[0] = mainProgram.getImage(mainProgram.getPwd(), "images/wickedbattle.gif");
		// rewrite:  Why am I a\passing in the canvas in the constructor?
		// I can set it when the picture gets added.  ( interface? )
		Picture wickedbattle = new Picture( 0, 0, 400, 400, false, Layer.BOTTOM, "wickedbattle", f, canvas);

		Image im2[] = new Image[4];
		im2[0] = mainProgram.getImage(mainProgram.getPwd(), "images/cloud1.gif");
		im2[1] = mainProgram.getImage(mainProgram.getPwd(), "images/cloud2.gif");
		im2[2] = mainProgram.getImage(mainProgram.getPwd(), "images/cloud3.gif");
		im2[3] = mainProgram.getImage(mainProgram.getPwd(), "images/cloud4.gif");

		BoundedSprite clouds[] = new BoundedSprite[4];
		clouds[0] = new BoundedSprite( -10, 20, 20, 20, false, Layer.TOP, "cloud1", im2, canvas, 5, 3, 0 );
		clouds[1] = new BoundedSprite( 0, 20, 20, 20, false, Layer.TOP, "cloud2", im2, canvas, 6, 4, 0 );
		clouds[2] = new BoundedSprite( 0, 30, 20, 20, false, Layer.TOP, "cloud3", im2, canvas, 4, 5, 0 );
		clouds[3] = new BoundedSprite( 60, 40, 20, 20, false, Layer.TOP, "cloud4", im2, canvas, 3, 6, 0 );
		clouds[0].setBounds( new Rectangle ( -20, -80, 300, 400) ); 
		clouds[1].setBounds( new Rectangle ( 0, 0, 300, 400) ); 
		clouds[2].setBounds( new Rectangle ( -20, -80, 300, 420) ); 
		clouds[3].setBounds( new Rectangle ( -20, -80, 320, 400) ); 
		for ( int i = 0; i < clouds.length; i++)
		{
			clouds[i].setReaction( BoundedSprite.WARP );
			canvas.add ( clouds[i] );
		}


		canvas.add ( wickedbattle );

		configPanel.setLayout(new GridLayout(8,1));
		configPanel.add( scenarioButton );
		configPanel.add( battleButton );
		configPanel.add( campaignButton );

		this.add( "Center",canvas);
		this.add( "East",configPanel);

	}
	public void start()
	{
		super.start();
		this.canvas.start();

		ResourceLoader.loadMusic( "get.mid");
		ResourceLoader.loopMusic( "get" );
		ResourceLoader.loadSound( "gun.au");
		ResourceLoader.playSound( "gun" );
		
	}
	public void actionPerformed( ActionEvent e )
	{
		DebugLog.println("ActionEvent" + e);
		if ( e.getSource() == scenarioButton )
		{
			screenManager.showScreen( new PracticeSetupScreen( mainProgram, screenManager ) );
		}
		if ( e.getSource() == battleButton )
		{
			screenManager.showScreen( new BattleSetupScreen( mainProgram, screenManager ) );
		}
	}

}
