package ru.blindspace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ru.blindspace.game.Utils.APathFinder;
import ru.blindspace.game.Utils.GridNode;

import static com.badlogic.gdx.utils.JsonWriter.*;

/**
 * Created by ihzork on 15.12.17.
 */

public class LevelMap {
    ModelInstance map;
    //ArrayList<HexGridCell> hexCells;
    ArrayList<Hexagon> hexagons;
    ArrayList<Vector3> points;
    public ArrayList<HexGridCell> hexGridCells;
    float[] vertices;
    short[] indices;
    int vertexSize =0;
    Matrix4 mat;
    public  int radius =11;
    private Hexagon4 hex4;
    private Vector3 centerMap = new Vector3();

    ArrayList<Vector2> directions = new ArrayList<Vector2>();

    public LevelMap(ModelInstance map) {

        this.map = map;
        hexGridCells = new ArrayList<HexGridCell>();

        //map.transform.scale(0.01f,0.01f,0.01f);
/*
        directions.add(new Vector2(+1,  0));
        directions.add(new Vector2(+1, -1));
        directions.add(new Vector2( 0, -1));
        directions.add(new Vector2(-1,  0));
        directions.add(new Vector2( 0, +1));
        directions.add(new Vector2(+1, +1));
*/
        directions.add(new Vector2(+1,  0));
        directions.add(new Vector2(+1, -1));
        directions.add(new Vector2( 0, -1));
        directions.add(new Vector2(-1,  0));
        directions.add(new Vector2( 0, +1));
        directions.add(new Vector2(+1, +1));


    }
    public void constructMap()
    {
        //mat =   map.getNode("testmap1").globalTransform;
        Vector2 mapPosition = new Vector2();
        Vector3 worldPosition = new Vector3();

        for(int i=0;i<map.model.meshes.size;i++)
        {


            vertexSize = map.model.meshes.get(i).getVertexSize() / 4;


            //Gdx.app.log("tankGenerator log ", "vertexSize: " +vertexSize);
            vertices = new float[map.model.meshes.get(i).getNumVertices() * map.model.meshes.get(i).getVertexSize() / 4];
            indices = new short[map.model.meshes.get(i).getNumIndices()];
            map.model.meshes.get(i).getIndices(indices);
            map.model.meshes.get(i).getVertices(vertices);
        }


        ArrayList<TileTriangle> triangles = new ArrayList<TileTriangle>();
        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i] * vertexSize;
            int i2 = indices[i + 1] * vertexSize;
            int i3 = indices[i + 2] * vertexSize;
            Vector3 tmp1 = new Vector3();
            Vector3 tmp2 = new Vector3();
            Vector3 tmp3 = new Vector3();
            tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]);
            tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]);
            tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]);
            triangles.add(new TileTriangle(tmp1,tmp2,tmp3));
        }
        points = new ArrayList<Vector3>();
        hexagons =  new ArrayList<Hexagon>();
        //hexGridCells = new ArrayList<HexGridCell>();
        for(int i=0;i<triangles.size();i+=6)
        {
            Hexagon hexagon = new Hexagon();
            hexagon.addTriangle(triangles.get(i));
            hexagon.addTriangle(triangles.get(i+1));
            hexagon.addTriangle(triangles.get(i+2));
            hexagon.addTriangle(triangles.get(i+3));
            hexagon.addTriangle(triangles.get(i+4));
            hexagon.addTriangle(triangles.get(i+5));
            hexagon.setCenter();

            hexagon.setIndex(hexagons.size());
            hexagons.add(hexagon);
        }
        //points = CoordinateRange(radius);
        String out = "";
        int t=0;


        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {

                //Gdx.app.log("tankGenerator log ",(t++)+" " +q+ " : " +r);
                //Vector3 tmp =GetPositionVector(10f,q,r);
                //points.add(tmp);
                //points.add(new Vector3(q*8.66f,0,r*7.5f));
                for(int i=0;i<hexagons.size();i++)
                {

                  hexagons.get(i).isPosition(q,r);//,tmp.x ,tmp.z);

                }

            }
        }
