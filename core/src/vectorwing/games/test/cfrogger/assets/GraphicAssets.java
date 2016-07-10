package vectorwing.games.test.cfrogger.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * The helper for every graphical asset used in C-Frogger.
 */
public class GraphicAssets implements Disposable
{
	/** The amazing, incredible, OUTSTANDING logo for the game! Just kidding, it's pretty mediocre. */
	public static Texture logo;
	/** The street, the lake and the lily pads. */
	public static Texture background;
	/** Every game object exists in this singular sprite sheet. Refer to the TextureRegions for separate sprites. */
	public static Texture game_sprites;

	// Frogger
	public static Animation frog_jump;
	public static TextureRegion frog_dead;

	// Streets
	public static Animation car_slow;
	public static Animation car_medium;
	public static Animation car_fast;
	public static Animation truck;

	// Lake
	public static TextureRegion log_left;
	public static TiledDrawable log_middle;
	public static TextureRegion log_right;
	public static Animation turtle;
	public static Animation turtle_sink;
	public static Animation turtle_surface;
	public static TextureRegion flag;

	/** Just to short-hand things a bit. */
	public static Texture loadTexture (String file)
	{
		return new Texture(Gdx.files.internal(file));
	}

	public static void load()
	{
		logo = loadTexture("logo.png");
		background = loadTexture("stage.png");

		game_sprites = loadTexture("sheet_game.png");
		frog_jump = new Animation(0.1f,
				new TextureRegion(game_sprites, 16, 0, 16, 16),
				new TextureRegion(game_sprites, 0, 0, 16, 16));
		frog_dead = new TextureRegion(game_sprites, 32, 0, 16, 16);
		car_slow = new Animation(0.2f,
				new TextureRegion(game_sprites, 0, 16, 16, 16),
				new TextureRegion(game_sprites, 16, 16, 16, 16));
		car_medium = new Animation(0.2f,
				new TextureRegion(game_sprites, 0, 32, 16, 16),
				new TextureRegion(game_sprites, 16, 32, 16, 16));
		car_fast = new Animation(0.1f,
				new TextureRegion(game_sprites, 0, 48, 16, 16),
				new TextureRegion(game_sprites, 16, 48, 16, 16));
		truck = new Animation(0.2f,
				new TextureRegion(game_sprites, 0, 64, 32, 16),
				new TextureRegion(game_sprites, 32, 64, 32, 16));
		log_left = new TextureRegion(game_sprites, 32, 48, 16, 16);
		log_middle = new TiledDrawable(new TextureRegion(game_sprites, 48, 48, 16, 16));
		log_right = new TextureRegion(game_sprites, 64, 48, 16, 16);
		turtle = new Animation(0.2f,
				new TextureRegion(game_sprites, 32, 16, 16, 16),
				new TextureRegion(game_sprites, 48, 16, 16, 16),
				new TextureRegion(game_sprites, 64, 16, 16, 16),
				new TextureRegion(game_sprites, 48, 16, 16, 16));
		turtle_sink = new Animation(0.2f,
				new TextureRegion(game_sprites, 32, 32, 16, 16),
				new TextureRegion(game_sprites, 48, 32, 16, 16),
				new TextureRegion(game_sprites, 64, 32, 16, 16),
				new TextureRegion(game_sprites, 80, 32, 16, 16));
		turtle_surface = new Animation(0.2f,
				new TextureRegion(game_sprites, 80, 32, 16, 16),
				new TextureRegion(game_sprites, 64, 32, 16, 16),
				new TextureRegion(game_sprites, 48, 32, 16, 16),
				new TextureRegion(game_sprites, 32, 32, 16, 16));
		flag = new TextureRegion(game_sprites, 64, 64, 16, 16);
	}

	@Override
	public void dispose()
	{
		background.dispose();
		game_sprites.dispose();
	}
}
