package be.ac.umons.babaisyou.game;

/**
 * Représente la règle SINK.
 * 
 * WIN fait gagner la partie si un joueur est effectivement sur le bloc où est la règle WIN.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionWin extends Action {
	
	@Override
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
