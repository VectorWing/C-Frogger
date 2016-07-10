package vectorwing.games.test.cfrogger.object;

/**
 * Cars move either left or right, at set speeds. If outside screen bounds, they wrap.
 */
public class EnemyCar extends GridSprite
{
	public CarType type;
	public boolean facingRight;
	private int speed;
	private int startGridX;
	private int startGridY;
	private int screen_width;

	public float stateTime;

	public enum CarType {
		SLOW, MEDIUM, FAST, TRUCK
	}

	public EnemyCar(CarType type, boolean facingRight, int startGridX, int startGridY, int screen_width)
	{
		this.type = type;
		this.setSize(16, 16);
		this.facingRight = facingRight;
		this.setGridPosition(startGridX, startGridY);
		this.startGridX = startGridX;
		this.startGridY = startGridY;
		this.screen_width = screen_width;
		switch (type) {
			case SLOW:
				this.speed = 50;
				break;
			case MEDIUM:
				this.speed = 70;
				break;
			case FAST:
				this.speed = 120;
				break;
			case TRUCK:
				this.speed = 50;
				this.setSize(32, 16);
				break;
		}
		this.stateTime = 0;
	}


	public void tick(float delta_time)
	{
		this.stateTime += delta_time;
		if (facingRight) {
			this.translateX(speed * delta_time);
		}
		else {
			this.translateX(-speed * delta_time);
		}
		if (this.getX() >= screen_width && facingRight) {
			this.setX(-1 * this.getWidth());
		}
		if (this.getX() + this.getWidth() <= 0 && !facingRight) {
			this.setX(screen_width);
		}
	}

	public void reset()
	{
		this.setGridPosition(startGridX, startGridY);
	}
}
