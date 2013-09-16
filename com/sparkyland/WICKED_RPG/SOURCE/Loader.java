//package v12;

import java.applet.*;
import java.awt.*;
import java.net.*; //URL MalformedURLException
import java.io.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/*----------------------------------------------------------------------------------------------------
The Loader holds all of the Multimedia for the applet
----------------------------------------------------------------------------------------------------*/
class Loader { // maybe it should be a thread in the future.
	go wicked;
	AudioClip allMusic[], allEffects[];
	Image allPictures[], allAnimatedPictures[], allWSprites[], allPersons[], allMortals[];
	MediaTracker tracker;
	URL pwd;
	File genericFile;
	String aboutString;
		// just change these numbers (highest picture number + 1).
	final int MUSICS = 2, 
			EFFECTS = 4, 
			PICTURES = 28, 
			ANIMATEDPICTURES = 13, 
			WSPRITES = 12, 
			PERSONS = 12, 
			MORTALS = 5;

	public Loader(go wicked, URL dir) {
		this.wicked = wicked;
		pwd = dir;
		aboutString = new String("");

		tracker = new MediaTracker(wicked);
		allMusic = new AudioClip[MUSICS];
		allEffects = new AudioClip[EFFECTS];

		allPictures = new Image[PICTURES];
		allAnimatedPictures = new Image[ANIMATEDPICTURES];
		allWSprites = new Image[WSPRITES];
		allPersons = new Image[PERSONS];
		allMortals = new Image[MORTALS];
		//black blue cyan darkGray gray green lightGray magenta orange pink red white yellow
	}

	public void loadStartMedia() {

		int dum;
		// music
		System.out.println(">> start midi files <<");
		allMusic[0] = wicked.getAudioClip(pwd, "snd/mission.mid");
		allMusic[1] = wicked.getAudioClip(pwd, "snd/overworld.mid");
		System.out.println(">>  end midi files  <<");

		// sound effects
		allEffects[0] = wicked.getAudioClip(pwd, "snd/click.au");
		allEffects[1] = wicked.getAudioClip(pwd, "snd/beep.au");
		allEffects[2] = wicked.getAudioClip(pwd, "snd/chop.au");
		allEffects[3] = wicked.getAudioClip(pwd, "snd/blink.au");

		// Pictures 0 ... PICTURES - 1
		for (dum = 0; dum < PICTURES; dum++ ) {
			allPictures[dum] = wicked.getImage(pwd, "gx/pictures/" + dum + ".gif");
			tracker.addImage(allPictures[dum],0);
			}
		// AnimatedPictures 0 ... ANIMATEDPICTURES - 1
		for (dum = 0; dum < ANIMATEDPICTURES; dum++ ) {
			allAnimatedPictures[dum] = wicked.getImage(pwd, "gx/animatedpictures/" + dum + ".gif");
			tracker.addImage(allAnimatedPictures[dum],0);
			}
		// WSprites 0 ... WSPRITES - 1
		for (dum = 0; dum < WSPRITES; dum++ ) {
			allWSprites[dum] = wicked.getImage(pwd, "gx/wsprites/" + dum + ".gif");
			tracker.addImage(allWSprites[dum],0);
			}
		// Persons 0 ... PERSONS - 1
		for (dum = 0; dum < PERSONS; dum++ ) {
			allPersons[dum] = wicked.getImage(pwd, "gx/persons/" + dum + ".gif");
			tracker.addImage(allPersons[dum],0);
			}
		// Mortals 0 ... MORTALS - 1
		for (dum = 0; dum < MORTALS; dum++ ) {
			allMortals[dum] = wicked.getImage(pwd, "gx/mortals/" + dum + ".gif");
			tracker.addImage(allMortals[dum],0);
			}
		// open files
		try{
			URL fileURL = new URL( pwd, "files/about.txt");
			System.out.println("Open this: " + fileURL);
			//sucks FileInputStream in = new FileInputStream(new InputStreamReader( fileURL.openStream() ) );
			//DataInputStream in = new DataInputStream( new FileInputStream( new File(fileURL.toString())) );
			BufferedReader in = new BufferedReader( new InputStreamReader( fileURL.openStream() ) );

			String line;
			while (1 == 1)
			{
				line = in.readLine();
				if (line == null)
					break;
				aboutString = aboutString + line + "\n";
			}
			in.close();
		} 
		catch(MalformedURLException e){
			System.out.println("URLException:"+e); } 
		catch(IOException e){
			System.out.println("IOException:"+e); }

		// wait for ALL to load.
		try { 
			tracker.waitForAll();
			}
		catch (InterruptedException e) { }
		}
	
