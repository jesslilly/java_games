package com.sparkyland.wickedbattle;

import java.awt.*;

/**----------------------------------------------------------------------------
Class ArmyStatsPanel

@author Jess M. Lilly
@version 1
@see full-classname
@see full-classname#method-name
@param parameter-name description
@return description
@exception full-classname description
-----------------------------------------------------------------------------*/

public class ArmyStatsPanel extends Container
{
	protected WPArmy army;
	protected Label statisticsLabels[];
	private final int NUMLABELS = 8;

	public ArmyStatsPanel ( WPArmy army )
	{
		super();
		this.army = army;
		
		statisticsLabels = new Label[ NUMLABELS ];
		for ( int label = 0; label < NUMLABELS; label++ )
		{
			statisticsLabels[ label ] = new Label();
		}

		this.setLayout( new GridLayout( NUMLABELS , 1 ) );

		statisticsLabels[0].setText( army.getName() );
		statisticsLabels[1].setText( army.getStrength() + "/" + army.getSize() );

		for ( int label = 0; label < NUMLABELS; label++ )
		{
			this.add( statisticsLabels[ label ] );
		}

	}
	public void update()
	{
		statisticsLabels[1].setText( army.getStrength() + "/" + army.getSize() );
	}
}
