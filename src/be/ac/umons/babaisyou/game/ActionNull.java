package be.ac.umons.babaisyou.game;

/**
 * Implémente une règle vide.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionNull extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// Ne fait rien
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// Ne fait rien
		
	}

}
