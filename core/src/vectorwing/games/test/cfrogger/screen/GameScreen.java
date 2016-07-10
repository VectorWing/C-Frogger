package vectorwing.games.test.cfrogger.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import vectorwing.games.test.cfrogger.CFrogger;
import vectorwing.games.test.cfrogger.Settings;
import vectorwing.games.test.cfrogger.assets.GraphicAssets;
import vectorwing.games.test.cfrogger.assets.SoundAssets;
import vectorwing.games.test.cfrogger.object.EnemyCar;
import vectorwing.games.test.cfrogger.object.Platform;
import vectorwing.games.test.cfrogger.object.PlayerFrogger;

/**
 * The main stage for C-Frogger. Player, obstacles and objectives, all that fluff.
 */
public class GameScreen implements Screen
{
	public final CFrogger game;

	private OrthographicCamera camera;

	// Objects
	private PlayerFrogger frogger;
	private Array<EnemyCar> cars;
	private Array<Platform> lake;

	// Logic
	private boolean[] lily_pads = new boolean[] {false, false, false, false, false};
	private Array<Vector2> lily_pad_tiles;
	private int lives;
	private int score;
	private float time_left;
	private int max_time;
	private int frog_height;

	// Regions
	private final int lake_bottom = 144;
	private final int lake_top = 224;

	public GameScreen(CFrogger game)
	{
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

		frogger = new PlayerFrogger(7, 2);
		cars = new Array<EnemyCar>();
		lake = new Array<Platform>();

		lives = 3;
		score = 0;
		max_time = 20;
		time_left = max_time;
		frog_height = 2;

		lily_pad_tiles = new Array<Vector2>();
		lily_pad_tiles.add(new Vector2(1, 14));
		lily_pad_tiles.add(new Vector2(4, 14));
		lily_pad_tiles.add(new Vector2(7, 14));
		lily_pad_tiles.add(new Vector2(10, 14));
		lily_pad_tiles.add(new Vector2(13, 14));

		this.playLevel(1);
	}

	/** Runs a single logic tick for the screen. */
	public void run()
	{
		// Step 0: Get the gosh darn delta time! And don't let it go above 60 FPS either.
		float delta_time = Gdx.graphics.getDeltaTime();
		if (delta_time > 0.017) {
			delta_time = 0.017f;
		}

		// Step 0.5: If Frogger is dead, give it some time to respawn.
		if (!frogger.isAlive && frogger.getRevivalTime() == 0) {
			if (this.lives == 0) {
				game.setScreen(new MenuScreen(this.game));
				this.dispose();
			} else {
				this.restart();
			}
		}

		// Step 0.5.1 beta: If Frogger entered all 5 lily pads, victory!
		boolean victory = true;
		for (boolean cleared : lily_pads) {
			if (!cleared) victory = false;
		}
		if (victory) {
			score += Settings.SCORE_VICTORY;
			game.setScreen(new MenuScreen(this.game));
			this.dispose();
		}

		// Step 1: Frogger jumps somewhere.
		frogger.tick(delta_time);
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			// Frogger can enter lily pads, but not the surrounding walls, or occupied lily pads.
			if (frogger.getGridPosition().y == 13) {
				Vector2 destination = frogger.simulateRealign(0, 1);
				int i = 0;
				for (Vector2 tile : lily_pad_tiles) {
					if (destination.equals(tile) && !lily_pads[i]) {
						lily_pads[i] = true;
						frogger.jumpY(1);
						frogger.reset();
						frog_height = 2;
						score += Settings.SCORE_LILY_PAD;
						score += Settings.SCORE_TIME_SECOND * MathUtils.round(time_left);
						time_left = max_time;
						SoundAssets.lily_pad.play();
						break;
					}
					i++;
				}
			} else {
				frogger.jumpY(1);
				if (frogger.getGridPosition().y == frog_height + 1) {
					frog_height++;
					score += Settings.SCORE_LEAP;
				}
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && frogger.getY() > 48) {
			frogger.jumpY(-1);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			frogger.jumpX(-1);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			frogger.jumpX(1);
		}

		// Step 2: Obstacles and platforms move.
		for (EnemyCar car : cars)
		{
			car.tick(delta_time);
			if (frogger.isAlive && car.getBoundingRectangle().overlaps(frogger.getBoundingRectangle()))
			{
				frogger.die(true);
				if (lives > 0) lives--;
			}
		}
		for (Platform plat : lake)
		{
			plat.tick(delta_time);
			if (plat.isSurfaced && plat.getBoundingRectangle().contains(frogger.getCenter()))
			{
				frogger.translateWithPlatform(plat);
			}
		}

		// Step 3: Verify if Frogger is riding a platform or not, and its related conditions.
		if (!frogger.standingOnPlatform) {
			if (frogger.isAlive && frogger.getY() >= lake_bottom && frogger.getY() <= lake_top){
				frogger.die(false);
				if (lives > 0) lives--;
			}
			else if ((frogger.getX() - 3) % 16 != 0){
				float alignedX = MathUtils.round((frogger.getX() / 16)) * 16;
				frogger.setPosition(alignedX + 3, frogger.getY());
				frogger.updateCenter();
			}
		}

		// Step 4: Frogger has to be within bounds! It dies at left and right sides, and cannot pass up and down sides.
		if (frogger.getX() + frogger.getWidth() < 0 || frogger.getX() > Settings.GAME_WIDTH) {
			if (frogger.isAlive) {
				frogger.die(false);
				if (lives > 0) lives--;
			}
		}

		// Step 5: The clock is ticking, gentlemen.
		if (time_left > 0 && frogger.isAlive) time_left -= delta_time;
		if (time_left <= 0) {
			frogger.die(false);
			if (lives > 0) lives--;
		}
	}

