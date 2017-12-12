package towers;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import entities.Entity;
import states.Game;

/**
 * Die allgemeine Turmklasse.
 * Diese speichert Eigenschaften, welche
 * alle Türme im Spiel gemein haben.
 * Es kann kein allgemeiner Turm erstellt werden,
 * es ist eine spezialisierte Klasse erforderlich.
 *
 */
public abstract class Tower {
	protected Game game;
	
	protected Point position;
	
	protected int upgradeLevel;
	protected int[] range, cost;
	protected Image[] textures;
	
	/**
	 * Erstellt einen allgemeinen Turm, wird von allen spezialisierten Turmklassen aufgerufen.
	 * @param position Die Position des Turms
	 * @param range Die Reichweiten des Turms (für alle Upgradestufen)
	 * @param cost Die Kosten des Turms und aller Upgradestufen
	 * @param textures Die Texturen für alle Upgradestufen
	 * @param game Eine Referenz zum Spielobjekt
	 */
	protected Tower(Point position, int[] range, int[] cost, Image[] textures, Game game) {
		this.position = position;
		this.range = range;
		this.cost = cost;
		this.textures = textures;
		this.game = game;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Point getTilePosition() {
		return new Point(
			(int)Math.floor(position.getX()/48),
			(int)Math.floor(position.getY()/48)
		);
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getUpgradeLevel() {
		return upgradeLevel;
	}
	
	public void setUpgradeLevel(int upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}

	public int getRange() {
		return range[upgradeLevel];
	}

	public int getCost() {
		return cost[upgradeLevel];
	}
	
	public int getCost(int i) {
		return cost[i];
	}
	
	public int getUpgradeCost() {
		return (upgradeLevel < 2)? cost[upgradeLevel+1] : 0;
	}
	
	/**
	 * Erstellt eine Liste aller in Reichweite befindlichen Gegner und gibt diese zurück
	 * @return Die Liste aller Gegner in Reichweite
	 */
	public ArrayList<Entity> getEntitiesInRange() {
		ArrayList<Entity> entityList = game.getEntityList();
		ArrayList<Entity> entitiesInRange = new ArrayList<Entity>();
		for(Entity entity: entityList) {
			//Position holen
			Vector2f entityPos = entity.getPosition();
			
			//Distanz zum Gegner ausrechnen
			double distance = Math.sqrt(Math.pow(Math.abs(entityPos.x-position.getX()), 2)+Math.pow(Math.abs(entityPos.y-position.getY()), 2));
			if(distance <= getRange()) {
				entitiesInRange.add(entity);
			}
		}
		return entitiesInRange;
	}

	/**
	 * Entscheidet, ob ein Turm einen Gegner
	 * aufgrund seines Typs angreifen kann oder nicht.
	 * Wird für jede spezialisierte Turmklasse realisiert.
	 * @param entity Die zu überprüfende Einheit
	 * @return Wahrheitswert, ob der Turm angreifen kann
	 */
	public abstract boolean canTarget(Entity entity);
	
	/**
	 * Berechnet die Änderungen des Turms für den nächsten Frame.
	 * Wird von der update-Methode des Game Objektes aufgerufen.
	 * Wird für jede spezialisierte Turmklasse realisiert.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param delta Zeit des letzten Frames in ms, wird für zeitliche, fps-unabhängige Berechnungen verwendet
	 */
	public abstract void update(GameContainer gc, int delta);
	
	/**
	 * Rendert den Turm auf dem Bildschirm.
	 * Wird von der render-Methode des Game Objektes aufgerufen.
	 * Wird für jede spezialisierte Turmklasse realisiert.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param g Graphics Objekt, welches Render/Zeichenmethoden bereitstellt
	 */
	public abstract void render(GameContainer gc, Graphics g);
}




