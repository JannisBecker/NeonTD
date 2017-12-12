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
 * Repräsentiert einen Luftangriff Turm (Hellblau) im Spiel.
 *
 */
public class AirTower extends ShootingTower {
	
	/**
	 * Erstellt einen Air Tower.
	 * @param position Die Position des Turms
	 * @param game Eine Referenz zum Spielobjekt
	 */
	public AirTower(Point position, Game game) {
		super(position,
				new int[]{7,8,9}, //Schaden
				new int[]{600,500,400}, //Schussintervall
				new int[]{150,200,250}, //Reichweite
				new int[]{150,200,300}, //Kosten
				new Image[]{
					ImageManager.getImage(ImageManager.AIR_TOWER_1),
					ImageManager.getImage(ImageManager.AIR_TOWER_2),
					ImageManager.getImage(ImageManager.AIR_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_CYAN), game);
	}

	@Override
	public boolean canTarget(Entity entity) {
		if(entity.isFlying()) {
			return true;
		}
		return false;
	}
}



