import java.awt.*;
class Sword extends Weapon 
{
	private Color topColor, bottomColor;

	public Sword(int x, int y, int w, int h, boolean s, 
					int l, String n, Image f[], go wicked, int freq, int vx, int vy,
					String t, Direction dir, Color swordColor
					)
	{
		super(x, y, w, h, s, l, n, f, wicked, freq, vx, vy, t, dir);
		this.topColor = swordColor;
		this.bottomColor = swordColor.darker();
	}
	public Sword(String name, Color swordColor, go wicked)
	{
		this.wicked = wicked;
		layer = 2;
		this.type = "Sword";
		this.name = name;
		this.topColor = swordColor;
		this.bottomColor = swordColor.darker();
	}

	public void paint (Graphics g)
	{
		int swordLength = 20;
		int halfWidth = 2;
		int centerWidth = 10;
		int offWidth = 10 - halfWidth;

		// duplicate code!!!!
		if (direction.getIntDirection() == Direction.UP)
		{
			g.setColor(topColor);
			g.fillRect(locx + offWidth, locy, halfWidth, swordLength);
			g.setColor(bottomColor);
			g.fillRect(locx + centerWidth, locy, halfWidth, swordLength);
		}
		else if (direction.getIntDirection() == Direction.DOWN)
		{
			g.setColor(topColor);
			g.fillRect(locx + offWidth, locy, halfWidth, swordLength);
			g.setColor(bottomColor);
			g.fillRect(locx + centerWidth, locy, halfWidth, swordLength);
		}
		else if (direction.getIntDirection() == Direction.RIGHT)
		{
			g.setColor(topColor);
			g.fillRect(locx, locy + offWidth, swordLength, halfWidth);
			g.setColor(bottomColor);
			g.fillRect(locx, locy + centerWidth, swordLength, halfWidth);
		}
		else if (direction.getIntDirection() == Direction.LEFT)
		{
			g.setColor(topColor);
			g.fillRect(locx, locy + offWidth, swordLength, halfWidth);
			g.setColor(bottomColor);
			g.fillRect(locx, locy + centerWidth, swordLength, halfWidth);
		}
	}

}
