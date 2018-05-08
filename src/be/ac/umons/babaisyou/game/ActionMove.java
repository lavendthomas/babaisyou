package be.ac.umons.babaisyou.game;

import java.util.LinkedList;
import java.util.List;

/**
 * Représente la règle "MOVE".
 * 
 * "MOVE" déplace tout les blocs de ce type vers la direction par défaut de chaque bloc.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionMove extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		//Ne rien faire
		
	}

	@Override
	public void onEachTour(BlockType type) {
		
		List<Position> movingBlocksPositions = new LinkedList<Position>();
		
		for (int x=0; x < level.getWidth(); x++) {
			for (int y=0; y < level.getHeight(); y++) {
				Position currentPos = new Position(x, y);
				if (level.hasBlockType(type, currentPos)) {
					movingBlocksPositions.add(currentPos);
				}
			}
		}
		
		for (Position pos : movingBlocksPositions) {
			boolean hasMoved = level.moveOneBlock(type, pos);
			if (!hasMoved) {
				//Mettre le montre dans la direction opposée
				level.oppositeBlockDirection(type, pos);
				level.moveOneBlock(type, pos);
			}
		}
		
	}
	
	
}
