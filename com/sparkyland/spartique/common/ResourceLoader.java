package com.sparkyland.spartique.common;

import java.applet.*;
import java.awt.*;
import java.net.*; //URL MalformedURLException
import java.io.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.Vector;

/*----------------------------------------------------------------------------------------------------
The ResourceLoader holds all of the Multimedia for the applet
----------------------------------------------------------------------------------------------------*/

// rewrite: Music should play in a thread because it can be choppy.
public class ResourceLoader
{
	protected static Applet applet;
	protected static Hashtable allImages, allMusic, allSounds;
	protected static MediaTracker tracker;
	protected static URL pwd;
	protected static File genericFile;
	protected static String currentMusic;
	protected static final int MUSICS = 2, 
			SOUNDS = 4, 
			IMAGES = 100;

	// static initializer.
	static
	{
		currentMusic = null;
	}

	public static void init(Applet applet, URL dir)
	{
		DebugLog.println( "ResourceLoader.init():" + applet );
		ResourceLoader.applet = (Applet)applet;
		DebugLog.println( "ResourceLoader p dir" + dir );
		pwd = dir;
		DebugLog.println( "ResourceLoader m pwd" + pwd );

		tracker = new MediaTracker(applet);

		allImages = new Hashtable(IMAGES);
		allMusic = new Hashtable(MUSICS);
		allSounds = new Hashtable(SOUNDS);
	}
	public static void destroy()
	{
		stopMusic();
	}

	public static void loadStartMedia()
	{
		DebugLog.println(">> Load au and wav sound effects. <<");
		loadMediaFromFile( "sound/", "sounds.csv", "Sound" );

		DebugLog.println(">> Load gif images. <<");
		loadMediaFromFile( "images/", "images.csv", "Image");

		// wait for ALL to load.
		try
		{ 
			tracker.waitForAll();
		}
		catch (InterruptedException e)
		{ }
	}
	
	// play music / sounds.
	// ----------------------------------------------------------------
	public static void playMusic( String music )
	{
		((AudioClip)allMusic.get(music)).play();
	}
	public static void loopMusic( String music )
	{
		stopMusic();
		DebugLog.println("Loop music " + music );
		((AudioClip)allMusic.get(music)).loop();
		currentMusic = music;
	}
	public static void stopMusic()
	{
		if ( currentMusic != null )
		{
			DebugLog.println("Stop music " + currentMusic);
			((AudioClip)allMusic.get(currentMusic)).stop();
		}
	}
	public static void playSound( String effect )
	{
		DebugLog.println("playSound -- " + effect );
		((AudioClip)allSounds.get(effect)).play();
	}

