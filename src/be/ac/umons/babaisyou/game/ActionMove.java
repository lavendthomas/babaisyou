package be.ac.umons.babaisyou.game;

import java.util.LinkedList;
import java.util.List;

public class ActionMove extends Action {

	@Override
	public void execute(Block block, Position player_position, Direction player_direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEachTour(BlockType type) {
		
		// TODO Auto-generated method stub
		
		List<Position> movingBlocksPositions = new LinkedList<Position>();
		
		for (int x=0; x < level.getWidth(); x++) {
			for (int y=0; y < level.getHeight(); y++) {
				Position currentPos = new Position(x, y);
				if (level.hasBlockType(type, currentPos)) {
					movingBlocksPositions.add(currentPos);
				}
			}
		}
		
		//TODO garder une liste de position pour chaque monster...
		
		for (Position pos : movingBlocksPositions) {
			level.moveOneBlock(type, pos);
		}
		
	}
	
	
}
