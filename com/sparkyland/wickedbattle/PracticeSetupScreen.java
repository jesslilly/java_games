package com.sparkyland.wickedbattle;

import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.videogame.DisplayCanvas;
import com.sparkyland.spartique.videogame.sprite.Picture;
import com.sparkyland.spartique.videogame.sprite.BoundedSprite;
import com.sparkyland.spartique.physical.Size;
import com.sparkyland.spartique.physical.Layer;
import com.sparkyland.spartique.common.SpinVector;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.lang.Integer;

/**----------------------------------------------------------------------------
Class PracticeSetupScreen

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class PracticeSetupScreen extends CardScreen implements ActionListener, ItemListener
{
	DisplayCanvas canvas;
	Panel configPanel, scenarioPanel;
	Label tag1, tag2;
	Button startScenario;
	Button nextScenario;
	SpinVector scenarios;

	public PracticeSetupScreen ( WickedBattle mainProgram, ScreenManager screenManager )
	{
		super( mainProgram, screenManager );

		scenarios = CSVParser.loadScenarios();
		scenarios.pointToFirstObject();
		Scenario currentScenario = (Scenario)scenarios.getSelectedObject();
		DebugLog.println("currentScenario" + currentScenario );

		this.scenarioPanel = currentScenario.getPanel( this );
		DebugLog.println("scenarioPanel" + scenarioPanel );

		this.configPanel = new Panel();

		this.tag1 = new Label( " " );
		this.tag2 = new Label( " " );

		this.startScenario = new Button("Start Scenario");
		this.nextScenario = new Button("Next Scenario");



		startScenario.setEnabled(true);
		nextScenario.setEnabled(true);
		//3/20/2002startScenario.setBackground( mainProgram.getBackground() );
		// rewrite: make this button stand out some other way.
		//3/20/2002startScenario.setForeground( Color.yellow );
		startScenario.addActionListener( this );
		nextScenario.addActionListener( this );

		this.setLayout(new BorderLayout( ));
		
		configPanel.setLayout(new GridLayout(1,5));
		configPanel.add( tag1 );
		configPanel.add( tag2 );
		configPanel.add( nextScenario );
		configPanel.add( startScenario );

		this.add( "Center", scenarioPanel );
		this.add( "South", configPanel);

	}
	public void start()
	{
		super.start();
	}
	public void itemStateChanged( ItemEvent e )
	{
		DebugLog.println("ItemEvent" + e.getSource() + " " + e.getItem() );

		Scenario currentScenario = (Scenario)scenarios.getSelectedObject();
		if ( e.getSource() == currentScenario.getPlayer1ArmyChoice() )
		{
			currentScenario.setArmy1( e.getItem().toString() );
			DebugLog.println("Player 1 Army " + e.getItem() );
		}
		else if ( e.getSource() == currentScenario.getPlayer2ArmyChoice() )
		{
			currentScenario.setArmy2( e.getItem().toString() );
			DebugLog.println("Player 2 Army " + e.getItem() );
		}
		else
		{
			DebugLog.println("Error! ItemEvent" + e );
		}
	}
	public void actionPerformed( ActionEvent e )
	{
		DebugLog.println("ActionEvent" + e.getActionCommand() );

		if ( e.getSource() == startScenario )
		{
			GameScreen gameScreen = new GameScreen( mainProgram, screenManager );
			screenManager.showScreen( gameScreen );

			// Show the screen before we create the battle so we can initialize the Labels.
			// rewrite: This kindof sucks.  WickedBattleCanvas should be a Screen in the
			// screen manager.  The side panel would be part of it, so we can get at
			// those widgets more easily.

			Scenario currentScenario = (Scenario)scenarios.getSelectedObject();
			gameScreen.getWickedBattleCanvas().makeScenario( currentScenario );

		}

		if ( e.getSource() == nextScenario )
		{
			this.remove( scenarioPanel );

			scenarios.getNextObject();
			Scenario currentScenario = (Scenario)scenarios.getSelectedObject();
			DebugLog.println("currentScenario" + currentScenario );
			this.scenarioPanel = currentScenario.getPanel( this );
			//DebugLog.println("scenarioPanel" + scenarioPanel );

			this.add( scenarioPanel );
			mainProgram.validateTree();
		}
	}

}
