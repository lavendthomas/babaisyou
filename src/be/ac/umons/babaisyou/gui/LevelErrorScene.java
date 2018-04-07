package be.ac.umons.babaisyou.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelErrorScene {
	
	//Singleton
		private static LevelErrorScene instance;
		
		private Scene levelErrorScene;
		
		private LevelErrorScene(Stage stage) {
			VBox warning = new VBox(10);
			warning.setAlignment(Pos.CENTER);
			
			Label text = new Label("The level you tried to play is either corrupted or doesn't respect"
					+ "or is not a level file");
			warning.getChildren().add(text);
			
			Button backButton = new Button("Return to main titlescreen");
			backButton.setOnAction(e -> {
				stage.setScene(MenuScene.getInstance(stage).getScene());
			});
			warning.getChildren().add(backButton);
			
			levelErrorScene = new Scene(warning,640,640);
			levelErrorScene.getStylesheets().add(Main.THEME_PATH);
		}
		
		/**
		 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
		 * @param stage La fentre principale
		 * @return
		 */
		public static LevelErrorScene getInstance(Stage stage) {
			if (instance == null) {
				return new LevelErrorScene(stage);
			} else {
				return instance;
			}
		}
		
		/**
		 * Renvoie la scène qui affiche la liste de des servers.
		 * @return
		 */
		public Scene getScene() {
			return levelErrorScene;
		}

}
