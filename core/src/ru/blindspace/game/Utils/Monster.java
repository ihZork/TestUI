package ru.blindspace.game.Utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by ihzork on 24.11.16.
 */
public class Monster {


    public String name;
    public int atk;
    public int def;


    public Monster(String name, int atk, int def) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        Gdx.app.log("tankGenerator log ", this.name+" "+this.atk+" "+ this.def);

    }
}