	/** Sets everything to the indicated level. */
	public void playLevel(int level)
	{
		switch (level)
		{
			case 1:
				// Lane 1 (bottom)
				for (int i = 8; i < 17; i += 3) {
					cars.add(new EnemyCar(EnemyCar.CarType.SLOW, true, i, 3, Settings.GAME_WIDTH));
				}
				// Lane 2
				for (int i = 0; i < 15; i += 4) {
					cars.add(new EnemyCar(EnemyCar.CarType.MEDIUM, false, i, 4, Settings.GAME_WIDTH));
				}
				// Lane 3 (middle)
				cars.add(new EnemyCar(EnemyCar.CarType.FAST, true, 7, 5, Settings.GAME_WIDTH));
				// Lane 4
				for (int i = 1; i <= 4; i += 2) {
					cars.add(new EnemyCar(EnemyCar.CarType.SLOW, false, i, 6, Settings.GAME_WIDTH));
				}
				for (int i = 9; i <= 12; i += 2) {
					cars.add(new EnemyCar(EnemyCar.CarType.SLOW, false, i, 6, Settings.GAME_WIDTH));
				}
				// Lane 5 (top, truck)
				cars.add(new EnemyCar(EnemyCar.CarType.TRUCK, true, 0, 7, Settings.GAME_WIDTH));
				cars.add(new EnemyCar(EnemyCar.CarType.TRUCK, true, 3, 7, Settings.GAME_WIDTH));

				// River 1 (bottom)
				for (int i = 0; i < 20; i += 8) {
					lake.add(new Platform(Platform.PlatformType.TURTLE, false, i, 9, 3, Settings.GAME_WIDTH, 32));
				}
				for (int i = 4; i < 20; i += 8) {
					lake.add(new Platform(Platform.PlatformType.TURTLE_DIVE, false, i, 9, 3, Settings.GAME_WIDTH, 32));
				}
				// River 2
				for (int i = 0; i < 15; i += 5) {
					lake.add(new Platform(Platform.PlatformType.LOG, true, i, 10, 3, Settings.GAME_WIDTH, 32));
				}
				// River 3 (middle)
				for (int i = 0; i < 24; i += 8) {
					lake.add(new Platform(Platform.PlatformType.LOG, true, i, 11, 6, Settings.GAME_WIDTH, 48).setSpeed(100));
				}
				// River 4
				for (int i = 0; i < 12; i += 3) {
					lake.add(new Platform(Platform.PlatformType.TURTLE, false, i, 12, 2, Settings.GAME_WIDTH, 0));
				}
				// River 5 (top)
				for (int i = 2; i < 20; i += 6) {
					lake.add(new Platform(Platform.PlatformType.LOG, true, i, 13, 4, Settings.GAME_WIDTH, 48));
				}
				break;
		}
	}

	@Override
	public void render(float delta)
	{
		this.run();

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.shape.setProjectionMatrix(camera.combined);

		game.batch.disableBlending();
		game.batch.begin();
		game.batch.draw(GraphicAssets.background, 0, 0);
		game.batch.end();

		game.batch.enableBlending();
		game.batch.begin();
		for (Platform plat : lake) {
			plat.draw(game.batch);
		}
		frogger.draw(game.batch);
		for (EnemyCar car : cars) {
			TextureRegion keyframe;
			float side = car.facingRight ? 1 : -1;
			int car_width = 16;
			switch (car.type)
			{
				case SLOW:
					keyframe = GraphicAssets.car_slow.getKeyFrame(car.stateTime, true);
					break;
				case MEDIUM:
					keyframe = GraphicAssets.car_medium.getKeyFrame(car.stateTime, true);
					break;
				case FAST:
					keyframe = GraphicAssets.car_fast.getKeyFrame(car.stateTime, true);
					break;
				case TRUCK:
					keyframe = GraphicAssets.truck.getKeyFrame(car.stateTime, true);
					car_width = 32;
					break;
				default:
					keyframe = new TextureRegion();
					break;
			}
			if (side < 0)
				game.batch.draw(keyframe, MathUtils.ceil(car.getX() + car_width), car.getY(), side * car_width, 16);
			else
				game.batch.draw(keyframe, MathUtils.ceil(car.getX()), car.getY(), side * car_width, 16);
		}

		// VICTORY FLAGS //
		for (int i = 0; i < lily_pad_tiles.size; i++) {
			if (!lily_pads[i]) {
				continue;
			}
			float flagX = lily_pad_tiles.get(i).x * 16;
			float flagY = lily_pad_tiles.get(i).y * 16;
			game.batch.draw(GraphicAssets.flag, flagX, flagY);
		}

		// USER INTERFACE //
		StringBuilder score_string = new StringBuilder("SCORE: ");
		score_string.append(score);
		game.font.draw(game.batch, score_string.toString(), 8, 16);
		game.font.draw(game.batch, score_string.toString(), 8, 16);
		TiledDrawable ui_lives = new TiledDrawable(GraphicAssets.frog_jump.getKeyFrame(1));
		ui_lives.draw(game.batch, Settings.GAME_WIDTH - (lives * 16 + 8), 4, lives * 16, 16);
		game.batch.end();

		game.shape.begin(ShapeRenderer.ShapeType.Filled);
		game.shape.rect(8, 20, Math.round(224 * (time_left/max_time)), 4, Color.RED, Color.GREEN, Color.GREEN, Color.RED);
		game.shape.end();
	}

	public void restart()
	{
		frogger.reset();
		frog_height = 2;
		time_left = max_time;

		for (EnemyCar car : cars) {
			car.reset();
		}
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
