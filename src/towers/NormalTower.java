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
 * Repräsentiert einen alles angreifenden, normalen Turm im Spiel.
 *
 */
public class NormalTower extends ShootingTower {

	/**
	 * Erstellt einen Normal Tower.
	 * @param position Die Position des Turms
	 * @param game Eine Referenz zum Spielobjekt
	 */
	public NormalTower(Point position, Game game) {
		super(position,
				new int[]{8,9,10}, //Schaden
				new int[]{800,650,500}, //Schussintervall
				new int[]{150,200,250}, //Reichweite
				new int[]{150,200,300}, //Kosten
				new Image[]{
					ImageManager.getImage(ImageManager.NORMAL_TOWER_1),
					ImageManager.getImage(ImageManager.NORMAL_TOWER_2),
					ImageManager.getImage(ImageManager.NORMAL_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_BLUE), game);
	}

	@Override
	public boolean canTarget(Entity entity) {
		return true;
	}
}



