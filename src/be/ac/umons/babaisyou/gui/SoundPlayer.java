package be.ac.umons.babaisyou.gui;

public class SoundPlayer {

	//Singleton
	private static SoundPlayer instance;
	
	private SoundPlayer() {
	}
	
	/**
	 * Lance le son.
	 * @param sound
	 * @param repeat Si le son recommence une fois terminé
	 */
	public void play(Sounds sound, boolean repeat) {
		if (ControlsScene.musicOn) {
			if (!sound.isPlaying()) {
				sound.play(repeat);
			} else {
				sound.restart();
			}
		} else {
			stopAll();
		}
		
	}
	
	/**
	 * Lance le son sans répétition.
	 * @param sound
	 */
	public void play(Sounds sound) {
		play(sound, false);
	}
	
	/**
	 * Coupe tout son en cours
	 */
	public void stopAll() {
		for (Sounds sound : Sounds.values()) {
			sound.stop();
		}
	}
	
	
	/**
	 * Permet d'obtenir l'unique instance ou en crée une si aucune n'existe
	 * @return
	 */
	public static SoundPlayer getInstance() {
		if (instance == null) {
			return new SoundPlayer();
		} else {
			return instance;
		}
	}

}