	// get Pictures.
	// ----------------------------------------------------------------
	// rewrite: rename to getPicture
	public static Image getPictures( String picName ) {
			return (Image)allImages.get( picName );
		}
	public static Image[] getPictures( String basePicName, int beginAt, int endAt )
	{
		DebugLog.println("Get Picture: "+basePicName+ " from " + beginAt + " to " + endAt );
		Image tempArray[];
		if ( beginAt == 0 )
		{
			tempArray = new Image[1];
			tempArray[0] = getPictures( basePicName.trim() );
		}
		else
		{
			int size = (endAt - beginAt) + 1;
			tempArray = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = getPictures( basePicName.trim() + (i+1) );
				DebugLog.println("+array+" + basePicName.trim() + (i+1) + "+");
			}
		}
		return tempArray;
	}

	// get files.
	// ----------------------------------------------------------------
	// rewrite: Using this method for all file access will allow to skip lines beginning with # much sooner.
	public static Vector getFileContents( String directory, String fileName)
	{
		Vector returnVector = new Vector();

		try
		{
			URL fileURL = new URL( pwd, directory + "/" + fileName);
			DebugLog.println("Open this: " + fileURL);
			//sucks FileInputStream in = new FileInputStream(new InputStreamReader( fileURL.openStream() ) );
			//DataInputStream in = new DataInputStream( new FileInputStream( new File(fileURL.toString())) );
			BufferedReader in = new BufferedReader( new InputStreamReader( fileURL.openStream() ) );

			String line = new String();
			while (1 == 1)
			{
				line = in.readLine();
				DebugLog.println(line);
				if (line == null)
					break;
				returnVector.addElement(line);
			}
			in.close();
		} 
		catch(MalformedURLException e) { DebugLog.println("URLException:"+e); } 
		catch(IOException e){ DebugLog.println("IOException:"+e); }
		return returnVector;
	}

	private static void loadMediaFromFile( String directory, String fileName, String mediaType )
	{

		// rewrite: use ResourceLoader.getFileContents(), then process the vector.
		Vector mediaNames = new Vector();
		try{
			URL fileURL = new URL( pwd, directory + fileName );
			DebugLog.println("Open this: " + fileURL);
			//sucks FileInputStream in = new FileInputStream(new InputStreamReader( fileURL.openStream() ) );
			//DataInputStream in = new DataInputStream( new FileInputStream( new File(fileURL.toString())) );
			BufferedReader in = new BufferedReader( new InputStreamReader( fileURL.openStream() ) );

			String line;
			while (true)
			{
				line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("#",0))
				{
					// Ignore line.
				}
				else
				{
					mediaNames.addElement( line );
				}
			}
			in.close();
		} 
		catch(MalformedURLException e){
			DebugLog.println("URLException:"+e); } 
		catch(IOException e){
			DebugLog.println("IOException:"+e); }

		String mediaKey;
		String mediaName;
		int dotPosition;

		for ( int i = 0; i < mediaNames.size(); i++ )
		{
			// remove the extension (.gif, .mid, .au ) from the file name
			mediaName = (String)mediaNames.elementAt(i);

			dotPosition = mediaName.lastIndexOf('.');

			mediaKey = mediaName.substring( 0, dotPosition );

			DebugLog.println("Store media file " + mediaName + " as " + mediaKey );

			if ( mediaType.equals("Image") )
			{
				allImages.put( mediaKey, applet.getImage(pwd, directory + mediaName ) );
			}
			if ( mediaType.equals("Music") )
			{
				DebugLog.println("getAudio: " + applet + "\n" + pwd + directory + mediaName );
				allMusic.put( mediaKey, applet.getAudioClip(pwd, directory + mediaName ) );
			}
			if ( mediaType.equals("Sound") )
			{
				allSounds.put( mediaKey, applet.getAudioClip(pwd, directory + mediaName ) );
			}
		}

	}

	// rewrite: duplicate code, jack-whole.
	public static void loadMusic( String fileName )
	{
		String directory = "sound/";
		String mediaKey;
		String mediaName = fileName;
		int dotPosition;

		dotPosition = mediaName.lastIndexOf('.');

		mediaKey = mediaName.substring( 0, dotPosition );

		AudioClip audio = applet.getAudioClip(pwd, directory + mediaName );
		if ( audio == null )
		{
			DebugLog.println("ERROR Loading --" + pwd + directory + mediaName );
			DebugLog.println("ERROR Got audio --" + audio );	
		}
		DebugLog.println("Store media file " + mediaName + " as " + mediaKey );
		allMusic.put( mediaKey, audio );
	}
	public static void loadAndLoopMusic( String fileName )
	{
		String directory = "sound/";
		String mediaKey;
		String mediaName = fileName;
		int dotPosition;

		dotPosition = mediaName.lastIndexOf('.');

		mediaKey = mediaName.substring( 0, dotPosition );

		AudioClip audio = applet.getAudioClip(pwd, directory + mediaName );
		if ( audio == null )
		{
			DebugLog.println("ERROR Loading --" + pwd + directory + mediaName );
			DebugLog.println("ERROR Got audio --" + audio );	
		}
		DebugLog.println("Store media file " + mediaName + " as " + mediaKey );
		allMusic.put( mediaKey, audio );
		loopMusic( mediaKey );
	}

	public static void loadSound( String fileName )
	{
		String directory = "sound/";
		String mediaKey;
		String mediaName = fileName;
		int dotPosition;

		dotPosition = mediaName.lastIndexOf('.');

		mediaKey = mediaName.substring( 0, dotPosition );

		AudioClip audio = applet.getAudioClip(pwd, directory + mediaName );
		if ( audio == null )
		{
			DebugLog.println("ERROR Loading --" + pwd + directory + mediaName );
			DebugLog.println("ERROR Got audio --" + audio );	
		}
		DebugLog.println("Store media file " + mediaName + " as " + mediaKey );
		allSounds.put( mediaKey, audio );
	}

	// rewrite: rename load image
	public static Image getImage( String fileName )
	{
		String directory = "images/";
		String mediaKey;
		String mediaName = fileName;
		int dotPosition;

		dotPosition = mediaName.lastIndexOf('.');

		mediaKey = mediaName.substring( 0, dotPosition );

		Image image = applet.getImage(pwd, directory + mediaName );
		if ( image == null )
		{
			DebugLog.println("ERROR Loading --" + pwd + directory + mediaName );
			DebugLog.println("ERROR Got image --" + image );	
		}
		DebugLog.println("Store media file " + mediaName + " as " + mediaKey );
		allImages.put( mediaKey, image );
		return image;
	}

	// rewrite: rename load image
	// rewrite: hard coded gif.
	public static Image[] getImage( String fileName, int beginAt, int endAt )
	{
		Image tempArray[];
		if ( beginAt == 0 )
		{
			tempArray = new Image[1];
			tempArray[0] = getImage( fileName + ".gif" );
		}
		else
		{
			int size = (endAt - beginAt) + 1;
			tempArray = new Image[size];
			for (int i = 0; i < size; i ++)
			{
				tempArray[i] = getImage( fileName + (i+1) + ".gif" );
				DebugLog.println("+y-array+" + fileName + (i+1) + ".gif" );
			}
		}
		return tempArray;

	}

}


