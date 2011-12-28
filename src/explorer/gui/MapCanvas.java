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

import explorer.Cell;
import explorer.Map;

/**
 * The Class MapCanvas.
 */
@SuppressWarnings("serial")
public class MapCanvas extends Canvas {
	
	/** The maps. */
	Map maps[];
	
	/** The maps count. */
	int count;
	
	/**
	 * Instantiates a new map canvas.
	 */
	public MapCanvas() {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
		maps=null;
		count=0;
	}
	
	/**
	 * Instantiates a new map canvas.
	 */
	public MapCanvas(Map[] maps, int count) {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
		
		//Add all the cells as CellGraphics
		this.maps=maps;
		this.count=count;
	}

	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2;
		g2 = (Graphics2D) g;

		for(int i=0;i<count;i++)
		{
			if(maps[i]!=null)
			{
				for(Cell cell: maps[i].cells.values())
					((CellGraphics)cell).draw(g2,0,i*(CellGraphics.maxYCell-CellGraphics.minYCell+5));	
			}
			else
			{
				g2.setColor(Color.RED);
				g2.drawString("Missing cells map", 30, 30);
			}
			g2.setColor(Color.WHITE);
			g2.drawString("Player hitpoints: "+maps[i].hitpoints, i*500+10, this.getHeight()-10);
		}
	}
}
