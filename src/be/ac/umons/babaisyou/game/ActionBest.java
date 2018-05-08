package be.ac.umons.babaisyou.game;

/**
 * Représente la règle du jeu "BEST".
 * 
 * Lorsque un bloc à la propriété "BEST", un overlay doit lui être appliqué.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionBest extends Action {
	
	@Override
	public boolean isBest() {
		return true;
	}

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		//Ne rien faire
	}

	@Override
	public void onEachTour(BlockType type) {
		//Ne rien faire
		
	}

}
