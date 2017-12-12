package towers;

import java.util.ArrayList;
import java.util.Iterator;

import misc.Bullet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import states.Game;
import entities.Entity;

/**
 * Die allgemeine Klasse aller schießenden Türme.
 * Stellt Eigenschaften und Methoden für das Zielen
 * und Schießen von Projektilen bereit.
 * Von dieser Klasse kann kein Objekt erstellt werden,
 * es ist eine spezialisierte Klasse erforderlich.
 *
 */
public abstract class ShootingTower extends Tower {
	protected Image projectile;
	
	protected int[] damage, shootInterval;
	protected int lastShot;
	protected int angle;
	
	protected Entity target;
	protected ArrayList<Bullet> bullets;
	
	protected int killCount;
	
	/**
	 * Erstellt einen allgemeinen schießenden Turm, wird von allen spezialisierten Turmklassen aufgerufen.
	 * Es kann kein allgemeiner schießender Turm erstellt werden.
	 * @param position Die Position des Turms
	 * @param damage Die Schadenswerte des Turms und aller Upgradestufen
	 * @param shootInterval Die Schussfrequenzen des Turms und aller Upgradestufen
	 * @param range Die Reichweiten des Turms und aller Upgradestufen
	 * @param cost Die Kosten des Turms und aller Upgradestufen
	 * @param textures Die Texturen des Turms und aller Upgradestufen
	 * @param projectile Die Textur des Projektils dieses Turms
	 * @param game Eine Referenz zum Spielobjekt
	 */
	protected ShootingTower(Point position, int[] damage, int[] shootInterval,int[] range, int[] cost, Image[] textures, Image projectile, Game game) {
		super(position, range, cost, textures, game);
		this.projectile = projectile;
		this.damage = damage;
		this.shootInterval = shootInterval;		
		this.bullets = new ArrayList<Bullet>();
	}
	
	/**
	 * Sucht den Gegner mit den niedrigsten Lebenspunkten aller
	 * in Reichweite des Turms befindlichen Gegner und gibt diesen zurück.
	 * @return Der Gegner mit den niedrigsten Lebenspunkten in Reichweite
	 */
	public Entity lookForTarget() {
		ArrayList<Entity> entitiesInRange = getEntitiesInRange();
		/* Wenn Gegner in Reichweite sind */
		if(!entitiesInRange.isEmpty()) {	
			
			/* Zuerst auf "nichts gefunden" setzen */
			int index = -1;
			
			/* Suche Gegner mit geringstem Leben aller in Reichweite befindlichen Gegner */
			for(int i = 0; i < entitiesInRange.size();i++) {
				if(canTarget(entitiesInRange.get(i))) {
					
					/* Index beim ersten Fund setzen und sonst überprüfen, ob jeder nächste Fund weniger Leben als der erste Fund hat */
					if(index == -1 || entitiesInRange.get(i).getHealth() < entitiesInRange.get(index).getHealth()) {
						index = i;
					}
				}
			}
			
			/* Wenn ein Gegner gefunden wurde, zurückgeben, sonst null */
			if(index != -1) {
				return entitiesInRange.get(index);
			}
		}
		return null;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	/**
	 * Löscht das momentane Ziel des Turms und das aller seiner Geschosse
	 */
	public void clearTarget() {
		target = null;
		for(Bullet b : bullets) {
			b.setTarget(null);
		}
	}
	
	public int getDamage() {
		return damage[upgradeLevel];
	}
	
	public int getShootInterval() {
		return shootInterval[upgradeLevel];
	}

	public int getKillCount() {
		return killCount;
	}

	/**
	 * Updatezyklus des Turms.
	 * Instanziiert Geschosse und ruft deren update Methoden auf,
	 * sucht nach neuen Gegnern in Reichweite und verrechnet Schaden,
	 * falls ein Projektil trifft
	 */
	@Override
	public void update(GameContainer gc, int delta) {
		/* Zähle die vergangene Zeit seit dem letzten Schuss */
		lastShot += delta;
		
		/* Falls der Turm ein Ziel hat */
		if(target != null) {
			
			/* Lasse den Turm in Richtung Ziel schauen */
			angle = (int)new Vector2f(target.getPosition().getX()-position.getX(),target.getPosition().getY() - position.getY()).getTheta();
			
			/* Falls das Schussintervall überschritten wurde, instanziiere ein neues Geschoss */
			if(lastShot >= shootInterval[upgradeLevel]) {
				bullets.add(new Bullet(new Vector2f(position.getX(),position.getY()),new Vector2f(angle), target,projectile.copy()));
				lastShot = 0;
			}
			
			/* Wenn das Ziel außerhalb der Reichweite des Turmes ist, dann setze target auf null (um ein neues Ziel zu suchen) */
			double distanceToTarget = Math.sqrt(Math.pow(Math.abs(target.getPosition().getX()-position.getX()), 
					2)+Math.pow(Math.abs(target.getPosition().getY()-position.getY()), 2));
			if(distanceToTarget > getRange()) {
				target = null;
			}
		} else {
			target = lookForTarget();
		}
		
		/* Update alle Bullets und überprüfe die Lebenszeit und Kollision */
		Iterator<Bullet> iterator = bullets.iterator();
		while(iterator.hasNext()){
           	Bullet b = iterator.next();
           	
            b.update(gc, delta);
            
            if(b.getLifeTime() > 10000) {
				iterator.remove();
			}
            
            /* Wenn das Geschoss mit seinem zugehörigen Ziel kollidiert */
            if(b.hitsTarget()) {
            	
            	/* Dann verrechne Schaden */
            	Entity t = b.getTarget();
            	t.setHealth(t.getHealth()-damage[upgradeLevel]);
            	
            	/* Wenn das Ziel nun gestorben ist, entferne alle Referenzen und benachrichtige die anderen Türme */
            	if(t.getHealth() <= 0) {
            		game.removeEntity(t);
            		game.setGold(game.getGold()+game.getMap().getKillMoney());
            		killCount++;
            	}
            	
            	iterator.remove();
            }
        }
	}
	
	/**
	 * Rotiert den Turm und rendert ihn sowie alle seine Geschosse.
	 */
	@Override
	public void render(GameContainer gc, Graphics g) {
		Image tower = this.textures[upgradeLevel];
		tower.setRotation(angle);
		tower.drawCentered(position.getX(), position.getY());

		for(Bullet b : bullets) {
			b.render(gc, g);
		}
	}
}


