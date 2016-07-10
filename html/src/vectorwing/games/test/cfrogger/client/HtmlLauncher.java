package vectorwing.games.test.cfrogger.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import vectorwing.games.test.cfrogger.CFrogger;
import vectorwing.games.test.cfrogger.Settings;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Settings.VIEW_WIDTH, Settings.VIEW_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CFrogger();
        }
}