package be.ac.umons.babaisyou.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import be.ac.umons.babaisyou.exceptions.GamedCompletedException;
import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;
import be.ac.umons.babaisyou.exceptions.WrongLevelDimensionException;
import be.ac.umons.babaisyou.game.Level;

public class LevelPack implements ILevelPack {
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	static final String DEFLAUT_MAPS_LOCATION = "." + File.separator + "src" + File.separator + 
			"be" + File.separator +"ac"+File.separator+"umons"+File.separator +
			"babaisyou" + File.separator + "ressources"+ File.separator + "levels"
			+ File.separator;
	
	static final String CUSTOM_MAPPACKS_LOCATION = "";
	
	/**
	 * Nom du fichier qui spécifier l'ordre dans lequel jouer les niveaux
	 */
	static final String DEFAULT_ORDER_FILENAME = "order";
	
	/**
	 * Nom du fichier qui retiens les parties déjà jouées
	 */
	static final String DEFAULT_FINISHED_FILENAME = ".finished";
	
	/**
	 * Niveau actuel.
	 */
	private String currentLevel;
	
	/**
	 * Indice du niveau actuel
	 */
	private int currentLevelIndex;
	
	/**
	 * Emplacement du niveau actuel
	 */
	private String currentLevelsLocation;
	
	/**
	 * Liste contenant tous les niveaux dans l'ordre
	 */
	private String[] levelsList;
	
	/**
	 * Stocke les niveaux que le joueur a terminé (Stockés dans .finished)
	 */
	private LinkedList<String> alreadyPlayedLevels;
	
	/**
	 * Initialise une série de niveaux à l'endroit spécifié.
	 * @param location
	 */
	public LevelPack(String location) {
		currentLevelsLocation = location + File.separator;
		//lecture de la liste des niveaux
		LinkedList<String> listOfLevel = new LinkedList<>();
		alreadyPlayedLevels = new LinkedList<>();
		
		try (BufferedReader buffer = new BufferedReader(new FileReader(currentLevelsLocation + DEFAULT_ORDER_FILENAME))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				listOfLevel.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.log(java.util.logging.Level.SEVERE, null, e);
			throw new RuntimeException(e);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//TODO Prévenir lútilisateur dans le GUI
			LOGGER.log(java.util.logging.Level.SEVERE, null, e);
			throw new RuntimeException(e);
		}
		levelsList = listOfLevel.toArray(new String[listOfLevel.size()]);
		
		//Lecture de la liste des parties déjà jouées
		try (BufferedReader buffer = new BufferedReader(new FileReader(currentLevelsLocation + DEFAULT_FINISHED_FILENAME))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				//Ajouter seulement si présent dans les niveaux
				if (listOfLevel.contains(line)) {
					alreadyPlayedLevels.add(line);
				}
				
			}
		} catch (FileNotFoundException e) {
			// Si aucune session précédente n'a été faite, il ne faut rien ajouter à alreadyPlayedLevels
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.log(java.util.logging.Level.SEVERE, "Problem occured while reading already played Levels", e);
			throw new RuntimeException(e);
		}
		
		//Définis le premier niveau.
		if (levelsList.length != 0) {
			currentLevel = levelsList[0];
			currentLevelIndex = 0;
		}
		
	}
	
	/**
	 * Renvoie le niveau actuel dans le liste de niveaux
	 * @return
	 */
	@Override
	public Level getCurrentLevel() throws WrongFileFormatException {
		try {
			return Level.load(currentLevelsLocation + currentLevel);
		} catch (WrongFileFormatException | WrongLevelDimensionException | IOException e) {
			throw new WrongFileFormatException(e);
		}
	}
	
	/**
	 * Permet de chamger le permier niveau
	 * @param firstLevel Le nom du premier niveau à jouer
	 */
	@Override
	public void setFirstLevel(String firstLevel) {
		currentLevel = firstLevel;
		for (int i=0; i<levelsList.length; i++) {
			if (levelsList[i].equals(firstLevel)) {
				currentLevelIndex = i;
				break;
			}
		}
	}
	
	/**
	 * Passe au niveau suivant, retourne null si la partie est terminée
	 * @return Le niveau suivant
	 * @throws GamedCompletedException SI le joueur a fini la partie
	 */
	@Override
	public Level nextLevel() throws GamedCompletedException {
		//Ajouter l'élément à la liste des jeux joués et sauver dans le fichier
		if (!alreadyPlayedLevels.contains(currentLevel)) {
			//Ajouter si pas fini dans une session précédente
			alreadyPlayedLevels.add(currentLevel);
		}
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(currentLevelsLocation + DEFAULT_FINISHED_FILENAME, false))) {
			if (alreadyPlayedLevels.size() != 0) {
				buffer.write("");
				for (String level : alreadyPlayedLevels) {
					buffer.append(level + "\n");
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			LOGGER.log(java.util.logging.Level.SEVERE, "Problem occured while saving last played Level", e1);
			throw new RuntimeException(e1);
		}
		
		
		if (currentLevelIndex + 1 >= levelsList.length) {
			//Cas où le joueur a fini tous les niveaux
			throw new GamedCompletedException();
		}
		
		// Cas où le joueur n'as pas encore fini
		currentLevelIndex++;
		currentLevel = levelsList[currentLevelIndex];
		
		try {
			return Level.load(currentLevelsLocation + currentLevel);
		} catch (WrongFileFormatException | WrongLevelDimensionException | IOException e) {
			LOGGER.log(java.util.logging.Level.SEVERE, "Problem occured while loading current Level", e);
		}
		//Si le jeu n'as pas pu charger le niveau suivant
		return null;
		
	}
	
	/**
	 * Renvoie la liste de tous les niveaux jouables, c'est à dire tous les niveaux
	 * déjà joués plus le suivant.
	 * Si un niveau est jouable, son nom sera dans la liste, sinon La valeur sera null.
	 * @return liste de tous les niveaux jouables, liste de String
	 */
	@Override
	public String[] getPlayableLevels() {
		String[] playableLevels = new String[levelsList.length];
		boolean previousFinished = true;
		boolean played = false;
		for (int i=0; i<levelsList.length; i++) {
			played = alreadyPlayedLevels.contains(levelsList[i]);
			if (played) {
				playableLevels[i] = levelsList[i];
				previousFinished = true;
			}
			else if (previousFinished) {
				//Ce niveau n'a jamais été joué mais il est accessible car le précédent est réussi.
				playableLevels[i] = levelsList[i];
				previousFinished = false;
			}
		}
		return playableLevels;
	}
	

}
