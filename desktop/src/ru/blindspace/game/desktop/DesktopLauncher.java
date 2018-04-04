package ru.blindspace.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.blindspace.game.testUIGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "my-gdx-game";
		config.useGL30 = false;
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new testUIGame(), config);
	}
}
