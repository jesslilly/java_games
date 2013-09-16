import java.awt.*;

class TalkWindow extends SimplePicture
{
	WCanvas gameCanvas;
	boolean showing;
	String talkText;
	FontMetrics talkMetrics;
	Coordinate cursor;			// relative to the window, not the screen.
	int character;				// number of characters on line;
	int line;

	Font talkFont;
	String displayString[];

	public TalkWindow(WCanvas wCanvas)
	{
		locx = locy = 20; //140;
		cursor = new Coordinate();
		width = 260; // constant
		height = 120; 
		layer = Layer.TOP;
		solid = false;

		gameCanvas = wCanvas;
		showing = false;
		talkText = "Init";
		talkFont = new Font("Helvetica",Font.PLAIN,12);
		talkMetrics = wCanvas.getFontMetrics(talkFont);
		displayString = new String[6];
		clearDisplayString();
	}

	public void update()
	{
		if (character < talkText.length() - 1)
			character++;
		else
			character = talkText.length() - 1;
		displayString[ line ] = talkText.substring(0, character);
		// check if this character is a space.
		if (talkText.charAt(character) == ' ')
		{
			// find the location of the next space.
			int spaceIndex = talkText.indexOf(' ', character);
			int spaceWidth = talkMetrics.stringWidth(talkText.substring(0,spaceIndex));
			System.out.println("Next space:" + spaceWidth);
			// if the location is greater than 240, increase a line.
			if ( spaceWidth >= 240)
			{
				incrementLine();
			}
		}
		this.setCursorX( talkMetrics.stringWidth( displayString[ line ] ) );
	}
	
	public void paint (Graphics g)
	{
		g.setColor(Color.blue);
		g.fillRect(locx, locy, width, height);
		g.setColor(Color.white);
		for (int i = 0; i < displayString.length ; i++ )
		{
			g.drawString(displayString[i], locx, (locy + 12 + (20 * i) ));
		}
		g.fillRect(cursor.getX(), cursor.getY(), 10, 20);
	}

	public void setTalkText ( String talkText )
	{
		this.talkText = talkText;
	}
	public void setShowing ( boolean s )
	{
		showing = s;
		if (showing)
			gameCanvas.add(this);
		else
		{
			gameCanvas.remove(this);
			clearDisplayString();
		}
	}

	public boolean getShowing ()
	{
		return this.showing;
	}

	private void clearDisplayString()
	{
		for (int i = 0; i < displayString.length ; i++)
		{
			displayString[i] = " ";
		}
		character = 0;
		line = 0;
		setCursorX(5);
		setCursorY(10);
	}
	private void setCursorX( int x )
	{
		cursor.setX(x + 25);
	}
	private void setCursorY( int y )
	{
		cursor.setY(y + 5);
	}
	private void incrementLine()
	{
		// increment 1 line.
		talkText = talkText.substring( character + 1, talkText.length() );
		if (line >= 5)
			scrollUp();
		else
		{
			line++;
			cursor.setY( cursor.getY() + 20 );
		}
		character = 1;
		setCursorX(0);
	}
	private void scrollUp()
	{
		for (int i = 0; i < displayString.length - 1; i++)
		{
			displayString[ i ] = displayString[ i + 1];
		}
		displayString[ displayString.length - 1 ] = " ";
	}
}
