package ru.blindspace.game;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by ihzork on 15.12.17.
 */

public class TileTriangle {

    Vector3 a,b,c;


    public TileTriangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public TileTriangle() {
    }

    public Vector3 getA() {
        return a;
    }

    public void setA(Vector3 a) {
        this.a = a;
    }

    public Vector3 getB() {
        return b;
    }

    public void setB(Vector3 b) {
        this.b = b;
    }

    public Vector3 getC() {
        return c;
    }

    public void setC(Vector3 c) {
        this.c = c;
    }
}
