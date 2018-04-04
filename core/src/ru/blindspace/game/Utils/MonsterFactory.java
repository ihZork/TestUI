package ru.blindspace.game.Utils;

import java.util.ArrayList;

/**
 * Created by ihzork on 24.11.16.
 */

public class MonsterFactory {


    private ArrayList<MonsterPrototype> prototypes;

    public Monster createMonster(int id) {

        MonsterPrototype p = prototypes.get(id);

        Monster m = new Monster(p.name, p.atk, p.def);


        return m;
    }
}
