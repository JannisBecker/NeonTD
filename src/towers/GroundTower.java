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
 * Repräsentiert einen Bodenangriff Turm (Grün) im Spiel.
 *
 */
public class GroundTower extends ShootingTower {
	
	/**
	 * Erstellt einen Ground Tower.
	 * @param position Die Position des Turms
	 * @param game Eine Referenz zum Spielobjekt
	 */
	public GroundTower(Point position,Game game) {
		super(position,
				new int[]{16,18,24}, //Schaden
				new int[]{1400,1300,1100}, //Schussintervall
				new int[]{200,250,300}, //Reichweite
				new int[]{150,200,300}, //Kosten
				new Image[]{
					ImageManager.getImage(ImageManager.GROUND_TOWER_1),
					ImageManager.getImage(ImageManager.GROUND_TOWER_2),
					ImageManager.getImage(ImageManager.GROUND_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_GREEN), game);
	}

	@Override
	public boolean canTarget(Entity entity) {
		if(!entity.isFlying()) {
			return true;
		}
		return false;
	}
}



