package be.ac.umons.babaisyou.game;

public class ActionSink extends Action {
	
	/**
	 * Fait couler le joueur et retire le block qui a la propriété sink.
	 * 
	 * @param block le block qui a déclanché l'exécution de l'action
	 * @param player_position la position du joueur (INUTILISÉE)
	 * @param player la direction du dernier mouvement du joueur (INUTILISÉE)
	 */
	public void execute(Block block, Position player_position, Direction player_direction) {
		level.remove(block, player_position);
		level.pop(player_position);
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

}
