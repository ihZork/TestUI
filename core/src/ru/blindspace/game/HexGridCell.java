package ru.blindspace.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class HexGridCell  {

	public int index;
	public boolean walkable = true;
	public Vector3 worldPosition;
	public Vector2 mapPosition;
	public String 	placedObject;
	public float	orientation;
	public int 	type;
	public float   height; ///?????????????
	ArrayList<TileTriangle> triangles;


	public HexGridCell() {

		triangles = new ArrayList<TileTriangle>(6);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<TileTriangle> getTriangles() {
		return triangles;
	}

	public void setTriangles(ArrayList<TileTriangle> triangles) {
		this.triangles = triangles;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
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

	/*
        public Hexagon getHexagon() {
            return hexagon;
        }

        public void setHexagon(Hexagon hexagon) {
            this.hexagon = hexagon;
        }
    */
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
	public boolean isMyPosition(int x,int y)
	{
		return (x==(int)mapPosition.x && y==(int)mapPosition.y);
	}
	public boolean isMyPosition(Vector2 pos)
	{
		return (pos.x==mapPosition.x && pos.y==mapPosition.y);
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
}