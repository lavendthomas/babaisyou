package be.ac.umons.babaisyou.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * LevelListScene génère la scène qui liste les niveaux dans un pack de niveau et permet
 * de jouer à ceux-ci.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class LevelListScene {
	//Singleton
	static LevelListScene instance;
	
	static final String DEFAULT_NONPLAYABLE_NAME = "...";
	
	BorderPane levelListLayout;
	Scene levelsListScene;
	
	
	private LevelListScene(ILevelPack levels, Scene previousScene, Stage stage) {
		levelListLayout = new BorderPane();
		levelListLayout.setPadding(new Insets(25, 25, 25, 25));
		
		ListView<String> levelListView = new ListView<>();
		levelListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Ne permet pas de jouer a plusieurs niveaux à la fois.
		
		for (String levelName : levels.getPlayableLevels()) {
			if (levelName == null) {
				//Le niveau n'est pas jouable dont on affiche la valeur par defaut.
				levelListView.getItems().add(DEFAULT_NONPLAYABLE_NAME);
			}
			else {
				//Le niveau est jouable donc afficher son nom
				levelListView.getItems().add(levelName);
			}
		}
		
		levelListLayout.setCenter(levelListView);
		
		
		//Ajout du bord inférieur avec le bouton quitter et jouer
		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10,10,10,10));
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			stage.setScene(previousScene);
		});
		menu.getChildren().add(backButton);
		
		Button playButton = new Button("Play");
		playButton.setOnAction(e -> {
			String levelSelected = levelListView.getSelectionModel().getSelectedItem();
			//Permettre de jouer au niveau uniquememt si c'est un niveau jouable
			if (levelSelected != null && !DEFAULT_NONPLAYABLE_NAME.equals(levelSelected)) {
				levels.setFirstLevel(levelSelected);
				LevelScene.getInstance(levels, stage).setLevelPack(levels);
				stage.setScene(LevelScene.getInstance(levels, stage).getScene());
			}

			
		});
		menu.getChildren().add(playButton);
		
		levelListLayout.setBottom(menu);
		
		levelsListScene = new Scene(levelListLayout,640,640);
		levelsListScene.getStylesheets().add(Main.THEME_PATH);
	}
	
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @param levels le pack ne niveaux dont on peux afficher la liste de niveaux
	 * @param previousScene la scene de l'appellant
	 * @param stage La fentre principale
	 * @return
	 */
	public static LevelListScene getInstance(ILevelPack levels, Scene previousScene, Stage stage) {
		if (instance == null) {
			return new LevelListScene(levels, previousScene, stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie la scène qui affiche la liste de tous les niveaux dans le pack mensionnée par le constructeur de l'objet.
	 * @return
	 */
	public Scene getScene() {
		return levelsListScene;
	}

}
