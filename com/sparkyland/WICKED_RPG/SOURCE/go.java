//package v12;

import java.applet.*;
import java.awt.*;
import java.net.*; //URL
import java.util.Random;
import java.awt.event.*;   // for ActionEvent

public class go extends Applet implements Runnable, KeyListener {
	URL pwd;
	volatile Thread animation, thisThread;
	Panel buttonPanel;
	WTextField textField1;
	Random random;

	boolean suspended, inputDisabled, talking;  // These vars should not be in this class.
	static final int REFRESH_RATE = 50;    // in ms 20fps.

	WCanvas canvas, gameCanvas;
	WTextArea textArea;
	WButton button1, button2, button3, button4;
	Loader loader;
	MapReader mapReader;
	Hero hero;

public void init() {
	System.out.println(">> init <<");
	setBackground(Color.black);
	initVariables();
	setUpGUI();
	mainMenu();

	gameCanvas.add(new Picture(0, 0, 300, 260, false, 1, "wicked", loader.getPictures(9,9), this));
	gameCanvas.add(new WSprite(120, 140, 20, 20, true, 3, "satellite", loader.getWSprites(0,3), this, 1, -1, 0));
	gameCanvas.add(new WSprite(120, 140, 20, 20, true, 3, "asteroid", loader.getWSprites(4,7), this, 1, 1, 1));
	}


public void initVariables() {
	random = new Random(1317L);
	suspended = talking = inputDisabled = false;
	String temp = getCodeBase().toString();
	temp = temp.substring(0, temp.indexOf("/source")) + "/";
	System.out.println(temp);
	try
	{
		pwd = new URL(temp);
	}
	catch (MalformedURLException e)
	{
	}
	System.out.println(pwd);
	loader = new Loader(this, pwd);
	loader.loadStartMedia();   // load the media.
	SimplePicture.setGVX(0);
	SimplePicture.setGVY(0);
	SimplePicture.setGX(0);
	SimplePicture.setGY(0);
	addKeyListener(this);
	}

public void start() {
	System.out.println(">> start <<");
	loader.loopMusic(0);
    animation = new Thread(this);
	animation.setName("animation");
	if (animation != null) {
		animation.start();   // run() method starts now.  Body of thread.
		}
	}

public void update(Graphics g) {
	paint(g);
	}

public void paint(Graphics g) {
	g.setColor(Color.black);
	g.fillRect(0,0,300,300);  // clear applet.
	gameCanvas.repaint();
	}

public void run() { 
    thisThread = Thread.currentThread();
	while (animation == thisThread)  // threadAPI
		{ // universal animation loop.  paint-update-sleep.
		repaint();
		try 
			{
			animation.sleep (REFRESH_RATE); // start threadAPI
			if (suspended == true) {
				synchronized(this) {
                    while (suspended && animation == thisThread)
						//System.out.println("Animation in busy sleep loop waiting on:" + Thread.currentThread());
						animation.sleep ( 500 ); // half a second.
						// With java 1.0 event API, wait() worked.
						// Now, with 1.1 API, the EventQueue thread is not allowed to notify animation.
                        //wait();
					}// stop threadAPI
				}
			}
		catch (Exception exc)
			{
			};
		}
	}
		

public void stop() {
	System.out.println(">> stop <<");
	loader.stopMusic(0);
	loader.stopMusic(1);
	animation = null; // threadAPI
	}


// -----------------------------------  START EVENT PROCESSING ----------------------------------------------------

public void keyTyped ( KeyEvent e ) {;}
public void keyPressed( KeyEvent event)
{
	int intKey = event.getKeyCode();
	char charKey = (char)intKey;
	// get the direction
	// based on direction, get the change in coordinates.
	if (inputDisabled)
	{
		return;  // very naughty.
	}
	boolean arrowKey = true;
	Hero hero = (Hero)gameCanvas.get("hero");
	Direction directionPressed = new Direction(intKey);  // This should go some where in there V.

	// The hero is right where we want him.  ha ha ha!
	if (hero != null && hero.atASquare())
	{
		if (Direction.isValidDirection( intKey ))
		{
			hero.getBody().setDirection( directionPressed );
			Coordinate deltaCoord = new Coordinate(0,0);
			deltaCoord.adjust( 20, directionPressed );
			Coordinate requestedCoord = 
				new Coordinate (hero.getBody().getX() + deltaCoord.getX(), hero.getBody().getY() + deltaCoord.getY());

			// Check if we are going to go off the screen.
			// Could make a Coordinate method to handle this check.
			if ((requestedCoord.getX() > 280)
				|| (requestedCoord.getX() < 0)
				|| (requestedCoord.getY() > 240)
				|| (requestedCoord.getY() < 0))
			{
				System.out.println("Out of bounds!");

				Coordinate offsetCoord = new Coordinate(0,0);
				// should be able to multiply and certainly subtract coordinates!!!!
				offsetCoord.setX( deltaCoord.getX() * 15 );   // magic numbers!!!!
				offsetCoord.setY( deltaCoord.getY() * 13 );

				gameCanvas.removeAll();
				(gameCanvas.getBattle()).removeAll();
				// Set the hero's coordinates and direction for when he comes back on the stage.
				hero.getBody().setDirection( directionPressed );
				hero.getBody().setX(requestedCoord.getX() - offsetCoord.getX());  // could pass in the coordinate, B!
				hero.getBody().setY(requestedCoord.getY() - offsetCoord.getY());
				// take the current offscreen image and turn it into a Picture.
				Image wholeScreen[] = new Image[1];
				wholeScreen[0] = gameCanvas.getMapImage();
				// Add this Picture to the gameCanvas Layer 3 to keep it on top.
				gameCanvas.add(new Picture
					(-(offsetCoord.getX()), -(offsetCoord.getY()),
					300, 260, false, 3, "wholeScreen", wholeScreen, this ));
				SimplePicture.setGX(offsetCoord.getX());
				SimplePicture.setGY(offsetCoord.getY());
				// find out what file we need to load.
				String nextMap = gameCanvas.getNextMap( directionPressed );
				System.out.println(this.getPwd().toString() + "files/" + nextMap + ".txt");
				mapReader.loadCanvasFromFile(this.getPwd().toString() + "files/" + nextMap + ".txt");
				// Tell the game canvas to monitor GX and GY to stop to Global shift.
				gameCanvas.setTracking();
				// set GVY and GVX.
				SimplePicture.setGVX( - (deltaCoord.getX()) / 2 );
				SimplePicture.setGVY( - (deltaCoord.getY()) / 2 );
			}
			// check the solidity of the thing we are going to step on.
			else
			{
				boolean isSolid = false;

				isSolid = gameCanvas.getSolidAt(requestedCoord.getX(), requestedCoord.getY(), 1);  // layer 1
				if (isSolid == false)
				{
					isSolid = gameCanvas.getSolidAt(requestedCoord.getX(), requestedCoord.getY(), 2);  // layer 2
				}
				if (isSolid == false)
				{
					System.out.println("X<" + requestedCoord.getX() + "> Y<" + requestedCoord.getY() + ">");
					hero.startWalk( directionPressed );
				}
				else
				{
					// It is solid. bump!
					hero.getBody().setDirection( directionPressed );
					//System.out.println("SOLLID LAYER 2!");
				}

			}
		}
		else // Not a directional key.
		{
			switch (charKey)
			{
				case KeyEvent.VK_A : // (a)ction
				{
					if (talking)
					{
						gameCanvas.removeTalk();
						talking = false;
					}
					else
					{
						AbstractPicture target = (AbstractPicture)gameCanvas.getTargetOfHero(Layer.HERO);
						if (target instanceof Mortal)
						{ 
							Mortal speaker = (Mortal)target;
							if (speaker instanceof Monster)
							{
								gameCanvas.showTalk( ((Monster)speaker).getTalk(0,0) );
								talking = true;
								//System.out.println( speaker.getTalk(0,0) );
							} 
						}
					}
					break;
				}
				case KeyEvent.VK_S : // (s)trike
				{
					hero.startAttack();
					break;
				}
				case KeyEvent.VK_C : // Create prototype
				{
					mapReader.createPrototypes();
					break;
				}
			}
		}
	}
}


public void keyReleased( KeyEvent event )
{
	int intKey = event.getKeyCode();
	char charKey = (char)intKey;
	Hero hero = (Hero)gameCanvas.get("hero");
	if (hero != null)
	{
		switch (charKey)
		{
			case KeyEvent.VK_A : // (a)ction
			{
				break;
			}
		}
	}
}



public void actionPerformed( ActionEvent e )
{
	System.out.println("ActionEvent");
	String label = e.getActionCommand();
	if ("Start Game".equals(label) ) {
		this.suspendAnimation();
		this.choseCharacter();
		}
	if ("About".equals(label) ) {
		this.suspendAnimation();
		this.aboutWicked();
		}
	if ("Back".equals(label) ) {
		this.mainMenu();
		this.resumeAnimation();
		}
	if ("OK".equals(label)) {
		this.beginGame();
		this.resumeAnimation();
		}
}



public void mousePressed( MouseEvent e )
{
	e.consume();
	int x = e.getX(), y = e.getY();
	System.out.println("X: " + x + " Y: " + y);
	int newVX = random.nextInt();
	int newVY = random.nextInt();
	int avx, avy;
	WSprite tempSprite;
	
	// All this crap is not very object oriented!!!!!!!
	tempSprite = (WSprite)gameCanvas.get("satellite");
	if ((tempSprite != null) && (tempSprite.clicked(x,y) == true)) {
		loader.playEffect(1);
		avx = (0 - Math.abs(newVX % 7));
		avy = (newVY % 7);
		tempSprite.setVelocity(avx, avy);
		tempSprite.setFrequency(Math.abs(avy));
		}
	tempSprite = (WSprite)gameCanvas.get("asteroid");
	if ((tempSprite != null) && (tempSprite.clicked(x,y) == true)) {
		loader.playEffect(0);
		avx = tempSprite.getVX() + (newVX % 2);
		avy = tempSprite.getVY() + (newVY % 2);
		tempSprite.setVelocity(avx, avy);
		tempSprite.setFrequency(tempSprite.getFrequency() + 1);
		}
}

// -----------------------------------  END EVENT PROCESSING ----------------------------------------------------

public void beginGame() {
	// in the future, it will be guiManager.begineGame();
	SimplePicture.setGX(0);
	SimplePicture.setGY(0);

	this.removeAll();
	loader.stopMusic(0);
	loader.loopMusic(1);

	gameCanvas.removeAll();
	mapReader.loadCanvasFromFile(pwd.toString() + "files/level1.txt");
	hero = new Hero(140, 100, 20, 20, false, 2, "hero", loader.getPersons(0,11), this, 1, 0, 0);
	gameCanvas.add(hero);

	this.add("Center",gameCanvas);
	this.add("South",buttonPanel);
	this.repaint();
	}

public void choseCharacter() {
	// in the future, it will be guiManager.choseCharacter();

	this.remove(gameCanvas);

	buttonPanel.removeAll();
	button2.setLabel("<<Previous");
	button3.setLabel("Next>>");
	button4.setLabel("OK");
	textField1.setText("Sam Lilly");
	buttonPanel.add(textField1);
	buttonPanel.add(button2);
	buttonPanel.add(button3);
	buttonPanel.add(button4);
	buttonPanel.doLayout();	// deprecated layout();

	textArea.setText("Sam Lilly.\n"
		+ "Country of Origin: Brechnia\n"
		+ "Mission: Keepin' it real.\n"
		+ "Moves: A for Action and S for Strike!\n"
		+ "Arrow keys move me.");
	canvas.add(	new Picture(0, 0, 80, 80, false, 1, "sam", loader.getPictures(7,7), this));
	canvas.setBounds(0,0,80,80); // deprecated resize(80,80)
	this.add("North",canvas);
	this.add("Center",textArea);

	this.doLayout();	// deprecated layout();
	this.repaint();
	}

public void mainMenu() {
	// in the future, it will be guiManager....();

	this.remove(textArea);
	this.add(gameCanvas);

	button4.setLabel("About");
	button1.setEnabled(true); // deprecated enable()
	button2.setEnabled(true);
	button3.setEnabled(true);
	button4.setEnabled(true);

	this.doLayout();	// deprecated layout();
	this.repaint();
	}

public void aboutWicked() {
	// in the future, it will be guiManager....();

	this.remove(gameCanvas);

	button1.setEnabled(false);   // deprecated disable()
	button2.setEnabled(false);
	button3.setEnabled(false);
	button4.setLabel("Back");

	this.add("Center",textArea);

	this.doLayout();	// deprecated layout();
	}


public void setUpGUI() {
	// in the future, it will be guiManager....();

	WButton.setClickSound(loader.getEffect(0));
	gameCanvas = new WCanvas(this);
	mapReader = new MapReader(gameCanvas, loader, this);
	canvas = new WCanvas(this);
	button1 = new WButton("Start Game");
	button2 = new WButton("Load Game");
	button3 = new WButton("Intro");
	button4 = new WButton("About");
	button1.addActionListener( gameCanvas );
	button2.addActionListener( gameCanvas );
	button3.addActionListener( gameCanvas );
	button4.addActionListener( gameCanvas );
	textArea = new WTextArea();
	textField1 = new WTextField();
	buttonPanel = new Panel();

	textArea.setText(loader.getFileContents("about.txt"));
	//textArea.setWordWrap(true);
	textArea.setEditable(false);

	buttonPanel.setLayout(new GridLayout(1,4));  //arrange crap in a grid
	buttonPanel.add(button1); //add the buttons
	buttonPanel.add(button2);
	buttonPanel.add(button3);
	buttonPanel.add(button4);

	this.setLayout(new BorderLayout()); //set the layout
	this.add("Center",gameCanvas);
	this.add("South",buttonPanel);
	}

	/*---------------------------------
	Setters and getters.
	---------------------------------*/
	public void suspendAnimation() { // threadAPI
		suspended = true; 
	}
	public void resumeAnimation() { // threadAPI
		suspended = false; 
		System.out.println("In Java 1.0 event model, notify() woke my animation back up.");
	}
	public URL getPwd() { return pwd; }
	public Hero getHero() { return hero; }
	public Random getRandom() { return random; }
	public boolean getSuspended() { return suspended; }
	public WCanvas getGameCanvas() { return gameCanvas; }
	public Loader getLoader() { return loader; }
	public MapReader getMapReader() { return mapReader; }
} // end class