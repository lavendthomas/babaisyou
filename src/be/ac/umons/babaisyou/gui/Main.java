package be.ac.umons.babaisyou.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import be.ac.umons.babaisyou.game.Level;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	Stage window; // Fenêtre principale
	Scene menuScene, levelScene;
	private static final Logger LOGGER =  Logger.getGlobal();
	
	/**
	 * Emplactement du thème css qui s'applique
	 */
	public static final String THEME_PATH = File.separator +
			"be" + File.separator +"ac"+File.separator+"umons"+File.separator +
			"babaisyou" + File.separator + "gui"+ File.separator + "dark.css";
	
	Level currentLevel;
	
	public final static String LEVELPACK = "." + File.separator + "src" + File.separator + 
			"be" + File.separator +"ac"+File.separator+"umons"+File.separator +
			"babaisyou" + File.separator + "ressources"+ File.separator + "levels"
			+ File.separator;
	
	
	@Override
	public void start(Stage primaryStage) {
		//Logs

		try {
			LogManager.getLogManager().reset();
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(java.util.logging.Level.SEVERE);
			LOGGER.addHandler(ch);
			FileHandler fh = new FileHandler("log.log");
			fh.setFormatter(new SimpleFormatter());
			fh.setLevel(java.util.logging.Level.INFO);
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e1) {
			LOGGER.info("Could not initialise Logger");
		}

		try {
			window = primaryStage;
			
			menuScene = MenuScene.getInstance(primaryStage).getScene();
			
			window.setScene(menuScene);
			window.setTitle("Baba is you");
			window.setOnCloseRequest(e -> closeProgram());
			window.show();
			
			SoundPlayer soundplayer = SoundPlayer.getInstance();
			soundplayer.play(Sounds.BACKGOUND, true);

			
			 
		} catch(Exception e) {
			//TODO Eviter
			LOGGER.log(java.util.logging.Level.SEVERE, null, e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void closeProgram() {
		//Save levels
		window.close();
	}
	
	//Redéfinir init et stop si besoin
	
}
