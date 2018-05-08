package be.ac.umons.babaisyou.game;

import java.util.LinkedList;

import be.ac.umons.babaisyou.exceptions.NotADirectionException;

/**
 * Modélise une carte d'un niveau
 * @author Thomas Lavend'Homme
 *
 */
public class Board {
	
	/*
	 * Chaque cellule de ce tableau contient la liste de tous les blocs qui sont à la position associée à cette cellule.
	 */
	private LinkedList<Block>[][] board;
	
	/**
	 * La largeur du niveau.
	 */
	private int width;
	
	/**
	 * La hauteur du niveau.
	 */
	private int height;
	
	
	/**
	 *
	 * Instancie une carte remplie de bloc par défaut.
	 * @param width La largeur de la carte
	 * @param height La hauteur de la carte
	 */
	@SuppressWarnings("unchecked")
	public Board(int width, int height) {
		board = (LinkedList<Block>[][]) new LinkedList[height][width];
		this.height = height;
		this.width = width;
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				board[y][x] = new LinkedList<Block>();
			}
		}
	}
	
	/**
	 * Fixe un élement de la carte.
	 * @param block L'élément à placer
	 * @param position L'emplacement ou mettre le bloc.
	 */
	public void set(Block block, Position position) {
		board[position.getY()][position.getX()] = new LinkedList<Block>();
		board[position.getY()][position.getX()].add(block);
	}
	
	/**
	 * Ajoute un élement à la carte.
	 * @param block L'élément à placer
	 * @param position L'emplacement ou mettre le bloc.
	 */
	public void add(Block block, Position position) {
		if (block != null) {
			board[position.getY()][position.getX()].add(block);
		}
	}
	
	public Block pop(BlockType type, Position position) {
		for (Block block : get(position)) {
			if (block == null) {
				return null;
			}
			if (block.getType() == type) {
				board[position.getY()][position.getX()].remove(block);
				return block;
			}
		}
		return null;
	}
	
	/**
	 * Supprime le premier bloc à la position mensionnée
	 * @param position
	 * @return le block à la position mensionnée
	 */
	Block pop(Position position) {
		Block blockToPop = board[position.getY()][position.getX()].get(0);
		board[position.getY()][position.getX()].remove(0);
		return blockToPop;
	}
	
	/**
	 * Renvoie les éléments de la carteobj
	 * @param position La position désirée
	 * @return Le bloc à la position mensionnée
	 */
	public Block[] get(Position position) {
		LinkedList<Block> blocks = board[position.getY()][position.getX()];
		return blocks.toArray(new Block[blocks.size()]);
	}
	
	/**
	 * 
	 * @param x La largeur par rapport au point supérieur gauche
	 * @param y La hauteur par rapport au point supérieur gauche
	 * @return L'élement à la position désirée
	 */
	public Block[] get(int x, int y) {
		Position position = new Position(x, y);
		return get(position);
	}
	
	/**
	 * Supprimme la premiere occurence de block a la position mensionnée
	 * @param block Le bloc à supprimer
	 * @param position la position où on doit supprimer le bloc
	 */
	void remove(Block block, Position position) {
		for (Block block_ : get(position)) {
			if (block_.equals(block)) {
				board[position.getY()][position.getX()].remove(block);
			}
		}
	}
	
	
	/**
	 * Renvoie un élément de la carte si il y a un élement dans la carte, sinon renvoie le block par défaut
	 * @param position La position désirée
	 * @return Le bloc à la position mensionnée
	 * @throws OutOfLevelException
	 */
	public Block[] getOtherwiseDefaut(Position position) {
		if (!(isInBoard(position))) {
			Block[] res = {new Block()};
			return res;
		}
		return get(position);
	}
	
	/**
	 * Change la direction du block de type mensionné vers la direction mensionnée
	 * @param type Le type du block dont il faut changer la direction
	 * @param position La position du block dont il faut faut changer la direction
	 * @param direction La nouvelle direction
	 */
	void changeBlockDirection(BlockType type, Position position, Direction direction) {
		for (Block block : get(position)) {
			if (block.isSameType(type)) {
				block.setDirection(direction);
			}
		}
	}
	
	/**
	 * Change la direction du block de type mensionné vers la direction oppossée
	 * @param type Le type du block dont il faut changer la direction
	 * @param position La position du block dont il faut faut changer la direction
	 */
	void oppositeBlockDirection(BlockType type, Position position) {
		for (Block block : get(position)) {
			if (block.isSameType(type)) {
				block.oppositeDirection();
			}
		}
	}
	
	/**
	 * Change la position du block de type mensionné vers la position opposée
	 * @param type la type de la 
	 * @param position
	 * @param direction
	 */
	void oppositeBlockDirection(BlockType type, Position position, Direction direction) {
		
	}
	
	/**
	 * Utilisé pour renvoyer l'état sous forme de chîne de caratère pour la sauvegarde
	 * sur un fichier texte.
	 * @return
	 */
	public String toSave() {
		
		String save = "";
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				Block[] blocks = get(x, y);
				for (Block block : blocks) {
					try {
						String line = "";
						if (block.getType() != Block.DEFAULT_TYPE) {
							line += block.getId() + " " + x + " " + y ;
							if (block.getDirection() != Block.DEFAULT_DIRECTION) {
								line += " " + block.getDirection().toInt();						
							}
							line += "\n";
							save += line;
						}
	
					} catch (NotADirectionException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return save;
	}
	
	
	
	/**
	 * Vérifie si la position est dans les limites de la carte
	 */
	private boolean isInBoard(Position position) {
		if (position.getX() < 0 || position.getY() < 0 || position.getX() >= width || position.getY() >= height) {
			return false;
		}
		else {
			return true;
		}
	}

}
