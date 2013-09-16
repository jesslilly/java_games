package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.common.CSVTokenizer;
import com.sparkyland.spartique.common.ResourceLoader;
import com.sparkyland.spartique.common.DebugLog;

import java.util.Vector;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.lang.Character;
import java.lang.System;

/////////////////////////////////////////////////////////////////
public class HighScoreSaver extends Object
{
	private double worstScore;
	private Vector scores;
	private String directory, fileName;

	public HighScoreSaver ( String directory, String fileName )
	{
		this.directory = directory;
		this.fileName = fileName;
		worstScore = 0.0;
		scores = new Vector();
	}

	public void init()
	{
		scores = ResourceLoader.getFileContents(directory, fileName);
		if ( scores.size() > 0 )
		{
			// set worst score.
		}
		else
		{
			worstScore = 60.0;
		}
	}

	public void saveScore( String scoreLine )
	{
		try
		{
			CSVTokenizer scoreTokens = new CSVTokenizer();
			scoreTokens.tokenize(scoreLine);
			CSVTokenizer fileTokens = new CSVTokenizer();
			double score = scoreTokens.getDoubleAt(0);
			String line;

			RandomAccessFile file = new RandomAccessFile( 
				"C:/spartique/com/sparkyland/remembery/" +
				directory + "/" +
				fileName
				, "rw" );

			long fileLength = file.length();

			if ( fileLength > 0 )
			{
				scoreLine = System.getProperty("line.separator") + scoreLine;
				file.seek( fileLength );
			}

			file.writeBytes( scoreLine );
			file.close();
		}
		catch (IOException e)
		{
			DebugLog.println( e );
		}

		
	}

}