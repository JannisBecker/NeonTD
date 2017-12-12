package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

import states.*;

/**
 * Startklasse des gesamten Programms.
 * Initialisiert die meisten States (Bildschirme) und startet das Spiel.
 *
 */
public class NeonTD extends StateBasedGame {

	public NeonTD(String name) {
		super(name);
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
				addState(new MainMenu());
				addState(new MapMenu());
				addState(new Credits());
	}
	
	public static void main(String[] args) {
		try {
			System.setProperty("org.newdawn.slick.pngloader", "false");

			extractResources();
			
			AppGameContainer game = new AppGameContainer(new NeonTD("NeonTD"));
			game.setDisplayMode(1280,800,false);
			game.setAlwaysRender(true);
			game.setShowFPS(false);
			game.setVSync(true);
			game.start();		
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Entpackt notwendige Ressourcen aus dem .jar Archiv, falls noch nicht vorhanden.
	 */
	public static void extractResources() {
		try {
			File resOnDisk = new File("res/");
			if(!resOnDisk.exists() || !resOnDisk.isDirectory()) {
				resOnDisk.mkdir();

				ZipFile zipFile = new ZipFile("NeonTD.jar");
				Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
				while (enumeration.hasMoreElements()) {
					ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
					if(zipEntry.getName().startsWith("res")) {
						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
						int size;
						byte[] buffer = new byte[2048];

						File f = new File(zipEntry.getName());
						if(!zipEntry.isDirectory()) {
							f.getParentFile().mkdirs();
							
							BufferedOutputStream bos = new BufferedOutputStream(
									new FileOutputStream(zipEntry.getName()), buffer.length);
							while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
								bos.write(buffer, 0, size);
							}
							
							bos.flush();
							bos.close();
							bis.close();
						} else {
							f.mkdirs();
						}
					}
				}
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
