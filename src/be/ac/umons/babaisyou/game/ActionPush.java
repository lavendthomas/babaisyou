package be.ac.umons.babaisyou.game;

/**
 * Représente la règle PUSH.
 * 
 * PUSH permet au joueur de déplacer un bloc en ce déplaçant vers lui.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionPush extends Action{
	
	@Override
	public boolean isPushable() {
		//Poussable par définition de l'action.
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
