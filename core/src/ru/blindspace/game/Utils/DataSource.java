package ru.blindspace.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by ihzork on 24.11.16.
 */

public class DataSource {

    private static DataSource d = null;
    public static DataSource get() {
        if(d == null) {
            d = DataSource.newInstance();
        }
        return d;
    }



    public Planet[] planets;
    public int items;


    public static Planet getPlanet(String id) {
        return get().planets[Integer.parseInt(id)];
    }

    public int getItems()
    {
        return get().items;
    }
    public DataSource() {}

    public static DataSource newInstance() {
        Json json = new Json();
        DataSource ds = json.fromJson(DataSource.class, Gdx.files.internal("data.json"));

        return ds;
    }
}
