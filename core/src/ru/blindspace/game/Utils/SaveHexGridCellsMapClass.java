package ru.blindspace.game.Utils;

import java.util.ArrayList;

/**
 * Created by ihzork on 27.03.18.
 */

public class SaveHexGridCellsMapClass {

    private  ArrayList<HexGridCellsMapClass> hexGridCells;

    public SaveHexGridCellsMapClass() {
        hexGridCells = new ArrayList<HexGridCellsMapClass>();
    }
    public void add(HexGridCellsMapClass a)
    {
        hexGridCells.add(a);
    }

    public ArrayList<HexGridCellsMapClass> getHexGridCells() {
        return hexGridCells;
    }

    public void setHexGridCells(ArrayList<HexGridCellsMapClass> hexGridCells) {
        this.hexGridCells = hexGridCells;
    }
}
