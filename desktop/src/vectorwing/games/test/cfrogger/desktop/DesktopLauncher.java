package vectorwing.games.test.cfrogger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import vectorwing.games.test.cfrogger.CFrogger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 512;
		config.backgroundFPS = 0;
		new LwjglApplication(new CFrogger(), config);
	}
}
