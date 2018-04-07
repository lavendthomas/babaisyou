package be.ac.umons.babaisyou.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import be.ac.umons.babaisyou.exceptions.NotADirectionException;
import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;
import be.ac.umons.babaisyou.exceptions.WrongLevelDimensionException;

public class Level {
	
	/**
	 * Nom du niveau. (par défaut le nom du fichier)
	 */
	private String levelName;
	
	/**
	 * Nom du niveau
	 */
	private final String DEFAULT_LEVEL_NAME = "level";
	
	/**
	 * Le nombre de cases en hauteur du niveau
	 */
	private int height;
	
	/**
	 * Le nombre de cases en largeur du niveau
	 */
	private int width;

	
	/**
	 * Carte contenant tous les blocs du jeu avec lequel le joueur peux interagir.
	 */
	private Board board;
	
	/**
	 * Liste de la position de tous les joueurs.
	 */
	private HashMap<BlockType, List<Position>> playerPositions;
	
	/**
	 * Donne le type du joueur actuellement joué.
	 */
	private LinkedList<BlockType> playerTypes;
	
	/**
	 * Indique si le joueur a réussi le niveau
	 */
	private boolean hasWon;
	
	/**
	 * HashMap contenant toutes les règles en vigeur pendant une partie.
	 */
	private HashMap<BlockType, List<Action>> rules;
	
	/**
	 * Crée un niveau vide
	 * @param width la largeur du niveau
	 * @param height la hauteur du niveau
	 * @throws WrongLevelDimensionException
	 */
	public Level(int width, int height) {
		levelName = DEFAULT_LEVEL_NAME;
		
		this.height = height;
		this.width = width;
		
		board = new Board(width, height);

		playerPositions = new HashMap<BlockType, List<Position>>();
		rules = new HashMap<BlockType, List<Action>>();
		hasWon = false;
		
		//Permet	 aux règles d'avoir un lien vers le niveau
		Action.setLevel(this);
		
		updatePlayerList();
		parseRules();
	}
	
	/**
	 * Crée un niveau vide
	 * @param width la largeur du niveau
	 * @param height la hauteur du niveau
	 * @param name le nom du niveau
	 * @throws WrongLevelDimensionException
	 */
	public Level(int width, int height, String name) {
		this(width, height);
		levelName = name;
	}
	
	/**
	 * Renvoie la hauteur du niveau
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Renvoie la largeur du niveau
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Renvoie true si le joueur a gagné la partie. Sinon false
	 */
	public boolean hasWon() {
		return hasWon;
	}
	
	/**
	 * Renvoie tous les blocs à une position. (obselète)
	 * @param position
	 * @return 
	 */
	public Block[] getBlocksAt(Position position) {
		return board.get(position);
	}
	
	/**
	 * Renvoie tous les blocs à une position.
	 * @param position
	 * @return 
	 */
	public Block[] get(Position position) {
		return board.get(position);
	}
	
	/**
	 * Utilisée pour les tests unitaires
	 * @param block
	 * @param position
	 */
	void set(Block block, Position position) {
		board.set(block, position);
	}
		
	/**
	 * Supprimme la premiere occurence de block a la position mensionnée
	 * @param block Le bloc à supprimer
	 * @param position la position où on doit supprimer le bloc
	 */
	void remove(Block block, Position position) {
		board.remove(block, position);
	}
	
	/**
	 * Supprime le premier bloc à la position mensionnée
	 * @param position
	 */
	void pop(Position position) {
		board.pop(position);
	}
	
	/**
	 * Supprime le premier joueur à la position mensionnée et le renvoie.
	 * 
	 * Si aucun joueur n'est présent, null est renvoyé.
	 * @param position
	 */
	Block popPlayer(Position position) {
		for (Block block : get(position)) {
			if (playerTypes.contains(block.getType())) {
				remove(block, position);
				return block;
			}
		}
		return null;
	}
	
