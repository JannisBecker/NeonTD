package misc;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import entities.Entity;

/**
 * Repräsentiert ein Projektil im Spiel.
 *
 */
public class Bullet {
	private Vector2f position, direction;
	private Entity target;
	private Image texture;
	private int radius;
	
	int lifeTime;
	
	/**
	 * Erstellt ein neues Projektil.
	 * @param position Initiale Position des Projektils
	 * @param direction Initiale Flugrichtung des Projektils in Form eines Vektors
	 * @param target Das anzufliegende Ziel des Projektils
	 * @param texture Die zu rendernde Textur
	 */
	public Bullet(Vector2f position, Vector2f direction, Entity target, Image texture) {
		this.position = position;
		this.direction = direction;
		this.target = target;
		this.texture = texture;
		this.radius = 12;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public void setTarget(Entity target) {
		this.target = target;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	/**
	 * Berechnet die Änderungen des Projektils für den nächsten Frame.
	 * Wird von der update-Methode des zugehörigen Tower Objektes aufgerufen.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param delta Zeit des letzten Frames in ms, wird für zeitliche, fps-unabhängige Berechnungen verwendet
	 */
	public void update(GameContainer gc, int delta) {
		lifeTime += delta;
		if (target != null)
			direction = new Vector2f(target.getPosition().x - position.x,target.getPosition().y - position.y).normalise();
		position.add(direction.copy().scale((delta/1000f)*500));
	}
	
	/**
	 * Rendert das Projektil auf dem Bildschirm.
	 * Wird von der render-Methode des zugehörigen Tower Objektes aufgerufen.
	 * @param gc GameContainer Referenz, verweist auf das Spielfenster
	 * @param g Graphics Objekt, welches Render/Zeichenmethoden bereitstellt
	 */
	public void render(GameContainer gc, Graphics g) {
		texture.setRotation(180 + (float)direction.getTheta());	
		texture.drawCentered(position.x,position.y);
	}

	/**
	 * Untersucht, ob das Projektil mit seinem zugeordneten Ziel kollidiert.
	 * @return Wahrheitswert, ob eine Kollision stattfindet
	 */
	public boolean hitsTarget() {
		if(target != null) {
			Vector2f entityPos = target.getPosition();
			double distance = Math.sqrt(Math.pow(Math.abs(entityPos.x-position.getX()), 2)+Math.pow(Math.abs(entityPos.y-position.getY()), 2));
			int targetRadius = target.getRadius();
			if (distance < (targetRadius + radius)) {
				return true;
			}
		}
		return false;
	}
}




