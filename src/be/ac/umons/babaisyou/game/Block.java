package be.ac.umons.babaisyou.game;

/**
 * Modélise un block du jeu.
 * 
 * Un bloc est composé de un Type de block et une direction par défaut.
 * 
 * La direction représsente la direction dans lequel le bloc va se déplacer si il a la règle "MOVE".
 * 
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
	
	/**
	 * Crée un block ayant le type mensionné et la direction mensionnée.
	 * @param type Le type du block désiré
	 * @param direction La direction du block désirée.
	 */
	public Block(BlockType type, Direction direction) {
		this.type = type;
		this.direction = direction;
	}
	
	/**
	 * Crée un block ayant le type mensionné et la direction par défaut.
	 * @param type Le type du block désiré
	 */
	public Block(BlockType type) {
		this.type = type;
		this.direction = DEFAULT_DIRECTION;
	}
	
	/**
	 * Crée un block ayant le type par défaut et la direction mensionnée.
	 * @param type Le type du block désiré
	 */
	public Block(Direction direction) {
		this.type = DEFAULT_TYPE;
		this.direction = direction;
	}
	/**
	 * Crée un block ayant le type par défaut et la direction par défaut.
	 */
	public Block() {
		this.type = DEFAULT_TYPE;
		this.direction = DEFAULT_DIRECTION;
	}
	/**
	 * Renvoie le type de ce bloc.
	 * @return le type de ce bloc.
	 */
	public BlockType getType() {
		return type;
	}
	
	/**
	 * Change le type du bloc.
	 * @param type le nouveau type du bloc.
	 */
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
	
	/**
	 * Change la direction du bloc vers sa direction opposée.
	 */
	void oppositeDirection() {
		direction = direction.getOpposite();
	}
	
	/**
	 * Renvoie la direction du bloc.
	 * @return la direction du bloc.
	 */
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
	
	/**
	 * Renvoie un booléen si le bloc et le bloc en paramètre sont de même type.
	 * @param other le bloc à comparer
	 * @return true si les blocs ont le même type. false sinon.
	 */
	boolean isSameType(Block other) {
		return type == other.getType();
	}
	
	/**
	 * Renvoie un booléen si le bloc a le BlockType type.
	 * @param other Le BlockType à vérifier.
	 * @return true si le bloc est du type spécifié. false sinon.
	 */
	boolean isSameType(BlockType type) {
		return this.type == type;
	}
	
	/**
	 * Renvoie vrai si ce bloc est du type par défaut.
	 */
	boolean isDeflaultType() {
		return type == DEFAULT_TYPE;
	}
	
	/** 
	 * Renvoie un booléen si le bloc est poussable par défaut (hors règles).
	 * @return true si le bloc est poussable par défaut. false sinon
	 */
	boolean isPushable() {
		return type.isPushable();
	}
	
	/**
	 * Renvoie un booléen si le bloc est un sélécteur, c'est à dire il a un identifiant de la forme "text_..."
	 * @return true si le bloc est un sélécteur. false sinon.
	 */
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
