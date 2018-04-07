package be.ac.umons.babaisyou.game;

import java.util.Random;

import be.ac.umons.babaisyou.exceptions.NotADirectionException;

/**
 * Represents a direction according to the 4 poles.
 * @author TZK-
 * @see https://github.com/TZK-/Sokoban/blob/master/src/fr/iutvalence/sokoban/core/Direction.java
 */
public enum Direction {
	
	/**
	 * The up direction
	 */
	UP(0,-1),
	
	/**
	 * The down direction
	 */
	DOWN(0,1),
	
	/**
	 * The left direction
	 */
	LEFT(-1,0),
	
	/**
	 * The right direction
	 */
	RIGHT(1,0);
	
	/**
	 * The delta on the X-Axis
	 */
	private final int deltaX;
	
	/**
	 * The delta on the Y-Axis
	 */
	private final int deltaY;
	
	
	/**
	 * Creates a new Direction
	 * @param deltaX the delta X
	 * @param deltaY the delta Y
	 */
	private Direction(int deltaX, int deltaY){
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	/**
	 * Gets the delta X
	 * @return the delta X
	 */
	public int getDeltaX() {
		return this.deltaX;
	}

	/**
	 * Gets the delta Y
	 * @return the delta Y
	 */
	public int getDeltaY() {
		return this.deltaY;
	}
	
	/**
	 * Gets the position formed by delta X and delta Y
	 * @return the position formed by delta X and delta Y
	 */
	public Position getDeltaPosition(){
		return new Position(this.deltaX, this.deltaY);
	}

	public static Direction getRandomDirection() {
		Random randomDir = new Random();

		return Direction.values()[randomDir.nextInt(4)];
	}
	
	/**
	 * Renvoie la direction depuis un entier
	 * 
	 * Conversion : 
	 * 
	 * 0: Droite
	 * 1: Haut
	 * 2: Gauche
	 * 3: Bas
	 */
	public static Direction fromInt(int direction) throws NotADirectionException {
		switch (direction) {
			case 0:
				return Direction.UP;
			case 1:
				return Direction.RIGHT;
			case 2:
				return Direction.LEFT;
			case 3:
				return Direction.DOWN;
			default :
				throw new NotADirectionException();
		}
	}
	
	/**
	 * Renvoie l'entier en fonction de la direction.
	 * 
	 * Convension : 
	 * 
	 * 0: Droite
	 * 1: Haut
	 * 2: Gauche
	 * 3: Bas
	 * @throws NotADirectionException 
	 * 
	 */
	public int toInt() throws NotADirectionException {
		switch (this) {
			case UP:
				return 0;
			case RIGHT:
				return 1;
			case LEFT:
				return 2;
			case DOWN:
				return 3;
			default:
				throw new NotADirectionException();
		}
	}


}