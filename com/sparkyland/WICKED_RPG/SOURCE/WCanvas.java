//package v12;

import java.awt.*;
import java.awt.image.ImageProducer;
import java.applet.*;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.event.*;   // for MouseListener

class WCanvas extends Canvas implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
	go wicked;
	Vector layer1, layer2, layer3;
	Graphics offScreenG; // 2buf
	Image offScreenImage; // 2buf
	TalkWindow talkWindow;
	String nextMapUP, nextMapDOWN, nextMapRIGHT, nextMapLEFT;
	Battle battle;

	boolean tracking;
	int trackerX, trackerY; // These variables may not be used.
	int trackerDistance; // These variables may not be used

	public WCanvas(go wicked) {
		super();
		this.wicked = wicked;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		layer1 = new Vector();
		layer2 = new Vector();
		layer3 = new Vector();
		setNextMaps(" ", " ", " ", " ");

		offScreenImage = wicked.createImage(300,260); // 2buf make offscreen buffer
		offScreenG = offScreenImage.getGraphics(); // 2buf
		offScreenG.setFont(new Font("Helvetica",Font.PLAIN,12));

		talkWindow = new TalkWindow(this);
		talkWindow.setName("talkWindow");

		tracking = false;
		battle = new Battle(this);
	}

// -----------------------------------  START EVENT PROCESSING ----------------------------------------------------

	public void actionPerformed( ActionEvent e )
	{
		wicked.actionPerformed( e );
	}

	public void mousePressed( MouseEvent e )
	{
		wicked.mousePressed( e );
	}

	public void keyPressed ( KeyEvent e )
	{
		wicked.keyPressed( e );
	}
	public void keyReleased ( KeyEvent e )
	{
		wicked.keyReleased ( e );
	}
	public void keyTyped ( KeyEvent e ) {;}
	// Because I implement Mouse Listener interface,
	// My class will be abstract if I do not implement each method of the interface.
	public void mouseReleased(MouseEvent e) {;}
	public void mouseClicked(MouseEvent e) {;}
	public void mouseEntered(MouseEvent e) {;}
	public void mouseExited(MouseEvent e) {;}
	public void mouseDragged(MouseEvent e) {;}
	public void mouseMoved(MouseEvent e) {;}
