/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import explorer.Cell.Type;
import explorer.Cell.Visibility;


/**
 * The Class ExplorationEngine.
 */
public class ExplorationEngine {
	
	/** The Constant AGING_RATING. */
	public static final int AGING_RATING=-1;

	/** The map. */
	Map map;
	
	/** The visible cell. */
	HashSet<Cell> visibleCells;
	
	/** The known cells are the cells which I have either explored either observed. */
	HashSet<Cell> knownCells;
	
	/** The wall cells. */
	HashSet<Cell> knownWallCells;
	
	/** The unvisited but known cells. */
	HashSet<Cell> unvisitedCells;
	
	/** The current position. */
	Cell currentCell;
	

	/**
	 * Instantiates a new exploration engine.
	 *
	 * @param map the map
	 */
	public ExplorationEngine(Map map) {
		super();
		
		//Prepare the fields
		this.map = map;
		this.currentCell=map.getCell(map.startX, map.startY);
		this.visibleCells=new HashSet<Cell>();
		this.knownCells=new HashSet<Cell>();
		this.knownWallCells=new HashSet<Cell>();
		updateVisible();
		this.knownCells.add(currentCell);
		this.unvisitedCells=new HashSet<Cell>(visibleCells);
		this.unvisitedCells.remove(currentCell);
		
		//Mark the cells accordingly
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Visible;
		currentCell.visible=Visibility.Current;
	}
	
	/**
	 * Checks if is on goal state.
	 *
	 * @return true, if is on goal state
	 */
	public boolean isOnGoalState()
	{
		return currentCell.type==Type.Goal;
	}
	
	/**
	 * Performs the next step.
	 *
	 * @return the text description of the action performed.
	 */
	public String nextStep()
	{
			
		MainLauncher.debug("Calculating next step from "+currentCell);

		//Check for goal state
		if(isOnGoalState())
			return "Reached goal cell!";
		
		//Get updated ratings
		updateCosts();
		MainLauncher.debug("Cells after ratings calculation "+knownCells);
		//Get potential targets
		PriorityQueue<Cell> potentialTargets=getPotentialTargets();
		MainLauncher.debug("Potential target cells: "+potentialTargets);
		//Select potential target and get path
		Cell target=potentialTargets.remove();
		LinkedList<Cell> path=getPathTo(currentCell, target);
		MainLauncher.debug("Selected target "+target+" with path: "+path);
		//Update to the new position
		this.updateCurrent(path.getFirst());
		this.updateVisible();
		MainLauncher.debug("Moving to "+currentCell);
		//Perform action on current cell (if any)
		String moveDescription;
		moveDescription=actionOnCell();
		return  moveDescription;
	}
	
	private String actionOnCell()
	{
		String descr="";
		if(currentCell.type==Type.Trap)
		{
			descr="Trying to stop trap\n";
			boolean exploded=map.explodes(currentCell);
			currentCell.type=Type.Empty;
			if(exploded)
			{
				descr+="Trap exploded\n";
				map.hitpoints-=currentCell.damage;
			}	
		}
		
		return descr+"Move to ["+currentCell.x+","+currentCell.y+"]";
	}
	
	/**
	 * Update visible.
	 */
	private void updateVisible()
	{
		//Mark the old cells accordingly
		this.visibleCells.remove(currentCell);
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Known;
		
		//Mark the new cells accordingly
		HashSet<Cell> allVisibleCells=map.getVisibleCells(currentCell.x, currentCell.y);
		visibleCells.clear();
		//Break the visible cells into wall cells and normal cells
		for(Cell cell:allVisibleCells)
			if(cell.type==Type.Wall)
				knownWallCells.add(cell);
			else
				visibleCells.add(cell);
		
		//Mark the visible cells accordingly
		this.visibleCells.remove(currentCell);
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Visible;
		
		//Add the new cells to the known list
		this.knownCells.addAll(visibleCells);
		
		MainLauncher.debug("Visible after update: "+visibleCells);
		MainLauncher.debug("Known after update: "+knownCells);
		MainLauncher.debug("Known wall cells after update: " + knownWallCells);
	}
	
	/**
	 * Update the current cell.
	 *
	 * @param newCell the new cell
	 */
	public void updateCurrent(Cell newCell)
	{
		this.currentCell.visible=Visibility.Known;
		this.currentCell=newCell;
		newCell.visible=Visibility.Current;
	}
	
	/**
	 * Update costs of all the known cells.
	 */
	private void updateCosts()
	{
		// initialize graph
		for (Cell cell : knownCells) {
			cell.cost = Integer.MAX_VALUE;
			cell.predecessor = null;
		}
		currentCell.cost = 0;

		// All nodes in the graph are unoptimized - thus are in Q
		PriorityQueue<Cell> Q = new PriorityQueue<Cell>(knownCells);
		while (!Q.isEmpty()) 
		{
			Cell current = Q.remove();
			if (current.cost == Integer.MAX_VALUE)
				break; // all remaining vertices are inaccessible from source

			// For every neighbour
			ArrayList<Cell> neigh = map.getNeighbours(current.x, current.y);
			for (Cell cell : neigh)
				// if it hasn't been eliminated from the queue
				if (Q.contains(cell)) {
					// Try to relax
					int dist = current.cost + cell.getDefaultRating();
					if (dist < cell.cost) {
						cell.cost = dist;
						cell.predecessor = current;
						// Reorder cell in the queue
						Q.remove(cell);
						Q.add(cell);
					}
				}
		}
	}
	
	/**
	 * Gets the potential goal cells of the current step. It returns all the visible clue cells and all the
	 * cells that are on the visible border (there's a potential to explore more from there). If the final
	 * goal cell is visible, the method only returns that one.
	 *
	 * @return the potential goals
	 */
	private PriorityQueue<Cell> getPotentialTargets()
	{
		PriorityQueue<Cell> goals=new PriorityQueue<Cell>();
		for(Cell cell:knownCells)
		{
			if(cell.type==Type.Goal)
			{
				goals.clear();
				goals.add(cell);
				break;
			}
			if(isCellVisibleBorder(cell) || cell.type==Type.Clue)
				goals.add(cell);
		}
		return goals;
	}
	
	/**
	 * Checks if is the cell is on the visible border, which means it has at least one neighbour not known
	 * to the engine.
	 *
	 * @param cell the cell
	 * @return true, if is cell visible border
	 */
	private boolean isCellVisibleBorder(Cell cell)
	{
		List<Cell> neigh=map.getNeighboursFull(cell.x, cell.y);
		
		//if one of the neighbours is not known by the robot, it's a border cell
		for(Cell ncell : neigh)
			if(!knownCells.contains(ncell) && !knownWallCells.contains(ncell))
				return true;
		return false;
	}
	
	/**
	 * Gets the path to a given cell, using the predecessor field from the cells.
	 *
	 * @param source the source
	 * @param destination the destination
	 * @return the path to
	 */
	private LinkedList<Cell> getPathTo(Cell source, Cell destination)
	{
		LinkedList<Cell> path=new LinkedList<Cell>();
		Cell cell=destination;
		while(cell!=source)
		{
			path.addFirst(cell);
			cell=cell.predecessor;
		}
		return path;
	}
}
