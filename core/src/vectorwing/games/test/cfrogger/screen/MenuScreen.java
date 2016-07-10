package vectorwing.games.test.cfrogger.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import vectorwing.games.test.cfrogger.CFrogger;
import vectorwing.games.test.cfrogger.Settings;
import vectorwing.games.test.cfrogger.assets.GraphicAssets;

public class MenuScreen implements Screen
{
	public final CFrogger game;

	private OrthographicCamera camera;

	public int fx_text_enter;

	public MenuScreen(CFrogger game)
	{
		this.game = game;
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
	}

	@Override
	public void render(float delta)
	{
		fx_text_enter++;

		if (fx_text_enter > 60) {
			fx_text_enter = 0;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			game.setScreen(new GameScreen(this.game));
		}

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.shape.setProjectionMatrix(camera.combined);

		game.batch.disableBlending();
		game.batch.begin();
		game.batch.draw(GraphicAssets.background, 0, 0);
		game.batch.end();

		game.shape.begin(ShapeRenderer.ShapeType.Filled);
		game.shape.setColor(Color.BLACK);
		game.shape.rect(16, 64, 208, 48);
		game.shape.end();

		game.batch.enableBlending();
		game.batch.begin();
		game.batch.draw(GraphicAssets.logo, Settings.GAME_WIDTH / 2 - GraphicAssets.logo.getWidth() / 2, 170);
		String buffer = "Made by .vectorwing";
		game.font.draw(game.batch, buffer, getTextCenter(game.font, buffer), 170);
		buffer = "This is a simple clone of";
		game.font.draw(game.batch, buffer, getTextCenter(game.font, buffer), 108);
		buffer = "the game Frogger.";
		game.font.draw(game.batch, buffer, getTextCenter(game.font, buffer), 98);
		buffer = "Use ARROW KEYS to move.";
		game.font.draw(game.batch, buffer, getTextCenter(game.font, buffer), 78);
		buffer = "Press ENTER to play!";
		if (fx_text_enter <= 30) {
			game.font.draw(game.batch, buffer, getTextCenter(game.font, buffer), 20);
		}
		game.batch.end();
	}

	/** Given a font and a string, returns the X coordinate to use in order to center the text on the viewport. */
	public float getTextCenter(BitmapFont font, CharSequence text)
	{
		float text_width = new GlyphLayout(font, text).width;
		return (Settings.GAME_WIDTH / 2) - (text_width / 2);
	}

	@Override
	public void show(){}

	@Override
	public void resize(int width, int height){}

	@Override
	public void pause(){}

	@Override
	public void resume(){}

	@Override
	public void hide(){}

	@Override
	public void dispose(){}
}
