package be.ac.umons.babaisyou.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

/**
 * ServerChoiceScene génère la scène qui permet de choisir sur quel serveur se connecter.
 * Permet aussi de supprimer toutes les données du dossier servers.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ServerChoiceScene {
	//Singleton
	private static ServerChoiceScene instance;
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	public static final String SERVER_FOLDER_LOCATION = "servers";
	
	/**
	 * Emplacement du ficher qui contient l'historique de 
	 */
	public final static String SERVER_HISTORY_LOCATION = SERVER_FOLDER_LOCATION+File.separator+"history";
	
	private BorderPane serverChoiceLayout;
	private Scene serverChoiceScene;
	/**
	 * Affiche la liste des servers à l'utilisateur
	 */
	private ComboBox<String> serverBox;
	private LinkedList<String> servers;
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
			if (serverIp != null) {
				play(serverIp);
			}
			
			
		});
		menu.getChildren().add(playButton);
		
		serverChoiceLayout.setBottom(menu);
		
		//Crée la scène
		serverChoiceScene = new Scene(serverChoiceLayout,Main.getWindowWidth(),Main.getWindowHeight());
		serverChoiceScene.getStylesheets().add(Main.THEME_PATH);
		
	}
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @param stage La fentre principale
	 * @return
	 */
	public static ServerChoiceScene getInstance(Stage stage) {
		stage.setWidth(Main.getWindowWidth());
		stage.setHeight(Main.getWindowHeight());
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
			// Ajout à l'historique des servers
			if (!servers.contains(ip)) {
				try (BufferedWriter buffer = new BufferedWriter(new FileWriter(new File(SERVER_HISTORY_LOCATION), true))) {
					buffer.append(ip + "\n");
					updateServers();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Could not write to history file : " + e.getMessage());
				}
			}
			
			//Switch la scene
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
		servers = new LinkedList<>();
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
		//Remove folder data
		File serverFolder = new File(SERVER_FOLDER_LOCATION);
		for (File folder : serverFolder.listFiles()) {
			if (folder.isDirectory()) {
				delete(folder);
			}
		}
		
	}
	
	/**
	 * Supprime un dossier non vide récursivement
	 * @param f le fichier à supprimer
	 * @throws IOException
	 * @author erickson
	 * @see https://stackoverflow.com/questions/779519/delete-directories-recursively-in-java
	 */
	private void delete(File f) {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    LOGGER.warning("Failed to delete file: " + f);
		}

}
