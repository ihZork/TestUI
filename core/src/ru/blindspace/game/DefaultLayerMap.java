package ru.blindspace.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by ihzork on 28.03.18.
 */

public class DefaultLayerMap {

    ArrayList<HexGridCell> cells;


    public DefaultLayerMap() {
        cells = new ArrayList<HexGridCell>();

    }

    public ArrayList<HexGridCell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<HexGridCell> cells) {
        this.cells = cells;
    }
    public void addCell(HexGridCell cell)
    {
        cells.add(cell);
    }
}
