package com.sparkyland.remembery;

import com.sparkyland.spartique.videogame.GridGameCanvas;
import com.sparkyland.spartique.videogame.CSVCanvasLoader;
import com.sparkyland.spartique.videogame.HighScoreSaver;
import com.sparkyland.spartique.videogame.GameApplet;
import com.sparkyland.spartique.videogame.cards.*;
import com.sparkyland.spartique.videogame.sprite.*;
import com.sparkyland.spartique.physical.*;
import com.sparkyland.spartique.common.DebugLog;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.common.RandomNumGen;
import com.sparkyland.spartique.gui.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.lang.Integer;

// rewrite: a lot of this functionality maybe should be pushed to other classes.
public class RememberyCanvas extends GridGameCanvas
{
	private WButton cards[][];
	private FlipCard currentCards[];
	private WCounter clickCounter;
	private WTimer timer;
	private Vector deck;
	private String deckFileName;
	// rewrite: use spin vecotr.
	private int currentCardIndex, numberOfMatches;
	private int numRows, numCols;
	private final int matchesToWin = 7;
	private AnimatedPicture bugle;

	public RememberyCanvas(Applet mainProgram, Dimension size, Size gridRowsCols )
	{
		super(mainProgram, size, gridRowsCols);
		numRows = 2;
		numCols = 7;
		this.numberOfMatches = 0;
		cards = new WButton[numCols][numRows];
		currentCards = new FlipCard[2];
		currentCardIndex = 0;
		deck = new Vector();
		deckFileName = "knomes-deck.csv";
		defaultDeck();
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		super.mouseReleased( e );
		String action = "";
		Mouseable m = getCurrentMouseable();
		if ( m != null && m.wasClicked() )
		{
			action = m.getActionCommand();
		}

		DebugLog.println("Clicked " + action );
		if ( m instanceof FlipCard )
		{

			FlipCard card = (FlipCard)m;
			if ( card.isFaceUp() )
			{
				ResourceLoader.playSound( "beep" );
			}
			else
			{
				timer.setTiming( true );

				// rewrite: crappy crappy code.
				ResourceLoader.playSound( "click2" );
				card.flip();
				clickCounter.increment();
				bugle.setStep(bugle.getFrequency());
				bugle.animateOnce();

				switch ( currentCardIndex )
				{
				case 0 :
					{
						// rewrite: dupe code below.
						currentCards[ currentCardIndex ] = (FlipCard)card;
						currentCardIndex++;
						break;
					}
				case 1 :
					{
						if ( currentCards[0].getActionCommand().equals( card.getActionCommand() )  )
						{
							// Match!
							ResourceLoader.playSound( "click1" );
							currentCardIndex = 0;
							numberOfMatches++;
							// See if they won the game.
							if ( isComplete() )
							{
								addComplete();
							}
						}
						else
						{
							// Second Card.  No match.
							currentCards[ currentCardIndex ] = (FlipCard)card;
							currentCardIndex++;
						}
						break;
					}
				default :
					{
						// Third card in a row clicked.  Flip the other 2.
						currentCards[0].flip();
						currentCards[1].flip();

						currentCardIndex = 0;
						currentCards[ currentCardIndex ] = (FlipCard)card;
						currentCardIndex++;
						break;
					}
				}
			}
		}
		else if ( action.equals("previousDeck") || action.equals("nextDeck") )
		{
			
			WMultipleChoice deckChoice = (WMultipleChoice)this.get( "deckName" );
			if ( deckChoice != null )
			{
				WLabel deckLabel = (WLabel)deckChoice.getChoice();
				this.deckFileName = deckLabel.getName();
			}
		}
		else if ( action.equals("newGame"))
		{
			this.loadNewDeck( this.deckFileName );
			this.gameScreen();
		}
		else if ( action.equals("gameOptions"))
		{
			this.deckFileName = "knomes-deck.csv";
			this.optionsScreen();
		}
	}
	protected void optionsScreen()
	{
		
	}
	protected void gameScreen()
	{
		this.numberOfMatches = 0;

		this.bugle = (AnimatedPicture)this.get( "bugle" );
		bugle.setAnimating(false);

		Image[] b = ResourceLoader.getPictures( "buttonbackground", 0, 0 );

		this.clickCounter = new WCounter(180, 400, 40, 30, false, 1, "counter", b, this, 0, 0, 0 );
		this.add( clickCounter );

		this.timer = new WTimer(300, 400, 40, 30, false, 1, "counter", b, this, 0, 0, 0 );
		this.add( timer );

		addCards();

		// rewrite: debug only. to be removed.
		//addComplete();
	}
	private void defaultDeck()
	{
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-face", ResourceLoader.getPictures( "knome-face", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-face", ResourceLoader.getPictures( "knome-face", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-lady", ResourceLoader.getPictures( "knome-lady", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-lady", ResourceLoader.getPictures( "knome-lady", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-in-tree", ResourceLoader.getPictures( "knome-in-tree", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-in-tree", ResourceLoader.getPictures( "knome-in-tree", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-flower", ResourceLoader.getPictures( "knome-on-a-flower", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-flower", ResourceLoader.getPictures( "knome-on-a-flower", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "mrseriouswtree", ResourceLoader.getPictures( "mrseriouswtree", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "mrseriouswtree", ResourceLoader.getPictures( "mrseriouswtree", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-lily", ResourceLoader.getPictures( "knome-on-a-lily", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-lily", ResourceLoader.getPictures( "knome-on-a-lily", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-log", ResourceLoader.getPictures( "knome-on-a-log", 0, 0 ), this, 0, 0, 0 ) );
		deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, "knome-on-a-log", ResourceLoader.getPictures( "knome-on-a-log", 0, 0 ), this, 0, 0, 0 ) );
	}
	private void loadNewDeck( String csvFileName )
	{
		deck.removeAllElements();
		Vector cardNames = ResourceLoader.getFileContents( "data", csvFileName );

		for ( int i = 0; i < 14; i++ )
		{
			String cardName = (String)cardNames.elementAt(i);
			deck.addElement( new FlipCard(0, 0, 80, 120, false, 1, cardName, ResourceLoader.getPictures( cardName, 0, 0 ), this, 0, 0, 0 ) );
		}
	}
	private void addCards()
	{
		// get a deck.
		// there are 7 cards right now.
		int cardNum;
		WButton tempCard;
		Vector cardNumbers = new Vector();
		for ( int i=0; i<14; i++)
		{
			cardNumbers.addElement( new Integer(i) );
		}

		// rewrite: hard coded array indexes.
		for ( int colNum = 0; colNum < numCols; colNum++ )
		{
			for ( int rowNum = 0; rowNum < numRows; rowNum++ )
			{
				cardNum = RandomNumGen.rollXSidedDie( cardNumbers.size() - 1 );
				int tempInt = ((Integer)cardNumbers.elementAt( cardNum )).intValue();
				cardNumbers.removeElementAt( cardNum );
				DebugLog.println("Add a card " + tempInt );
				tempCard = (FlipCard)deck.elementAt( tempInt );
				tempCard.setX( colNum * 80 );
				tempCard.setY( rowNum * 120 + 100 );
				this.add( tempCard );
			}
		}

	}
	private boolean isComplete() { return numberOfMatches >= matchesToWin; }
	private void addComplete()
	{
		//bugle.setAnimating(true);
		timer.setTiming( false );
		//popHighScoreEntry();
	}

	private void popHighScoreEntry()
	{

		String winMessage = 
			"You win! " +
			clickCounter.getLabel() + " clicks in " + 
			timer.getLabel() + " seconds!";

		DebugLog.println( winMessage );

		this.add( new WLabel(300, 340, 100, 30, false, 3, winMessage, ResourceLoader.getPictures( "buttonbackground", 0, 0 ), this, 0, 0, 0 ) );

		// rewrite: this cast to game applet is so lame.
		WPopupEntry popup = new WPopupEntry( "High Score!", "Enter your name: ", ((GameApplet)mainProgram).getFrame() );
		popup.popUp();

		HighScoreSaver saver = new HighScoreSaver( "data", "all-time-high-scores.csv" );

		// 50,20,0:10.0,2005-01-02 10:00:00,Knomes,NIRAC P L
		// 51,20,0:11.0,2005-02-24 10:00:00,Farm,Popsicle Master
		// 52,20,0:12.0,2005-03-17 10:00:00,Weird?!,spartique
		// 53,20,0:13.0,2005-04-14 10:00:00,Knomes,BoardMan
		// 54,22,0:10.0,2005-05-11 10:00:00,Knomes,Sasha Smith
		// rewrite: bad line end character and hard code.
		String scoreLine = "50," + 
			clickCounter.getLabel() + "," + 
			timer.getLabel() + "," + 
			"2003-05-14 00:00:00," +
			"knomes," +
			popup.getAnswer();

		DebugLog.println( "Write this core to the file: " + scoreLine );
		saver.saveScore( scoreLine );

	}


}