// -----------------------------------  END EVENT PROCESSING ----------------------------------------------------

	public void showTalk( String talkText )
	{
		if (! talkWindow.getShowing())
		{
			talkWindow.setShowing(true);
			talkWindow.setTalkText( talkText );
		}
	}
	public void removeTalk ()
	{
		talkWindow.setShowing(false);
	}
	public TalkWindow getTalkWindow()
	{
		return talkWindow;
	}

    public void paint(Graphics g) {

	//	offScreenG.setColor(Color.black);
	//	offScreenG.fillRect(0,0,300,300);  // clear buffer
	//	We don't clear the buffer because then we can keep it and use it.
		int i;
		for (i = 0; i < (layer1.size()); i++) {
			((AbstractPicture)layer1.elementAt(i)).paint(offScreenG);
		}
		for (i = 0; i < (layer2.size()); i++) {
			((AbstractPicture)layer2.elementAt(i)).paint(offScreenG);
		}
		for (i = 0; i < (layer3.size()); i++) {
			((AbstractPicture)layer3.elementAt(i)).paint(offScreenG);
		}
		offScreenG.setColor(Color.red);
		offScreenG.fillRect(40,260,5,5);
		if (wicked.getHero() != null)
			offScreenG.drawString("HP:" + wicked.getHero().hitPoints, 0, 260);

		// Create a clipped structure to hold all objects that get clipped.
		// Loop through them here and use drawImage(im,0,0,getclip,getclip,this);
		g.drawImage(offScreenImage,0,0,wicked);
    }

	// Called implicitly with Wcanvas.repaint().
    public void update(Graphics g)
	{
		SimplePicture.updateGlobals();

		if (tracking)
		{
			if (SimplePicture.getGX() == 0 && SimplePicture.getGY() == 0)
			{
				tracking = false;
				SimplePicture.setGVX(0);
				SimplePicture.setGVY(0);
				this.add(wicked.getHero());
			}
		}

		// Update all of the little guys.
		int i;
		for (i = 0; i < layer1.size(); i++) {
			((AbstractPicture)layer1.elementAt(i)).update();
		}
		for (i = 0; i < layer2.size(); i++) {
			((AbstractPicture)layer2.elementAt(i)).update();
		}
		for (i = 0; i < layer3.size(); i++) {
			((AbstractPicture)layer3.elementAt(i)).update();
		}

		paint(g);
	}
	
	public Image getMapImage() {
		ImageProducer producer = offScreenImage.getSource();
		Image temp = createImage(producer);
		return temp;
	}

	public void setTracking()
	{
		trackerX = 0;
		trackerY = 0;
		this.trackerDistance = trackerDistance;
		tracking = true;
	}

	// Add methods.
	// Only add Objects that know how to paint and update themselves.
	// Add at a certain layer please.  layers 1, 2, 3.  Including background.
	// Object's added should have a unique name. Checking is too much work.
	// Go ahead and add objects of the same name, but if you try to get them,
	// you will always get the first object added at the lowest layer.

	// There is also nothing to prevent you from adding 2 objects at the same x,y
	// and the same layer. (BUT YOU SHOULDN'T !!!!!)
	//-----------------------------------------------------------
    public void add(AbstractPicture pict) {
		addToClipTracker( pict );
		battle.add( pict );
		switch (pict.getLayer()) {
		case 1:
			layer1.addElement(pict);
			break;
		case 2:
			layer2.addElement(pict);
			break;
		case 3:
			layer3.addElement(pict);
			break;
		}
    }	
	// Remove methods.
	//-----------------------------------------------------------
    public void remove(String name) {
		System.out.println("WCanvas.remove(\"name\") is deprecated!  Remove(AbstractPicture) instead.");
		AbstractPicture pict = this.get(name);
		this.remove(pict);
	}

    public void remove(AbstractPicture pict)
	{
		switch (pict.getLayer()) {
		case 1:
			layer1.removeElement(pict);
			break;
		case 2:
			layer2.removeElement(pict);
			break;
		case 3:
			layer3.removeElement(pict);
			break;
		}
	}

    public void removeAll() {
		while ( 0 < layer1.size() ) {
			layer1.removeElementAt(0);
		}
		while ( 0 < layer2.size() ) {
			layer2.removeElementAt(0);
		}
		while ( 0 < layer3.size() ) {
			layer3.removeElementAt(0);
		}
    }
	// Get Methods
	//------------------------------------
	public AbstractPicture get( String name ) {
		// Check for name in layer2 first.
		int i;
		for (i = 0; i < layer2.size(); i++) {
			if ( name == ((AbstractPicture)layer2.elementAt(i)).getName() ) {
				return (AbstractPicture)layer2.elementAt(i);
			}
		}
		for (i = 0; i < layer3.size(); i++) {
			if ( name == ((AbstractPicture)layer3.elementAt(i)).getName() ) {
				return (AbstractPicture)layer3.elementAt(i);
			}
		}
		for (i = 0; i < layer1.size(); i++) {
			if ( name == ((AbstractPicture)layer1.elementAt(i)).getName() ) {
				return (AbstractPicture)layer1.elementAt(i);
			}
		}
		System.out.println(name + " isn't in here.");
		return null;
	}

	public AbstractPicture getPictAt( Coordinate xy, int l )
	{
		return getPictAt( xy.getX(), xy.getY(), Layer.MIDDLE );
	}

	public AbstractPicture getPictAt( int x, int y, int l ) {
		int i;
		switch (l) {
		case 1:		
			for (i = 0; i < layer1.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer1.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		case 2:
			for (i = 0; i < layer2.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer2.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		case 3:
			for (i = 0; i < layer3.size(); i++)
			{
				AbstractPicture temp = (AbstractPicture)layer3.elementAt(i);
				if ( temp.getX() == x && temp.getY() == y)
					return temp;
			}
			break;
		default:
			System.out.println("Nice try jerk!  Use a good layer.");
		}
		return null;
	}
	// Must call with layer 1->3
	public boolean getSolidAt(int locx, int locy, int layer) {

		boolean solid = false;
		AbstractPicture pict = getPictAt( locx, locy, layer );
		if (pict != null)
		{
			solid = pict.getSolid();
		}
		//System.out.println("solid at layer" + layer + " at " + (locx) + " " + (locy) + " = " + solid);
		return solid;

	}
	// Return the object that is infront of the hero on his layer.
	public AbstractPicture getTargetOfHero (int targetLayer)
	{
		int layer = targetLayer;
		Hero hero = wicked.getHero();

		if (targetLayer < 1 || targetLayer > 3)
			layer = hero.getLayer();

		Coordinate targetLoc = new Coordinate( hero.getX(), hero.getY() );
		targetLoc.adjust( 20 , hero.getBody().getDirection() );

		return getPictAt( targetLoc, layer);
	}
	public Graphics getOffScreenG()
	{
		return this.offScreenG;
	}
	public void setNextMaps( String right, String left, String up, String down )
	{
		nextMapRIGHT = right;
		nextMapLEFT = left;
		nextMapUP = up;
		nextMapDOWN = down;
	}
	public String getNextMap( Direction dir )
	{
		String nextMap = " ";
		if (dir.getIntDirection() == Direction.RIGHT)
			nextMap = nextMapRIGHT;
		else if (dir.getIntDirection() == Direction.LEFT)
			nextMap = nextMapLEFT;
		else if (dir.getIntDirection() == Direction.UP)
			nextMap = nextMapUP;
		else if (dir.getIntDirection() == Direction.DOWN)
			nextMap = nextMapDOWN;
		return nextMap;
	}

// --------------- Clipping -----------------------------------------
	private void addToClipTracker ( AbstractPicture pict )
	{
		if (pict.isClipped())
		{
			System.out.println("Add " + pict.getName() + " to the clipTrakcer.");
		}
	}


	public Battle getBattle() { return battle; }
	public Hero getHero() { return wicked.getHero(); }
}





