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
import java.util.ArrayList;
import java.util.List;

import explorer.Cell;

/**
 * The Class MapCanvas.
 */
@SuppressWarnings("serial")
public class MapCanvas extends Canvas {
	
	ArrayList<CellGraphics> cells;
	
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
	public MapCanvas(List<Cell> cells) {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
		
		//Add all the cells as CellGraphics
		this.cells=new ArrayList<CellGraphics>();
		for(Cell cell: cells)
		{
			this.cells.add((CellGraphics)cell);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2;
		g2 = (Graphics2D) g;

		if(cells!=null)
		{
			for(CellGraphics cell: cells)
				cell.draw(g2);	
		}
		else
		{
			g2.setColor(Color.RED);
			g2.drawString("Missing cells map", 30, 30);
		}
	}
}
