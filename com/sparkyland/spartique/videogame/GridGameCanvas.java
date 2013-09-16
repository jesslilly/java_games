package com.sparkyland.spartique.videogame;

import com.sparkyland.spartique.physical.*;
import com.sparkyland.spartique.videogame.sprite.AbstractPicture;
import com.sparkyland.spartique.videogame.Grid;
import com.sparkyland.spartique.common.DebugLog;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Dimension;

// rewrite: Make a Grid object for this class to use: 2d array and size.
public class GridGameCanvas extends GameCanvas
{
	// A Square is 1 coordinate on the Grid.
	// The gridRowsCols is the number of Squares across.
	// The square size is the number of pixels across a square.
	// An Abstract Picture must be solid to add to the grid!
	protected Size gridSize;
	protected Size squareSize;
	protected Size gridRowsCols;
	protected AbstractPicture occupiedGrid[][];  // synchronized 
	protected boolean showGrid;

	public GridGameCanvas(Applet mainProgram, Dimension size, Size gridRowsCols)
	{
		super(mainProgram, size );
		this.gridRowsCols = gridRowsCols;
		squareSize = new Size( 36, 36 );  // rewrite: magic.
		this.gridSize = new Size( squareSize.getHeight() * gridRowsCols.getHeight(), squareSize.getWidth() * gridRowsCols.getWidth() );

		//DebugLog.println("gridSize: " + gridSize );			

		showGrid = true;

		occupiedGrid = new AbstractPicture[ gridRowsCols.getWidth() ][ gridRowsCols.getHeight() ];

		Grid grid = new Grid( 0, 0, gridSize.getWidth(), gridSize.getHeight(), Layer.MIDDLE, squareSize, this);

		this.add( grid );
	}


	public static Coordinate calculateGridCoordinate( Coordinate xyCoord, Size squareSize )
	{
		return new Coordinate( xyCoord.getX()/ squareSize.getWidth(), xyCoord.getY()/ squareSize.getHeight() );
	}

	public void add(AbstractPicture pict)
	{
		super.add( pict );
		// All AbstractPictures that get added to the GridGameCanvas get a gridCoordinate.
		pict.setGridCoordinate( GridGameCanvas.calculateGridCoordinate( pict.getLoc(), this.squareSize ) );
		addNewLock( pict );
    }	

    public void remove(AbstractPicture pict)
	{
		super.remove( pict );
		unlockGridCoordinate( pict );
	}

    public void removeAll()
	{
		super.removeAll();
		clearOccupiedGrid();
    }


	// ---------- Begin Locking methods -------------------------------

	// rewrite: check for null and isGrdObject on all public methods.
	// private methods will not have to.
	private void addNewLock( AbstractPicture pict )
	{
		if ( this.isGridObject( pict ) )
		{
			//DebugLog.println("Add new lock.");
			setOccupied( pict, pict.getGridCoordinate() );
		}
	}
	protected boolean isGridObject( AbstractPicture pict )
	{
		//DebugLog.println( pict.getName() + " GridObject: " + pict.isSolid() );
		return ( pict instanceof AbstractPicture && pict.isSolid() );
	}

	public void unlockGridCoordinate( AbstractPicture piece )
	{
		if ( this.isGridObject( piece ) )
		{
			Coordinate pieceGridCoordinate = new Coordinate( piece.getGridCoordinate( ) );
			if ( pieceGridCoordinate != null )
			{
				setOccupied( null, pieceGridCoordinate );
			}
			else
				DebugLog.println("Cannot unlock this piece:" + piece.getName() );
		}
	}

	protected synchronized boolean moveLock( AbstractPicture piece, Direction moveDirection )
	{
		boolean success = false;
		Coordinate currentCoordinate = new Coordinate( piece.getGridCoordinate( ) );
//		DebugLog.println( "currentCoordinate: " + currentCoordinate );
		if ( currentCoordinate != null )
		{
			Coordinate goalCoordinate = new Coordinate( currentCoordinate );
			goalCoordinate.adjust( 1, moveDirection );

			if ( ! isOccupied( goalCoordinate ) )
			{
				// rewritten: synchronized locks this function.  Therefore, this is a transaction.
				setOccupied( piece, goalCoordinate );
				setOccupied( null, currentCoordinate );
				piece.setGridCoordinate( goalCoordinate );
				success = true;
			}
		}
		return success;
	}

