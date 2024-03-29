package be.ac.umons.babaisyou.game;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;

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
		assertTrue("Reconnaisance de règle verticale échouée",
				level.hasPushable(new Position(0,3)));
		
	}
	
	@org.junit.Test
	public void testRecognitionRuleOnMove() {
		Level level = new Level(4,4);
		level.set(new Block(BlockType.BABA), new Position(0,1));
		level.set(new Block(BlockType.WALL), new Position(3,3));
		level.set(new Block(BlockType.TEXT_BABA), new Position(0,3));
		level.set(new Block(BlockType.IS), new Position(1,3));
		level.set(new Block(BlockType.YOU), new Position(2,3));
		level.set(new Block(BlockType.TEXT_WALL), new Position(1,0));
		level.set(new Block(BlockType.IS), new Position(1,1));
		level.set(new Block(BlockType.PUSH), new Position(1,2));
		assertTrue("La reconnaissance de règle avant déplacement à échouée",
				level.hasPushable(new Position(3,3)));
		level.move(Direction.RIGHT);
		assertFalse("La reconnaissance de règle après cassage à échouée : toujours présente",
				level.hasPushable(new Position(3,3)));
		level.move(Direction.LEFT);
		level.move(Direction.UP);
		level.move(Direction.RIGHT);
		level.move(Direction.LEFT);
		level.move(Direction.DOWN);
		level.move(Direction.DOWN);
		level.move(Direction.RIGHT);
		assertTrue("La reconnaissance de règle après la création d'une règle à échouée",
				level.hasPushable(new Position(3,3)));
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
		assertTrue("Reconnaisance de règle horizontale échouée",
				level.hasPushable(new Position(3,0)));
		
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
		
		assertTrue("Le joueur n'a pas quitté la cellule de départ",
				Arrays.equals(emptyCell,level.getToId(2,3)));
		
		//Test si le joueur est bien arrivé sur la cellule d'arrivée.
		String[] nextCell = {BlockType.BABA.getId()};
		assertTrue("Le joueur n'est pas arrivé sur la cellule d'arrivée",
				Arrays.equals(level.getToId(3,2), nextCell));
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
		assertFalse("Faux positif : le niveau indique erronément qu'il est réussi",
				level.hasWon());
		
		level.move(Direction.DOWN);
		//Teste que la détection du win
		assertTrue("Le niveau devrait être réussi mais ne l'est pas",
				level.hasWon());
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
		assertTrue("Le joueur a bougé alors qu'il était bloqué par la limite de la carte",
				Arrays.equals(level.getToId(3,3), nextCell));
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
		assertTrue("Le block n'a pas été poussé",
				Arrays.equals(level.getToId(2,4), wallCell));
		
		//Teste que le BABA se soit bien déplacé
		assertTrue("Le joueur n'a avancé",
				Arrays.equals(level.getToId(2,3), babaCell));
		
		//Teste que le BABA aie bien quitté la case précédente
		assertTrue("Le joueur n'a pas été supprimé de la case de départ",
				Arrays.equals(level.getToId(2,2), emptyCell));
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
		
		//Teste que le WALL ne se soit pas déplacé
		assertTrue("Le block STOP a été déplacé",
				Arrays.equals(level.getToId(2,3), wallCell));
		
		//Teste que le BABA ne se soit pas déplacé
		assertTrue("Le joueur s'est déplacé alors qu'il était bloqué par un STOP",
				Arrays.equals(level.getToId(2,2), babaCell));
	}
	
	/**
	 * S'assure que charger un fichier de niveau de cause pas un crash.
	 * 
	 * Toutes des exceptions WrongFileFormatException doivent être rattrapées par l'interface graphique et
	 * donc ne posent pas problème.
	 */
	@org.junit.Test
	public void testLevelLoadingFromFile() {
		File testFile = new File("tmp");
		
		//Écris un fichier de niveau invalide
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(testFile))) {
			buffer.write("5 5\n");
			buffer.write("baba 2 4\n");
			buffer.write("notACorrectFile\n");
			buffer.write("is 4 4\n");
		} catch (IOException e) {
			assertTrue("Le fichier correspondant au niveau n'as pas pu être écrit.", false);
		}
		
		try {
			Level level = Level.load(testFile);
			assertFalse("Le niveau a été chargé sur un fichier corrompu", true);
		} catch (WrongFileFormatException e) {
			//État normal
		}
		testFile.delete();
		
	}

}
