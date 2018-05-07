package be.ac.umons.babaisyou.game;

/**
 * Représente la règle HOT.
 * 
 * Un bloc ayant la propriété "HOT" fait disparaître tout block ayant la propriété "MELT" qui est sur la même cellule.
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
