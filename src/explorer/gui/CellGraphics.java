/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import explorer.Cell;

/**
 * The Class CellGraphics.
 */
public class CellGraphics extends Cell {

	public static final int size=30;
	public static final Color exploredColor=	Color.LIGHT_GRAY;
	public static final Color visibleColor=		Color.WHITE;
	public static final Color unexploredColor=	Color.GRAY;
	public static final int padding=1;
	public static int minX=0;
	public static int minY=0;
	public static final Color emptyColor=new Color(0.2f, 0.5f, 0.4f);
	
	
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
	 * Draw.
	 *
	 * @param g the g
	 */
	public void draw(Graphics g)
	{
		Graphics2D g2=(Graphics2D) g;
		
		//Calculate which cell this one is (compared to the most left and top one)
		//Also, skip 1 row and 1 column (padding)
		int xCount=this.x-CellGraphics.minX+1;
		int yCount=this.y-CellGraphics.minY+1;
		
		//Calculate coordinates
		int xPos=xCount*(size+padding);
		int yPos=yCount*(size+padding);
		
		//Draw the text inside
		int textXPos=xPos+10;
		int textYPos=yPos+20;
		String text=null;
		Color colorText=null;
		Color color=null;
		switch(this.type)
		{
		case Empty:
			text="E";
			colorText=emptyColor;
			break;
		case Trap:
			text="T";
			colorText=emptyColor;
			break;
		case Wall:
			text="W";
			colorText=emptyColor;
			break;
		case Clue:
			text="?";
			colorText=emptyColor;
			break;
		default:
			text="!";
			colorText=Color.RED;
		}
		
		switch(this.visible)
		{
		case Explored: 	color=exploredColor; break;
		case Hidden:	color=unexploredColor; break;
		case Visible:	color=visibleColor; break;
		}

		//Draw the cell
		g2.setColor(color);
		g2.fillRect(xPos, yPos, size, size);
		
		//Draw the text inside
		g2.setColor(colorText);
		g2.drawString(text, textXPos, textYPos);
		
		
		
	}
}
