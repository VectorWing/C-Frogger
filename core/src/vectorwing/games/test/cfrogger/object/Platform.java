package vectorwing.games.test.cfrogger.object;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import vectorwing.games.test.cfrogger.assets.GraphicAssets;

/**
 * An object that can be ridden. It saves its last translation so that other objects may use it to move themselves.
 */
public class Platform extends GridSprite
{
	private float lastTranslation;
	public PlatformType type;
	public boolean facingRight;
	private int speed;
	private int startGridX;
	private int startGridY;
	private int screen_width;
	private int wrap_offset;
	private float state_time;
	public boolean isSurfaced;

	public enum PlatformType {
		LOG, TURTLE, TURTLE_DIVE
	}

	public Platform(PlatformType type, boolean facingRight, int startGridX, int startGridY, int tile_width, int screen_width, int wrap_offset)
	{
		if (tile_width < 2) tile_width = 2;
		this.setSize(tile_width * 16, 16);
		this.lastTranslation = 0;
		this.type = type;
		this.facingRight = facingRight;
		this.speed = 50;
		this.setGridPosition(startGridX, startGridY);
		this.startGridX = startGridX;
		this.startGridY = startGridY;
		this.screen_width = screen_width;
		this.wrap_offset = wrap_offset;
		this.state_time = 0;
		this.isSurfaced = true;
	}

	public Platform setSpeed(int speed)
	{
		if (speed > 200) {
			speed = 200;
		}
		this.speed = speed;
		return this;
	}

	public float getLastTranslation()
	{
		return lastTranslation;
	}

	public void tick(float delta_time)
	{
		state_time += delta_time;
		if (this.type == PlatformType.TURTLE_DIVE) {
			if (this.state_time >= 1.6f && this.state_time <= 2.4f) {
				this.isSurfaced = false;
			} else {
				this.isSurfaced = true;
			}
			if (this.state_time >= 3.2f) {
				this.state_time = 0;
			}
		}
		if (facingRight) {
			this.translateX(speed * delta_time);
			lastTranslation = speed * delta_time;
		}
		else {
			this.translateX(-speed * delta_time);
			lastTranslation = -speed * delta_time;
		}
		if (this.getX() >= screen_width + this.getWidth() - this.wrap_offset && facingRight) {
			this.setX(-1 * this.getWidth());
		}
		if (this.getX() + this.getWidth() + this.wrap_offset <= 0 && !facingRight) {
			this.setX(screen_width);
		}
	}

	@Override
	public void draw(Batch batch)
	{
		TiledDrawable turtle;
		switch (type) {
			case LOG:
				batch.draw(GraphicAssets.log_left, this.getX(), this.getY());
				GraphicAssets.log_middle.draw(batch,
						this.getX() + tile_width,
						this.getY(),
						(this.getWidth() / tile_width - 2) * tile_width,
						tile_height);
				batch.draw(GraphicAssets.log_right, this.getX() + this.getWidth() - tile_width, this.getY());
				break;
			case TURTLE:
				turtle = new TiledDrawable(GraphicAssets.turtle.getKeyFrame(state_time, true));
				turtle.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
				break;
			case TURTLE_DIVE:
				if (this.state_time >= 0.8f && this.state_time <= 1.6f) {
					turtle = new TiledDrawable(GraphicAssets.turtle_sink.getKeyFrame(state_time, true));
				} else if (this.state_time >= 2.4f && this.state_time <= 3.2f) {
					turtle = new TiledDrawable(GraphicAssets.turtle_surface.getKeyFrame(state_time, true));
				} else {
					turtle = new TiledDrawable(GraphicAssets.turtle.getKeyFrame(state_time, true));
				}
				if (this.isSurfaced) {
					turtle.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
				}
				break;
		}
	}

	public void reset()
	{
		this.setGridPosition(startGridX, startGridY);
	}
}
