/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import explorer.Cell.Direction;
import explorer.Cell.Type;
import explorer.gui.CellGraphics;

/**
 * The Class Map.
 */
public class Map {

	/** The cells. */
	public ArrayList<Cell> cells;

	/** The start x. */
	public int startX;
	
	/** The start y. */
	public int startY;
	
	/** The hitpoints. */
	public int hitpoints;
	
	/**
	 * Instantiates a new map.
	 */
	public Map() {
		super();
		cells=new ArrayList<Cell>();
	}
	
	
	/**
	 * Load the map and game info from file.
	 * @throws FileNotFoundException 
	 */
	public ArrayList<CellGraphics> loadFromFile(String filename) throws FileNotFoundException
	{
		MainLauncher.debug("Loading map data from "+filename);
		// Use buffering, reading one line at a time
		Scanner input = new Scanner(new FileReader(filename));
		ArrayList<CellGraphics> cellsGraphics=new ArrayList<CellGraphics>();
		
		//Get the number of cells
		int cellsNo=input.nextInt();
		MainLauncher.debug("Number of cells: "+cellsNo);

		//Get the starting position
		startX = input.nextInt();
		startY = input.nextInt();
		MainLauncher.debug("Start point:" +startX+","+startY);
		
		//Get the starting hitpoints
		hitpoints = input.nextInt();

		//Get the cells
		int posX, posY, typeV;
		Type type;
		for(int i=0;i<cellsNo;i++)
		{
			//Reading cell data
			posX=input.nextInt();
			posY=input.nextInt();
			typeV=input.nextInt();
			
			//Get the type
			switch(typeV)
			{
			case  1: type=Type.Goal; break;
			case  0: type=Type.Empty; break;
			case -1: type=Type.Wall; break;
			case -2: type=Type.Trap; break;
			case  2: type=Type.Clue; break;
			case  3: type=Type.Exit; break;
			default: type=Type.Wall; break;
			}
		
			//Build the cell and read more data if necessary
			CellGraphics newCell=new CellGraphics(posX, posY, type);
			
			if(type==Type.Trap)
				newCell.probability=input.nextFloat();
			if(type==Type.Clue)
				newCell.hint=Direction.valueOf(input.next());
			
			MainLauncher.debug("New cell: "+newCell.toString());
			cells.add(newCell);
			cellsGraphics.add(newCell);
		}
		
		return cellsGraphics;
	}
}
