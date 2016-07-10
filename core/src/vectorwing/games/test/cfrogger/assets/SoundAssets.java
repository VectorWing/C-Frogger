package vectorwing.games.test.cfrogger.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * The helper for every sound and music asset used in C-Frogger.
 */
public class SoundAssets implements Disposable
{
	/** Sound for when Frogger jumps around. */
	public static Sound frog_jump;
	/** Sound for when Frogger gets trampled by a car. */
	public static Sound frog_dead_car;
	/** Sound for Frogger's generic deaths, such as lake and boundaries. */
	public static Sound frog_dead_world;
	/** Sound for landing on a valid lily pad and getting a flag. */
	public static Sound lily_pad;

	public static Sound loadSound (String file)
	{
		return Gdx.audio.newSound(Gdx.files.internal("sfx/" + file));
	}

	public static void load()
	{

		frog_jump = loadSound("frog_jump.wav");
		frog_dead_car = loadSound("frog_dead_car.wav");
		frog_dead_world = loadSound("frog_dead_world.wav");
		lily_pad = loadSound("lily_pad.wav");
	}

	@Override
	public void dispose()
	{
		frog_jump.dispose();
		frog_dead_car.dispose();
		frog_dead_world.dispose();
		lily_pad.dispose();
	}
}
