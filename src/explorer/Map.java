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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import explorer.Cell.Direction;
import explorer.Cell.Type;
import explorer.Cell.Visibility;
import explorer.gui.CellGraphics;

/**
 * The Class Map.
 */
public class Map {

	/** The cells. */
	public HashMap<Position, Cell> cells;

	/** The start x. */
	public int startX;

	/** The start y. */
	public int startY;

	/** The hitpoints. */
	public int hitpoints;

	/** The range. */
	public int range;
	
	/**
	 * Instantiates a new map.
	 */
	public Map() {
		super();
		cells = new HashMap<Position,Cell>(30, 0.5f);
	}

	/**
	 * Load the map and game info from file. Returns a list of CellGraphics
	 * directly referencing the same cells as the ones from the map. The list
	 * can be used for the graphics of the application.
	 * 
	 * @throws FileNotFoundException
	 */
	public ArrayList<CellGraphics> loadFromFile(String filename) throws FileNotFoundException {

		MainLauncher.debug("Loading map data from " + filename);
		// Use buffering, reading one line at a time
		Scanner input = new Scanner(new FileReader(filename));
		ArrayList<CellGraphics> cellsGraphics = new ArrayList<CellGraphics>();

		// Get the starting position
		startX = input.nextInt();
		startY = input.nextInt();
		MainLauncher.debug("Start point: " + startX + "," + startY);

		// Get the starting hit points
		hitpoints = input.nextInt();
		MainLauncher.debug("Starting hitpoints: " + hitpoints);
		
		// Get the visibility range
		range = input.nextInt();
		MainLauncher.debug("Visibility range: " + range);

		// Get the number of cells
		int cellsNo = input.nextInt();
		MainLauncher.debug("Number of cells: " + cellsNo);

		// Get the cells
		int posX, posY, typeV;
		Type type;
		for (int i = 0; i < cellsNo; i++) {
			// Reading cell data
			posX = input.nextInt();
			posY = input.nextInt();
			typeV = input.nextInt();

			// Get the type
			switch (typeV) {
			case 1:
				type = Type.Goal;
				break;
			case 0:
				type = Type.Empty;
				break;
			case -1:
				type = Type.Wall;
				break;
			case -2:
				type = Type.Trap;
				break;
			case 2:
				type = Type.Clue;
				break;
			case 3:
				type = Type.Exit;
				break;
			default:
				type = Type.Wall;
				break;
			}

			// Build the cell and read more data if necessary
			CellGraphics newCell = new CellGraphics(posX, posY, type);

			if (type == Type.Trap)
				newCell.probability = input.nextFloat();
			if (type == Type.Clue)
				newCell.hint = Direction.valueOf(input.next());

			// Mark the starting color
			if (posX == startX && posY == startY)
				newCell.visible = Visibility.Robot;

			MainLauncher.debug("New cell: " + newCell.toString());
			cells.put(new Position(posX, posY), newCell);
			cellsGraphics.add(newCell);
		}

		return cellsGraphics;
	}

	/**
	 * Gets the neighbours of a given position on the map.
	 *
	 * @param cellX the cell x
	 * @param cellY the cell y
	 * @return the neighbours
	 */
	public ArrayList<Cell> getNeighbours(int cellX, int cellY) {
		ArrayList<Cell> nCells = new ArrayList<Cell>(4);
		Cell cell;

		// Above
		cell = cells.get(new Position(cellX - 1, cellY));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);

		// Below
		cell = cells.get(new Position(cellX + 1, cellY));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);

		// Left
		cell = cells.get(new Position(cellX, cellY - 1));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);

		// Right
		cell = cells.get(new Position(cellX, cellY + 1));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);

		return nCells;
	}
	
	/**
	 * Gets the visible cells.
	 *
	 * @param cellX the cell x
	 * @param cellY the cell y
	 * @return the visible cells
	 */
	public HashSet<Cell> getVisibleCells(int cellX, int cellY)
	{
		HashSet<Cell> visible=new HashSet<Cell>();
		LinkedList<Position> borderList=new LinkedList<Position>();
		Position startPos=new Position(cellX, cellY);
		
		//We're doing a BFS search
		borderList.push(startPos);
		while(!borderList.isEmpty())
		{
			//Curent position in BFS
			Position pos=borderList.pop();
			Position newP;
			
			//Get current neighbours
			List<Cell> neigh=this.getNeighbours(pos.x, pos.y);
			for(Cell cell : neigh)
			{
				//If the neighbour is in range and it hasn't been expanded, we add it to the borderList
				newP=new Position(cell.x, cell.y);
				if(inRange(startPos, newP) &&
						!visible.contains(cell))
				{
					borderList.add(newP);
					visible.add(cell);
				}
			}
			
		}
		
		return visible;
	}
	
	/**
	 * If the pos is in range of startPos.
	 *
	 * @param startPos the start pos
	 * @param pos the pos
	 * @return true, if successful
	 */
	private boolean inRange(Position startPos, Position pos)
	{
		if (Math.abs(startPos.x - pos.x) <= range && Math.abs(startPos.y - pos.y) <= range)
			return true;
		return false;
	}
	

}
