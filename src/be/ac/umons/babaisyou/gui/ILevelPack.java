package be.ac.umons.babaisyou.gui;

import be.ac.umons.babaisyou.exceptions.GamedCompletedException;
import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;
import be.ac.umons.babaisyou.game.Level;

/**
 * Représente un pack de niveau (en ligne ou hors ligne)
 * Un pack est composé de order et les niveaux.
 * @author thomas
 *
 */
public interface ILevelPack {
	
	/**
	 * Renvoie le niveau actuel dans le liste de niveaux
	 * @return
	 * @throws WrongFileFormatException si le niveau ne correspond pas à un niveau de BabaIsYou
	 */
	Level getCurrentLevel() throws WrongFileFormatException;
	
	/**
	 * Permet de chamger le permier niveau
	 * @param firstLevel Le nom du premier niveau à jouer
	 */
	void setFirstLevel(String firstLevel);
	
	/**
	 * Passe au niveau suivant, retourne null si la partie est terminée
	 * @return Le niveau suivant
	 * @throws GamedCompletedException SI le joueur a fini la partie
	 */
	Level nextLevel() throws GamedCompletedException;
	
	/**
	 * Renvoie la liste de tous les niveaux jouables, c'est à dire tous les niveaux
	 * déjà joués plus le suivant.
	 * Si un niveau est jouable, son nom sera dans la liste, sinon La valeur sera null.
	 * @return liste de tous les niveaux jouables, liste de String
	 */
	String[] getPlayableLevels();

}
