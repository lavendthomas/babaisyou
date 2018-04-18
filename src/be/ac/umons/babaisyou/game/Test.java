package be.ac.umons.babaisyou.game;

import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Classe comprenant des tests unitaires
 * @author Thomas Lavend'Homme
 *
 */
public class Test {
	
	/**
	 * Vérifie que la reconnaissance de règle se fasse correctement verticalement.
	 */
	@org.junit.Test
	public void testRuleRecognitionVertical() {
		Level level = new Level(1,4);
		level.set(new Block(BlockType.TEXT_WALL), new Position(0,0));
		level.set(new Block(BlockType.IS), new Position(0,1));
		level.set(new Block(BlockType.PUSH), new Position(0,2));
		level.set(new Block(BlockType.WALL), new Position(0,3));
		assertTrue(level.hasPushable(new Position(0,3)));
		
	}
	
	/**
	 * Vérifie que la reconnaissance de règle se fasse correctement horizontale.
	 */
	@org.junit.Test
	public void testRuleRecognitionHorizontal() {
		Level level = new Level(4,1);
		level.set(new Block(BlockType.TEXT_WALL), new Position(0,0));
		level.set(new Block(BlockType.IS), new Position(1,0));
		level.set(new Block(BlockType.PUSH), new Position(2,0));
		level.set(new Block(BlockType.WALL), new Position(3,0));
		assertTrue(level.hasPushable(new Position(3,0)));
		
	}
	
	/**
	 * Vérifie que le joueur puisse bien se déplacer si il a le règle YOU.
	 */
	@org.junit.Test
	public void testMove() {
		Level level = new Level(5,5);
		level.set(new Block(BlockType.TEXT_BABA), new Position(1,1));
		level.set(new Block(BlockType.IS), new Position(2,1));
		level.set(new Block(BlockType.YOU), new Position(3,1));
		level.set(new Block(BlockType.BABA), new Position(2,2));
		
		level.move(Direction.RIGHT);
		
		//Test si le joueur a bien quitté la cellule de départ.
		String[] emptyCell = {Block.DEFAULT_TYPE.getId()};
		
		assertTrue(Arrays.equals(emptyCell,level.getToId(2,3)));
		
		//Test si le joueur est bien arrivé sur la cellule d'arrivée.
		String[] nextCell = {BlockType.BABA.getId()};
		assertTrue(Arrays.equals(level.getToId(3,2), nextCell));
	}
	
	/**
	 * Vérifie que la partie soit bien gagnée si un joueur tombe sur WIN et pas avant.
	 */
	@org.junit.Test
	public void testHasWon() {
		Level level = new Level(5,5);
		level.set(new Block(BlockType.TEXT_WALL), new Position(1,1));
		level.set(new Block(BlockType.IS), new Position(2,1));
		level.set(new Block(BlockType.WIN), new Position(3,1));
		level.set(new Block(BlockType.TEXT_BABA), new Position(1,0));
		level.set(new Block(BlockType.IS), new Position(2,0));
		level.set(new Block(BlockType.YOU), new Position(3,0));
		level.set(new Block(BlockType.BABA), new Position(2,2));
		level.set(new Block(BlockType.WALL), new Position(2,3));
		
		//Teste les faux positifs
		assertFalse(level.hasWon());
		
		level.move(Direction.DOWN);
		//Teste que la détection du win
		assertTrue(level.hasWon());
	}
	
	/**
	 * Vérifie que le joueur ne puisse pas se déplacer hors de la carte.
	 */
	@org.junit.Test
	public void testMoveOutOfLevel() {
		Level level = new Level(4,4);
		level.set(new Block(BlockType.TEXT_BABA), new Position(1,1));
		level.set(new Block(BlockType.IS), new Position(2,1));
		level.set(new Block(BlockType.YOU), new Position(3,1));
		level.set(new Block(BlockType.BABA), new Position(3,3));
		level.move(Direction.DOWN);
		
		//Teste que le joueur n'aie pas bougé
		String[] nextCell = {BlockType.BABA.getId()};
		assertTrue(Arrays.equals(level.getToId(3,3), nextCell));
		
	}
	
	/**
	 * Vérifie que le joueur puisse bien pousser un bloc qui a la proporiété PUSH.
	 */
	@org.junit.Test
	public void testPushedNonRecursive() {
		Level level = new Level(5,5);
		level.set(new Block(BlockType.TEXT_WALL), new Position(1,1));
		level.set(new Block(BlockType.IS), new Position(2,1));
		level.set(new Block(BlockType.PUSH), new Position(3,1));
		level.set(new Block(BlockType.TEXT_BABA), new Position(1,0));
		level.set(new Block(BlockType.IS), new Position(2,0));
		level.set(new Block(BlockType.YOU), new Position(3,0));
		level.set(new Block(BlockType.BABA), new Position(2,2));
		level.set(new Block(BlockType.WALL), new Position(2,3));
		
		level.move(Direction.DOWN);
		
		String[] babaCell = {BlockType.BABA.getId()};
		String[] wallCell = {BlockType.WALL.getId()};
		String[] emptyCell = {Block.DEFAULT_TYPE.getId()};
		
		//Teste que le WALL aie bien été poussé
		assertTrue(Arrays.equals(level.getToId(2,4), wallCell));
		
		//Teste que le BABA se soit bien déplacé
		assertTrue(Arrays.equals(level.getToId(2,3), babaCell));
		
		//Teste que le BABA aie bien quitté la case précédente
		assertTrue(Arrays.equals(level.getToId(2,2), emptyCell));
	}
	
	/**
	 * Vérifie que le joueur soit bien bloqué un bloc qui a la proporiété STOP.
	 */
	@org.junit.Test
	public void testStopNonRecursive() {
		Level level = new Level(5,5);
		level.set(new Block(BlockType.TEXT_WALL), new Position(1,1));
		level.set(new Block(BlockType.IS), new Position(2,1));
		level.set(new Block(BlockType.STOP), new Position(3,1));
		level.set(new Block(BlockType.TEXT_BABA), new Position(1,0));
		level.set(new Block(BlockType.IS), new Position(2,0));
		level.set(new Block(BlockType.YOU), new Position(3,0));
		level.set(new Block(BlockType.BABA), new Position(2,2));
		level.set(new Block(BlockType.WALL), new Position(2,3));
		
		level.move(Direction.DOWN);
		
		String[] babaCell = {BlockType.BABA.getId()};
		String[] wallCell = {BlockType.WALL.getId()};
		String[] emptyCell = {Block.DEFAULT_TYPE.getId()};
		
		//Teste que le WALL ne se soit pas déplacé
		assertTrue(Arrays.equals(level.getToId(2,3), wallCell));
		
		//Teste que le BABA ne se soit pas déplacé
		assertTrue(Arrays.equals(level.getToId(2,2), babaCell));
	}

}
