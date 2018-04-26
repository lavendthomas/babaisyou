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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	Stage window; // Fenêtre principale
	Scene menuScene, levelScene;
	private static final Logger LOGGER =  Logger.getGlobal();
	
	private static int WINDOW_HEIGHT = 940;
	
	private static int WINDOW_WIDTH = 940;
	
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
			window.setMinWidth(480);
			window.setMinHeight(480);
			window.show();
			
			window.widthProperty().addListener(new ChangeListener<Number>() {
				 @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				        WINDOW_WIDTH = newSceneWidth.intValue();
				    }
			});
			
			window.heightProperty().addListener(new ChangeListener<Number>() {
				 @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				        WINDOW_HEIGHT = newSceneHeight.intValue();
				    }
			});
			
			window.maximizedProperty().addListener((observableValue, oldVal, newVal) -> {
				if (newVal) { //Si la fenêtre viens d'être maximisée
					Screen screen = Screen.getPrimary();
					Rectangle2D bounds = screen.getVisualBounds();
					WINDOW_WIDTH = ((Number) bounds.getWidth()).intValue();
					WINDOW_HEIGHT = ((Number) bounds.getHeight()).intValue();
				}
				else {
					WINDOW_WIDTH = window.heightProperty().getValue().intValue();
					WINDOW_HEIGHT = window.widthProperty().getValue().intValue();
				}
				
			});
			
			
			
			SoundPlayer soundplayer = SoundPlayer.getInstance();
			soundplayer.play(Sounds.BACKGOUND, true);

		} catch(Exception e) {
			LOGGER.log(java.util.logging.Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Renvoie la largeur de la fenêtre
	 * @return la largeur de la fenêtre en pixels
	 */
	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	
	/**
	 * Renvoie la hauteur de la fenêtre
	 * @return la hauteur de la fenêtre en pixels
	 */
	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void closeProgram() {
		//Save levels
		window.close();
	}	
}
