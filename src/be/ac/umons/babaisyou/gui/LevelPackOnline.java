package be.ac.umons.babaisyou.gui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.*;

import be.ac.umons.babaisyou.exceptions.GamedCompletedException;
import be.ac.umons.babaisyou.exceptions.WrongFileFormatException;
import be.ac.umons.babaisyou.game.Level;

public class LevelPackOnline implements ILevelPack {
	
	/**
	 * Nom du fichier qui spécifier l'ordre dans lequel jouer les niveaux
	 */
	private static final String DEFAULT_ORDER_FILENAME = "order";
	
	private static final String DEFAULT_SERVER_DATA_LOCATION = "servers";
	
	private static final Logger LOGGER =  Logger.getGlobal();
	
	/**
	 * Utilise un LevelPack hors ligne un fois tout téléchargé
	 */
	private LevelPack levelPack;
	
	/**
	 * Initialise une série de niveaux depuis un server Web à l'adresse spécifiée.
	 * @param url l'adresse du server Web
	 * @throws IOException Si le server n'est pas accessible ou n'existe pas ou n'a pas une structure correcte
	 * @throws MalformedURLException 
	 */
	public LevelPackOnline(String url) throws MalformedURLException, IOException, FileNotFoundException {
		//Crée le dossier de servers si il n'existe pas
		File serversFolder = new File(DEFAULT_SERVER_DATA_LOCATION);
		if (!serversFolder.exists() || !serversFolder.isDirectory()) {
			serversFolder.mkdir();
		}
		serversFolder = null;

		//On utilise le hash pour éviter les caractères spéciaux.
		File serverFolder = new File(DEFAULT_SERVER_DATA_LOCATION + File.separator + getFolderHash(url));
		String serverFolderPath = serverFolder.getPath() + File.separator;
		if (!serverFolder.exists()) {
			serverFolder.mkdir();
		}
		//Téléchargement du fichier order
		LOGGER.info("Downloading "+url+DEFAULT_ORDER_FILENAME);
		try {
			getFileFromUrl(url + DEFAULT_ORDER_FILENAME, serverFolderPath + DEFAULT_ORDER_FILENAME);
		} catch (IOException e1) {
			LOGGER.log(java.util.logging.Level.WARNING, "Unable to download "+url+DEFAULT_ORDER_FILENAME + " : " + e1.getMessage());
			throw e1;
		}
		
		
		//lecture de la liste des niveaux
		
		try (BufferedReader buffer = new BufferedReader(new FileReader(serverFolderPath + DEFAULT_ORDER_FILENAME))) {
			String line;
			while ((line = buffer.readLine()) != null) {
				//Téléchargement des fichiers de niveaux
				LOGGER.info("Downloading " + url + line);
				try {
					getFileFromUrl(url + line, serverFolderPath + line);
				} catch (IOException e) {
					LOGGER.log(java.util.logging.Level.WARNING, "Unable to download "+url+line, e);
					throw e;
				}
			}
		}
		
		levelPack = new LevelPack(serverFolderPath);
	}
	
	
	@Override
	public Level getCurrentLevel() throws WrongFileFormatException {
		return levelPack.getCurrentLevel();
	}

	@Override
	public void setFirstLevel(String firstLevel) {
		levelPack.setFirstLevel(firstLevel);

	}

	@Override
	public Level nextLevel() throws GamedCompletedException {
		return levelPack.nextLevel();
	}

	@Override
	public String[] getPlayableLevels() {
		return levelPack.getPlayableLevels();
	}
	
	/**
	 * Change le dossier en son hash pour éviter les caractères spéciaux. 
	 * @param url l'url du server
	 * @return son hash via l'algorithme SHA-256
	 * @author Brendan Long
	 * @see https://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
	 */
	private String getFolderHash(String url) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			 // Change this to UTF-16 if needed
		    md.update(url.getBytes(StandardCharsets.UTF_8));
		    byte[] digest = md.digest();

		    String hex = String.format("%064x", new BigInteger(1, digest));
		    return hex;
		} catch (NoSuchAlgorithmException e) {
			// N'arrive jamais car toujour algorithme SHA-256
			throw new RuntimeException(e);
		}

		
		
		
	}
	
	
	/**
	 * Télécharge un fichier depuis un server Web
	 * @param url l'url de la ressource en ligne
	 * @param filename l'emplacement où stocker le fichier
	 * @return Un fichier téléchargé depuis internet
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @author Jared Burrows
	 * @see https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
	 */
	private void getFileFromUrl(String url, String filename) throws MalformedURLException, IOException {
		BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(url).openStream());
	        fout = new FileOutputStream(filename);

	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}

}
