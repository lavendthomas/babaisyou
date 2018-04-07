package be.ac.umons.babaisyou.gui;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class LevelPacksScene {
	//Singleton
	private static LevelPacksScene instance;
	
	public static String LEVELPACKS_LOCATION = "levels";
	
	private BorderPane levelPacksLayout;
	private Scene levelsPackScene;
	
	private LevelPacksScene(Stage stage) {
		levelPacksLayout = new BorderPane();
		levelPacksLayout.setPadding(new Insets(25, 25, 25, 25));

		/*
		 * Charge tous les dossiers dans save
		 */
		ListView<String> levelPacksView = new ListView<>();
		levelPacksView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Ne permet pas de jouer a plusieurs niveaux à la fois.
		
		File levelPacksFolder = new File(LEVELPACKS_LOCATION);
		if (levelPacksFolder.exists() && levelPacksFolder.isDirectory()) {
			for (File pack : levelPacksFolder.listFiles()) {
				if (pack.isDirectory()) {
					levelPacksView.getItems().add(pack.getName());
				}
			}
		}
		levelPacksLayout.setCenter(levelPacksView);
		
		//Ajout du bord inférieur avec le bouton quitter et jouer
		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10,10,10,10));
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			stage.setScene(MenuScene.getInstance(stage).getScene());
		});
		menu.getChildren().add(backButton);
		
		Button playButton = new Button("Next");
		playButton.setOnAction(e -> {
			String levelPackPath = levelPacksFolder + File.separator +levelPacksView.getSelectionModel().getSelectedItem();
			LevelPack pack = new LevelPack(levelPackPath);
			LevelScene.getInstance(pack, stage).setLevelPack(pack);
			stage.setScene(LevelListScene.getInstance(pack, getScene() ,stage).getScene());
			
		});
		menu.getChildren().add(playButton);
		
		levelPacksLayout.setBottom(menu);
		
		
		levelsPackScene = new Scene(levelPacksLayout,640,640);
		levelsPackScene.getStylesheets().add(Main.THEME_PATH);
		
	}
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @param stage La fentre principale
	 * @return
	 */
	public static LevelPacksScene getInstance(Stage stage) {
		if (instance == null) {
			return new LevelPacksScene(stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie la scène qui affiche la liste de tous les packs de niveaux.
	 * @return
	 */
	public Scene getScene() {
		return levelsPackScene;
	}
	
}
