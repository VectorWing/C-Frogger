package vectorwing.games.test.cfrogger.object;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import vectorwing.games.test.cfrogger.assets.GraphicAssets;
import vectorwing.games.test.cfrogger.assets.SoundAssets;

public class PlayerFrogger extends GridSprite
{
	private int jump_delay;
	private int direction;
	private Vector2 center;
	public boolean isAlive;
	public boolean standingOnPlatform;
	private int startGridX;
	private int startGridY;
	private int time_revive;
	private float state_time;

	public PlayerFrogger(int startGridX, int startGridY)
	{
		this.center = new Vector2(0, 0);
		this.setSize(10, 10);
		this.setGridPosition(startGridX, startGridY);
		this.isAlive = true;
		this.standingOnPlatform = false;
		this.startGridX = startGridX;
		this.startGridY = startGridY;
		this.time_revive = 0;
		this.state_time = 0;
		this.setOrigin(8, 8);
	}

	/** Moves frogger to a tile in the grid. Grid tiles are always 16x16. (0, 0) is at the bottom left. */
	@Override
	public void setGridPosition(int gridX, int gridY)
	{
		this.setPosition(gridX * 16 + 3, gridY * 16 + 3);
		this.updateCenter();
	}

	/** Gets frogger's current grid tile. If not aligned, returns null instead. */
	public Vector2 getGridPosition()
	{
		return new Vector2((this.getX() - 3) / 16, (this.getY() - 3) / 16);
	}

	public void updateCenter()
	{
		this.center.set(this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2));
	}

	public Vector2 getCenter()
	{
		return this.center;
	}

	/** Makes the frog jump 'gridX' tiles left or right. */
	public void jumpX(int gridX)
	{
		if (!isAlive) return;

		if (jump_delay == 0) {
			this.translateGridX(gridX);
			this.direction = gridX < 0 ? 90 : 270;
			this.state_time = 0;
		}
		this.jump_delay = 7;
		this.updateCenter();
		SoundAssets.frog_jump.play();
	}

	/** Makes the frog jump 'gridY' tiles up or down. */
	public void jumpY(int gridY)
	{
		if (!isAlive) return;

		if (jump_delay == 0) {
			this.translateGridY(gridY);
			this.direction = gridY < 0 ? 180 : 0;
			this.state_time = 0;
		}
		this.jump_delay = 7;
		this.updateCenter();
		SoundAssets.frog_jump.play();
	}

	/** Moves frogger according to a platform's last translation, as if frogger was riding it.
	 *  Also sets standingOnPlatform to true. */
	public void translateWithPlatform(Platform platform)
	{
		if (!isAlive) return;

		this.standingOnPlatform = true;
		this.translateX(platform.getLastTranslation());
		this.updateCenter();
	}

	/** Calculates the grid tile that frogger would land on after realigning, using a jumpX and jumpY.
	 *  Returns a Vector2 with the tile's coordinates. */
	public Vector2 simulateRealign(int jumpX, int jumpY)
	{
		// Copy frogger's X and Y for simulation
		// simulate a jump using parameters. if 0, no jump happens
		float testX = this.getX() + (jumpX * this.tile_width);
		float testY = this.getY() + (jumpY * this.tile_height);

		// realign frogger to nearest grid tile if not currently snapped to grid
		testX = MathUtils.round(testX / 16) * 16;
		testY = MathUtils.round(testY / 16) * 16;

		// return that tile's coords
		return new Vector2(testX / this.tile_width, testY / this.tile_height);
	}

	/** Updates the frogger's state in the game. */
	public void tick(float delta_time)
	{
		state_time += delta_time;
		if (jump_delay > 0)	{
			jump_delay--;
		}
		if (time_revive > 0) {
			time_revive--;
		}
		this.standingOnPlatform = false;
	}

	@Override
	public void draw(Batch batch)
	{
		if (!isAlive) {
			batch.draw(GraphicAssets.frog_dead, this.getX() - 3, this.getY() - 3, this.getOriginX(), this.getOriginY(),
					16, 16, 1, 1, direction);
		} else {
			batch.draw(GraphicAssets.frog_jump.getKeyFrame(state_time), this.getX() - 3, this.getY() - 3, this.getOriginX(), this.getOriginY(),
					16, 16, 1, 1, direction);
		}
	}

	/** The frog likes to take a while to revive. If you want to respect that before restarting, use this function. */
	public int getRevivalTime()
	{
		return this.time_revive;
	}

	/** This kills the frog. */
	public void die(boolean killedByCar)
	{
		if (!isAlive) return;

		this.isAlive = false;
		this.time_revive = 150;
		if (killedByCar) {
			SoundAssets.frog_dead_car.play();
		} else {
			SoundAssets.frog_dead_world.play();
		}
	}

	public void reset()
	{
		this.isAlive = true;
		this.standingOnPlatform = false;
		this.direction = 0;
		this.setGridPosition(startGridX, startGridY);
	}
}
