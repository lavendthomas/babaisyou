package be.ac.umons.babaisyou.game;

/**
 * Represents a direction according to the 4 poles.
 * @author TZK- , modified by Thomas Lavend'Homme
 * @see https://github.com/TZK-/Sokoban/blob/master/src/fr/iutvalence/sokoban/core/Position.java
 *
 */
public class Position {
	
	/**
	 * The value of default axis
	 */
	public final static int DEFAULT_POSITION = 0;
	
	/**
	 * X-axis position 
	 */
	private final int x;
	
	/**
	 * Y-axis position
	 */
	private final int y;
	
	/**
	 * Creates a new position with coordinates (0, 0)
	 */
	public Position(){
		x= Position.DEFAULT_POSITION;
		y = Position.DEFAULT_POSITION;
	}
	
	/**
	 * Create a new position with the values passed as parameters
	 * @param posX the X-axis position
	 * @param posY the Y-axis position
	 */
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the value of the X-axis
	 * @return the value of the X-axis
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the value of the Y-axis
	 * @return the value of the Y-axis
	 */
	public int getY() {
		return y;
	}

	/**
	 * Adds the current position with an other position 
	 * @param pos the position to add
	 * @return a new position resulting from the addition of two positions.
	 */
	public Position addTo(Position pos){
		return new Position(x + pos.getX(), y + pos.getY());
	}
	
	/**
	 * Returns the position matches with the direction
	 * @param dir The direction
	 * @return the position matches with the direction
	 */
	public Position nextPosition(Direction dir){
		return this.addTo(dir.getDeltaPosition()); 
	}
	
	/**
	 * Returns position as a string format
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/** 
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

	/**
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
		Position other = (Position) obj;
		if (x != other.getX())
			return false;
		if (y != other.getY())
			return false;
		return true;
	}
	
}

