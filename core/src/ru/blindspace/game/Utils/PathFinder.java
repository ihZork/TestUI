package ru.blindspace.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by ihzork on 16.02.18.
 */

public class PathFinder {

    Vector2 start,finish;
    ArrayList<ArrayList<Vector2>> map;
    //int[][] cMap;
    ArrayList<ArrayList<Vector2>> waveMap;
    ArrayList<ArrayList<Vector2>> waveSMap;
    int size =0;
    ArrayList<Vector3> directions = new ArrayList<Vector3>();

    public PathFinder(ArrayList<ArrayList<Vector2>> _map) {
        //start = a;
        //finish = b;
        this.map = _map;

        directions.add(new Vector3(+1,  0, 1));
        directions.add(new Vector3(+1, -1, 1));
        directions.add(new Vector3( 0, -1 ,1));
        directions.add(new Vector3(-1,  0 ,1));
        directions.add(new Vector3( 0, +1 ,1));
        directions.add(new Vector3(+1, +1 ,1));

        waveMap = new ArrayList<ArrayList<Vector2>>();
        waveSMap = new ArrayList<ArrayList<Vector2>>();
        size = this.map.get(0).size();
      //  cMap = new int[size][size];

        Gdx.app.log("tankGenerator log ","ssize "+this.map.size());
                for(int i=0;i< this.map.size();i++) {

                    ArrayList<Vector2> row =  new ArrayList<Vector2>();
                    ArrayList<Vector2> rowS =  new ArrayList<Vector2>();
                    Gdx.app.log("tankGenerator log ","ssize "+this.map.get(i).size());
                    for (int j = 0; j < this.map.get(i).size(); j++) {
                        //Gdx.app.log("tankGenerator log ","- > "+  map.get(i).get(j).toString());
                        if ((int)map.get(i).get(j).y == 1) {

                            row.add(new Vector2(map.get(i).get(j).x,-2));

                        } else {
                            row.add(new Vector2(map.get(i).get(j).x,-1));
                            //waveMap.get(y).get(x).y = -1;
                        }
                        rowS.add(new Vector2(0,0));

                        //waveMap.get(i).get(j).x = 0;
                        //waveMap.get(i).get(j).y = 0;
                    }
                    waveMap.add(row);
                    waveSMap.add(rowS);
                }

        int k=0;
        for (int y = 0; y < waveMap.size(); y++) {
            for (int x = 0; x < waveMap.get(y).size(); x++) {
                Gdx.app.log("tankGenerator log ",k+" " + waveMap.get(y).get(x).toString());//out+=waveMap.get(y).get(x)+":";
                k++;
            }
            //out+="\n";
        }
    }

