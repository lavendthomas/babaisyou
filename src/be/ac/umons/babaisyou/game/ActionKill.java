package be.ac.umons.babaisyou.game;

public class ActionKill extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// TODO Auto-generated method stub
		
		while (level.hasPlayer(player_position)) {
			level.popPlayer(player_position);
		}
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

}
