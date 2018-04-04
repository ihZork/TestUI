package ru.blindspace.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Json;

import ru.blindspace.game.Utils.DataSource;
import ru.blindspace.game.Utils.MonsterFactory;
import ru.blindspace.game.Utils.Planet;

/**
 * Created by ihzork on 24.11.16.
 */

public class LoadScreen implements Screen, InputProcessor {

    public LoadScreen()
    {

/*
        DataSource d = new DataSource();

        d.items=100000;
        d.planets = new Planet[2];


        Planet p = new Planet();
        p.id =0;
        p.name="eeeeee";
        p.distance= new String[2];
        p.distance[0]="eee";
        p.distance[1]="ee1";

        d.planets[0]=p;

        Planet p1 = new Planet();
        p1.id =0;
        p1.name="eeeeee";
        p1.distance= new String[4];
        p1.distance[0]="eee";
        p1.distance[1]="ee1";
        p1.distance[2]="ee2";
        p1.distance[3]="ee3";

        d.planets[1]=p1;

        Json json = new Json();
        json.addClassTag("DataSource", DataSource.class); //As above
        json.toJson(d, Gdx.files.local("data.json"));
*/


                      DataSource  d= DataSource.get();

                        Gdx.app.log("tankGenerator log ", String.valueOf(d.items));
                        for(int i=0;i<d.planets.length;i++)
                        {
                            Gdx.app.log("tankGenerator log ", String.valueOf(d.planets[i].id));
                            Gdx.app.log("tankGenerator log ", String.valueOf(d.planets[i].name));

                            for(int j=0;j<d.planets[i].distance.length;j++)
                            {
                                Gdx.app.log("tankGenerator log ", String.valueOf(d.planets[i].distance[j]));
                            }
                        }
        Gdx.app.log("tankGenerator log getItems : ", String.valueOf(d.getItems()));


    }




    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