	/**
	 * Permet aux règles de définir si le joueur a réussi le niveau.
	 * @param hasWon
	 */
	void setWon(boolean hasWon) {
		this.hasWon = hasWon;
	}
	
	/**
	 * Renvoie tous les élements à une position sous forme de chaîne de charactères.
	 * @param position
	 * @return 
	 */
	public String[] getToId(int x,int y) {
		LinkedList<String> blocks = new LinkedList<String>();
		for (Block block : board.get(new Position(x,y))) {
			blocks.add(block.getId());
		}
		//Si la case est vide alors on ajoute le bloc par défaut (VOID)
		if (blocks.size() == 0) {
			blocks.add(Block.DEFAULT_TYPE.getId());
		}
		
		return blocks.toArray(new String[blocks.size()]);
		
	}
	
	/**
	 * Fait bouger tous les joueurs dans la direction donnée l'état du jeu le permet.
	 * Ensuite, exécute un cycle du jeu en fonction des règles.
	 * 
	 * @param direction La direction dans laquelle déplacer les joueurs
	 */
	public void move(Direction direction) {
		
		// Ne rien déplacer si il n'y a pas de joueurs
		// Ou pas de IS YOU ou aucun bloc correspodant sur la carte
		if (playerTypes.size() == 0 || playerPositions.size() == 0) {
			return;
		}
		
		boolean canmove = true;
		for (BlockType type : playerTypes) {
			for (Position pos : playerPositions.get(type)) {
				if (!canMove(pos, direction)) {
					canmove = false;
					break;
				}
			}
		}
		
		if (canmove) {
			for (BlockType type : playerTypes) {
				for (Position position : playerPositions.get(type)) {
					
					//TODO code dupplique avec moveOnePlayer
					
					if (canMove(position, direction)) {
						
						Block player = board.pop(type, position);
						Position nextPosition = position.nextPosition(direction);
						
						if (hasPushable(nextPosition)) {
							push(nextPosition, direction);
						}
						
						board.add(player, nextPosition);
						
						 // Lancer les actions pour les blocks sur lesquels les joueurs tombent
						if (player != null) {
							launchActions(nextPosition, player.getDirection());
						}
					}
				}
			}
		}

		/*
		 * Execute les regles globales
		 */
		for (BlockType type : rules.keySet()) {
			for (Action rule : rules.get(type)) {
				rule.onEachTour(type);
			}
		}
		
		updatePlayerList();
		parseRules();
		
	}
	
	/**
	 * Pousse le bloc dans une direction donnée si possible.
	 * 
	 * Si un autre bloc poussable le suis dans la direction donnée, 
	 * ce dernier sera également poussé si possible
	 * 
	 * @param current la position actuelle du joueur.
	 * @param direction la direction dans laquelle le joueur veux pousser le bloc.
	 */
	private void push(Position current, Direction direction) {
		if (canPush(current, direction)) {
			Position nextPos = current.nextPosition(direction);
			
			if (!isInLevel(nextPos) || hasBlocking(current) || hasBlocking(current)) {
				return;
			}
			
			else if (hasPushable(current)) {
				for (Block block : get(current)) {
					if (isPushable(block.getType())) {
						push(nextPos, direction);
						Block moved = board.pop(block.getType(), current);
						board.add(moved, nextPos);
						
						//TODO appeler les regles
						launchActions(nextPos, direction);
					}
				}
			}
		}
	}
	
	/**
	 * Déplace un Block à la position mensionnée dans sa direction par défaut.
	 * @param type Le type de block que l'on veux déplacer.
	 * @param position La position originale du bloc que l'on veux déplacer
	 */
	void moveOneBlock(BlockType type, Position position) {
		for (Block block : get(position)) {
			if (block.getType() == type) {
				//TODO FAIRE TOUT LE BORDEL
				Direction direction = block.getDirection();
				if (canMove(position, direction)) {
					
					Block player = board.pop(type, position);
					Position nextPosition = position.nextPosition(direction);
					
					if (hasPushable(nextPosition)) {
						push(nextPosition, direction);
					}
					
					board.add(player, nextPosition);
					
					/*
					 * Lancer les actions pour les blocks sur lesquels les joueurs tombent
					 */
					if (player != null) {
						launchActions(nextPosition, player.getDirection());
					}
				}
				
			}
		}
		
	}
	
