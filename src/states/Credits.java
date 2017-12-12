package states;

import main.ImageManager;
import misc.LatoFont;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Repräsentiert und rendert den Credits-Bildschirm.
 * 
 */
public class Credits extends BasicGameState {
	Image background;
	Color lightCyan;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = ImageManager.getImage(ImageManager.MENU_BACKGROUND);
		lightCyan = Color.decode("#87CEFA");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0,0);
		LatoFont.draw(576,100,"Credits",32,lightCyan);
		LatoFont.draw(554,250,"Raffael Balthasar",22);
		LatoFont.draw(564,320,"Daniel Bannert",22);
		LatoFont.draw(571,390,"Jannis Becker",22);
		LatoFont.draw(542,460,"Thomas Skowronek",22);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			sbg.enterState(1,new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return 5;
	}
}
