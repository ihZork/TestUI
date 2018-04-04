package ru.blindspace.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 * Created by ihzork on 21.12.17.
 */

public class Hexagon {



    Vector2 hexPosition = new Vector2();
    Vector3 center = new Vector3();

    ArrayList<Vector2> directions = new ArrayList<Vector2>();

    ArrayList<TileTriangle> triangles;
   // private Hexagon4 hex4;
    private int index;
    boolean walkable=true;
    int typeBuzy = 0;


    public Hexagon() {
        triangles = new ArrayList<TileTriangle>();

        directions.add(new Vector2(+1,  0));
        directions.add(new Vector2(+1, -1));
        directions.add(new Vector2( 0, -1));
        directions.add(new Vector2(-1,  0));
        directions.add(new Vector2( 0, +1));
        directions.add(new Vector2(+1, +1));


    }
    public void addTriangle(TileTriangle _triangle)
    {
        triangles.add(_triangle);
    }
    public ArrayList<TileTriangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<TileTriangle> triangles) {
        this.triangles = triangles;
    }
    public void setCenter() {
        if (!triangles.isEmpty()) {
            double cx = new BigDecimal(triangles.get(0).getC().x).setScale(1, RoundingMode.UP).doubleValue();
            double cz = new BigDecimal(triangles.get(0).getC().z).setScale(1, RoundingMode.UP).doubleValue();
            center = new Vector3((float)cx,(triangles.get(0).getC().y),(float)cz);
        }
    }


    public Vector3 hexToCube(int hq, int hr)
    {
        float x= hq;
        float z = hr;
        float y =-x-z;
        return new Vector3(x,y,z);
    }

    public Vector3 cubeAdd(Vector3 a,Vector3 b)
    {
        return a.add(b);
    }
    public Vector3 cube_neighbor(Vector3 a, int dir)
    {
        return cubeAdd(a,hexToCube((int)directions.get(dir).x,(int)directions.get(dir).y));
    }

    public Vector2 hex_direction(int direction) {
        return directions.get(direction);
    }
    public Vector2 hex_neighbor(Vector2 hex, int direction) {
        Vector2 dir = hex_direction(direction);
        return new Vector2(hex.x+dir.x,hex.y+dir.y);
    }



    public Vector2 offset_neighbor(Hexagon hex,int direction)
    {
       Vector2 dir = directions.get(direction);
        return new Vector2(hex.getHexPosition().x+dir.x,hex.getHexPosition().y+dir.y);
    }

    public float hex_distance(Hexagon a, Hexagon b) {
        return (Math.abs(a.getHexPosition().x - b.getHexPosition().x)
                + Math.abs(a.getHexPosition().x + a.getHexPosition().y - b.getHexPosition().x - b.getHexPosition().y)
                + Math.abs(a.getHexPosition().y - b.getHexPosition().y)) / 2;
    }


    public int getTypeBuzy() {
        return typeBuzy;
    }

    public void setTypeBuzy(int typeBuzy) {
        this.typeBuzy = typeBuzy;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public Vector3 getCenter()
    {
       return center;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public Vector2 getHexPosition() {
        return hexPosition;
    }


    boolean isPosition(int q,int r)//,float x,float z)
    {
        float tileSizeL = 7.5f;
        float tileSizeH = 8.66f;

        /*double ccx=new BigDecimal(getCenter().x).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        double ccy=new BigDecimal(getCenter().z).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        double cx= (ccx/tileSizeH);
        double cy=  (ccy/tileSizeL);
        ///[Gdx.app.log("tankGenerator log ",  cx+ " "+ cy);
        int xh= new BigDecimal(cx).setScale(1,BigDecimal.ROUND_HALF_UP).intValue();
        int yh= new BigDecimal(cy).setScale(1,BigDecimal.ROUND_HALF_UP).intValue();*/
        float qx= q*tileSizeH;
        float ry = r*tileSizeL;
        float x = (float) (10/2f * Math.sqrt(3f) * (r + q / 2f));
        float z = 10/2f * 3f/2f * q;
        //Gdx.app.log("tankGenerator log ", x + " "+ z + " "+ getCenter().x+ " "+getCenter().z+ " "+ q+ " "+ r);
       // if(xh == q && yh==r)
        //String out = x + " "+ z + " "+ getCenter().x+ " "+getCenter().z+ " "+ q+ " "+ r+"\n";
        if(getCenter().x>=(x-0.2f) && getCenter().x<=(x+0.2f) && getCenter().z>=(z-0.2f) && getCenter().z<=(z+0.2f))
        {
           // Gdx.app.log("tankGenerator log ", xh + " "+ yh);//+ " "+x + " "+ z);

           // tmp2.set(new Vector2(x,y));
            hexPosition=new Vector2(q,r);
            //return hexPosition;
        }
        return false;
    }

    public boolean isMyPosition(int x,int y)
    {
        return (x==(int)hexPosition.x && y==(int)hexPosition.y);
    }


    @Override
    public String toString() {
        String out="";
        if(!triangles.isEmpty())
            for(TileTriangle t: triangles)
            {
                out+=t.getA().toString()+" "+ t.getB().toString()+" "+t.getC().toString()+"\n";
            }
        return out;
    }
}
