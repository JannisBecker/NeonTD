package towers;

import main.ImageManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import entities.Entity;
import states.Game;

/**
 * Repräsentiert einen Verlangsamungs Turm (Weiß) im Spiel.
 *
 */
public class SlowTower extends Tower {
	
	private float[] slowValues;
	
	/**
	 * Erstellt einen Slow Tower.
	 * @param position Die Position des Turms
	 * @param game Eine Referenz zum Spielobjekt
	 */
	public SlowTower(Point position, Game game) {
		super(position,
				new int[]{100,100,100}, //Reichweite
				new int[]{150,200,300}, //Kosten
				new Image[]{
					ImageManager.getImage(ImageManager.SLOW_TOWER_1),
					ImageManager.getImage(ImageManager.SLOW_TOWER_2),
					ImageManager.getImage(ImageManager.SLOW_TOWER_3)		
				}, game);
		slowValues = new float[]{0.70f,0.6f,0.5f};
	}
	
	public float getSlowValue() {
		return slowValues[upgradeLevel];
	}

	/**
	 * Updatezyklus des Slow Towers.
	 * Wird eigenständig von der Update-Methode des Game Objektes aufgerufen.
	 * In jedem Durchlauf (Frame) wird für jede Einheit in Reichweite ein Verlangsamungswert
	 * gesetzt, der nach der Bewegung der Einheit wieder aufgehoben wird. So wird die Einheit wieder
	 * schneller, falls sie den Wirkungsbereich des Slow Towers verlässt.
	 */
	@Override
	public void update(GameContainer gc, int time) {
		for(Entity entity : this.getEntitiesInRange()) {
			if(canTarget(entity))
				entity.setSlowValue(slowValues[upgradeLevel]);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		Image tower = this.textures[upgradeLevel];
		tower.drawCentered(position.getX(), position.getY());
	}

	@Override
	public boolean canTarget(Entity entity) {
		if(!entity.isFlying()) {
			return true;
		}
		return false;
	}
}