	/**
	 * Renvoie vrai si le joueur peux se déplacer dans la direction mensionnée.
	 * @param current La position actuelle du joueur
	 * @param direction La direction dans laquelle on veux tester la posibilité de mouvement.
	 * @return true si le joueur peux se déplacer. false sinon.
	 */
	private boolean canMove(Position current, Direction direction) {
		
		Position nextPos = current.nextPosition(direction);
		
		if (!isInLevel(nextPos)) {
			return false;
		}
		
		if (hasBlocking(nextPos)) {
			return false;
		}
		
		if (hasPushable(nextPos)) {
			return canPush(nextPos, direction);
		}
		return true;
	}
	
	/**
	 * Renvoie vrai si le bloc peux être poussé dans la direction mensionnée.
	 * Si un autre bloc suit le bloc dans la direction mensionnée, vérifie
	 * également la possibilité de pousser le bloc suivant dans la même direction.
	 * 
	 * @param position La position actuelle du bloc que l'on veux pousser.
	 * @param direction La direction dans laquelle on veut pousser le bloc
	 * @return true si on peux le pousser. false sinon.
	 */
	private boolean canPush(Position position, Direction direction) {
		
		if (hasBlocking(position)) {
			return false;
		}
		else if (hasPushable(position)) {
			Position nextPos = position.nextPosition(direction);
			if (!isInLevel(nextPos)) {
				return false;
			}
			else if (hasBlocking(nextPos)) {
				return false;
			}
			else {
				return canPush(nextPos, direction);
			}
		}
		return true;
		
	}
	
	/**
	 * Lance toutes les règles définies pour tous les blocks à la position donnée.
	 * @param position La position où les règles doivent être exécutées.
	 */
	private void launchActions(Position position, Direction direction) {
		for (Block block : get(position)) {
			if (block == null || rules.get(block.getType()) == null) {
				continue;
			}
			Action[] posRules = rules.get(block.getType()).toArray(new Action[rules.get(block.getType()).size()]);
			
			for (Action rule : posRules) {
				if (rule != null) {
					rule.execute(block, position, direction);
				}
			}
		}
	}
	
