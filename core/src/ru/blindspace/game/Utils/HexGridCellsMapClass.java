package ru.blindspace.game.Utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by ihzork on 27.03.18.
 */

public class HexGridCellsMapClass {

    private int index;
    //private String name;
    private boolean walkable = true;
    private Vector3 worldPosition;
    private Vector2 mapPosition;
    private String 	placedObject;
    private float	orientation;
    private int 	type;
    private float   height; ///?????????????

    public HexGridCellsMapClass() {

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public Vector3 getWorldPosition() {
        return worldPosition;
    }

    public void setWorldPosition(Vector3 worldPosition) {
        this.worldPosition = worldPosition;
    }

    public Vector2 getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(Vector2 mapPosition) {
        this.mapPosition = mapPosition;
    }

    public String getPlacedObject() {
        return placedObject;
    }

    public void setPlacedObject(String placedObject) {
        this.placedObject = placedObject;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
