package be.ac.umons.babaisyou.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * LevelErrorScene génère la scène d'erreur lors de la connection à un serveur ou
 * si le serveur n'est pas compatible avec le jeu.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ServerErrorScene {
	
	//Singleton
	private static ServerErrorScene instance;
	
	private Scene serverErrorScene;
	
	private ServerErrorScene(Stage stage) {
		VBox warning = new VBox(10);
		warning.setAlignment(Pos.CENTER);
		
		Label text = new Label("The server you tried to connect is not availible or isn't compatible with this game.\n"
				+ "Please check your connection or contact the server administrator.");
		text.setWrapText(true);
		warning.getChildren().add(text);
		
		Button backButton = new Button("Return to main titlescreen");
		backButton.setOnAction(e -> {
			stage.setScene(MenuScene.getInstance(stage).getScene());
		});
		warning.getChildren().add(backButton);
		
		serverErrorScene = new Scene(warning,640,640);
		serverErrorScene.getStylesheets().add(Main.THEME_PATH);
	}
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @param stage La fentre principale
	 * @return
	 */
	public static ServerErrorScene getInstance(Stage stage) {
		if (instance == null) {
			return new ServerErrorScene(stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie la scène qui affiche la liste de des servers.
	 * @return
	 */
	public Scene getScene() {
		return serverErrorScene;
	}

}
