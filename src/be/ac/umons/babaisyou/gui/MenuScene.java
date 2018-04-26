package be.ac.umons.babaisyou.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MenuScene génère la scène du menu principal du jeu.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class MenuScene {
	//Singleton
	private static MenuScene instance;
	Scene menuScene;
	
	private MenuScene(Stage stage) {
		
		VBox menuLayout = new VBox(20);
		menuLayout.setAlignment(Pos.CENTER);
		
		Button levelsButton = new Button("Play");
		levelsButton.getStyleClass().add("button-mainmenu");
		levelsButton.setOnAction(e -> stage.setScene(LevelPacksScene.getInstance(stage).getScene()));
		menuLayout.getChildren().add(levelsButton);
		
		Button onlineButton = new Button("Play Online");
		onlineButton.getStyleClass().add("button-mainmenu");
		onlineButton.setOnAction(e -> stage.setScene(ServerChoiceScene.getInstance(stage).getScene()));
		menuLayout.getChildren().add(onlineButton);
		
		Button controlsButton = new Button("Settings");
		controlsButton.getStyleClass().add("button-mainmenu");
		controlsButton.setOnAction(e -> stage.setScene(ControlsScene.getInstance(stage).getScene()));
		menuLayout.getChildren().add(controlsButton);
		
		
		Button quitButton = new Button("Quit");
		quitButton.getStyleClass().add("button-mainmenu");
		quitButton.setOnAction(e -> stage.close());
		menuLayout.getChildren().add(quitButton);
		
		menuScene = new Scene(menuLayout,Main.getWindowWidth(),Main.getWindowHeight());
		menuScene.getStylesheets().add(Main.THEME_PATH);
	}
	
	/**
	 * 
	 * @param stage La fentre principale
	 * @return
	 */
	public static MenuScene getInstance(Stage stage) {
		stage.setWidth(Main.getWindowWidth());
		stage.setHeight(Main.getWindowHeight());
		if (instance == null) {
			return new MenuScene(stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie la Scene du menu principal
	 * @return
	 */
	public Scene getScene() {
		LevelScene.deleteInstance();
		return menuScene;
	}
}
