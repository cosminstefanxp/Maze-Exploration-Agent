/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import explorer.Cell;

/**
 * The Class CellGraphics.
 */
public class CellGraphics extends Cell {

	public static final int size = 32;
	public static final int padding = 1;
	public static final Color knownColor = new Color(0.6f,0.6f,0.6f);
	public static final Color visibleColor = new Color(0.87f,0.88f,0.97f);
	public static final Color hiddenColor = Color.GRAY.darker();
	public static final Color emptyColor = new Color(0.2f, 0.5f, 0.4f);
	public static final Color trapColor = Color.ORANGE;
	public static final Color wallColor = Color.RED;
	public static final Color wallBackColor = new Color(0.3f,0.3f,0.3f);
	public static final Color goalColor = Color.MAGENTA;
	public static final Color exitColor = Color.BLUE;
	public static final Color currentPositionColor = Color.CYAN;
	public static final Color currentTargetColor = new Color(0.95f,0.7f,0.7f);
	public static final Color enemyColor = Color.RED;
	public static final Color deadColor = Color.GREEN.darker().darker();
	public static int minXCell = 0;
	public static int minYCell = 0;
	public static int maxYCell = 0;
	
	/**
	 * Instantiates a new cell graphics.
	 *
	 * @param x the x
	 * @param y the y
	 * @param type the type
	 */
	public CellGraphics(int x, int y, Type type) {
		super(x, y, type);
	}


	/**
	 * Draw the cell.
	 *
	 * @param g the g
	 */
	public void draw(Graphics2D g, int mapSkipXCells, int mapSkipYCells)
	{
		// Calculate which cell this one is (compared to the most left and top
		// one)
		// Also, skip 1 row and 1 column (padding)
		int xCount = this.x - CellGraphics.minXCell + 1 + mapSkipXCells;
		int yCount = this.y - CellGraphics.minYCell + 1 + mapSkipYCells;

		// Calculate coordinates
		int xPos = xCount * (size + padding);
		int yPos = yCount * (size + padding);


		drawAt(g, yPos, xPos);	//swap to consider x as line number and y as column number
	}
	
	/**
	 * Draw the cell at a given position.
	 *
	 * @param g the g
	 * @param xPos the x pos
	 * @param yPos the y pos
	 */
	public void drawAt(Graphics2D g, int xPos, int yPos)
	{
		// Prepare attributes
		String text = null;
		Color colorText = null;
		Color color = null;
		int textXPos = xPos + 10;
		int textYPos = yPos + 22;		
		
		// Color
		switch (this.visible)
		{
		case Known: 	color=knownColor; break;
		case Hidden:	color=hiddenColor; break;
		case Visible:	color=visibleColor; break;
		case Current:	color=currentPositionColor; break;
		case Target:	color=currentTargetColor; break;
		case Finished:		color=deadColor; break;
		}

		// Text attributes
		switch (this.type) {
		case Empty:
			text = "";
			colorText = emptyColor;
			break;
		case Trap:
			textXPos-=6;
			text = String.format("%2.1f", probability);
			colorText = trapColor;
			break;
		case Wall:
			text = "#";
			colorText = wallColor;
			color = wallBackColor;
			break;
		case Clue:
			text = "?"+this.hint;
			textXPos-=8;
			colorText = emptyColor;
			break;
		case Goal:
			text = "G";
			colorText = goalColor;
			break;
		case Exit:
			text = "e";
			colorText = exitColor;
			break;
		default:
			text = "!";
			colorText = Color.RED;
		}
		
		if(enemy!=null)
		{
			text = "X"+enemy;
			textXPos-=4;
			colorText=enemyColor;
		}
		
		// Draw the cell
		g.setColor(color);
		g.fillRect(xPos, yPos, size, size);

		// Draw the text inside
		g.setColor(colorText);
		g.drawString(text, textXPos, textYPos);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CellGraphics clone() {
		CellGraphics newCell=new CellGraphics(this.x, this.y, this.type);
		newCell.cost=this.cost;
		newCell.damage=this.damage;
		newCell.hint=this.hint;
		newCell.predecessor=null;
		newCell.probability=this.probability;
		newCell.visible=this.visible;
		
		return newCell;
	}
}