	private synchronized void setOccupied( AbstractPicture gridObject, Coordinate coordinate )
	{
		occupiedGrid[ coordinate.getX() ][ coordinate.getY() ] = gridObject;
		/*
		if ( gridObject == null )
			DebugLog.println("Unlocking x: " + coordinate.getX() + " y: " + coordinate.getY() );
		else
			DebugLog.println("Lock x: " + coordinate.getX() + " y: " + coordinate.getY() );
			*/
	}

	// ---------- End Locking methods -------------------------------

	public Coordinate getGridCoordinate( AbstractPicture piece )
	{
		// rewrite: maybe unit should keep track of it's grid coordinate.
		DebugLog.println( "rewrite: Huge overhead 2D array search. I don't think this is called anymore." );
		Coordinate gridCoordinate = null;
		// rewrite: what if piece is null or not gridobject?
		mainloop: for ( int x = 0; x < gridRowsCols.getWidth(); x ++ )
		{
			for ( int y = 0; y < gridRowsCols.getHeight(); y ++ )
			{
				//DebugLog.println( "Check x: " + x + " y: " + y + " " + occupiedGrid[ x ][ y ] );
				if ( piece == (AbstractPicture)occupiedGrid[ x ][ y ] )
				{
					gridCoordinate = new Coordinate( x, y );
					break mainloop;
				}
			}
		}
		// can return null!
		return gridCoordinate;
	}

	public synchronized boolean isOccupied( Coordinate coordinate )
	{
		return isOccupied( coordinate.getX(), coordinate.getY()  );
	}
	public synchronized boolean isOccupied( int xIndex, int yIndex )
	{
		boolean occupied = true;
		if ( isInsideGrid( xIndex, yIndex ) )
		{
			if ( occupiedGrid[ xIndex ][ yIndex ] == null )
				occupied = false;
		}

		//DebugLog.println( "Occupied: " + occupied + " @ " + xIndex + "," + yIndex );
		return occupied;
	}
	protected boolean isInsideGrid( int xIndex, int yIndex )
	{
		boolean inside = false;
		if ( xIndex < gridRowsCols.getWidth()
		&& xIndex >= 0
		&& yIndex < gridRowsCols.getHeight()
		&& yIndex >= 0 )
		{
			inside = true;
		}
		return inside;
	}
	public void clearOccupiedGrid()
	{
		// rewrite:  needs implementation.  Cool way?
	}

	// This has to be protected because we don't check the grid coordinates first.
	protected AbstractPicture pieceAt( Coordinate coordinate )
	{
		return occupiedGrid[ coordinate.getX() ][ coordinate.getY() ];
	}
	public AbstractPicture getPieceAt( Coordinate coordinate )
	{
		AbstractPicture piece = null;
		if ( isInsideGrid( coordinate.getX(), coordinate.getY() ) )
			piece = pieceAt( coordinate );
		return piece;		
	}

	public AbstractPicture getClosestPiece( Coordinate gridCoordinate, Direction direction )
	{
		// rewrite: Is there a better way?
		// Warning: This method will modify the gridCoordinate that you pass in!
		AbstractPicture piece = null;
		gridCoordinate.adjust( 1, direction );
		while ( isInsideGrid( gridCoordinate.getX(), gridCoordinate.getY() ) )
		{
			piece = pieceAt( gridCoordinate );
			if ( piece != null )
				break;
			gridCoordinate.adjust( 1, direction );
		}
		// can return null!
		return piece;
	}
	public AbstractPicture getPictureBelow( AbstractPicture guyOnTop )
	{

		Coordinate gridCoord = new Coordinate( guyOnTop.getGridCoordinate() );
		// rewrite: Assumption that the unit is a square....
		// rewrite: hard code. not an issue when I have a terrain grid.
		gridCoord.translate( 36 /*Unit.defaultSize.getHeight()*/ );

		// rewrite: This is all pretty lame because we should have a grid[][] thing
		// to access the terrain and pictures.
		return getPictAt( gridCoord.getX(), gridCoord.getY(), Layer.BOTTOM );
		//DebugLog.println( guyOnTop + " is on " + guyBelow );

	}

	
	public Size getGridSize() { return gridSize; }
	public void setGridSize( Size gridSize ) { this.gridSize = gridSize; }
	public Size getSquareSize() { return squareSize; }
	public void setSquareSize( Size squareSize ) { this.squareSize = squareSize; }

}





