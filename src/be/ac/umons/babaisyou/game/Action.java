package be.ac.umons.babaisyou.game;

/**
 * Représente une des règle du jeu.
 * @author thomas
 *
 */
public abstract class Action {
	
	/**
	 * level est l'objet plateau sur lequel les actions vont travailler.
	 */
	protected static Level level;
	
	/**
	 * Permet d'assigner le niveau sur lequel toutes les action vont travailler
	 * @param level le niveau
	 */
	public static void setLevel(Level level) {
		Action.level = level;
	}
	
	/**
	 * Exécute l'action associée
	 * 
	 * @param block le block qui a déclanché l'exécution de l'action
	 * @param player_position la position du joueur
	 * @param player la direction du dernier mouvement du joueur
	 */
	public abstract void execute(Block block, Position player_position, Direction player_direction);
	
	/**
	 * Excecute l'action à faire à chaque tour.
	 * @param le type de block auquel l'action est associée
	 */
	public abstract void onEachTour(BlockType type) ;
	
	/**
	 * Renvoie si la règle a la propriété "STOP".
	 * @return true si la règle a la propriété "STOP". false sinon.
	 */
	public boolean isBlocking() {
		//Ne permet pas de bloquer si aucune règle l'autorise.
		return false;
	}
	
	/**
	 * Renvoie si la règle a la propriété "PUSH".
	 * @return true si la règle a la propriété "PUSH".. false sinon.
	 */
	public boolean isPushable() {
		//Ne permet pas de pusher si aucune règle l'autorise.
		return false;
	}
	
	/**
	 * Renvoie si la règle a la propriété "BEST".
	 * @return true si la règle a la propriété "BEST". false sinon.
	 */
	public boolean isBest() {
		//N'est pas BEST par défaut.
		return false;
	}
	
	/**
	 * Renvoie si la règle a la propriété "HOT".
	 * @return true si la règle a la propriété "HOT". false sinon.
	 */
	public boolean isHot() {
		//N'est pas HOT par défaut.
		return false;
	}
}
