package be.ac.umons.babaisyou.game;

/**
 * Représente la règle MELT.
 * 
 * MELT fait fondre le block si un hot est aussi présent sur la même case.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionMelt extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		boolean hasHot = false;
		for (Block blockInCell : level.get(player_position)) {
			if (level.isHot(blockInCell.getType())) {
				hasHot = true;
				break;
			}
		}
		
		if (hasHot) {
			for (Block blockInCell : level.get(player_position)) {
				if (block.isSameType(blockInCell)) {
					level.remove(blockInCell, player_position);
				}
			}
		}
		
	}

	@Override
	public void onEachTour(BlockType type) {
		// Ne rien faire
		
	}

}
