//package v12;

import java.applet.*;
import java.awt.*;
class Hero implements Mortal, AbstractPicture {
	boolean animateWalking, atASquare, weaponOut;
	Weapon weapon;
	public int hitPoints; // duplicate code!!
	WSprite body;

	public Hero(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], go wicked, int freq, int vx, int vy) {
		// f should be [12] but we do not check.  0->7 for walking, 8->11 for attacking.
		body = new WSprite (x, y, w, h, s, l, n, f, wicked, 0, 0, 0);
		animateWalking = false;
		atASquare = true;
		weapon = new Sword("Short Sword", Color.lightGray, wicked);  //orange, pink, yellow, cyan
		hitPoints = 60;
	}
	// Begin: Implement AbstractPicture.
	// ----------------------------------------------------
	public void update()
	{
		// Time the weapon.
		if (weaponOut)
		{
			body.incrementStep();
			if (body.getStep() == 6)
			{
				finishAttack();
			}
		}
		else
		// Step forward.
		{
			body.updateLocation( );
			// only stop the walking with this grid check if they are going that direction.
			// Stop walking at every square.
			if (body.isAtASquare())
			{
				body.setVX( 0 );
				body.setVY( 0 );
				atASquare = true;
				animateWalking = false;
			}
		}
	}
	public void paint( Graphics g )
	{
		if (animateWalking == true) {
			switch (body.getStep()) {
			case 0:
				body.setStep(1);
				break;
			case 1: 
				body.setStep(0);
				break;
			}
			int frame = (body.getDirection().getIntDirection() * 2) + body.getStep();
			body.paint(g, frame);
		}
		else if ( weaponOut )
		{
			int frame = (body.getDirection().getIntDirection() + 8);
			body.paint(g, frame);
		}
		else
		{
			int frame = body.getDirection().getIntDirection() * 2;
			body.paint(g, frame);
		}
	}
	public int getLayer() { return body.getLayer(); }
	public String getName() { return body.getName(); }
	public int getX() { return body.getX(); }
	public int getY() { return body.getY(); }
	public boolean getSolid() { return body.getSolid(); }
	public boolean isClipped() { return body.isClipped(); }
	// End: Implement AbstractPicture.
	// ----------------------------------------------------

	// Begin: Implement Mortal.
	// ----------------------------------------------------------------------------
	public void reduceHitPoints( int damage )
	{
		hitPoints = hitPoints - damage;
	}
	public int getHitPoints( ) { return hitPoints; }
	public void setHitPoints( int number ) { hitPoints = number; }
	// End: Implement Mortal.
	// ----------------------------------------------------------------------------

	public void startWalk(Direction direction) {
		/*if (direction < 0 || direction > 3) {
			System.out.println("Cant' walk in that direction!");
		}*/
		body.setDirection(direction);
		animateWalking = true;
		atASquare = false;
		switch (body.getDirection().getIntDirection()) {
		case Direction.RIGHT:
			body.setVX(5);
			body.setVY(0);
			break;
		case Direction.LEFT: 
			body.setVX(-5);
			body.setVY(0);
			break;
		case Direction.UP: 
			body.setVX(0);
			body.setVY(-5);
			break;
		case Direction.DOWN: 
			body.setVX(0);
			body.setVY(5);
			break;
		}
	}


	public void startAttack ()
	{
		WCanvas gameCanvas = body.getWicked().getGameCanvas();

		AbstractPicture target = gameCanvas.getTargetOfHero(Layer.HERO);

		// begin weapon crap.
		weapon.setDirection( body.getDirection() );
		Coordinate weaponLoc = new Coordinate ( body.getX(), body.getY() );
		weaponLoc.adjust( 20, body.getDirection() );
		body.getWicked().getLoader().playEffect(2);
		weapon.setLocation( weaponLoc );
		if (weaponOut == false)
		{
			gameCanvas.add(weapon);
			weaponOut = true;
		}
		// done weapon crap

		if (target instanceof Monster)
		{
			Monster victim = (Monster)target;
			Battle battle = gameCanvas.getBattle();
			battle.newRound();
			// Hero gets the first attack in any Round.
			battle.takeTurn();
			victim.reduceHitPoints( 3 );
			//battle.commenceRound();
			if (victim.isDead())
			{
				body.getWicked().getLoader().playEffect(0);
				gameCanvas.remove(target);
			}
		}
		else if (target == null)
		{
		}
	}
	private void finishAttack ( )
	{
		body.getWicked().getGameCanvas().remove(weapon);
		body.setStep(0);
		weaponOut = false;
	}


	public boolean atASquare() {
		return atASquare;
	}
	public WSprite getBody() { return body; }
	public void setBody(WSprite w) { body = w; }
}
