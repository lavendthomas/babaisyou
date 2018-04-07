package be.ac.umons.babaisyou.game;

public class ActionBlocking extends Action {

	@Override
	public boolean isBlocking() {
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
