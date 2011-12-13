/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

/**
 * The Class Cell.
 */
public class Cell {

	/**
	 * The Enum Type.
	 */
	public enum Type {
		/** The Wall. */
		Wall,		
		/** The Clue. */
		Clue,		
		/** The Trap. */
		Trap,		
		/** The Empty. */
		Empty,
		/** The Goal. */
		Goal,
		/** The Exit. */
		Exit
	}
	
	/**
	 * The Enum Visibility.
	 */
	public enum Visibility {
		/** The cell is Explored. */
		Explored,
		/** The cell is Visible. */
		Visible,
		/** The cell is Hidden. */
		Hidden,
		/** The Robot is there. */
		Robot
		
	}
	
	/**
	 * The Enum Direction.
	 */
	public enum Direction {
		N, NE, E, SE, S, SW, W, NW
	}
	
	/** The x position. */
	public int x;
	
	/** The y position. */
	public int y;
	
	/** The type. */
	public Type type;
	
	/** The visible. */
	public Visibility visible;
	
	/** The probability of exploding, if it's a trap. */
	public float probability=0f;
	
	/** The hint. */
	public Direction hint;
	
	/**
	 * Instantiates a new cell.
	 *
	 * @param x the x
	 * @param y the y
	 * @param type the type
	 */
	public Cell(int x, int y, Type type) {
		super();
		this.x = x;
		this.y = y;
		this.type = type;
		this.visible=Visibility.Hidden;
		this.hint=null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cell [x=" + x + ", y=" + y + ", type=" + type + ", visible=" + visible + ", hint=" + hint
				+ ", probability=" + probability + "]";
	}
}
