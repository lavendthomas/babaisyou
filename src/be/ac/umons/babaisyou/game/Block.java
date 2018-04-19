package be.ac.umons.babaisyou.game;

/**
 * Modélise un block du jeu.
 * 
 * Un bloc est composé de un Type de block et une direction par défaut.
 * @author Thomas Lavend'Homme
 *
 */
public class Block {
	
	/**
	 * Type de bloc qui consitue le jeu si aucun autre bloc n'est prédéfini.
	 */
	public static final BlockType DEFAULT_TYPE = BlockType.VOID;
	
	/**
	 * Direction du bloc qui consitue le jeu si aucune autre direction n'est prédéfini
	 */
	public static final Direction DEFAULT_DIRECTION = Direction.UP;
	
	/**
	 * Type de ce bloc.
	 */
	private BlockType type;
	
	/**
	 * Direction de ce block
	 */
	private Direction direction;
	
	public Block(BlockType type, Direction direction) {
		this.type = type;
		this.direction = direction;
	}
	
	public Block(BlockType type) {
		this.type = type;
		this.direction = DEFAULT_DIRECTION;
	}
	
	public Block(Direction direction) {
		this.type = DEFAULT_TYPE;
		this.direction = direction;
	}
	
	public Block() {
		this.type = DEFAULT_TYPE;
		this.direction = DEFAULT_DIRECTION;
	}
	
	public BlockType getType() {
		return type;
	}
	
	void setType(BlockType type) {
		this.type = type;
	}
	
	/**
	 * Change la direction par défaut du block
	 * @param direction La nouvelle direction du block
	 */
	void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	void oppositeDirection() {
		direction = direction.getOpposite();
	}
	
	
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Renvoie d'identifiant textuel de BlockType que contient le block.
	 * @return Renvoie d'identifiant textuel de BlockType que contient le block.
	 */
	public String getId() {
		return type.getId();
	}
	
	boolean isSameType(Block other) {
		return type == other.getType();
	}
	
	boolean isSameType(BlockType type) {
		return this.type == type;
	}
	
	boolean isDeflaultType() {
		return type == DEFAULT_TYPE;
	}
	
	boolean isPushable() {
		return type.isPushable();
	}
	
	boolean isSelector() {
		return type.isSelector();
	}
	
	@Override
	public String toString() {
		return type.getId();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (type != other.type)
			return false;
		if (direction != other.direction)
			return false;
		return true;
	}

}
