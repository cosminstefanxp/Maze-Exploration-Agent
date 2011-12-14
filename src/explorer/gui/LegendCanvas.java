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

import explorer.Cell.Direction;
import explorer.Cell.Type;
import explorer.Cell.Visibility;

/**
 * The Class LegendCanvas that show the cell legend to the user.
 */
@SuppressWarnings("serial")
public class LegendCanvas extends Canvas {
	
	/**
	 * Instantiates a new map canvas.
	 */
	public LegendCanvas() {
		setBackground (Color.DARK_GRAY);
		setFont(new Font("Dialog", Font.BOLD, 14));
	}

	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2;
		g2 = (Graphics2D) g;

		CellGraphics cell;
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Empty);
		cell.drawAt(g2, 5, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Free", 45, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Goal);
		cell.drawAt(g2, 100, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Goal", 135, 33);

		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Clue);
		cell.hint=Direction.NE;
		cell.drawAt(g2, 200, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Hint", 235, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Trap);
		cell.probability=0.7f;
		cell.drawAt(g2, 300, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Trap", 335, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Wall);
		cell.drawAt(g2, 400, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Wall", 435, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Exit);
		cell.drawAt(g2, 500, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Exit  | ", 535, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Empty);
		cell.visible=Visibility.Known;
		cell.drawAt(g2, 600, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Known", 635, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Empty);
		cell.visible=Visibility.Hidden;
		cell.drawAt(g2, 700, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Hidden", 735, 33);
		
		//Desenam celula
		cell=new CellGraphics(0, 0, Type.Empty);
		cell.visible=Visibility.Visible;
		cell.drawAt(g2, 800, 10);
		//Scriem hintul
		g2.setColor(Color.WHITE);
		g2.drawString("Visible", 835, 33);
	}
}
