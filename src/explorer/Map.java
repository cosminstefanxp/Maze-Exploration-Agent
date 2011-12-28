/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import explorer.Cell.Type;
import explorer.gui.CellGraphics;

/**
 * The Class Map.
 */
public class Map {

	/** The rand. */
	private Random rand=new Random();
	
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
	
	/* The minimum column of the map. */
	public int minYCell, maxYCell;
	
	/**
	 * Instantiates a new map.
	 */
	public Map() {
		super();
		cells = new HashMap<Position,Cell>(30, 0.5f);
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
	 * Gets the visible cells. Including wall cells.
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
	 * @return the boolean
	 */
	public Boolean areNeighbours(Cell cell1, Cell cell2)
	{
		if(Math.abs(cell1.x-cell2.x)==1 && cell1.y==cell2.y)
			return true;
		if(Math.abs(cell1.y-cell2.y)==1 && cell1.x==cell2.x)
			return true;
		return false;
	}

	/**
	 * True if the trap is activated.
	 *
	 * @param cell the cell
	 * @return true, if successful
	 */
	public boolean explodes(Cell cell)
	{
		float explosionProb=rand.nextFloat();
		if(explosionProb<cell.probability)
			return true;
		return false;
	}
}
