package states;

import java.util.ArrayList;

import main.ImageManager;
import misc.Map;
import misc.LatoFont;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;

import entities.Entity;
import towers.AirTower;
import towers.GroundTower;
import towers.NormalTower;
import towers.ShootingTower;
import towers.SlowTower;
import towers.Tower;

/**
 * Repräsentiert einen Spiel-State (Bildschirm).
 * Kümmert sich um alle Berechnungen des Hauptspiels.
 * 
 */
public class Game extends BasicGameState {
	
	/* Grafiken */
	Image sidebar_background, info;
	
	/* Buttons */
	MouseOverArea button_upgrade,button_sell, button_startWave,
	button_normalTower, button_groundTower, button_airTower, button_slowTower,
	button_quitGame, button_cancel;
	
	/* Pause Menü anzeigen? */
	private boolean pause;
	
	private Map map;
	private ArrayList<Entity> entityList;
	private ArrayList<Tower> towerList;
	
	/* Listen um Löschungen vorzumerken, die im nächsten Updatezyklus durchgeführt werden */
	private ArrayList<Entity> entityRemovalList;
	private ArrayList<Tower> towerRemovalList;
	
	private Tower buyTower, selectedTower;
	private AStarPathFinder buyTowerPathfinder;
	
	private Color selectedFill, selectedRing;
	
	private ArrayList<Entity> spawnList;
	
	private boolean lost;
	
	private int wave;
	private int spawnInterval = 1000, lastSpawn;
	
	private int gold;
	private int baseHealth, currentHealth;

	private boolean gotMoneyForWave = true;
	
	/**
	 * Erstellt einen neuen Spiel-State
	 * @param map Die zu spielende Karte
	 */
	public Game(Map map) {
		/* Hintergrund der Sidebar und Info-Button wird geladen */
		sidebar_background = ImageManager.getImage(ImageManager.GAME_SIDEBAR_BG);
		info = ImageManager.getImage(ImageManager.GAME_INFO);
		
		map.setGame(this);
		
		buyTowerPathfinder = new AStarPathFinder(map,200,false);
		
		this.map = map;
		this.entityList = new ArrayList<Entity>();
		this.entityRemovalList = new ArrayList<Entity>();
		this.towerList = new ArrayList<Tower>();
		this.towerRemovalList = new ArrayList<Tower>();
		this.spawnList = new ArrayList<Entity>();
		this.gold = map.getStartingMoney();
		this.baseHealth = 20;
		this.currentHealth = baseHealth;
		
		this.selectedFill =  new Color(41,136,255,40);
		this.selectedRing =  new Color(41,136,255,180);
	}

	public Map getMap() {
		return map;
	}
	
	/**
	 * Führt die Löschung der gestorbenen Einheiten aus und gibt die Liste der aktuell im Spiel befindlichen Einheiten zurück.
	 * Ersteres ist wichtig, da manche Türme nach dem Töten eines Gegners im selben Frame bereits ein neues Ziel suchen könnten.
	 * @return Liste der Einheiten im Spiel
	 */
	public ArrayList<Entity> getEntityList() {
		if(!entityRemovalList.isEmpty()) {
			for(Entity e : entityRemovalList) {
				entityList.remove(e);
			}
		}
		return entityList;
	}

	public ArrayList<Tower> getTowerList() {
		return towerList;
	}

