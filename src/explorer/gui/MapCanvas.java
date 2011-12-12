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

import explorer.Cell.Type;
import explorer.Cell.Visibility;

/**
 * The Class MapCanvas.
 */
@SuppressWarnings("serial")
public class MapCanvas extends Canvas {
	
	/**
	 * Instantiates a new map canvas.
	 */
	public MapCanvas() {
		setBackground (Color.DARK_GRAY);
		this.setFont(new Font("Dialog", Font.BOLD, 14));
	}

	/** The i. */
	int i = 0;

	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2;
		int Height;

		g2 = (Graphics2D) g;
		//g2.drawRect(20, 20, 100, 50);
		//Height = getHeight();
		//i++;
		//g2.drawString("The CustomCanvas is in the CENTER area: " + i, 10, Height / 2);
		ArrayList<CellGraphics> cells=new ArrayList<CellGraphics>();
		cells.add(new CellGraphics(0, 0, Type.Empty));
		cells.add(new CellGraphics(0, 1, Type.Trap));
		cells.add(new CellGraphics(0, 3, Type.Wall));
		cells.add(new CellGraphics(1, 0, Type.Clue));
		cells.add(new CellGraphics(1, 1, Type.Empty));
		cells.add(new CellGraphics(1, 3, Type.Empty));
		
		cells.get(2).visible=Visibility.Explored;
		cells.get(3).visible=Visibility.Visible;
		cells.get(1).visible=Visibility.Hidden;
		
		for(CellGraphics cell: cells)
			cell.draw(g);
	}
}
