package be.ac.umons.babaisyou.game;

/**
 * Représente la règle HOT.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionHot extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// Ne rien faire
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// Ne rien faire
		
	}
	
	@Override
	public boolean isHot() {
		return true;
	}
	
}
