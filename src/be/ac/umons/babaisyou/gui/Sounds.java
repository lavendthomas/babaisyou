package be.ac.umons.babaisyou.gui;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public enum Sounds {
	
	BACKGOUND("background.wav"),
	WIN("win.wav"),
	ACHIEVEMENT("achievement.wav");
	
	private String path;
	private boolean playing;
	MediaPlayer mediaplayer;
	
	public static File currentDirectory = new File("");
	
	public static final String DELFAUT_SOUNDS_LOCATION = currentDirectory.getAbsolutePath()
			+ File.separator + "src" + File.separator
			+ "be" + File.separator +"ac"+File.separator+"umons"+File.separator
			+ "babaisyou" + File.separator + "ressources"+ File.separator + "sounds" + File.separator;
	
	private Sounds(String path) {
		this.path = path;
		playing = false;
	}
	
	public String getURI() {
		File sound = new File(DELFAUT_SOUNDS_LOCATION + path);
		return sound.toURI().toString();
	}
	
	/**
	 * Lance le son
	 * @param repeat Si le son recommence une fois terminé
	 */
	public void play(boolean repeat) {
		mediaplayer = new MediaPlayer(new Media(getURI()));
		if (repeat) {
			mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
		}
		mediaplayer.play();
		playing = true;
	}
	/**
	 * Lance le son
	 */
	public void play() {
		play(false);
	}
	
	/**
	 * Coupe le son.
	 */
	public void stop() {
		if (playing) {
			mediaplayer.stop();
			mediaplayer = null;
			playing = false;
		}

	}
	
	public void restart() {
		if (playing) {
			mediaplayer.stop();
			mediaplayer.play();
		}
	}
	
	public void setPlaying(boolean status) {
		playing = status;
	}
	
	public boolean isPlaying() {
		return playing;
	}

}
