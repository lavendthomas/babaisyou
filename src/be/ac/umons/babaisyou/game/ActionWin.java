package be.ac.umons.babaisyou.game;

public class ActionWin extends Action {
	
	public void execute(Block block, Position player_position, Direction player_direction) {
		// Est exetuté si le joueur passe sur un case qui contient la régle win.
		// Donc faire gagner le joueur directememt.
		if (level.hasPlayer(player_position)) {
			level.setWon(true);
		}
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

	
}