	/**
	 * Gibt eine Referenz zum aktuell zu kaufenden Turm zurück.
	 * @return Referenz zum zu kaufenden Turm oder null, wenn kein Turm gekauft wird.
	 */
	public Tower getBuyTower() {
		return buyTower;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getWave() {
		return wave;
	}

	/**
	 * Initialisiert einige Grafiken und erstellt Listener für die Buttons.
	 * Wird einmalig bei der Erstellung von der GameEngine ausgeführt.
	 */
	@Override
	public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {	
		button_upgrade = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_UPGRADE),1100,332);
		button_sell =  new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_SELL),1100,378);
		button_startWave =  new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_STARTWAVE),1100,755);
		
		button_normalTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.NORMAL_TOWER_1),1104,48);
		button_groundTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GROUND_TOWER_1),1154,48);
		button_airTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.AIR_TOWER_1),1104,96);
		button_slowTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.SLOW_TOWER_1),1154,96);
		
		button_quitGame = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_QUITGAME),550,287);
		button_cancel = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_CANCEL),550,345);
		
		/* Tower Button Listener */
		button_normalTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new NormalTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_groundTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new GroundTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_airTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new AirTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_slowTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new SlowTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		
		/* Button um die nächste Wave zu starten */
		button_startWave.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				if(spawnList.isEmpty() && wave < map.getWaveList().size()) {
					wave++;
					spawnList = map.getEntityList(wave);
					
					if(!gotMoneyForWave) {
						gold += map.getWaveMoney();
					} else {
						gotMoneyForWave = false;
					}
				}
			}		
		});
		
		/* Buttons für das Upgraden/Verkaufen */
		button_upgrade.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				int upgradeLevel = selectedTower.getUpgradeLevel();
				int cost = selectedTower.getUpgradeCost();
				if(upgradeLevel < 2 && gold >= cost) {
					selectedTower.setUpgradeLevel(upgradeLevel+1);
					gold -= cost;
				}
			}		
		});
		
		button_sell.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				int reward = 0;
				for(int i = 0; i <= selectedTower.getUpgradeLevel();i++) {
					reward += selectedTower.getCost(i)/2;
				}
				
				gold += reward;
				
				Game.this.removeTower(selectedTower);
				Point position = selectedTower.getTilePosition();
				map.setTower((int)position.getX(), (int)position.getY(), false);
				
				selectedTower = null;
			}		
		});
		
		/* Buttons für das Pause Menü */
		button_quitGame.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				map.resetTowerList();
				sbg.enterState(1,new FadeOutTransition(), new FadeInTransition());
			}		
		});
		
		button_cancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				pause = false;
			}		
		});
	}

	/**
	 * Haupt Updatezyklus der GameEngine.
	 * Führt alle weiteren Update Methoden aus, reagiert auf Tastatureingaben
	 * und behandelt außerdem allgemeine Dinge wie den Turmkauf.
	 * Wird eigenständig von der GameEngine ausgeführt.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		/* Bildschirm wechseln, wenn man verloren hat */
		if(lost) {
			Summary summary = new Summary(this,false);
			summary.init(gc, sbg);
			sbg.addState(summary);
			sbg.enterState(4,new FadeOutTransition(), new FadeInTransition());
		}
		
		Input input = gc.getInput();
		if(!pause) {
			lastSpawn += delta;
			
			/* Wenn noch Gegner zum Spawnen in der Liste sind und das Intervall vergangen ist */
			if(!spawnList.isEmpty() && lastSpawn > spawnInterval) {
				/* Spawne den Gegner */
				entityList.add(spawnList.get(0));
				spawnList.remove(0);
				lastSpawn = 0;
			/* Wenn die Wave vorbei ist */
			} else if(spawnList.isEmpty() && entityList.isEmpty()) {
				
				if(!gotMoneyForWave) {
					gold += map.getWaveMoney();
					gotMoneyForWave = true;
				}
				
				if(wave == map.getWaveList().size()) {
					/* Ist das Spiel gewonnen */
					Summary summary = new Summary(this,true);
					summary.init(gc, sbg);
					sbg.addState(summary);
					sbg.enterState(4,new FadeOutTransition(), new FadeInTransition());
				}
			}
			
			/* Durchzuführende Löschungen vornehmen */
			if(!entityRemovalList.isEmpty()) {
				for(Entity e : entityRemovalList) {
					entityList.remove(e);
				}
			}
			if(!towerRemovalList.isEmpty()) {
				for(Tower t : towerRemovalList) {
					towerList.remove(t);
				}
			}
			
			/* Update Methoden aller Entities (Gegner) und Türme aufrufen */
			for(Tower tower : towerList) {
				tower.update(gc, delta);
			}
			for(Entity entity : entityList) {
				entity.update(gc, delta);
			}
			
			int mouseX = Mouse.getX();
			int mouseY = 800-Mouse.getY();
			
			/* Wenn gerade ein Tower platziert wird, bewege ihn am Mauszeiger entlang */
			if(buyTower != null) {
				if(mouseX < 1056 && mouseY < 720) {
					int tileposx = (int)Math.floor((mouseX)/48);
					int tileposy = (int)Math.floor((mouseY)/48);
					
					/* Bewege den Tower am Mauszeiger entlang */
					int towerPosX = tileposx*48+24;
					int towerPosY = tileposy*48+24;				
					buyTower.setPosition(new Point(towerPosX,towerPosY));
				}
			}
			
			/* Wenn Linke Maustaste geklickt */
			if(input.isMousePressed(0)) {
				
				/* Wenn sich der Mauszeiger innerhalb der Karte befindet */
				if(mouseX < 1056 && mouseY < 720) {
					
					int tileposx = (int)Math.floor((mouseX)/48);
					int tileposy = (int)Math.floor((mouseY)/48);
					
					/* Wenn gerade ein Tower platziert wird */
					if(buyTower != null) {

						/* Wenn dort noch kein Tower steht */
						if(!map.isTower(tileposx, tileposy)) {
							
							/* Wenn der Spieler genug Geld hat */			
							if(buyTower.getCost() <= gold) {
								
								/* Überprüfe, ob es nach dem Setzen des Towers noch einen Pfad von jedem Spawnpunkt zur Basis gäbe */
								boolean blocking = false;
								Point base = map.getBase();
								for(Point spawn : map.getSpawnList()) {
									if(buyTowerPathfinder.findPath(null,(int)spawn.getX(),(int)spawn.getY(),(int)base.getX(),(int)base.getY()) == null) {
										blocking = true;
										break;
									/* Der Pathfinder überprüft nicht den Spawn an sich. Überprüfe also, ob der Spieler auf einem Spawn bauen will */
									} else if(tileposx == spawn.getX() && tileposy == spawn.getY()) {
										blocking = true;
									}
								}
								
								/* Wenn noch Pfade vorhanden sind */
								if(!blocking) {
									/* Ziehe Geld ab und platziere den Tower */
									gold -= buyTower.getCost();
									towerList.add(buyTower);
									/* Füge den Tower der Kollisionsliste hinzu */
									map.setTower(tileposx, tileposy, true);
									
									/* Deselektiere den gekauften Tower */
									selectedTower = null;
									
									/* Wenn Shift gehalten wird, dann ermögliche dem Spieler, weitere Tower zu bauen */
									if(input.isKeyDown(Input.KEY_LSHIFT)) {
										if(buyTower instanceof NormalTower) {			
											buyTower = new NormalTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof GroundTower) {
											buyTower = new GroundTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof AirTower) {
											buyTower = new AirTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof SlowTower) {
											buyTower = new SlowTower(new Point(-1000,-1000), Game.this);
											
										}
										
										/* Selektiere den neuen Tower */
										selectedTower = buyTower;
									} else {
										buyTower = null;
									}		
								}
							}
						}
						
					/* Wenn auf einen Tower geklickt wurde */
					} else if(map.isTower(tileposx, tileposy)) {
						Tower clickedTower = null;
						
						Point clickedTilePos = new Point(tileposx, tileposy);
						for(Tower t: towerList) {
							Point towerTilePos = t.getTilePosition();
							if(clickedTilePos.getX() == towerTilePos.getX() && clickedTilePos.getY() == towerTilePos.getY()) {
								clickedTower = t;
							}
						}
						
						/* Selektiere den Tower */
						selectedTower = clickedTower;
						
					} else {
						/* Deselektiere den aktuell selektierten Tower */
						selectedTower = null;
					}
				}
			}
		}
		
		/* Wenn ESC Taste gedrückt */
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			if(buyTower != null) {
				buyTower = null;
				selectedTower = null;
			} else {
				pause = !pause;
			}
		}
	}

	/**
	 * Haupt Rendermethode der GameEngine.
	 * Stellt die Spiel UI dar und führt außerdem 
	 * alle weiteren Render-Methoden von Türmen und Gegnern aus.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		/* Karte darstellen */
		map.getMap().render(0,0);
		
		/* Sidebar darstellen */
		sidebar_background.draw(0,0);
		
		/* Gold darstellen */
		LatoFont.draw(66,763,gold+"",22);
		
		/* Restliche Leben darstellen */
		LatoFont.draw(215,763,currentHealth+"/"+baseHealth,22);
		
		/* Aktuelle Wave darstellen */
		LatoFont.draw(960,763,"Wave "+wave+"/"+map.getWaveList().size(),22);
		
		/* NextWave Button darstellen */
		button_startWave.render(gc,g);
		
		/* Infos für nächste Wave darstellen */	
		Integer[] nextWave = map.getWaveUnits(wave+1);
		if(nextWave != null) {
			LatoFont.draw(1105,660,"Next Wave:",14);
			LatoFont.draw(1105,680,"Ground: "+nextWave[0],14);
			LatoFont.draw(1105,700,"Air: "+nextWave[1],14);
			LatoFont.draw(1105,720,"Boss: "+nextWave[2],14);
		} else {
			LatoFont.draw(1105,720,"Last Wave!",14);
		}
		
		/* Tower Buttons darstellen */
		button_normalTower.render(gc,g);
		button_groundTower.render(gc,g);
		button_airTower.render(gc,g);
		button_slowTower.render(gc,g);
		
		
		if(selectedTower != null) {
			/* Reichweite des selektierten Towers zeichnen */
			Point position = selectedTower.getPosition();
			Circle rangeCircle = new Circle(position.getX(),position.getY(),selectedTower.getRange());
			
			g.setColor(selectedFill);
			g.fill(rangeCircle);
			
			g.setColor(selectedRing);
			g.draw(rangeCircle);
			
			
			/* Info Menü */
			info.draw(1100,180);

			if(selectedTower instanceof ShootingTower) {
				ShootingTower shTower = (ShootingTower) selectedTower;
				LatoFont.draw(1105, 220, "Damage: " + shTower.getDamage(), 14);
				LatoFont.draw(1105, 240, "Range: " + shTower.getRange(), 14);
				LatoFont.draw(1105, 260, "Atk Speed: " + Math.round(1000f/shTower.getShootInterval()*100)/100f, 14);
				LatoFont.draw(1105, 280, "Kills: " + shTower.getKillCount(), 14);
				if(selectedTower == buyTower)
					LatoFont.draw(1105, 300, "Cost: " + buyTower.getCost(), 14);
			} else if(selectedTower instanceof SlowTower) {
				SlowTower slowTower = (SlowTower) selectedTower;
				LatoFont.draw(1105, 220, "Range: "+slowTower.getRange(), 14);
				LatoFont.draw(1105, 240, "Slow Value: "+(int)((1-slowTower.getSlowValue())*100)+"%", 14);
				if(selectedTower == buyTower)
					LatoFont.draw(1105, 260, "Cost: " + buyTower.getCost(), 14);
			}
			
			if(selectedTower != buyTower && selectedTower != null) {
				if(selectedTower.getUpgradeLevel() < 2) {
					LatoFont.draw(1170, 339, selectedTower.getUpgradeCost()+"", 22);
					button_upgrade.render(gc, g);
				}
				button_sell.render(gc, g);		
			}
		}
		
		/* Wenn wir gerade einen Tower kaufen, dann rendere ihn (am Mauszeiger) */
		if(buyTower != null) {
			buyTower.render(gc, g);			
		}
		
		/* Render Methoden aller Entities (Gegner) und Türme aufrufen */
		for(Tower tower : towerList) {
			tower.render(gc, g);
		}
		for(Entity entity : entityList) {
			entity.render(gc, g);
		}
		
		/* Pause Menü rendern, wenn nötig */
		if(pause) {
			g.setColor(new Color(0, 15, 35, 198));
			g.fillRect(0,0,1280,800);
			
			LatoFont.draw(455, 233, "Do you really want to quit?", 32);
			button_quitGame.render(gc, g);
			button_cancel.render(gc, g);
		}
	}
	
	@Override
	public int getID() {
		return 3;
	}
	
	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Aktualisiert die Anzahl der Leben des Spielers
	 * und beendet das Spiel, wenn diese auf 0 sinken.
	 * @param health Die neue Lebensanzahl des Spielers
	 */
	public void setHealth(int health) {
		if(health > 0) {
			currentHealth = health;
		} else {
			/* Verloren */
			lost = true;
		}
	}
	
	/**
	 * Merke einen Turm zum Löschen vor.
	 * Um Concurrent Modifications zu vermeiden, wird der Turm erst zu Anfang des nächsten Updatezyklus gelöscht.
	 * @param tower Der zu löschende Turm
	 */
	public void removeTower(Tower tower) {
		towerRemovalList.add(tower);
	}
	
	/**
	 * Merke eine Einheit zum Löschen vor.
	 * Um Concurrent Modifications zu vermeiden, wird die Einheit erst zu Anfang des nächsten Updatezyklus gelöscht.
	 * Gleichzeitig werden alle Türme, die die Einheit als Ziel hatten angewiesen, ihr Ziel zu ändern.
	 * @param entity Die zu löschende Einheit
	 */
	public void removeEntity(Entity entity) {
		/* Merke Einheit zum Löschen vor (um ConcurrentModification während des Entity Updates zu umgehen) */
		entityRemovalList.add(entity);
		
		/* Benachrichtigt alle Türme, die dieses Ziel anvisiert hatten, dass sie sich ein neues suchen sollen */
		for(Tower t : towerList) {
			if(t instanceof ShootingTower) {
				ShootingTower st = (ShootingTower) t;
				if(st.getTarget() == entity) {
					st.clearTarget();
				}
			}
		}
	}
}
