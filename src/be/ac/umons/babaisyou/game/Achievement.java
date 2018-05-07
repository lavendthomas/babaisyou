package be.ac.umons.babaisyou.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import be.ac.umons.babaisyou.gui.LevelScene;
import be.ac.umons.babaisyou.gui.SoundPlayer;
import be.ac.umons.babaisyou.gui.Sounds;

/**
 * Repésente les succès du jeu.
 * 
 * @author Thomas Lavend'Homme
 *
 */
public class Achievement {
	//Singleton car unique pour une installation.
	private static Achievement instance;
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	/**
	 * Chamin d'accès du fichier où sont stockés les achievements
	 */
	private final static String ACHIEVEMENTS_FILE = "achievements";
	
	/**
	 * Les succès sont stockés dans cette HashMap, chaque succès ont un identifiant textuel et une valeur entière assocciée.
	 */
	private HashMap<String, Integer> data;
	
	
	private Achievement() {
		instance = this;
		data = new HashMap<String, Integer>();
		load();
	}
	
	/**
	 * Traite le cas lorsque un joueur se déplace.
	 */
	public void onMove() {
		data.put("moves", data.get("moves") + 1);
		//Sauvegarder sur disque tous les 25 déplacements
		if (data.get("moves") % 25 == 0) {
			save();
		}
		switch (data.get("moves")) {
		case 10: showMessage("Walker : move 10 times"); break;
		case 100: showMessage("High-level runner : move 100 times"); break;
		case 1000: showMessage("Globetrotter: move 1000 times"); break;
		case 10000: showMessage("Space Tourist : move 10000 times"); break;
		}
		
		
	}
	
	/**
	 * Traite le cas lorsque un joueur gagne.
	 */
	public void onWin() {
		data.put("wins", data.get("wins") + 1);
		switch (data.get("wins")) {
		case 5: showMessage("Lucky : win 5 times"); break;
		case 15: showMessage("Smart : win 15 times"); break;
		case 75: showMessage("Addicted : win 75 times"); break;
		case 200: showMessage("You're loosing your time :D : win 200 times"); break;
		}
	}
	
	/**
	 * Traite le cas lorsque un joueur recommence un niveau.
	 */
	public void onReload() {
		data.put("reloads", data.get("reloads") + 1);
		switch (data.get("reloads")) {
		case 5: showMessage("Sore loser : reload 5 times"); break;
		case 15: showMessage("Cheater: reload 15 times"); break;
		case 75: showMessage("Gambler: reload 75 times"); break;
		case 200: showMessage("Crook: reload 200 times"); break;
		}
	}
	
	/**
	 * Traite le cas lorsque un joueur meurt (se déplace dans KILL).
	 */
	public void onDeath() {
		data.put("deaths", data.get("deaths") + 1);
		switch (data.get("deaths")) {
		case 5: showMessage("Criminal : kill yourself 5 times"); break;
		case 15: showMessage("Serial killer: kill yourself 15 times"); break;
		case 75: showMessage("Soldier: kill yourself 75 times"); break;
		case 200: showMessage("Genocide contributor: kill yourself 200 times"); break;
		}
		
	}
	
	/**
	 * Affiche le message sur l'écran
	 * @param message le message à afficher
	 */
	private void showMessage(String message) {
		LevelScene ls = LevelScene.getInstance();
		if (ls != null) {
			SoundPlayer.getInstance().play(Sounds.ACHIEVEMENT);
			LevelScene.getInstance().showAchievementText(message);
		}
		
	}
	


	/**
	 * Renvoie d'instance déjà présente de Achievement ou en crée une nouvelle.
	 * @param levels
	 * @return
	 */
	public static Achievement getInstance() {
		if (instance == null) {
			return new Achievement();
		} else {
			return instance;
		}
	}
	
	/**
	 * Sauvegarde les données d'achievement dans le fichier ACHIEVEMENTS_FILE.
	 */
	public void save() {
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(ACHIEVEMENTS_FILE))) {
			writer.writeObject(data);
		} catch (IOException e) {
			LOGGER.severe("Could not write achievement data : " + e.getMessage());
		}
	}
	
	/**
	 * Charge les données depuis le fichier de sauvegarde.
	 */
	public void load() {
		Object data;
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(ACHIEVEMENTS_FILE))) {
			data = reader.readObject();
		} catch (ClassNotFoundException | IOException e) {
			LOGGER.severe("Could not read achievement data" + e.getMessage());
			this.data.put("moves", 0);
			this.data.put("wins", 0);
			this.data.put("reloads", 0);
			this.data.put("deaths", 0);
			return;
		}
		try {
			this.data = (HashMap<String, Integer>) data;
			
		} catch (Exception e) {
			LOGGER.warning("Could not read achievement data" + e.getMessage());
			this.data.put("moves", 0);
			this.data.put("wins", 0);
			this.data.put("reloads", 0);
			this.data.put("deaths", 0);
		}
	}

}
