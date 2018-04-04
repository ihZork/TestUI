package ru.blindspace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

import static com.badlogic.gdx.math.Intersector.intersectRayTriangles;

/**
 * Created by ihzork on 15.12.17.
 */

public class MapTile {
    float[] vertices;
    short[] indices;
    Vector3 center;
    Vector3 normal;
    int vertexSize =0;
    Vector3 best;
    BoundingBox b;
    int index;
    public final Vector3 mmin = new Vector3();
    public final Vector3 mmax = new Vector3();
    ArrayList<Vector3> points;
    ArrayList<Vector3> t;
    ShapeRenderer shapeRenderer;

    float sx,sy,sz;

    Matrix4 mat;

    public MapTile(int _index, float[] _vertices, int vertexSize, Matrix4 _mat) {
        shapeRenderer = new ShapeRenderer();
        this.index = _index;
        mat = _mat;
        b = new BoundingBox();
        //Gdx.app.log("tankGenerator log ", "size"+_vertices.length);
        vertices = new float[_vertices.length];

        vertices = _vertices;
        t =new ArrayList<Vector3>();

/*
        for(int i =0;i<_vertices.length;i++) {
            vertices[i] = _vertices[i];
            Gdx.app.log("tankGenerator log ", i+ " x " + vertices[i]+" y " + vertices[i+1]+" z " + vertices[i+2] );
        }*/
        points= new ArrayList<Vector3>();
        for(int i =0;i<vertices.length-3;i+=3) {
            sx+=vertices[i];
            sy+=vertices[i+1];
            sz+=vertices[i+2];
            Vector3 tmp = new Vector3(vertices[i],vertices[i+1],vertices[i+2]);
           /* Gdx.app.log("tankGenerator log ", " mul "+i + tmp);
            tmp.mul(mat);
            Gdx.app.log("tankGenerator log ", "--> "+i + tmp);*/
            t.add(tmp);
            ext (vertices[i],vertices[i+1],vertices[i+2]);
            //points.add(new Vector3(vertices[i],vertices[i+1],vertices[i+2]));

        }
        center = new Vector3(sx/vertices.length,sy/vertices.length,sz/vertices.length);

        points.add(mmax);
        points.add(mmin);
       // Gdx.app.log("tankGenerator log ", _index+" -- "+center);
        //Gdx.app.log("tankGenerator log ", _index+" -- "+mmin + " "+ mmax);
        //b.set(points);

        //Gdx.app.log("tankGenerator log ", index+ " "+  b.toString());
        //b.getCenter(center);
        //setCenter();
        best = new Vector3();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void ext (float x, float y, float z) {
        mmin.set(min(mmin.x, x), min(mmin.y, y), min(mmin.z, z));
        mmax.set(max(mmax.x, x),max(mmax.y, y), max(mmax.z, z));
        //return this.set();
    }
    public Vector3 intersect(Ray collisionRay)
    {

        if(intersectRayTriangles(collisionRay, vertices ,best))
        {
            //debug();
        }else
        {
            best.setZero();
        }
        return best;
    }
    float max ( float a, float b) {
        return a > b ? a : b;
    }
    float min (float a, float b) {
        return a > b ? b : a;
    }

    /*public Vector3 getMin (final Vector3 out) {
        return out.set(min);
    }*/

    /*public Vector3 getMax (final Vector3 out) {
        return out.set(max);
    }*/


    public Vector3 getCenter() {

        return this.center;
    }

    public void setCenter() {
        this.center = new Vector3(mmax.x-mmin.x,mmax.y-mmin.y,mmax.z-mmin.z);
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }
    public void debug(Camera camera)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        for(int i=0;i<t.size();i++)
        {
            t.get(i).prj(camera.combined);
            //camera.unproject(camera.project(t.get(i)));
           // camera.unproject(center);
            shapeRenderer.line(t.get(i).x,t.get(i).y, t.get(i).z, center.x,center.y, center.z);
            //shapeRenderer.point(t.get(i).x,t.get(i).y, t.get(i).z);
        }
        /*for(int i=0;i<t.size()-1;i+=1)
        {
            camera.project(t.get(i));
            camera.project(t.get(i+1));

            shapeRenderer.line(t.get(i).x,t.get(i).y, t.get(i).z, t.get(i+1).x,t.get(i+1).y, t.get(i+1).z);
            //shapeRenderer.point(t.get(i).x,t.get(i).y, t.get(i).z);
        }*/

        shapeRenderer.end();
    }
    /*
    public void calculateCenter()
    {
        if(tilePoints.isEmpty()) throw new RuntimeException("tilePoints can not be empty");
        BoundingBox box = new BoundingBox();
        box.set(tilePoints);
        box.getCenter(center);
    }*/


}