	/**
	 * Lis toutes les règles du niveau et les place dans "rules".
	 */
	private void parseRules() {
		//Clear all rules 
		rules = new HashMap<BlockType, List<Action>>();
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				for (Block block : board.get(x,y)) {
					if (block == null) {
						continue;
					}
					if (block.getType() == BlockType.IS) {
						//Recher des règles autour du is
						
						// Blocs droite et gauche
						Position leftPos = new Position(x-1, y);
						Position rightPos = new Position(x+1, y);
						if (isInLevel(leftPos) && isInLevel(rightPos)) {
							for(Block left : board.get(x-1, y)) {
								for (Block right : board.get(x+1, y)) {
									addRule(left.getType(), right.getType());
								}
							}
						}
						
						
						//addRule(board.getOtherwiseDefaut(new Position(x-1, y)).getType(), board.getOtherwiseDefaut(new Position(x+1, y)).getType());
						//Blocs haut et bas
						Position upPos = new Position(x, y-1);
						Position downPos = new Position(x, y+1);
						if (isInLevel(upPos) && isInLevel(downPos)) {
							for(Block up : board.get(upPos)) {
								for (Block down : board.get(downPos)) {
									addRule(up.getType(), down.getType());
								}
							}
						}	
					}
				}
			}
		}
	}
	
	/**
	 * Ajoute une règle à rules 
	 * @param block1 Le BlockType du bloc en haut ou à gauche du is
	 * @param block2 Le BlockType du bloc en bas ou à droite du is
	 */
	private void addRule(BlockType block1, BlockType block2) {
		
		Action action;
		/*
		 * 1. Cas ou il faut transformer un block en un autre
		 */
		if (block1.isSelector() && block2.isSelector()) {
			//TODO appeller Actionchangeblock
			//Note : ne pas ajouter à la liste des regles car changement immédiat
			//TODO contxinuer à appeller après ?
			action = new ActionChangeBlocks(block1, block2);
			
			action.execute(new Block(),new Position(0,0), Direction.LEFT); //La position n'a aucune importance pour ActionChangeBlocks, ni le block en question
			//TODO EXECUxTER ICI ?
			
		}
		/*
		 * 2. Cas un l'action doit être exécutée ssi le joueur est sur la case.
		 */
		else if (block1.isSelector() && block2.isAction()) {
			action = block2.getAction();
			if (rules.get(block1) == null) {
				List<Action> newList = new ArrayList<Action>() ;
				newList.add(action);
				rules.put(block1.getSelection(), newList);
			}
			else {
				rules.get(block1.getSelection()).add(action);
			}
		}
		
	}
	
	/**
	 * Renvoie vrai si le BlockType type a la propriété "Blocking" (STOP)
	 * @param type Le BlockType à déterminer la propriété.
	 * @return vrai si le BlockType type a la propriété "Blocking". false sinon.
	 */
	private boolean isBlocking(BlockType type) {
		if (!rules.containsKey(type)) {
			return false;
		}
		for (Action action : rules.get(type)) {
			if (action.isBlocking()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie vrai si le BlockType type a la propriété "Pushable" (PUSH)
	 * @param type Le BlockType à déterminer la propriété.
	 * @return vrai si le BlockType type a la propriété "Pushable". false sinon.
	 */
	private boolean isPushable(BlockType type) {
		if (type.isPushable()) {
			return true;
		}
		if (!rules.containsKey(type)) {
			return false;
		}
		for (Action action : rules.get(type)) {
			if (action.isPushable()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie si le type de bloc est controlé par le joueur ou pas
	 * @param type le BlockType à vérifier
	 * @return Vrai si type est controlé par le joueur
	 */
	boolean isPlayer(BlockType type) {
		return playerTypes.contains(type);
	}
	
	/**
	 * Renvoie si le bloc est controlé par le joueur ou pas
	 * @param type le Bloc à vérifier
	 * @return Vrai si type est controlé par le joueur
	 */
	boolean isPlayer(Block block) {
		return isPlayer(block.getType());
	}
	
	
	/**
	 * Renvoie vrai si un des blocks dans la case a la propriété "Is (IS)"
	 * @param position La position de la case à analyser
	 * @return true si un des blocks a la propriété "Is"
	 */
	boolean hasIs(Position position) {
		for (Block block : get(position)) {
			if (block.getType() == BlockType.IS) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie vrai si un des blocks dans la case a la propriété "Pushable (PUSH)"
	 * @param position La position de la case à analyser
	 * @return true si un des blocks a la propriété "Pushable"
	 */
	boolean hasPushable(Position position) {
		for (Block block : get(position)) {
			if (isPushable(block.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie vrai si un des blocks dans la case a la propriété "Blocking (STOP)"
	 * @param position La position de la case à analyser
	 * @return true si un des blocks a la propriété "Blocking"
	 */
	boolean hasBlocking(Position position) {
		for (Block block : get(position)) {
			if (isBlocking(block.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie vrai si un des blocs à la position mensionnée a un bloc contrôlable par le joueur
	 * @param position La position à vérifier
	 * @return Renvoie vrai si un des blocs à la position mensionnée a un bloc contrôlable par le joueur
	 */
	boolean hasPlayer(Position position) {
		for (Block block : get(position)) {
			if (isPlayer(block)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie true si il existe au moins un bloc du type mensionné à la position memsionnée
	 * @param type Le type de bloc à rechercher
	 * @param position La position où vérifier
	 * @return true si il existe au moins un bloc du type mensionné à la position memsionnée
	 */
	boolean hasBlockType(BlockType type, Position position) {
		for (Block block : get(position)) {
			if (block.isSameType(type)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Rafraîchit le stockage les joueurs (blocs se déplacent avec les flèches directionnelles).
	 * Tous les BlockType qui sont des joueurs sont être placés dans "playerTypes"
	 * 
	 * Et pour chaque BlockType, une liste de la position de tous les joueurs de ce BlockType sera
	 * ajoutée dans la HashMap "playerPositions"
	 * 
	 */
	private void updatePlayerList() {
		
		playerPositions = new HashMap<>();
		playerTypes = new LinkedList<BlockType>();
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				for (Block block : board.get(x, y)) {
					if (block == null) {
						continue;
					}
					if (block.getType() == BlockType.IS) {
						//Chercher si TEXT_YOU est autour du is.
						
						//Si IS en bas ou à droite : arrêter de charcher
						if ( !isInLevel(new Position(x+1, y)) || !isInLevel(new Position(x, y+1)) ) {
							continue;
						}
						
						
						//Gauche et droite
						for (Block right : board.get(x+1, y)) {
							if (right.getType() == BlockType.YOU) {
								//Supprimer du board et ajouter au joueur
								BlockType newPlayerType;
								
								for(Block left : board.get(x-1, y)) {
									if (left.isSelector()) {
										newPlayerType = left.getType().getSelection();
										addPlayerPostionFomType(newPlayerType);
										playerTypes.add(newPlayerType);
									}
								}
							}
						}

						//Haut et bas
						for (Block down : board.get(x, y+1)) {
							if (down.getType() == BlockType.YOU) {
								//Supprimer du board et ajouter au joueur
								BlockType newPlayerType;
								
								for(Block left : board.get(x, y-1)) {
									if (left.isSelector()) {
										newPlayerType = left.getType().getSelection();
										addPlayerPostionFomType(newPlayerType);
										playerTypes.add(newPlayerType);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Ajoute toutes les positions des blocs d'un type donnée vers "playerPositions"
	 * @param type le type du joueur à ajouter dans "playerPositions"
	 */
	private void addPlayerPostionFomType(BlockType type) {
		for(int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				for (Block block : board.get(x,y)) {
					if (block == null) {
						continue;
					}
					if (block.getType() == type) {
						List<Position> positions = playerPositions.get(type);
						if (positions == null) {
							List<Position> newList = new ArrayList<Position>();
							newList.add(new Position(x,y));
							playerPositions.put(type, newList);
						}
						else {
							positions.add(new Position(x,y));
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Charge un niveau depuis un fichier. 
	 * 
	 * Le fichier doit avoir les charactéristiques suivantes sous peine de lancer WrongFileFormatException.
	 * 
	 * Nom du fichier : Nom du niveau
	 * 1ere ligne : Dimension (largeur hauteur) du niveau (Entier)
	 * 
	 * Chaque ligne qui contient un bloc doit être de la forme suivante : 
	 * 
	 * id x y [dir]
	 * 
	 * où :
	 * 		id (String) : identifiant textuel du block à placer
	 * 		x (int) : Décalage à droite par rapport au point supérieur gauche
	 * 		y (int) : Décalage en bas par rapport au coint supérieur gauche
	 * 		dir (int) : direction du bloc par défaut (vaut 0 si omis)
	 * 
	 * Convension sur les directions : 
	 * 
	 * 0: Droite
	 * 1: Haut
	 * 2: Gauche
	 * 3: Bas
	 * 
	 * 
	 * @param file le fichier à charger
	 * @return Un niveau d'après le fichier
	 * @throws WrongLevelDimensionException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws WrongFileFormatException si le fichier ne correpond pas à la norme çi-dessus.
	 */
	public static Level load(File file) throws WrongFileFormatException, WrongLevelDimensionException, FileNotFoundException, IOException {
		
		try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
			
			/*
			 * Lecture de la dimension
			 */
			String dimLine = buffer.readLine();
			
			String[] dims = dimLine.split(" ");
			if (dims.length != 2) {
				throw new WrongFileFormatException();
			}
			
			int width = Integer.parseInt(dims[0]);
			int height = Integer.parseInt(dims[1]);
			
			Level level = new Level(width, height, file.getName());
			
			/*
			 * Ajout des blocs un a un.
			 */
			String line;
			String id;
			int x;
			int y;
			int dir;
			
			while ((line = buffer.readLine()) != null) { //null si le fichier est vide.
				
				String[] splitLine = line.split(" ");
				
				/*
				 * Ajout des blocs
				 */
				if (splitLine.length < 3 || splitLine.length > 4) {
					//incompatible
					throw new WrongFileFormatException();
				}
				
				id = splitLine[0];
				try {
					x = Integer.parseInt(splitLine[1]);
					y = Integer.parseInt(splitLine[2]);
					if (splitLine.length == 3) {
						//directin par défaut
						dir = 0;
					}
					else { // splitLine.length == 4.
						dir = Integer.parseInt(splitLine[3]);
					}
				} catch (NumberFormatException e) {
					throw new WrongFileFormatException();
				}
				
				/*
				 * Cas où on doit ajouter les blocks au background.
				 */
				try {
					level.board.add(new Block(BlockType.fromId(id), Direction.fromInt(dir)), new Position(x,y));
				} catch (NotADirectionException e) {
					// TODO Auto-generated catch block
					throw new WrongFileFormatException();
				}
			}
			level.updatePlayerList();
			level.parseRules(); 	//ajout des règles
			return level;
		} 
		//throw new WrongFileFormatException();
	}
	
	/**
	 * Voir la documentation de load(File file)
	 * @param
	 */
	public static Level load(String filename) throws WrongFileFormatException, WrongLevelDimensionException, FileNotFoundException, IOException {
		return load(new File(filename));
	}
	
	/**
	 * Sauvegarde dans un fichier texte comme précisé dans la méthode Level.load() .
	 * 
	 * Utilie le nom du niveau comme nom de fichier.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		save(levelName);
	}
	
	/**
	 * Sauvegarde dans un fichier texte comme précisé dans la méthode Level.load() .
	 * @param filename le chemin d'accès du fichier à charger
	 * @return Un niveau d'après le fichier
	 * @throws WrongLevelDimensionException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws WrongFileFormatException si le fichier ne correpond pas à la norme çi-dessus.
	 */
	public void save(String filename) throws IOException {
		
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(filename))) {
			//Écris la dimension
			buffer.write(width + " " + height + "\n");
			
			//Écris les blocks ligne par ligne
			
			buffer.write(board.toSave());
			
		}
	}
	
	/**
	 * Renvoie si la position est bien dans les limites du jeu.
	 * @param position
	 * @return
	 */
	private boolean isInLevel(Position position) {
		if (position.getX() < 0 || position.getY() < 0 || position.getX() >= width || position.getY() >= height) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Affiche une représentation des règles sous forme de chaîne de charactère.
	 * Utile pour le débogage.
	 * 
	 * @return une chaîne de charactère.
	 */
	private String printRules() {
		String res = "";
		for (BlockType key : rules.keySet()) {
			res += key.toString() + " : ";
			for (Action action : rules.get(key)) {
				res += action.getClass().toString();
			}
			res += "\n";
		}
		return res;
	}
	
	/**
	 * Renvoie une représentation du niveau sous forme de chaîne de charactère.
	 */
	public String toString() {
		String res = "";
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				Position pos = new Position(j,i);
				Block[] blocks = get(pos);
				
				for (Block block : blocks) {
					res += block.toString();
				}
				res += "|";
				//res += this.board.get(pos).toString() + ",";
			}
			res += "\n";
		}
		return res;
	}
		
	
	
}