	// play music / effects.
	// ----------------------------------------------------------------
	public void playMusic( int number ) {
			allMusic[number].play();
		}
	public void loopMusic( int number ) {
			allMusic[number].loop();
		}
	public void stopMusic( int number ) {
			allMusic[number].stop();
		}
	public void playEffect( int number ) {
			allEffects[number].play();
		}
	// get Pictures.
	// ----------------------------------------------------------------
	public Image[] getPictures( ) {
			return allPictures;
		}
	public Image getPictures(int which) {
			return allPictures[which];
		}
	public Image[] getPictures(int start, int end) {
			int size = (end - start) + 1;
			Image tempArray[] = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = allPictures[start + i];
			}
			return tempArray;
		}
	// get AnimatedPictures.
	// ----------------------------------------------------------------
	public Image[] getAnimatedPictures( ) {
			return allAnimatedPictures;
		}
	public Image getAnimatedPictures(int which) {
			return allAnimatedPictures[which];
		}
	public Image[] getAnimatedPictures(int start, int end) {
			int size = (end - start) + 1;
			Image tempArray[] = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = allAnimatedPictures[start + i];
			}
			return tempArray;
		}
	// get WSprites.
	// ----------------------------------------------------------------
	public Image[] getWSprites( ) {
			return allWSprites;
		}
	public Image getWSprites(int which) {
			return allWSprites[which];
		}
	public Image[] getWSprites(int start, int end) {
			int size = (end - start) + 1;
			Image tempArray[] = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = allWSprites[start + i];
			}
			return tempArray;
		}
	// get Persons.
	// ----------------------------------------------------------------
	public Image[] getPersons( ) {
			return allPersons;
		}
	public Image getPersons(int which) {
			return allPersons[which];
		}
	public Image[] getPersons(int start, int end) {
			int size = (end - start) + 1;
			Image tempArray[] = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = allPersons[start + i];
			}
			return tempArray;
		}
	// get Mortals.
	// ----------------------------------------------------------------
	public Image[] getMortals( ) {
			return allMortals;
		}
	public Image getMortals(int which) {
			return allMortals[which];
		}
	public Image[] getMortals(int start, int end) {
			int size = (end - start) + 1;
			Image tempArray[] = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = allMortals[start + i];
			}
			return tempArray;
		}
	// get audio.
	// ----------------------------------------------------------------
	public AudioClip getEffect ( int number ) {
		return allEffects[number];
		}
	// get files.
	// ----------------------------------------------------------------
	public String getFileContents(String fileName)
	{
		String returnString = new String();
		if (fileName == "about.txt")
		{
			returnString = aboutString;
		}
		else
		{
			try
			{
				URL fileURL = new URL( pwd, fileName);
				System.out.println("Open this: " + fileURL);
				//sucks FileInputStream in = new FileInputStream(new InputStreamReader( fileURL.openStream() ) );
				//DataInputStream in = new DataInputStream( new FileInputStream( new File(fileURL.toString())) );
				BufferedReader in = new BufferedReader( new InputStreamReader( fileURL.openStream() ) );

				String line = new String();
				while (1 == 1)
				{
					line = in.readLine();
					System.out.println(line);
					if (line == null)
						break;
					returnString = returnString + line + "\n";
				}
				in.close();
			} 
			catch(MalformedURLException e) { System.out.println("URLException:"+e); } 
			catch(IOException e){ System.out.println("IOException:"+e); }
		}
		return returnString;
	}
	

} // end class


