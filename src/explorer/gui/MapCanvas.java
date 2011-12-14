/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */

package explorer.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import explorer.Cell;
import explorer.Position;

/**
 * The Class MapCanvas.
 */
@SuppressWarnings("serial")
public class MapCanvas extends Canvas {
	
	HashMap<Position, Cell> cells;
	
	/**
	 * Instantiates a new map canvas.
	 */
	public MapCanvas() {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
		cells=null;
	}
	
	/**
	 * Instantiates a new map canvas.
	 */
	public MapCanvas(HashMap<Position, Cell> cells2) {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
		
		//Add all the cells as CellGraphics
		this.cells=cells2;

	}

	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2;
		g2 = (Graphics2D) g;

		if(cells!=null)
		{
			for(Cell cell: cells.values())
				((CellGraphics)cell).draw(g2);	
		}
		else
		{
			g2.setColor(Color.RED);
			g2.drawString("Missing cells map", 30, 30);
		}
	}
}
