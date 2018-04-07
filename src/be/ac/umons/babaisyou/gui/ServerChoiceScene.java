package be.ac.umons.babaisyou.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerChoiceScene {
	//Singleton
	private static ServerChoiceScene instance;
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	/**
	 * Emplacement du ficher qui contient l'historique de 
	 */
	public final static String SERVER_HISTORY_LOCATION = "servers"+File.separator+"history";
	
	private BorderPane serverChoiceLayout;
	private Scene serverChoiceScene;
	/**
	 * Affiche la liste des servers à l'utilisateur
	 */
	private ComboBox<String> serverBox;
	private Stage mainWindow;
	
	
	private ServerChoiceScene(Stage stage) {
		mainWindow = stage;
		serverChoiceLayout = new BorderPane();
		serverChoiceLayout.setPadding(new Insets(25, 25, 25, 25));
		
		//Crée la combobox qui contient les servers.
		VBox askIp = new VBox(20);
		askIp.setAlignment(Pos.CENTER);
		
		Label title = new Label("Enter the server address");
		askIp.getChildren().add(title);
		
		serverBox = new ComboBox<>();
		serverBox.setEditable(true); // permets d'ajouter de nouveaux servers
		updateServers();

		askIp.getChildren().add(serverBox);
		serverChoiceLayout.setCenter(askIp);
		
		//Ajout du bord inférieur avec le bouton quitter, supprimer l'historique et jouer
		
		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10,10,10,10));
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			stage.setScene(MenuScene.getInstance(stage).getScene());
		});
		menu.getChildren().add(backButton);
		
		Button deleteButton = new Button("Delete history");
		deleteButton.setOnAction(e -> {
			purgeHistory();
			updateServers();
		});
		menu.getChildren().add(deleteButton);
		
		Button playButton = new Button("Connect");
		playButton.setOnAction(e -> {
			String serverIp = serverBox.getSelectionModel().getSelectedItem();
			play(serverIp);
			
		});
		menu.getChildren().add(playButton);
		
		serverChoiceLayout.setBottom(menu);
		
		
		
		//Crée la scène
		serverChoiceScene = new Scene(serverChoiceLayout,640,640);
		serverChoiceScene.getStylesheets().add(Main.THEME_PATH);
		
	}
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @param stage La fentre principale
	 * @return
	 */
	public static ServerChoiceScene getInstance(Stage stage) {
		if (instance == null) {
			return new ServerChoiceScene(stage);
		} else {
			return instance;
		}
	}
	
	/**
	 * Renvoie la scène qui affiche la liste de des servers.
	 * @return
	 */
	public Scene getScene() {
		return serverChoiceScene;
	}
	
	/**
	 * Vérifie si le server est de la bonne stucture et lance le jeu si tout est ok.
	 * @param ip l'ip du server choisi
	 */
	private void play(String ip) {
		LOGGER.info("Connecting to server : " + ip);
		
		try {
			LevelPackOnline pack = new LevelPackOnline(ip);
			LevelScene.getInstance(pack, mainWindow).setLevelPack(pack);
			mainWindow.setScene(LevelListScene.getInstance(pack, getScene(), mainWindow).getScene());
			
		} catch (IOException e) {
			//Affiche un message d'erreur si pas possible de se connecter
			LOGGER.warning("Incomaptible server at " + ip);
			mainWindow.setScene(ServerErrorScene.getInstance(mainWindow).getScene());
		}
	}
	
	/**
	 * Rachaîchit la liste des servers dans la ComboBox
	 */
	private void updateServers() {
		File history = new File(SERVER_HISTORY_LOCATION);
		LinkedList<String> servers = new LinkedList<>();
		if (history.exists()) {
			try (BufferedReader buffer = new BufferedReader(new FileReader(history))) {
				String line;
				while ((line = buffer.readLine()) != null) {
					servers.add(line);
				}
				
			} catch (FileNotFoundException e) {
				LOGGER.log(Level.WARNING, "Could not load history file : " + e.getMessage());
			} catch (IOException e) {
				//TODO gérer GUI
				LOGGER.log(Level.WARNING, "Could not load history file : " + e.getMessage());
			}
		}
		
		serverBox.getItems().clear();
		for (String server : servers) {
			serverBox.getItems().add(server);
		}
		serverBox.getItems();
	}
	
	/**
	 * Supprime l'historique des servers en supprimant le fichier
	 */
	private void purgeHistory() {
		LOGGER.fine("Purging server history");
		File history = new File(SERVER_HISTORY_LOCATION);
		if (history.exists()) {
			history.delete();
		}
	}

}
