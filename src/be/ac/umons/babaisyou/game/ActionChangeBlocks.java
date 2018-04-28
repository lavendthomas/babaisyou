package be.ac.umons.babaisyou.game;

/**
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ActionChangeBlocks extends Action {
	
	/**
	 * Type de départ (doit )
	 */
	private BlockType from;
	
	/**
	 * Type d'arrivée
	 */
	private BlockType to;
	
	public ActionChangeBlocks(BlockType from, BlockType to) {
		if (!from.isSelector() || !to.isSelector()) {
			throw new UnsupportedOperationException("Must be a selector");		
		}
		this.from = from;
		this.to = to;
	}
	
	
	/**
	 * Change tous les blocks de type from vers des blocks de type to
	 * 
	 * @param block le block qui a déclanché l'exécution de l'action (INUTILISÉ)
	 * @param player_position la position du joueur (INUTILISÉE)
	 * @param player la direction du dernier mouvement du joueur (INUTILISÉE)
	 */
	public void execute(Block block, Position player_position, Direction player_direction) {
		
		for (int i=0; i<level.getHeight(); i++) {
			for (int j=0; j<level.getWidth(); j++) {
				Position position = new Position(j,i);
				
				for (Block block_ : level.get(position)) {
					if (block_.isSameType(from.getSelection())) {
						block_.setType(to.getSelection());
					}
				}
					
			}
		}
		level.requestRuleParsing();
	}

	@Override
	public void onEachTour(BlockType type) {
		// TODO Auto-generated method stub
		
	}

}
