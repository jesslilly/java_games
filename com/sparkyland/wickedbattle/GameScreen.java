package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.physical.Size;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**----------------------------------------------------------------------------
Class GameScreen

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class GameScreen extends CardScreen implements ActionListener, ItemListener
{
	protected WickedBattleCanvas wickedBattleCanvas;
	protected Panel sidePanel, buttonPanel;
	protected Checkbox pauseButton;
	protected Button newGameButton;
	protected Label unitStatsLabel, terrainLabel, whosTurnLabel, helpLabel1, nextUnitLabel;

	public GameScreen ( WickedBattle mainProgram, ScreenManager screenManager )
	{
		super( mainProgram, screenManager );

		// rewrite: hard code.
		wickedBattleCanvas = new WickedBattleCanvas( mainProgram, new Dimension( 324, 396 ), new Size( 11, 9 ));

		sidePanel = new Panel();
		buttonPanel = new Panel();
		pauseButton = new Checkbox( "Paused", false );
		newGameButton = new Button( "New Game" );
		nextUnitLabel = new Label( "(N)ext" );
		unitStatsLabel = new Label( "#-#-#/#" );
		whosTurnLabel = new Label( "Turn: ###" );
		helpLabel1 = new Label( "(F)inish Turn" );
		terrainLabel = new Label( "Defense Bonus " );
	}

	public void init()
	{

		this.setLayout( new BorderLayout() );

		sidePanel.setLayout( new GridLayout( 10, 1 ) );
		sidePanel.add( newGameButton );
		sidePanel.add( pauseButton );
		sidePanel.add( whosTurnLabel );
		sidePanel.add( unitStatsLabel );
		sidePanel.add( terrainLabel );
		sidePanel.add( nextUnitLabel );
		sidePanel.add( helpLabel1 );

		newGameButton.addActionListener( this );
		pauseButton.addItemListener( this );
		
		this.add( "West", wickedBattleCanvas );
		this.add( "Center", sidePanel );

	}
	public void start()
	{
		super.start();
		this.wickedBattleCanvas.start();
		wickedBattleCanvas.requestFocus();
	}
	public void paint(Graphics g) 
	{
		wickedBattleCanvas.repaint();
	}
	public WickedBattleCanvas getWickedBattleCanvas() { return wickedBattleCanvas; }

	public void actionPerformed( ActionEvent e )
	{
		DebugLog.println("ActionEvent" + e.getSource() );
		if ( e.getSource() == newGameButton  )
		{
			// rewrite: finalize everything.
			//wickedBattleCanvas.removeAll();
			screenManager.showScreen( new MainScreen( mainProgram, screenManager ) );
		}
		/*
		if ( e.getSource() == nextUnitButton  )
		{
			// rewrite: Sucks that the mouse click makes the canvas lose focus.
			// this will change once we romve the lame paused functionality.
			wickedBattleCanvas.selectNextUnit();
			wickedBattleCanvas.requestFocus();
		}
		*/
	}
	public void itemStateChanged( ItemEvent e )
	{
		DebugLog.println("ItemEvent" + e.getSource() );
		if ( e.getSource() == pauseButton  )
		{
			boolean checked = pauseButton.getState();
			if ( checked )
			{
				wickedBattleCanvas.setPaused( checked );
			}
			else
				wickedBattleCanvas.requestFocus();
		}
	}
	public Checkbox getPauseButton() { return pauseButton; }

	public void setUnitStatsLabel( String unitStats )
	{
		unitStatsLabel.setText( unitStats );
	}

	public void setTerrainLabel( String terrian )
	{
		terrainLabel.setText( terrian );
	}
	public void setWhosTurnLabel( String who )
	{
		whosTurnLabel.setText( "Turn: " + who );
	}

}