    /// <summary>
    /// Поиск пути
    /// </summary>
    /// <param name="startX">Координата старта X</param>
    /// <param name="startY">Координата старта Y</param>
    /// <param name="targetX">Координата финиша X</param>
    /// <param name="targetY">Координата финиша Y</param>
    public  ArrayList<Integer> FindWave(int start, int target)
    {
        boolean add=true;
           // int[,] cMap = new int[MapHeight,MapWidht];
        int x, y,step=0,startY=0,startX=0,finishX =0 ,finishY=0;
/*
        for (y = 0; y < this.map.size(); y++) {
            ArrayList<Vector2> row =  new ArrayList<Vector2>();
            for (x = 0; x < this.map.get(y).size(); x++) {

                if ((int)map.get(y).get(x).y == 1) {
                    waveMap.get(y).get(x).y = -2;
                   // row.add(new Vector2(0,0));
                    waveMap.get(y).get(x).set(map.get(y).get(x).x,-2);
                } else {
                    waveMap.get(y).get(x).set(map.get(y).get(x).x,-1);
                    //waveMap.get(y).get(x).y = -1;
                }
                //waveMap.get(y).get(x).x = map.get(y).get(x).x;

                if ((int)map.get(y).get(x).x == target) {

                    //waveMap.get(y).get(x).y = 0;
                    waveMap.get(y).get(x).set(map.get(y).get(x).x,0);
                }

                //Gdx.app.log("tankGenerator log ", waveMap.get(y).get(x).toString());
            }
        }*/
        for (y = 0; y < this.map.size(); y++) {
            for (x = 0; x < this.map.get(y).size(); x++) {

                if ((int)map.get(y).get(x).x == target) {

                    startY = y;
                    startX = x;

                }
                if ((int)map.get(y).get(x).x == start) {

                    finishY = y;
                    finishX = x;
                    waveMap.get(y).get(x).set(map.get(y).get(x).x,0);
                }

            }
        }





        /*
        for (y = 0; y < size; y++)
            for (x = 0; x < size; x++)
            {
                if (Map[y][x] == 1)
                cMap[y][x] = -2;//индикатор стены
                    else
                cMap[y][x] = -1;//индикатор еще не ступали сюда
            }
        cMap[targetY][targetX]=0;//Начинаем с финиша
        */
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("waveMap.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter outTxt= new PrintWriter(new BufferedWriter(fileWriter));


        int cost =0,x0,y0,lx0=0,ly0=0;
        while (true)
        {
            add = false;
            for (y = 0; y < waveMap.size(); y++) {
                for (x = 0; x < waveMap.get(y).size(); x++) {
                    cost = (int) (waveMap.get(y).get(x).y+1);
                    if (waveMap.get(y).get(x).y == step) {

                        for(int i=0;i< directions.size();i++)
                        {
                            x0= (int) (x+ directions.get(i).x);
                            y0= (int) (y+ directions.get(i).y);


                            Gdx.app.log("tankGenerator log ", waveMap.get(y).get(x).toString() + x0+" "+ y0);
                            if(x0>=0 && y0>=0 && x0<waveMap.get(y).size()-1 && y0<waveMap.size()-1)
                            {
                                if(waveMap.get(y0).get(x0).y != -2 && waveMap.get(y0).get(x0).y == -1) {
                                    waveMap.get(y0).get(x0).y = cost;
                                }
                                waveSMap.get(y0).get(x0).y = cost;
                            }


                        }
                        //Ставим значение шага+1 в соседние ячейки (если они проходимы)
                       /* if (x - 1 >= 0 && waveMap.get(y).get(x - 1).y != -2 && waveMap.get(y).get(x - 1).y == -1)
                            waveMap.get(y).get(x - 1).y = waveMap.get(y).get(x).y + 1;
                        if (y - 1 >= 0 && waveMap.get(y - 1).get(x).y != -2 && waveMap.get(y - 1).get(x).y == -1)
                            waveMap.get(y - 1).get(x).y = waveMap.get(y).get(x).y + 1;

                        if (x + 1 <size && waveMap.get(y).get(x + 1).y != -2 && waveMap.get(y).get(x + 1).y == -1)
                            waveMap.get(y).get(x + 1).y = waveMap.get(y).get(x).y + 1;
                        if (y + 1 <size && waveMap.get(y + 1).get(x).y != -2 && waveMap.get(y + 1).get(x).y == -1)
                            waveMap.get(y + 1).get(x).y = waveMap.get(y).get(x).y + 1;
/*
                        if (x - 1 >= 0 && y-1>=0 && waveMap.get(y-1).get(x - 1).y != -2 && waveMap.get(y-1).get(x - 1).y == -1)
                            waveMap.get(y-1).get(x - 1).y = waveMap.get(y).get(x).y + 1;

                        if (y - 1 >= 0 && x + 1 <size && waveMap.get(y - 1).get(x+1).y != -2 && waveMap.get(y - 1).get(x+1).y == -1)
                            waveMap.get(y - 1).get(x+1).y = waveMap.get(y).get(x).y + 1;

                        if (x + 1 <size && y + 1 <size && waveMap.get(y+1).get(x + 1).y != -2 && waveMap.get(y+1).get(x + 1).y == -1)
                            waveMap.get(y+1).get(x + 1).y = waveMap.get(y).get(x).y + 1;

                        if (y + 1 <size && x - 1 >= 0 &&  waveMap.get(y + 1).get(x-1).y != -2 && waveMap.get(y + 1).get(x-1).y == -1)
                            waveMap.get(y + 1).get(x-1).y = waveMap.get(y).get(x).y + 1;
*/
                        /*
                        if (y - 1 >= 0 && cMap[x - 1][y] != -2 && cMap[x - 1][y] == -1)
                        cMap[x - 1][y] = step + 1;
                        if (x - 1 >= 0 && cMap[x][y - 1] != -2 && cMap[x][y - 1] == -1)
                        cMap[x][y - 1] = step + 1;

                        if (y + 1 < size && cMap[x + 1][y] != -2 && cMap[x + 1][y] == -1)
                        cMap[x + 1][y] = step + 1;
                        if (x + 1 < size && cMap[x][y + 1] != -2 && cMap[x][y + 1] == -1)
                        cMap[x][y + 1] = step + 1;*/
                    }
                }
            }
            step++;
            add = true;
            if(waveMap.get(startY).get(startX).y!=-1) break;

           // if (cMap[startY][startX] != -1)//решение найдено
           //     add = false;
            if (step > size * size)//решение не найдено
                break;
        }
        String out ="";
        for (y = 0; y < waveMap.size(); y++) {
            for (x = 0; x < waveMap.get(y).size(); x++) {
                // Gdx.app.log("tankGenerator log ", waveMap.get(y).get(x).toString());
                if(waveMap.get(y).get(x).y>=0)
                    out+="  "+waveMap.get(y).get(x).y;else out+=" "+waveMap.get(y).get(x).y;

            }
            out+="\n";
        }
        out+="\n";
        for (y = 0; y < waveSMap.size(); y++) {
            for (x = 0; x < waveSMap.get(y).size(); x++) {
                // Gdx.app.log("tankGenerator log ", waveMap.get(y).get(x).toString());
                if(waveSMap.get(y).get(x).y>=0)
                    out+="  "+waveSMap.get(y).get(x).y;else out+=" "+waveSMap.get(y).get(x).y;

            }
            out+="\n";
        }

        outTxt.println(out);
        outTxt.close();

        ArrayList<Integer> path = new ArrayList<Integer>();
       // path.add((int)waveMap.get(startY).get(startX).x);
        int w=waveMap.size();
        int h=waveMap.get(0).size();
        int i = startY;
        int j = startX;
        Gdx.app.log("tankGenerator log ","step "+step);
        do{
            /*
            for(int d= 0;d<directions.size();d++)
            {
                int x0= (int) (i+ directions.get(d).x);
                int y0= (int) (j+ directions.get(d).y);

                if(x0>=0 && y0>=0 && x0<=size && y0<=size)
                {

                }
            }*/
            if(i<waveMap.size()-1) {
                if (waveMap.get(i+1).get(j).y == step - 1) {
                    path.add((int) waveMap.get(i++).get(j).x);
                }
            }
            if(j<h-1) {
                if (waveMap.get(i).get(j+1).y == step - 1) {
                    path.add((int) waveMap.get(i).get(j++).x);
                }
            }
            if(i>0) {
                if (waveMap.get(i-1).get(j).y == step - 1) {
                    path.add((int) waveMap.get(i--).get(j).x);
                }
            }
            if(j>0) {
                if (waveMap.get(i).get(j-1).y == step - 1) {
                    path.add((int) waveMap.get(i).get( j--).x);
                }
            }

            step--;
        }while (step!=0);



        return path;

    }

    public ArrayList<Vector2> search()
    {
        ArrayList<Vector2> dir = new ArrayList<Vector2>();
        dir.add(new Vector2(1,0));
        dir.add(new Vector2(0,1));
        dir.add(new Vector2(-1,0));
        dir.add(new Vector2(0,-1));
        int dirSize=dir.size();

        boolean finded = false;

        for(int i=0;i<map.size();i++)
        {
            for(int j=0;j<map.get(i).size();j++)
            {
                if(map.get(i).get(j).y>0){

                }
            }
        }



        ArrayList<Vector2> out = new ArrayList<Vector2>();

        return out;
    }
}
