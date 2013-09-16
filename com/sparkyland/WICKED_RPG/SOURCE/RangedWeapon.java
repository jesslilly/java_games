class RangedWeapon extends Weapon 
{
	int range; // in # of squares.

	RangedWeapon( int range )
	{
		if (range > 15)
			this.range = 15;
		else
			this.range = range;

		this.type = "RangedWeapon";
	}

	// Setters.
	// ---------
	public void setRange ( int range ) { this.range = range; }
	// Getters.
	// ---------
	public int getRange ( ) { return range; }

}
