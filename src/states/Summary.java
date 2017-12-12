package states;

import main.ImageManager;
import misc.LatoFont;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import towers.ShootingTower;
import towers.Tower;

/**
 * Repräsentiert und rendert den Sieg/Niederlage Bildschirm,
 * welcher eine Zusammenfassung des Spiels zeigt.
 *
 */
public class Summary extends BasicGameState {

	private Game game;
	private boolean won;
	
	private Image background;
	private MouseOverArea retryButton, quitButton;
	
	public Summary(Game game, boolean won) {
		this.game = game;
		this.won = won;
	}
	
	@Override
	public void init(final GameContainer gc, final StateBasedGame sbg) throws SlickException {
		background = ImageManager.getImage(ImageManager.MENU_BACKGROUND);
		
		retryButton = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_RETRY),400,345);
		quitButton = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_QUITGAME),700,345);
		
		retryButton.addListener(new ComponentListener() {
			public void componentActivated(AbstractComponent cmp) {
				game.getMap().resetTowerList();
				
				try {
					game = new Game(game.getMap());
					game.init(gc, sbg);
					sbg.addState(game);
					sbg.enterState(3,new FadeOutTransition(), new FadeInTransition());
				} catch (SlickException e) {
					e.printStackTrace();
				}	
			}	
		});
		
		quitButton.addListener(new ComponentListener() {
			public void componentActivated(AbstractComponent cmp) {
				game.getMap().resetTowerList();
				sbg.enterState(1,new FadeOutTransition(), new FadeInTransition());
			}	
		});
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		/* Hintergrund zeichnen */
		background.draw(0,0);
		
		/* Überschrift zeichnen */
		if(won) {
			LatoFont.draw(450, 150, "Congratulations, you won!", 32);
		} else {
			LatoFont.draw(435, 150, "Unexpected Error: You lost!", 32);
		}
		
		/* Buttons zeichnen */
		quitButton.render(gc, g);
		retryButton.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return 4;
	}
}
