package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import states.Game;

/**
 * Repräsentiert eine gegnerische Einheit.
 * 
 */
public class Entity implements Mover {
	private Game game;
	
	private Vector2f position;
	private int angle;
	private int tileposx, tileposy;
	
	private int health, maxhp, radius;
	private float speed;
	private boolean isFlying, isBoss;
	private float slowValue;
	private Image texture;
	
	private AStarPathFinder pathfinder;
	private Point targetPoint;
	private float rotation;
	
	/**
	 * Erstellt eine neue Einheit mit den gegebenen Eigenschaften.
	 * @param game Referenz zum Spielobjekt
	 * @param position Position, an der die Einheit erscheinen soll
	 * @param hp Initiale Lebenspunkte der Einheit
	 * @param speed Geschwindigkeitsfaktor der Einheit
	 * @param isFlying Bestimmt, ob die Einheit fliegt oder nicht
	 * @param isBoss Bestimmt, ob die Einheit ein Bossgegner ist
	 * @param texture Die Textur, mit der die Einheit gerendert werden soll
	 */
	public Entity(Game game, Vector2f position, int hp, float speed, boolean isFlying, boolean isBoss, Image texture) {
		this.game = game;
		
		this.position = position;
		this.tileposx = (int)Math.floor((position.x)/48);
		this.tileposy = (int)Math.floor((position.y)/48);
		
		this.health = hp;
		this.maxhp = hp;
		this.radius = 20;
		this.speed = speed;
		this.slowValue = 1;
		this.isFlying = isFlying;
		this.isBoss = isBoss;
		this.texture = texture;
		
		if(!isBoss) {
			texture.setCenterOfRotation(24, 24);
			radius = 10;
		}

		this.targetPoint = game.getMap().getBase();		
		this.pathfinder = new AStarPathFinder(game.getMap(), 200, false);
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxhp() {
		return maxhp;
	}
	
	public int getRadius() {
		return radius;
	}

	public Vector2f getPosition() {
		return position;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public boolean isBoss() {
		return isBoss;
	}

	
	public void setSlowValue(float slowValue) {
		if(slowValue < this.slowValue)
			this.slowValue = slowValue;
	}
	
	/**
	 * Berechnet die Änderungen der Einheit für den nächsten Frame.
	 * Wird von der update-Methode des Game Objektes aufgerufen.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param delta Zeit des letzten Frames in ms, wird für zeitliche, fps-unabhängige Berechnungen verwendet
	 */
	public void update(GameContainer gc, int delta) {	

		/* Koordinaten werden nur verändert, wenn sich die Einheit mindestens 1 Feld (48px) vom aktuellen entfernt hat */
		if(Math.abs(tileposx*48+24-position.x) >= 48) {
			tileposx = (int)Math.floor((position.x)/48);
		} else if(Math.abs(tileposy*48+24-position.y) >= 48) {
			tileposy = (int)Math.floor((position.y)/48);
		}
		
		rotation = (delta/1000.0f)*200*slowValue;
		texture.rotate(rotation);
		
		/* Frage Pathfinder nach neuem Pfad */
		Path p = pathfinder.findPath(this, tileposx, tileposy , (int)targetPoint.getX(), (int)targetPoint.getY());
		if(p != null) {
			/* Wenn es einen nächsten Punkt auf dem Pfad gibt */
			if(p.getLength() > 0) {
				/* Erstelle ein Vektor zum nächsten Punkt, skaliere ihn mit delta und setze neue Position */
				Step step = p.getStep(1);
				Vector2f nextPoint = new Vector2f(step.getX() - tileposx, step.getY() - tileposy);			
				nextPoint = nextPoint.scale((delta/1000.f)*48*speed*slowValue);
				position.add(nextPoint);
				
				/* Nach dem Bewegen wird der Slow Wert wieder normalisiert */
				slowValue = 1;
			}
		}
		/* Wenn der Gegner in der Base steht */
		else {
			Point base = game.getMap().getBase();
			if(base.getX() == tileposx && base.getY() == tileposy) {
				/* Entferne Gegner und ziehe Leben ab */
				game.removeEntity(this);
				game.setHealth(game.getHealth() - ((isBoss)? 5 : 1));
			}
		}
	}
	
	/**
	 * Rendert die Einheit auf dem Bildschirm.
	 * Wird von der render-Methode des Game Objektes aufgerufen.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param g Graphics Objekt, welches Render/Zeichenmethoden bereitstellt
	 */
	public void render(GameContainer gc, Graphics g) {
		if(isBoss) {
			texture.drawCentered(position.x, position.y);
		} else {
			texture.draw(position.x-24, position.y-24, 48, 48);
		}
		
		/* Rendere Lebensleiste */
		g.setColor(Color.red);
		g.fillRect(position.x-18, position.y-24, 36, 2);
		
		g.setColor(Color.green);
		float width = 36*((float)health/(float)maxhp);
		if(width > 0) {	
			g.fillRect(position.x-18, position.y-24, (int)width, 2);	
		}
	}
}



