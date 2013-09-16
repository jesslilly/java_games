//package v12;
import java.awt.Image;
import java.awt.Graphics;
import java.applet.Applet;

/////////////////////////////////////////////////////////////////
public class Monster implements AbstractPicture, Mortal, Talker {
	static int DAMAGE = 1;
	static int RANGE = 3;
	static int GLOBVELOCITY = 4;

	SimplePicture body;
	int hitPoints;
	int attackStep;
	WSprite glob;
	AnimatedPicture explosion;
	boolean exploding;
	int explosionTime;
	Weapon weapon;
	boolean attacking;
	int turn;				// which turn is mine?
	boolean myTurn;
	boolean countered;		// Have I countered this round?

	public Monster(int x, int y, int w, int h, boolean s, int l, String n, Image f[], go wicked)
	{
		this(x, y, w, h, s, l, n, f, wicked, 0, 0, 0);
	}
	public Monster(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], go wicked, int freq, int vx, int vy)
	{
		body = new AnimatedPicture( x, y, w, h, s, l, n, f, wicked, freq );
		hitPoints = 20;
		turn = 0;
		countered = false;
		Image frames[] = new Image[1];
		frames[0] = f[0];
		glob = new WSprite( x, x, w, h, false, Layer.TOP, "glob", frames, wicked, 1, 1, 1 );
		weapon = new RangedWeapon( 2 );
		Image frames2[] = new Image[2];
		frames2[0] = wicked.getLoader().getMortals(1);
		frames2[1] = null;
		explosion = new AnimatedPicture( x, x, w, h, false, Layer.TOP, "explosion",frames2, wicked, 1);
		explosion.setLifeSpan(6);
		explosionTime = 0;
		exploding = false;
	}

	// Begin: Implement AbstractPicture.
	// ----------------------------------------------------
	public void update() {
		body.update();

		if (attacking)
		{
			attackStep++;
			if (attackStep == 15)
			{
				finishAttack();
			}
		}
		if (exploding)
		{
			explosionTime++;
			if (explosionTime > 6)
			{
				exploding = false;
				explosionTime = 0;
				startAttack(body.getWicked().getHero());
			}
		}
		if (Battle.isItMyTurn(this.turn))
		{
			startAttack(body.getWicked().getHero());
		}

	}
	public void paint( Graphics g )
	{
		body.paint(g);
		if (exploding)
		{
			explosion.paint(g);
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

	public void startAttack ( Hero hero )
	{
		countered = true;
		turn = 0;
		attacking = true;
		glob.setX( this.getX() );
		glob.setY( this.getY() );
		// This would look cooler if all objects used a Coordinate instead of locx and locy.
		Direction globDirection = hero.getBody().getDirection().opposite();
		Coordinate globVelocity = new Coordinate(0,0);
		globVelocity.adjust( GLOBVELOCITY, globDirection );
		glob.setVX( globVelocity.getX() );
		glob.setVY( globVelocity.getY() );
		body.getWicked().getGameCanvas().add(glob);
		// Don't just hurt the hero, but anything in it's path.
		Coordinate inGlobsPath = new Coordinate(body.getX(), body.getY());
		AbstractPicture targetOfGlob;
		for (int i = 0; i < Monster.RANGE; i++)
		{
			inGlobsPath.adjust(20, globDirection);
			targetOfGlob = (AbstractPicture)body.getWicked().getGameCanvas().getPictAt( inGlobsPath, Layer.MIDDLE );
			if (targetOfGlob instanceof Mortal)
			{
				// This can hit Hero too because Hero is a Mortal.
				((Mortal)targetOfGlob).reduceHitPoints(Monster.DAMAGE);
				System.out.println("Damaged Target at:" + targetOfGlob.getX() + " " + targetOfGlob.getY() + " " + targetOfGlob.getName());
			}
		}

		if (hero.hitPoints <= 0)
		{
			body.getWicked().getGameCanvas().remove(hero);
		}
	}
	private void finishAttack()
	{
		body.getWicked().getGameCanvas().remove(glob);
		attackStep = 0;
		attacking = false;
	}

	public String getTalk ( )
	{
		return getTalk ( 0, 0 );
	}
	public String getTalk ( int trust, int alligance )
	{
		return body.getWicked().getLoader().getFileContents( "files/talk/" + body.getName().trim() + ".txt" );
	}
	private void explode()
	{
		explosion.setX( body.getX() );
		explosion.setY( body.getY() );
		this.exploding = true;
	}
	public void reduceHitPoints( int damage )
	{
		hitPoints = hitPoints - damage;
		explode();
		System.out.println("HP: " + hitPoints + " " + countered );
		if (countered == false && hitPoints > 0)
		{
			turn = (body.getWicked().getGameCanvas().getBattle()).addTurn();
			System.out.println("I get a turn: " + body.getName() + " " + turn);
		}
	}
	public String toString()
	{
		// super()
		return 
			"Monster.  Name: " + body.getName() + "\n"
			+ " Turn: " + turn
			;
	}
	public int getHitPoints( ) { return hitPoints; }
	public void setHitPoints( int number ) { hitPoints = number; }
	public boolean isDead() { return (hitPoints <= 0); }
	public int getTurn () { return turn; }
	public void setTurn( int t ) { turn = t; }
	public boolean getCountered () { return countered; }
	public void setCountered( boolean c ) { countered = c; }
}