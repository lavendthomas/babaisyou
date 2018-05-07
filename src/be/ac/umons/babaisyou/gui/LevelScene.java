package be.ac.umons.babaisyou.gui;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import be.ac.umons.babaisyou.game.Level;
import be.ac.umons.babaisyou.exceptions.GamedCompletedException;
import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;
import be.ac.umons.babaisyou.game.BlockType;
import be.ac.umons.babaisyou.game.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * LevelScene génère la scène qui affiche un niveau du jeu.
 * 
 * Le niveau à afficher est donné en paramètre lors de l'instanciation
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class LevelScene {
	
	/**
	 * Temps pour que le message d'achievement disparaisse en millisecondes.
	 */
	public final static int TIME_TO_FADE = 3000;
	
	private static LevelScene instance;
	
	private static final Logger LOGGER = Logger.getGlobal();
	
	private static final String BORDER_NAME = "border";
	
	/**
	 * Layout qui est à afficher dans 
	 */
	private GridPane levellayout;
	private Scene levelScene;
	private Level level;
	
	private Label achievementLabel;
	/**
	 * Utilisé pour réinitialisé le niveau
	 */
	ILevelPack levels;
	
	/**
	 * Fenêtre principale où est attachée le scene
	 */
	private Stage stage;
	
	private HashMap<String, Image> images; //Pour n'avoir qu'une instance d'image par type de bloc.
	
	public static int BLOCK_SIZE;
	
	public static final String DELFAUT_IMAGE_LOCATION =  File.separator +
			"be" + File.separator +"ac"+File.separator+"umons"+File.separator +
			"babaisyou" + File.separator + "ressources"+ File.separator + "images"
			+ File.separator;
	
	public static final String DELFAUT_IMAGE_EXTENSION = ".png";
	public static final String ALTERNATE_IMAGE_EXTENSION = ".gif";
	
	
	private LevelScene(ILevelPack levels, String firstLevel, Stage stage) {
		
		this.stage = stage;
		
		//Charger toutes les images.
		images = new HashMap<String, Image>();
		for(BlockType type : BlockType.values()) {
			Image image;
			try {
				File img = new File(DELFAUT_IMAGE_LOCATION + type.getId() + DELFAUT_IMAGE_EXTENSION);
				img.exists();
				image = new Image(DELFAUT_IMAGE_LOCATION + type.getId() + DELFAUT_IMAGE_EXTENSION, BLOCK_SIZE, BLOCK_SIZE, false, false);
			} catch (IllegalArgumentException e) {
				image = new Image(DELFAUT_IMAGE_LOCATION + type.getId() + ALTERNATE_IMAGE_EXTENSION, BLOCK_SIZE, BLOCK_SIZE, false, false);
			}
			
			images.put(type.getId(), image);
		}
		this.levels = levels;
		
		if (firstLevel != null ) {
			levels.setFirstLevel(firstLevel);
		}
		try {
			level = levels.getCurrentLevel();
		} catch (WrongFileFormatException e3) {
			// Si le niveau est corrompu, afficher scene d'erreur
			LOGGER.log(java.util.logging.Level.WARNING, "Could not load Level", e3);
			level=null;
			return;
		}
		
		StackPane gridPlusAchievement = new StackPane();
		
		levellayout = new GridPane();
		levellayout.getStyleClass().add("level-background");
		levellayout.setPadding(new Insets(10,10,10,10));
		levellayout.setVgap(0); //espace entre les cellules
		levellayout.setHgap(0);
		levellayout.setAlignment(Pos.CENTER);
		
		int width = level.getWidth();
		int height = level.getHeight();
		
		BLOCK_SIZE = Math.min(Main.getWindowWidth(), Main.getWindowHeight()) / Math.max(width+2, height+2); //+2 pour la bordure
		
		
		update(level); //Affiche les blocs une première fois
		
		gridPlusAchievement.getChildren().add(levellayout);
		
		//Ajout du label par dessus pour les achievement
		achievementLabel = new Label();
		achievementLabel.setWrapText(true);
		achievementLabel.getStyleClass().add("label-achievement");
		gridPlusAchievement.getChildren().add(achievementLabel);
		
		//Céation de la scène
		levelScene = new Scene(gridPlusAchievement,Main.getWindowWidth(),Main.getWindowHeight());
		levelScene.getStylesheets().add(Main.THEME_PATH);
		
		//Permet de redimensionner la fenêtre
		//Source :https://blog.idrsolutions.com/2012/11/adding-a-window-resize-listener-to-javafx-scene/
		levelScene.widthProperty().addListener(new ChangeListener<Number>() {
			 @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			        BLOCK_SIZE = Math.min(newSceneWidth.intValue(), Main.getWindowWidth()) / Math.max(width+2, height+2);
			        update(level);
			    }
		});
		
		levelScene.heightProperty().addListener(new ChangeListener<Number>() {
			 @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
			        BLOCK_SIZE = Math.min(Main.getWindowHeight(), newSceneHeight.intValue()) / Math.max(width+2, height+2);
			        update(level);
			    }
		});
		
		stage.maximizedProperty().addListener((observableValue, oldVal, newVal) -> {
			if (newVal) {
				Screen screen = Screen.getPrimary();
				Rectangle2D bounds = screen.getVisualBounds();
				BLOCK_SIZE = (int) (Math.min(bounds.getWidth(), bounds.getHeight()) / Math.max(width+2, height+2));
			}
			else {
				BLOCK_SIZE = Math.min(Main.getWindowHeight(), Main.getWindowWidth()) / Math.max(width+2, height+2);
			}
			update(level);
			
		});
		
		levelScene.setOnKeyPressed(e -> {
			LOGGER.finer("Key "+ e.getCode() + "pressed");
			if (e.getCode() == ControlsScene.up) {
				level.move(Direction.UP);
			} else if (e.getCode() == ControlsScene.down) {
				level.move(Direction.DOWN);
			} else if (e.getCode() == ControlsScene.left) {
				level.move(Direction.LEFT);
			} else if (e.getCode() == ControlsScene.right) {
				level.move(Direction.RIGHT);
			} else if (e.getCode() == ControlsScene.restart) {
				try {
					level.getAchievement().onReload();
					level = levels.getCurrentLevel();
				} catch (WrongFileFormatException e2) {
					// Si le niveau est corrompu, afficher scene d'erreur
					LOGGER.log(java.util.logging.Level.WARNING, "Could not load Level", e2);
					level = null;
				}
			} else if (e.getCode() == KeyCode.ESCAPE) {
				level.flush();
				stage.setScene(MenuScene.getInstance(stage).getScene());
			}

			if (level.hasWon()) {
				SoundPlayer.getInstance().play(Sounds.WIN);
				level.flush();
				try {
					levels.nextLevel();
				} catch (GamedCompletedException e1) {
					//Le joueur a fini la partie, donc revenir au menu principal.
					LOGGER.fine("Player completed the game pack");
					stage.setScene(MenuScene.getInstance(stage).getScene());
				}
				try {
					level = levels.getCurrentLevel();
				} catch (WrongFileFormatException e1) {
					// Si le niveau est corrompu, afficher scene d'erreur
					LOGGER.log(java.util.logging.Level.WARNING, "Could not load Level", e1);
					level = null;
				}
				
			}
			update(level);
			
		});
		
		instance = this;
	}
	
	/**
	 * Renvoie d'instance déjà présente de LevelScene ou en crée une noyvelle selon le LevelPack mensionné
	 * @param levels
	 * @return
	 */
	public static LevelScene getInstance(ILevelPack levels, String firstLevel ,Stage stage) {
		if (instance == null) {
			return new LevelScene(levels, firstLevel,  stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie d'instance déjà présente de LevelScene ou en crée une nouvelle selon le LevelPack mensionné
	 * @param levels
	 * @return
	 */
	public static LevelScene getInstance(ILevelPack levels ,Stage stage) {
		stage.setWidth(Main.getWindowWidth());
		stage.setHeight(Main.getWindowHeight());
		if (instance == null) {
			return new LevelScene(levels, null,  stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie d'instance déjà présente de LevelScene ou null si aucune n'existe
	 */
	public static LevelScene getInstance() {
		return instance;
	}
	
	/**
	 * Affiche le message sur l'écram
	 * @param message
	 * @see https://stackoverflow.com/questions/16764549/timers-and-javafx
	 */
	public void showAchievementText(String message) {
		achievementLabel.setText(message);
		new Timeline(new KeyFrame(
		        Duration.millis(TIME_TO_FADE),
		        ae -> achievementLabel.setText("")))
		    .play();
	}
	
	/**
	 * Permet de supprimer l'instance pour libérer de la mémoire.
	 */
	public static void deleteInstance() {
		instance = null;
	}
	
	/**
	 * Permet de changer les niveaux associés à la Scene
	 * @param pack Le nouveau pack de niveau
	 */
	public void setLevelPack(ILevelPack pack) {
		instance = new LevelScene(pack, null, stage);
		
	}
	
	/**
	 * Renvoie la scène générée
	 */
	public Scene getScene() {
		if (level == null) {
			//Renvoyer message d'erreur si niveau corrompu ou invalide
			return LevelErrorScene.getInstance(stage).getScene();
		}
		return levelScene;
	}
	
	/**
	 * Rafraîchit le niveau à l'écran depuis les données dans level
	 * @param level le niveau à afficher
	 */
	private void update(Level level) {
		levellayout.getChildren().clear();
		
		int width = level.getWidth();
		int height = level.getHeight();
		
		for (int i=-1; i<=height; i++) {
			for (int j=-1; j<=width; j++) {
				StackPane imageStack = new StackPane(); //ImageStack contient la pile d'images
				//Si on est hors carte
				if (i == -1 || i == height || j == -1 || j == width) {
					ImageView imageview = new ImageView(images.get(BORDER_NAME));
					imageview.setFitWidth(BLOCK_SIZE); //Taille des images
					imageview.setFitHeight(BLOCK_SIZE);
					imageStack.getChildren().add(imageview);
				}
				else {
					//Si on est dans la carte
					String[] blocks = level.getToId(j,i);
					for (String block : blocks) {
						LOGGER.finest("Refreshing block at x:"+j+"y:"+i+":"+block);
						ImageView imageview = new ImageView(images.get(block));
						imageview.setFitWidth(BLOCK_SIZE); //Taille des images
						imageview.setFitHeight(BLOCK_SIZE);
						imageStack.getChildren().add(imageview);
					}
				}
				
				//On ajoute la pile d'images à la gille principale
				GridPane.setConstraints(imageStack, j+1, i+1);
				levellayout.getChildren().add(imageStack);
			}
		}
	}
}
