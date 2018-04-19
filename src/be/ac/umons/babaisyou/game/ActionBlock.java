package be.ac.umons.babaisyou.game;

/**
 * Représente la règle STOP.
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

}
