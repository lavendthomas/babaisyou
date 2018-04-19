package be.ac.umons.babaisyou.game;

import be.ac.umons.babaisyou.exceptions.BlockNotFoundException;

public enum BlockType {
	//Matériaux, aucune action par nature.
	VOID("void", BlockStyle.MATERIAL),
	WALL("wall", BlockStyle.MATERIAL),
	ROCK("rock", BlockStyle.MATERIAL),
	BABA("baba", BlockStyle.MATERIAL),
	FALG("flag", BlockStyle.MATERIAL),
	GRASS("grass", BlockStyle.MATERIAL),
	WATER("water", BlockStyle.MATERIAL),
	LAVA("lava", BlockStyle.MATERIAL),
	METAL("metal", BlockStyle.MATERIAL),
	SKULL("skull", BlockStyle.MATERIAL),
	MONSTER("monster", BlockStyle.MATERIAL),
	
	//SELECTEURS
	IS("is", BlockStyle.IS, true),
	TEXT_VOID("text_void", BlockStyle.SELECTOR, true),
	TEXT_WALL("text_wall", BlockStyle.SELECTOR, true),
	TEXT_ROCK("text_rock", BlockStyle.SELECTOR, true),
	TEXT_BABA("text_baba", BlockStyle.SELECTOR, true),
	TEXT_FLAG("text_flag", BlockStyle.SELECTOR, true),
	TEXT_WATER("text_water", BlockStyle.SELECTOR, true),
	TEXT_LAVA("text_lava", BlockStyle.SELECTOR, true),
	TEXT_METAL("text_metal", BlockStyle.SELECTOR, true),
	TEXT_SKULL("text_skull", BlockStyle.SELECTOR, true),
	TEXT_MONSTER("text_monster", BlockStyle.SELECTOR, true),
	
	//ACTION
	WIN("win", BlockStyle.ACTION, new ActionWin(), true),
	STOP("stop", BlockStyle.ACTION, new ActionBlocking(), true),
	PUSH("push", BlockStyle.ACTION, new ActionPush(), true),
	YOU("you", BlockStyle.ACTION, new ActionPush(),  true),
	SINK("sink", BlockStyle.ACTION, new ActionSink(),  true),
	KILL("kill", BlockStyle.ACTION, new ActionKill(),  true),
	MOVE("move", BlockStyle.ACTION, new ActionMove(),  true),
	TEXT_BEST("text_best", BlockStyle.ACTION, new ActionBest(), true),
	BEST("best", BlockStyle.ACTION), // Ne doit pas être utilisée dans le jeu en tant que bloc à part entière.
	
	;
	/**
	 * Identifiant textuel du bloc.
	 */
	private final String ID;
	
	/**
	 * Action associée au bloc.
	 */
	private final Action ACTION;
	
	/**
	 * Catégorie du bloc.
	 */
	private final BlockStyle STYLE;
	
	/**
	 * true si le bloc est poussabe par défaut
	 */
	private final boolean PUSHABLE;
	
	private BlockType(String id, BlockStyle style, Action action, boolean pushable) {
		ID=id;
		ACTION=action;
		STYLE=style;
		PUSHABLE=pushable;
	}
	
	private BlockType(String id, BlockStyle style, boolean pushable) {
		this(id, style, null, pushable);
	}
	
	private BlockType(String id, BlockStyle style) {
		this(id, style, null, false);
	}
	
	/**
	 * Renvoie l'Iientifiant textuel du bloc.
	 * @return Renvoie l'Iientifiant textuel du bloc.
	 */
	public String getId() {
		return ID;
	}
	
	/**
	 * Renvoie l'action associée au bloc.
	 * @returnRenvoie l'action associée au bloc.
	 */
	public Action getAction() {
		return ACTION;
	}
	
	/**
	 * Renvoie si le block est poussable par défaut
	 * @return
	 */
	public boolean isPushable() {
		return PUSHABLE;
	}
	
	/**
	 * Renvoie si le bloc est de type "Material"
	 *
	 */
	public boolean isMaterial() {
		return STYLE == BlockStyle.MATERIAL;
	}
	
	/**
	 * Renvoie si le bloc est de type "Selector"
	 *
	 */
	public boolean isSelector() {
		return STYLE == BlockStyle.SELECTOR;
	}
	
	/**
	 * Renvoie le BlockType correpondant à la selection.
	 * @return
	 * @throws BlockNotFoundException Si le block n'est pas un sélecteur ou si il n'y a pas de bloc correpondant.
	 */
	public BlockType getSelection() {
		if (STYLE != BlockStyle.SELECTOR) {
			throw new RuntimeException(new BlockNotFoundException());
		}
		else {
			return BlockType.fromId(ID.substring(5)); //Coupe "text_" de l'id.
		}
	}
	
	/**
	 * Renvoie si le bloc est de type "Action"
	 *
	 */
	public boolean isAction() {
		return STYLE == BlockStyle.ACTION;
	}
	
	/**
	 * Renvoie le BlockType auquel correspond l'id correspondant
	 * @param id Un identifiant textuel d'un bloc
	 * @return Le block correspondant
	 * @throws BlockNotFoundException si aucun bloc ne correpond à l'id.
	 */
	public static BlockType fromId(String id) {
		
		for(BlockType type : BlockType.values()) {
			if (type.getId().equals(id)) {
				return type;
			}
		}
		//Si aucun bloc n'a l'id correspondant.
		System.out.println(id + " not found.");
		throw new RuntimeException(new BlockNotFoundException());
	}
	

}
