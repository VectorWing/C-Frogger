package vectorwing.games.test.cfrogger.object;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A Sprite that moves along a grid, and can understand movement in grid coordinates.
 * If tile_width and tile_height are not specified, it will assume 16x16.
 */
public abstract class GridSprite extends Sprite
{
	protected int tile_width;
	protected int tile_height;

	public GridSprite()
	{
		this.tile_width = 16;
		this.tile_height = 16;
	}

	/** Process the object's logic for one tick. */
	public abstract void tick(float delta_time);

	/** Resets everything in this GridSprite to starting values. */
	public abstract void reset();

	/** Moves the sprite to a given coordinate in the grid, instead of pixel coordinates. */
	public void setGridPosition(int gridX, int gridY)
	{
		this.setPosition(gridX * tile_width, gridY * tile_height);
	}

	/** Nudges the sprite by the specified number of tiles in the X axis. */
	public void translateGridX(int moveX)
	{
		this.translateX(moveX * tile_width);
	}

	/** Nudges the sprite by the specified number of tiles in the X axis. */
	public void translateGridY(int moveY)
	{
		this.translateY(moveY * tile_width);
	}
}
