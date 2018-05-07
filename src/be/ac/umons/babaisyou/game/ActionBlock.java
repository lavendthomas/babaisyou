package be.ac.umons.babaisyou.game;

/**
 * Représente la règle "STOP".
 * 
 * "STOP" empêche tout bloc de se déplacer sur un bloc ayant cette propriété.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionBlock extends Action {
	
	@Override
	public boolean isBlocking() {
		//Vrai par définition de l'action.
		return true;
	}

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// Ne rien faire
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// Ne rien faire
		
	}


}
