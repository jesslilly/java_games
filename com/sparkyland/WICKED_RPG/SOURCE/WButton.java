//package v12;

import java.awt.*;
import java.applet.*;

/////////////////////////////////////////////////////////////////
class WButton extends Button {

	static AudioClip clickSound;

	static boolean setClickSound(AudioClip click) {
		clickSound = click;
		return true;
	}

	public WButton(String label) {
		super(label);
		setFont(new Font("Helvetica",Font.PLAIN,12));
		setBackground(Color.black);
		setForeground(Color.orange);
	}
/*
	public boolean handleEvent( Event event ) {
		if (event.id == Event.ACTION_EVENT) {
			if (event.target == this )
			{
				clickSound.play();
				//return true;
			}
		}
		System.out.println("WBUTTTON!!!");
		return false;
	}*/

}