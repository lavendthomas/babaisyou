package be.ac.umons.babaisyou.game;

/**
 * Représente la règle du jeu "BEST".
 * 
 * Lorsque un bloc à la propriété "BEST", un overlay doit lui être appliqué.
 * 
 * @author thomas
 *
 */
public class ActionBest extends Action {
	
	@Override
	public boolean isBest() {
		return true;
	}

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

}
