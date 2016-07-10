package vectorwing.games.test.cfrogger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import vectorwing.games.test.cfrogger.assets.GraphicAssets;
import vectorwing.games.test.cfrogger.assets.SoundAssets;
import vectorwing.games.test.cfrogger.screen.GameScreen;
import vectorwing.games.test.cfrogger.screen.MenuScreen;

/**
 * C-Frogger - A simple clone of Frogger
 * Created by Lucas Barcellos (vectorwing) - 2016
 * ----------------------------------------------
 * Frogger is a reflex game where the player must conduct a frog to 5 "houses"
 * across the map, dodging hazards such as traffic, rivers, crocodiles etc.
 */
public class CFrogger extends Game
{
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public BitmapFont font;
	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("font/press start.fnt"), Gdx.files.internal("font/press start.png"), false);
		GraphicAssets.load();
		SoundAssets.load();
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		shape.dispose();
		font.dispose();
	}
}
