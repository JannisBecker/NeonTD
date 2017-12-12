package main;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Eine Klasse, welche sich um die Bereitstellung jeglicher Texturen
 * im Spiel kümmert.
 * Die Texturen werden beim Start des Spiels geladen.
 * 
 */
public class ImageManager {
	
	/* Türme */
	public static final int NORMAL_TOWER_1 = 0;
	public static final int NORMAL_TOWER_2 = 1;
	public static final int NORMAL_TOWER_3 = 2;
	
	public static final int GROUND_TOWER_1 = 3;
	public static final int GROUND_TOWER_2 = 4;
	public static final int GROUND_TOWER_3 = 5;
	
	public static final int AIR_TOWER_1 = 6;
	public static final int AIR_TOWER_2 = 7;
	public static final int AIR_TOWER_3 = 8;
	
	public static final int SLOW_TOWER_1 = 9;
	public static final int SLOW_TOWER_2 = 10;
	public static final int SLOW_TOWER_3 = 11;
	
	/* Menüs */
	public static final int MENU_BACKGROUND = 12;
	public static final int MENU_TEXT_NEONTD = 13;
	public static final int MENU_TEXT_CHOOSEMAP = 14;
	
	public static final int MENU_BUTTON_PLAY = 15;
	public static final int MENU_BUTTON_CREDITS = 16;
	public static final int MENU_BUTTON_EXIT = 17;
	
	public static final int MENU_BUTTON_LARROW = 18;
	public static final int MENU_BUTTON_RARROW = 19;
	
	/* Spiel UI */
	public static final int GAME_SIDEBAR_BG = 20;
	public static final int GAME_INFO = 21;
	public static final int GAME_BUTTON_UPGRADE = 22;
	public static final int GAME_BUTTON_SELL = 23;
	public static final int GAME_BUTTON_STARTWAVE = 24;
	public static final int GAME_BUTTON_QUITGAME = 25;
	public static final int GAME_BUTTON_CANCEL = 26;
	public static final int GAME_BUTTON_RETRY = 27;
	
	/* Projektile */
	public static final int PROJECTILE_BLUE = 28;
	public static final int PROJECTILE_CYAN = 29;
	public static final int PROJECTILE_GREEN = 30;
	
	/* Gegner */
	public static final int ENEMY_GROUND = 31;
	public static final int ENEMY_AIR = 32;
	
	private static ArrayList<Image> imageList;
	
	static {
		imageList = new ArrayList<Image>();
		
		try {
			/* Türme */
			imageList.add(new Image("res/images/towers/CircleTower1.png"));
			imageList.add(new Image("res/images/towers/CircleTower2.png"));
			imageList.add(new Image("res/images/towers/CircleTower3.png"));
			
			imageList.add(new Image("res/images/towers/HexagonTower1.png"));
			imageList.add(new Image("res/images/towers/HexagonTower2.png"));
			imageList.add(new Image("res/images/towers/HexagonTower3.png"));
			
			imageList.add(new Image("res/images/towers/PentagonTower1.png"));
			imageList.add(new Image("res/images/towers/PentagonTower2.png"));
			imageList.add(new Image("res/images/towers/PentagonTower3.png"));
			
			imageList.add(new Image("res/images/towers/GearTower1.png"));
			imageList.add(new Image("res/images/towers/GearTower2.png"));
			imageList.add(new Image("res/images/towers/GearTower3.png"));
			
			/* Menüs */
			imageList.add(new Image("res/images/menus/background.png"));
			
			imageList.add(new Image("res/images/menus/text-neontd.png"));
			imageList.add(new Image("res/images/menus/text-choose-map.png"));
			
			imageList.add(new Image("res/images/menus/button-play.png"));
			imageList.add(new Image("res/images/menus/button-credits.png"));
			imageList.add(new Image("res/images/menus/button-exit.png"));
			
			imageList.add(new Image("res/images/menus/button-arrow-left.png"));
			imageList.add(new Image("res/images/menus/button-arrow-right.png"));
			
			/* Spiel UI */
			imageList.add(new Image("res/images/menus/game-sidebarbg.png"));
			imageList.add(new Image("res/images/menus/info.png"));
			imageList.add(new Image("res/images/menus/button-upgrade.png"));
			imageList.add(new Image("res/images/menus/button-sell.png"));
			imageList.add(new Image("res/images/menus/button-startwave.png"));
			imageList.add(new Image("res/images/menus/button-quitgame.png"));
			imageList.add(new Image("res/images/menus/button-cancel.png"));
			imageList.add(new Image("res/images/menus/button-retry.png"));
			
			/* Projektile */
			imageList.add(new Image("res/images/projectiles/projectile-blue.png"));
			imageList.add(new Image("res/images/projectiles/projectile-cyan.png"));
			imageList.add(new Image("res/images/projectiles/projectile-green.png"));
			
			/* Gegner */
			imageList.add(new Image("res/images/enemies/enemy-ground.png"));
			imageList.add(new Image("res/images/enemies/enemy-air.png"));
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt eine Kopie der Textur mit der angegebenen ID zurück
	 * @param id ID der zurückzugebenden Textur
	 * @return Das gewünschte Image-Objekt
	 */
	public static Image getImage(int id) {
		return imageList.get(id).copy();
	}
}
