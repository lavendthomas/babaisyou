package be.ac.umons.babaisyou.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ControlsScene génère  la scène qui permet de changer les contrôles.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class ControlsScene {
	//Singleton
	private static ControlsScene instance;
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	private final static String CONFIG_FILE = "config.txt";
	
	private final static String MUSIC = "music";
	
	private final static String KEY_UP = "key_up";
	private final static String KEY_DOWN = "key_down";
	private final static String KEY_LEFT = "key_left";
	private final static String KEY_RIGHT = "key_right";
	private final static String KEY_RESTART = "key_reload";
	
	public static boolean musicOn = getMusicState();
	
	public static KeyCode up = getUp();
	public static KeyCode down = getDown();
	public static KeyCode left = getLeft();
	public static KeyCode right = getRight();
	public static KeyCode restart = getRestart();
	
	
	private Scene controlsScene;
	private BorderPane contolsLayout;
	
	private ControlsScene(Stage stage) {
		
		contolsLayout = new BorderPane();
		contolsLayout.setPadding(new Insets(25, 25, 25, 25));
		
		VBox settings = new VBox();
		settings.setAlignment(Pos.CENTER);
		settings.setSpacing(20);
		contolsLayout.setCenter(settings);
		
		Label soundLabel = new Label("Sound Settings");
		settings.getChildren().add(soundLabel);
		HBox musicOnOff = new HBox();
		musicOnOff.setAlignment(Pos.CENTER);
		musicOnOff.setSpacing(20);
		musicOnOff.getChildren().add(new Label("Music"));
		Button buttonMusicOnOff = new Button(stateToName(getMusicState()));
		buttonMusicOnOff.setOnAction(event -> {
			boolean newState = !nameToState(buttonMusicOnOff.getText());
			buttonMusicOnOff.setText(stateToName(newState));
			musicOn = newState;
			SoundPlayer.getInstance().play(Sounds.BACKGOUND, true);
			saveKeyConfig();
			
		});
		musicOnOff.getChildren().add(buttonMusicOnOff);
		settings.getChildren().add(musicOnOff);
		
		
		Label keyboardLabel = new Label("Keyboard Settings");
		settings.getChildren().add(keyboardLabel);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		settings.getChildren().add(grid);
		grid.setHgap(20);
		grid.setVgap(20);
		
		//Ajout du tableau de selection des touches
		Label labelUp = new Label("UP");
		grid.add(labelUp,0,0);
		GridPane.setHalignment(labelUp, HPos.RIGHT);
		Button buttonUp = new Button(up.getName());
		buttonUp.setOnAction(event -> {
			buttonUp.setText("...");
			controlsScene.setOnKeyPressed(key-> {
				up = key.getCode();
				buttonUp.setText(up.getName());
				saveKeyConfig();
				key.consume();
			});
		});
		grid.add(buttonUp,1,0);
		
		Label labelDown = new Label("DOWN");
		grid.add(labelDown,0,1);
		GridPane.setHalignment(labelDown, HPos.RIGHT);
		Button buttonDown = new Button(down.getName());
		buttonDown.setOnAction(event -> {
			buttonDown.setText("...");
			controlsScene.setOnKeyPressed(key-> {
				down = key.getCode();
				buttonDown.setText(down.getName());
				saveKeyConfig();
				key.consume();
			});
			event.consume();
		});
		grid.add(buttonDown,1,1);
		
		Label labelRight = new Label("RIGHT");
		grid.add(labelRight,0,2);
		GridPane.setHalignment(labelRight, HPos.RIGHT);
		Button buttonRight = new Button(right.getName());
		buttonRight.setOnAction(event -> {
			buttonRight.setText("...");
			controlsScene.setOnKeyPressed(key-> {
				right = key.getCode();
				buttonRight.setText(right.getName());
				saveKeyConfig();
				key.consume();
			});
		});
		grid.add(buttonRight,1,2);
		
		Label labelLeft = new Label("LEFT");
		grid.add(labelLeft,0,3);
		GridPane.setHalignment(labelLeft, HPos.RIGHT);
		Button ButtonLeft = new Button(left.getName());
		ButtonLeft.setOnAction(event -> {
			ButtonLeft.setText("...");
			controlsScene.setOnKeyPressed(key-> {
				left = key.getCode();
				ButtonLeft.setText(left.getName());
				saveKeyConfig();
				key.consume();
			});
		});
		grid.add(ButtonLeft,1,3);
		
		Label labelRestart = new Label("RESTART");
		grid.add(labelRestart,0,4);
		GridPane.setHalignment(labelRestart, HPos.RIGHT);
		Button ButtonRestart = new Button(restart.getName());
		ButtonRestart.setOnAction(event -> {
			ButtonRestart.setText("...");
			controlsScene.setOnKeyPressed(key-> {
				restart = key.getCode();
				ButtonRestart.setText(restart.getName());
				key.consume();
				saveKeyConfig();
			});
		});
		grid.add(ButtonRestart,1,4);
		
		
		
		
		
		//Ajout du bord inférieur avec le bouton retour
		HBox menu = new HBox(10);
		menu.setPadding(new Insets(10,10,10,10));
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			stage.setScene(MenuScene.getInstance(stage).getScene());
		});
		menu.getChildren().add(backButton);
		
		contolsLayout.setBottom(menu);
		
		controlsScene = new Scene(contolsLayout,Main.getWindowWidth(),Main.getWindowHeight());
		controlsScene.getStylesheets().add(Main.THEME_PATH);

	}
	
	private static String stateToName(boolean state) {
		if (state) {
			return "ON";
		} else {
			return "OFF";
		}
	}
	
	private static boolean nameToState(String state) {
		switch (state) {
		case "ON" : 
			return true; 
		case "OFF": 
			return false; 
		default :
			LOGGER.info("Could not load data from config file.");
			return true;
		}
	}
	
	private static boolean getMusicState() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(MUSIC) && splitLine.length == 2) {
					return nameToState(splitLine[1]) ;
				}
			}
		} catch (IOException e) {
			return true;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param stage La fentre principale
	 * @return
	 */
	public static ControlsScene getInstance(Stage stage) {
		stage.setWidth(Main.getWindowWidth());
		stage.setHeight(Main.getWindowHeight());
		if (instance == null) {
			return new ControlsScene(stage);
		} else {
			return instance;
		}
	}
	
	public Scene getScene() {
		return controlsScene;
	}
	
	private static void saveKeyConfig() {
		
		try (BufferedWriter buffer = new BufferedWriter(new FileWriter(CONFIG_FILE, false))) {
			buffer.write(MUSIC + ":" + stateToName(musicOn) + "\n");
			buffer.write(KEY_UP + ":" + up.getName() + "\n");
			buffer.write(KEY_DOWN + ":" + down.getName() + "\n");
			buffer.write(KEY_LEFT + ":" + left.getName() + "\n");
			buffer.write(KEY_RIGHT + ":" + right.getName() + "\n");
			buffer.write(KEY_RESTART + ":" + restart.getName() + "\n");
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Could not save key config", e);
		}
		
		
	}
	
	/**
	 * Renvoie le code clavier associé à la chaîne de caractère assocciée
	 * @param code La chaîne de caractère du code
	 * @return Le code clavier correspondant
	 */
	private static KeyCode codeFromString(String code) {
		for (KeyCode keycode : KeyCode.values()) {
			if (keycode.getName().equals(code)) {		
				return keycode;
			}
		}
		return null;
	}
	
	private static KeyCode getUp() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(KEY_UP) && splitLine.length == 2) {
					return codeFromString(splitLine[1]);
				}
			}
		} catch (IOException e) {
			LOGGER.info("Could not load data from config : " + e.getMessage());
			return KeyCode.UP;
		}
		return KeyCode.UP;
	}
	
	private static KeyCode getDown() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(KEY_DOWN) && splitLine.length == 2) {
					return codeFromString(splitLine[1]);
				}
			}
		} catch (IOException e) {
			LOGGER.info("Could not load data from config : " + e.getMessage());
			return KeyCode.DOWN;
		}
		return KeyCode.DOWN;
	}
	private static KeyCode getLeft() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(KEY_LEFT) && splitLine.length == 2) {
					return codeFromString(splitLine[1]);
				}
			}
		} catch (IOException e) {
			LOGGER.info("Could not load data from config : " + e.getMessage());
			return KeyCode.LEFT;
		}
		return KeyCode.LEFT;
	}
	private static KeyCode getRight() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(KEY_RIGHT) && splitLine.length == 2) {
					return codeFromString(splitLine[1]);
				}
			}
		} catch (IOException e) {
			LOGGER.info("Could not load data from config : " + e.getMessage());
			return KeyCode.RIGHT;
		}
		return KeyCode.RIGHT;
	}
	private static KeyCode getRestart() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] splitLine = line.split(":");
				if (splitLine[0].equals(KEY_RESTART) && splitLine.length == 2) {
					return codeFromString(splitLine[1]);
				}
			}
		} catch (IOException e) {
			LOGGER.info("Could not load data from config : " + e.getMessage());
			return KeyCode.R;
		}
		return KeyCode.R;
	}

}
