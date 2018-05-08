package be.ac.umons.babaisyou.game;

/**
 * Représente la règle SINK.
 * 
 * "SINK" fait disparaître tout les blocs qui se déplacent sur lui puis supprime le bloc ayant la propriété SINK.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionSink extends Action {
	
	/**
	 * Fait couler le joueur et retire le block qui a la propriété sink.
	 * 
	 * @param block le block qui a déclanché l'exécution de l'action
	 * @param player_position la position du joueur 
	 * @param player la direction du dernier mouvement du joueur
	 */
	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		level.remove(block, player_position);
		Block deleted = level.pop(player_position);
		if (!deleted.getType().isMaterial()) {
			//Reajouter l'eau et la position qui ont été supprimées erronément
			level.add(block, player_position); 
			level.add(deleted, player_position);
		}
	}

	@Override
	public void onEachTour(BlockType type) {
		//Ne rien faire
		
	}

}