/*
        PrintWriter outTxt;
        try {
            outTxt = new PrintWriter(new BufferedWriter(new FileWriter("map.txt", false)));
            outTxt.println(out);
            outTxt.close();

        } catch (IOException ex) {

        }

*/
        for(int i=0;i<hexagons.size();i++)
        {


            HexGridCell hexGridCell = new HexGridCell();
            hexGridCell.setMapPosition(hexagons.get(i).hexPosition);
            hexGridCell.setWorldPosition(hexagons.get(i).getCenter());
            hexGridCell.setIndex(hexagons.get(i).getIndex());
            hexGridCell.setWalkable(true);

           /* if(hexagons.get(i).getCenter().y<0.001 && hexagons.get(i).getCenter().y>-0.001)
                hexGridCell.setWalkable(true);
            else
                hexGridCell.setWalkable(false);*/
            // hexGridCell.setHexagon(hexagons.get(i));
            hexGridCells.add(hexGridCell);

            //Gdx.app.log("tankGenerator log ", "hex index "+ hexagons.get(i).getIndex()+" "+hexagons.get(i).hexPosition+" grid "+ hexGridCell.getIndex() + " "+ hexGridCell.getMapPosition());
        }




        centerMap = hexagons.get(hexagons.size()/2).getCenter().scl(new Vector3(0.5f,0f,0.5f));
            //Gdx.app.log("tankGenerator log ", "centerMap : "+centerMap);

    }

    public Vector3 GetPositionVector(float hexWidth,float q,float r)
    {
        //7.5f
        //hexWidth=8.66f;
        float x = (float) (hexWidth/2f * Math.sqrt(3f) * (r + q / 2f));
        float z = hexWidth/2f * 3f/2f * q;
        float y = 0;
        return new Vector3(x, y, z);
    }


    public Vector2 hex_direction(int direction) {
        return directions.get(direction);
    }
    public Vector2 hex_neighbor(Vector2 hex, int direction) {
        Vector2 dir = hex_direction(direction);
        return new Vector2(hex.x+dir.x,hex.y+dir.y);
    }

    public ArrayList<Integer> calculateMovementRange(HexGridCell start, int distance)
    {
        ArrayList<Integer> fringes = new ArrayList<Integer>();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("mapt.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter outTxt= new PrintWriter(new BufferedWriter(fileWriter));
        String out ="";
        for (int x = -distance; x <= distance; x++) {
            for (int y = Math.max(-distance, -x - distance); y <= Math.min(distance, -x + distance); y++) {
           // for (int y = -distance; y <= distance; y++) {
                int tmpx =(int) start.getMapPosition().x+x;
                int tmpy =(int) start.getMapPosition().y+y;

                for(HexGridCell h:hexGridCells)
                {
                    int hx= (int) h.getMapPosition().x;
                    int hy= (int) h.getMapPosition().y;
                    if(hx == tmpx && hy == tmpy)//  && h.isWalkable())
                    {
                        fringes.add(h.getIndex());

                        out+=" "+h.getIndex();
                        break;
                    }

                }
            }
            out+="\n";
        }


        outTxt.println(out);
        outTxt.close();
        return  fringes;

    }
    public ArrayList<Integer> calculateMovementRangeFrom(HexGridCell start,HexGridCell finish, int distance)
    {
        ArrayList<ArrayList<Vector2>> fringes = new ArrayList<ArrayList<Vector2>>();
        ArrayList<ArrayList<Vector2>> path = new ArrayList<ArrayList<Vector2>>();
        Set<HexGridCell> ret = new HashSet<HexGridCell>();

        int startx =(int) start.getMapPosition().x;
        int starty =(int) start.getMapPosition().y;
        int finishx =(int) finish.getMapPosition().x;
        int finishy =(int) finish.getMapPosition().y;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("map.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter outTxt= new PrintWriter(new BufferedWriter(fileWriter));


        String out ="";

        for (int x = 0; x <distance*2+1; x++) {
            ArrayList<Vector2> row = new ArrayList<Vector2>();
            for (int y = 0; y < distance * 2+1; y++) {
                row.add(new Vector2(0, 1));
                out+=" "+1;
            }
            out+="\n";
            fringes.add(row);
        }
        out+="\n";

        for (int x = -distance; x < distance+1; x++) {
        //for (int x = 0; x <distance*2; x++) {
            //for (int y = 0; y < distance * 2; y++) {
            for (int y = -distance; y < distance+1; y++) {
            //for (int y = Math.max(-distance, -x - distance); y <= Math.min(distance, -x + distance); y++) {
                //int tmpx =(int) start.getMapPosition().x+(x-distance);
                //int tmpy =(int) start.getMapPosition().y+(y-distance);

                int tmpx =(int) (start.getMapPosition().x+x);
                int tmpy =(int) (start.getMapPosition().y+y);

                for(HexGridCell h:hexGridCells)
                {
                    int hx= (int) h.getMapPosition().x;
                    int hy= (int) h.getMapPosition().y;
                    if(hx == tmpx && hy == tmpy)//  && h.isWalkable())
                    {
                        ret.add(h);
                        fringes.get(y+distance).get(x+distance).x=h.getIndex();
                        fringes.get(y+distance).get(x+distance).y=(h.isWalkable()?0:1);

                        break;
                    }

                }


            }



        }
        APathFinder aPathFinder = new APathFinder(fringes);
        aPathFinder.findPath(start.getIndex(),finish.getIndex());
        Array<GridNode> o = aPathFinder.GetPath();
        ArrayList<Integer> returnArray = new ArrayList<Integer>();
        for(GridNode g: o)
        {
            returnArray.add(g.getIndex());
        }
        //PathFinder pathFinder = new PathFinder(fringes);
        //ArrayList<Integer> returnArray =pathFinder.FindWave(start.getIndex(),finish.getIndex());

        out+="S "+start.getIndex()+ " F "+ finish.getIndex();
        out+="\n";
        // int[][] find = new int[fringes.size()][fringes.size()];
        for (int y = 0; y <  fringes.size(); y++) {
            for (int x = 0; x < fringes.get(y).size(); x++) {
                out+=" "+(int)fringes.get(y).get(x).y;
            }
            out+="\n";

        }
        out+="\n";
        for (int y = 0; y <  fringes.size(); y++) {
            for (int x = 0; x < fringes.get(y).size(); x++) {
                out+=" "+(int)fringes.get(y).get(x).x;
            }
            out+="\n";

        }
        out+="\n";
/*
        ArrayList<Integer> returnArray = new ArrayList<Integer>();
        for(int i=0;i<path.size();i++) {
            for (int j = 0; j < path.get(i).size(); j++) {
                if((int)path.get(i).get(j).y>=0)
                    returnArray.add((int)path.get(i).get(j).x);
            }
        }*/
        for (Integer pa:returnArray)
        {
            out+=" "+pa;
        }
        outTxt.println(out);
        outTxt.close();

        /*
        for(HexGridCell i:ret)
        {
            returnArray.add(i.getIndex());
        }*/

        return returnArray;

    }
    public ArrayList<Integer>  cube_reachable(int start, int movement)
    {



        Set<Integer> coords = new HashSet<Integer>();
        coords.add(start);

        ArrayList<ArrayList<Vector2>> fringes = new ArrayList<ArrayList<Vector2>>();
        fringes.add(new ArrayList<Vector2>());
        fringes.get(0).add(hexagons.get(start).getHexPosition());
        ArrayList<Vector2> obstacles = new ArrayList<Vector2>();


        for (int o=0;o<hexGridCells.size();o++) {
            HexGridCell h = hexGridCells.get(o);

            if(!h.isWalkable()) {
                obstacles.add(h.getMapPosition());
                Gdx.app.log("tankGenerator log ","obstacle "+h.getMapPosition() + " " + h.getIndex() );
            }
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("map.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter outTxt= new PrintWriter(new BufferedWriter(fileWriter));


        String out ="";
        for (int i = 1; i <= movement; i++) {
            fringes.add(new ArrayList<Vector2>());

            for (Vector2 c : fringes.get(i - 1)) {
                out+=c+" -> ";

                for (int d = 0; d < 6; d++) {
                    Vector2 neighbor = hex_neighbor(c, d);//c.add(directions.get(d));
                   // out+="["+hex_direction(d)+"]"+ neighbor;
                    boolean isObstacle = false;
                   /* Integer index=null;
                    for (int o=0;o<hexagons.size();o++) {
                    Hexagon h = hexagons.get(o);

                        if(h.getHexPosition().x==c.x && h.getHexPosition().y == c.y){// && !h.isWalkable()) {
                            //index = h.getIndex();
                            Gdx.app.log("tankGenerator log ","fringes "+h.getHexPosition()+ " "+ c + " "+ neighbor);
                            if(!h.isWalkable()) {
                                isObstacle = true;
                                break;
                            }*/

                    for (Vector2 obstacle : obstacles) {
                        //Gdx.app.log("tankGenerator log ", "fringes " + obstacle + " " + c);
                        if (c.x == obstacle.x && c.y == obstacle.y) {
                            //if (obstacle.equals(c)) {
                            isObstacle = true;
                            break;
                        }
                    }

                    // }
                    //}
                    if (!isObstacle) {
                        for (int o = 0; o < hexagons.size(); o++) {
                            Hexagon h = hexagons.get(o);
                            if (c.x == h.getHexPosition().x && c.y == h.getHexPosition().y) {
                                coords.add(h.getIndex());
                            }
                        }

                        fringes.get(i).add(neighbor);
                    }

                }
                out+="\n";

            }

        }
        outTxt.println(out);


        outTxt.close();
        ArrayList<Integer> returnArray = new ArrayList<Integer>();
        for(Integer i:coords)
        {
            returnArray.add(i);
        }

        return returnArray;

/*

        ArrayList<Integer> visited = new ArrayList<Integer>();
        //ArrayList<Hexagon> fringes = new ArrayList<Hexagon>();
        ArrayList<Fringe> fringesArray = new ArrayList<Fringe>();


        visited.add(start);
        //fringes.add(hexagons.get(start));

        Fringe f= new Fringe();
        f.addF(hexagons.get(start));
        fringesArray.add(f);


        //Gdx.app.log("tankGenerator log ","fringes "+fringes.get(0).getHexPosition());
        for(int k=1;k<=movement;k++)
        {
            Gdx.app.log("tankGenerator log ","fringes "+fringesArray.get(k-1).getF().size());
            for(Hexagon ha:fringesArray.get(k-1).getF())
                {
                    Fringe fa= new Fringe();
                        for(int i=0;i<6;i++) {

                            Vector2 neighbor = hex_neighbor(ha.getHexPosition(), i);
                            //Gdx.app.log("tankGenerator log ",i+ "neighbor " + neighbor + " " + ha.getHexPosition() +" "+hex_direction(i));

                            for (Hexagon h : hexagons) {


                                if (h.getHexPosition().x==neighbor.x && h.getHexPosition().y==neighbor.y){
                                    Gdx.app.log("tankGenerator log ",i+ "neighbor " + neighbor + " " + h.getHexPosition() +" "+hex_direction(i));
                                    //if (hexGridCells.get(h.getIndex()).isWalkable()) {

                                        Integer ind=null;
                                        for (int j = 0; j < visited.size(); j++) {
                                           if (visited.get(j) == h.getIndex()) {

                                                //ind = h.getIndex();
                                                Gdx.app.log("tankGenerator log ","index "+ind);
                                               break;
                                           }else
                                           {
                                               ind = h.getIndex();
                                           }
                                        }
                                        if(ind!=null) {
                                            visited.add(ind);
                                            fa.addF(h);
                                            //fringes.add(h);
                                        }
                                        //visited.add(h.getIndex());



                                   // }
                                }

                            }

                        }
                    fringesArray.add(fa);




               // Vector3 neighbor= cube.cube_neighbor(cube.hexToCube((int)cube.getHexPosition().x,(int)cube.getHexPosition().y),i);
            }
        }*/
        /*for(Hexagon h: fringes)
        {
            Gdx.app.log("tankGenerator log ","fringes "+h.getIndex()+ " "+ h.hexPosition);
        }*/
        /*for(Fringe a:fringesArray)
        {
            for(Hexagon b:a.getF())
            visited.add(b.getIndex());
        }*/
        //return visited;

    }


    public ArrayList<HexGridCell> getHexGridCells() {
        return hexGridCells;
    }

    public void setHexGridCells(ArrayList<HexGridCell> hexGridCells) {
        this.hexGridCells = hexGridCells;
    }
    public void saveMap() {
        Json json = new Json();
        //json.addClassTag("DefaultTank", DefaultTank.class); //As above
        json.setIgnoreUnknownFields(false);
        json.setUsePrototypes(false);
        json.setOutputType(OutputType.json);

        String jsonString = json.prettyPrint(this.getHexGridCells() );

        FileHandle file = Gdx.files.local("levelMap01.json");
        file.writeString(jsonString , false);
        /*
        Json json = new Json();
        FileHandle defaultDataFile = null;
        try {
            FileWriter writer= new FileWriter("levelMap01.json");

            json.setIgnoreUnknownFields(false);
            json.setUsePrototypes(false);
            json.setOutputType(OutputType.json);
            json.toJson(ArrayList.class,HexGridCell.class);

            writer.write(json.prettyPrint(hexGridCells));

            writer.flush();
        } catch (IOException se) {
            // TODO Auto-generated catch block
            se.printStackTrace();
        }*/
    }

    public Vector3 getCenterMap() {
        return centerMap;
    }

    public ArrayList<Vector3> getPoints() {
        return points;
    }

    public ArrayList<Hexagon> getHexagons() {
        return hexagons;
    }
/*
    public ArrayList<HexGridCell> getHexCells() {
        return hexCells;
    }*/

    public int getVertexSize() {
        return vertexSize;
    }

    public void setVertexSize(int vertexSize) {
        this.vertexSize = vertexSize;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public short[] getIndices() {
        return indices;
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }



    public ModelInstance getMap() {
        return map;
    }

    public void setMap(ModelInstance map) {
        this.map = map;
    }

}
