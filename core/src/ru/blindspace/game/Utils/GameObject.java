package ru.blindspace.game.Utils;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by ihzork on 01.02.18.
 */

public class GameObject {

    ModelInstance gameObjectInstance;
    int id;
    String nameOfObject;

    public GameObject() {
    }

    public boolean isMe(String name)
    {
        return (nameOfObject.equalsIgnoreCase(name));
    }
    public GameObject(ModelInstance gameObjectInstance, int id,String nameOfObject) {
        this.gameObjectInstance = gameObjectInstance;
        this.id = id;
        this.nameOfObject = nameOfObject;
    }

    public ModelInstance getGameObjectInstance() {
        return gameObjectInstance;
    }

    public void setGameObjectInstance(ModelInstance gameObjectInstance) {
        this.gameObjectInstance = gameObjectInstance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfObject() {
        return nameOfObject;
    }

    public void setNameOfObject(String nameOfObject) {
        this.nameOfObject = nameOfObject;
    }
}
