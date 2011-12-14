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
	 * @param filename the filename
	 * @return the array list
	 * @throws FileNotFoundException the file not found exception
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
				newCell.visible = Visibility.Current;

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

		// Right
		cell = cells.get(new Position(cellX, cellY + 1));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);
	
		// Left
		cell = cells.get(new Position(cellX, cellY - 1));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);
		
		// Above
		cell = cells.get(new Position(cellX - 1, cellY));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);

		// Below
		cell = cells.get(new Position(cellX + 1, cellY));
		if (cell != null && cell.type!=Type.Wall)
			nCells.add(cell);


		return nCells;
	}
	
	/**
	 * Gets all the neighbours of a given position on the map. Compared to getNeighbours, this method
	 * also returns the cells that do not exist on the map as wall cells; As a side-effect, the virtually
	 * generated wall cells are also added to the cells Map.
	 *
	 * @param cellX the cell x
	 * @param cellY the cell y
	 * @return the neighbours
	 */
	public ArrayList<Cell> getNeighboursFull(int cellX, int cellY) {
		ArrayList<Cell> nCells = new ArrayList<Cell>(4);
		Cell cell;

		// Left
		cell = cells.get(new Position(cellX, cellY - 1));
		if (cell != null)
			nCells.add(cell);
		else {
			Cell newCell = new CellGraphics(cellX, cellY - 1, Type.Wall);
			nCells.add(newCell);
			cells.put(new Position(cellX, cellY - 1), newCell);
		}

		// Right
		cell = cells.get(new Position(cellX, cellY + 1));
		if (cell != null)
			nCells.add(cell);
		else {
			Cell newCell = new CellGraphics(cellX, cellY + 1, Type.Wall);
			nCells.add(newCell);
			cells.put(new Position(cellX, cellY + 1), newCell);
		}
		// Above
		cell = cells.get(new Position(cellX - 1, cellY));
		if (cell != null)
			nCells.add(cell);
		else {
			Cell newCell = new CellGraphics(cellX - 1, cellY, Type.Wall);
			nCells.add(newCell);
			cells.put(new Position(cellX - 1, cellY), newCell);
		}

		// Below
		cell = cells.get(new Position(cellX + 1, cellY));
		if (cell != null)
			nCells.add(cell);
		else {
			Cell newCell = new CellGraphics(cellX + 1, cellY, Type.Wall);
			nCells.add(newCell);
			cells.put(new Position(cellX + 1, cellY), newCell);
		}

		return nCells;
	}
	
	/**
	 * Gets the visible cells. Ignores wall cells.
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
			List<Cell> neigh=this.getNeighboursFull(pos.x, pos.y);
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
	
	/**
	 * Gets the cell.
	 *
	 * @param pos the pos
	 * @return the cell
	 */
	public Cell getCell(Position pos)
	{
		return cells.get(pos);
	}
	
	/**
	 * Gets the cell.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the cell
	 */
	public Cell getCell(int x, int y)
	{
		return cells.get(new Position(x, y));
	}
	
	/**
	 * Checks if the given cells are neighbours.
	 *
	 * @param cell1 the cell1
	 * @param cell2 the cell2
	 */
	public Boolean areNeighbours(Cell cell1, Cell cell2)
	{
		if(Math.abs(cell1.x-cell2.x)==1 && cell1.y==cell2.y)
			return true;
		if(Math.abs(cell1.y-cell2.y)==1 && cell1.x==cell2.x)
			return true;
		return false;
	}
}
