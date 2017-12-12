package misc;

import java.util.ArrayList;

import states.Game;
import towers.Tower;
import main.ImageManager;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import entities.Entity;

/**
 * Repräsentiert eine Karte, auf welcher das Spiel
 * gespielt werden kann.
 * Beinhaltet alle Eigenschaften, die für ein Spiel benötigt werden,
 * sowie eine TiledMap, welche die Kartendatei repräsentiert.
 *
 */
public class Map implements TileBasedMap {
	private String name;
	private Image preview;
	private TiledMap map;
	private int startingMoney, killMoney, waveMoney;
	private int waveHealthMultiplier;
	private int startHpGround, startHpAir, startHpBoss;
	private ArrayList<Point> spawnList;
	private Point base;
	private ArrayList<Integer[]> waveList;
	
	private Game game;
	private int[][] towerList;
	
	/**
	 * 

	
	 */
	
	/**
	 * Erstellt ein Karten Objekt.
	 * @param map Die zu verwendende Kartendatei in Form eines TiledMap Objekts
	 * @param preview Ein Vorschaubild für die Kartenauswahl
	 * @param name Der Name der Karte
	 * @param startingMoney Das Startgeld
	 * @param killMoney Das Belohnungsgeld für jeden getöteten Gegner
	 * @param waveMoney Das Belohnungsgeld für eine überstandende Welle
	 * @param startHpGround Die Lebenspunkte der Bodengegner (ohne Multiplier)
	 * @param startHpAir Die Lebenspunkte der Luftgegner (ohne Multiplier)
	 * @param startHpBoss Die Lebenspunkte der Bossgegner (ohne Multiplier)
	 * @param waveHealthMultiplier Die Erhöhung der Lebenspunkte der Gegner nach jeder Welle
	 * @param spawnList Die Liste aller Spawnpunkte (Erscheinungspunkte) in der Karte
	 * @param base Die Koordinaten der Basis (Das Ziel der Einheiten)
	 * @param waveList Die Liste aller Anzahlen der Boden-,Luft- und Bossgegner jeder Welle
	 */
	public Map(TiledMap map, Image preview, String name, int startingMoney, int killMoney, int waveMoney, int startHpGround, int startHpAir, int startHpBoss, int waveHealthMultiplier, 
				ArrayList<Point> spawnList, Point base, ArrayList<Integer[]> waveList) {
		this.preview = preview;
		this.map = map;
		this.name = name;
		this.startingMoney = startingMoney;
		this.killMoney = killMoney;
		this.waveMoney = waveMoney;
		this.startHpGround = startHpGround;
		this.startHpAir = startHpAir;
		this.startHpBoss = startHpBoss;
		this.waveHealthMultiplier = waveHealthMultiplier;
		this.spawnList = spawnList;
		this.base = base;
		this.waveList = waveList;
		
		this.towerList = new int[22][15];
	}
	
	
	/**
	 * Leert das Kollisionsarray der Türme.
	 * Da die Map-Objekte bei einem erneuten Auswählen der Karte nicht neu erstellt werden,
	 * muss die Karte aller gebauten Türme zurückgesetzt werden, damit an diesen Stellen im neuen Spiel
	 * wieder Türme gebaut werden können.
	 */
	public void resetTowerList() {
		this.towerList = new int[22][15];
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public void setTower(int x, int y, boolean isTower) {
		towerList[x][y] = (isTower)? 1 : 0; 
	}
	
	/**
	 * Gibt zurück, ob an der Koordinate bereits ein Turm steht.
	 * @param x X-Koordinate des zu untersuchenden Feldes
	 * @param y Y-Koordinate des zu untersuchenden Feldes
	 * @return Wahrheitswert, ob dort ein Turm steht
	 */
	public boolean isTower(int x, int y) {
		return (towerList[x][y] == 1)? true : false;
	}

	public String getName() {
		return name;
	}

	public Image getPreview() {
		return preview;
	}

	public int getStartingMoney() {
		return startingMoney;
	}

	public int getKillMoney() {
		return killMoney;
	}

	public int getWaveMoney() {
		return waveMoney;
	}

	public ArrayList<Point> getSpawnList() {
		return spawnList;
	}

	public Point getBase() {
		return base;
	}

	public ArrayList<Integer[]> getWaveList() {
		return waveList;
	}
	
	public Integer[] getWaveUnits(int wave) {
		return (wave <= waveList.size())? waveList.get(wave-1): null;
	}

	/**
	 * Überprüft, ob die Koordinate auf der Karte durch den Wegesrand
	 * oder einen Turm blockiert wird.
	 * Beim Kauf eines Turms dient diese Methode dazu, zu überprüfen, ob
	 * dieser Turm an der gegebenen Position noch einen weiteren Weg vom
	 * Start zum Ziel zulassen würde.
	 * @param context Kontext des Pathfinders, stellt u.A. die anfragende Einheit bereit
	 * @param x X-Koordinate des anzufragenden Feldes
	 * @param y Y-Koordinate des anzufragenden Feldes
	 */
	@Override
	public boolean blocked(PathFindingContext context, int x, int y) {
		if(map.getTileId(x, y, 1) == 0)
			return true;
		
		if(towerList[x][y] != 0)
			return true;
		
		/*	
		 * Wenn nicht von einer Einheit angefragt, dann überprüfe, ob ein Tower gesetzt wird
		 *	und überprüfe dessen Position, ob diese den Pfad blockieren würde
		 */
		Mover mover = context.getMover();
		if(mover == null) {
			Tower buyTower = game.getBuyTower();
			if(buyTower != null) {
				Point towerPos = buyTower.getTilePosition();
				if(x == towerPos.getX() && y == towerPos.getY()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public float getCost(PathFindingContext context, int x, int y) {
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() {
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	public TiledMap getMap() {
		return map;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

	/**
	 * Bereitet anhand der Karten-Daten eine Liste von Gegnern für die gegebene Welle vor
	 * @param wave Die zu "erstellende" Welle
	 * @return Liste der Einheiten für die gegebene Welle
	 */
	public ArrayList<Entity> getEntityList(int wave) {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		
		/* Wenn die mitgegebene Wellenzahl kleiner/gleich der Anzahl der Wellen dieser Map ist */
		if(wave <= waveList.size() && wave > 0) {
			Integer[] numbers = waveList.get(wave-1);
			
			int spawn = 0;
			
			/* Nehme die Anzahl der Bodeneinheiten und erstelle deren Objekte */
			for(int g = 0; g < numbers[0];g++) {
				/* Erhöhe den Index des zu benutzenden Spawnpunktes */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* Hole den Spawnpunkt mit dem Index */
				Point spawnPoint = spawnList.get(spawn);
				
				/* Erstelle eine Einheit an dem Spawnpunkt und füge sie der Liste hinzu */
				entityList.add(new Entity(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpGround+(wave)*waveHealthMultiplier, 1.5f, false, false, ImageManager.getImage(ImageManager.ENEMY_GROUND)));
			}
			
			/* Nehme die Anzahl der Lufteinheiten und erstelle deren Objekte */
			for(int g = 0; g < numbers[1];g++) {
				/* Erhöhe den Index des zu benutzenden Spawnpunktes */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* Hole den Spawnpunkt mit dem Index */
				Point spawnPoint = spawnList.get(spawn);
				
				/* Erstelle eine Einheit an dem Spawnpunkt und füge sie der Liste hinzu */
				entityList.add(new Entity(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpAir+(wave)*waveHealthMultiplier, 2, true, false, ImageManager.getImage(ImageManager.ENEMY_AIR)));
			}
			
			/* Nehme die Anzahl der Bosseinheiten und erstelle deren Objekte */
			for(int g = 0; g < numbers[2];g++) {
				/* Erhöhe den Index des zu benutzenden Spawnpunktes */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* Hole den Spawnpunkt mit dem Index */
				Point spawnPoint = spawnList.get(spawn);
				
				/* Erstelle eine Einheit an dem Spawnpunkt und füge sie der Liste hinzu */
				entityList.add(new Entity(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.ENEMY_GROUND)));
			}
			return entityList;
		}
		return null;
	}

}

