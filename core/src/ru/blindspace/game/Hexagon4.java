package ru.blindspace.game;

/**
 * Created by ihzork on 13.09.16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Hexagon4 {


    private float radius;
    private float width;
    private float halfWidth;
    private float height;
    private float rowHeight;
    public  float ExtraHeight = height - rowHeight;
    public  float Edge = rowHeight - ExtraHeight;

    public Hexagon4(float radius)
    {
        this.radius = radius;
        this.height = 2 * radius;
        this.rowHeight = 1.5f * radius;
        this.halfWidth = (float) Math.sqrt((radius * radius) - ((radius / 2) * (radius / 2)));
        this.width = 2 * this.halfWidth;
    }

    public Vector2 TileOrigin(Vector2 tileCoordinate)
    {
        return new Vector2(
                (tileCoordinate.x * width) + ((Math.abs(tileCoordinate.y) % 2 == 1) ? halfWidth : 0), //Y % 2 == 1 is asking 'Is Y odd?'
                tileCoordinate.y * rowHeight);
    }

    public Vector2 TileCenter(Vector2 tileCoordinate)
    {
        Vector2 v = new Vector2(halfWidth, rowHeight);
        return TileOrigin(tileCoordinate).add(v);
    }
    public Vector3 ToPixel(Vector2 hc)
    {
        float x = (hc.x * width) + (((int)hc.y & 1) * width / 2);
        return new Vector3(x, 0, (float)(hc.y * 1.5 *radius));
    }

    public Vector2 ToHex(Vector3 pos)
    {
        float px = pos.x + halfWidth;
        float py = pos.z +height/2;

        int gridX = (int)Math.floor(px / width);
        int gridY = (int)Math.floor(py / rowHeight);

        float gridModX = Math.abs(px % width);
        float gridModY = Math.abs(py % rowHeight);

        boolean gridTypeA = (gridY % 2) == 0;

        float resultY = gridY;
        float resultX = gridX;
        float m = ExtraHeight / halfWidth;

        if (gridTypeA)
        {
            // middle
            resultY = gridY;
            resultX = gridX;
            // left
            if (gridModY < (ExtraHeight - gridModX * m))
            {
                resultY = gridY - 1;
                resultX = gridX - 1;
            }
            // right
            else if (gridModY < (-ExtraHeight + gridModX * m))
            {
                resultY = gridY - 1;
                resultX = gridX;
            }
        }
        else
        {
            if (gridModX >= halfWidth)
            {
                if (gridModY < (2 * ExtraHeight - gridModX * m))
                {
                    // Top
                    resultY = gridY - 1;
                    resultX = gridX;
                }
                else
                {
                    // Right
                    resultY = gridY;
                    resultX = gridX;
                }
            }

            if (gridModX < halfWidth)
            {
                if (gridModY < (gridModX * m))
                {
                    // Top
                    resultY = gridY - 1;
                    resultX = gridX;
                }
                else
                {
                    // Left
                    resultY = gridY;
                    resultX = gridX - 1;
                }
            }
        }

        return new Vector2(resultX, resultY);
    }

    public Vector3 pixel_to_hex(Vector2 coord) {


        int tileSize = 5;
        //return new Vector3(-tileSize * 12 / 2f +tileSize / 2, 0, 12 / 2f * rowHeight - rowHeight / 2);

        int x= (int)(coord.x/(tileSize*2));
        int y= (int)(coord.y/(tileSize*2));
        return new Vector3((float)x, 0, (float)y);
        /*double temp = Math.floor(coord.x + Math.sqrt(3) * coord.y + 1);
        double q = Math.floor((Math.floor(2*coord.x+1) + temp) / 3);
        double r = Math.floor((temp + Math.floor(-coord.x + Math.sqrt(3) * coord.y + 1))/3);
        return new Vector3((float)q,0f,(float)r);*/
    }

    public Vector2 hex_to_pixel(Vector2 coord) {

        float x = radius * 3/2 * coord.x;
        float y = radius * (float)Math.sqrt(3) * (coord.y + coord.x/2);
        return new Vector2(x,y);



    }
    public Vector2 GetHex(Vector3 pos)
    {
        float px = pos.x + halfWidth;
        float py = pos.z + height /2;
        int gridX = (int)px / (int)(width);
        int gridY = (int)py / (int)rowHeight;

        int gridModX = (int)px % (int)width;
        int gridModY = (int)py % (int)rowHeight;

        boolean gridTypeA =false;

        if (((int)gridY &1) ==0)
            gridTypeA =true;

        int resultY = gridY;
        int resultX = gridX;
        float m = ExtraHeight / halfWidth;

        if (gridTypeA)
        {
            // middle
            resultY = gridY;
            resultX = gridX;
            // left
            if (gridModY < (ExtraHeight-gridModX * m))
            {
                resultY = gridY-1;
                resultX = gridX-1;
            }
            // right
            if (gridModY < (-ExtraHeight + gridModX * m))
            {
                resultY = gridY -1;
                resultX = gridX;
            }
        }
        else
        {
            if (gridModX >= halfWidth)
            {
                if (gridModY < (2* ExtraHeight - gridModX * m))
                {
                    // Top
                    resultY = gridY -1;
                    resultX = gridX;
                }
            else
                {
                    // Right
                    resultY = gridY;
                    resultX = gridX;
                }
            }

            if (gridModX < halfWidth)
            {
                if (gridModY < (gridModX * m))
                {
                    // Top
                    resultY = gridY -1;
                    resultX = gridX;
                }
                else
                {
                    // Left
                    resultY = gridY;
                    resultX = gridX -1;
                }
            }
        }

        return new Vector2(resultX,resultY);
    }

    public Vector2 TileAt(Vector2 worldCoordinate)
    {
        double rise = height - rowHeight;
        double slope = rise / halfWidth;
        int X = (int)Math.floor(worldCoordinate.x / width);
        int Y = (int)Math.floor(worldCoordinate.y / rowHeight);

        double OffsetX = worldCoordinate.x - X * width;
        double OffsetY = worldCoordinate.y - Y * rowHeight;

        if (Y % 2 == 0) //Is this an even row?
        {
            Gdx.app.log("tankGenerator log ", "Is this an even row? ");
            //Section type A
            if (OffsetY < (-slope * OffsetX + rise)) //Point is below left line; inside SouthWest neighbor.
            {
                X -= 1;
                Y -= 1;
            }
            else if (OffsetY < (slope * OffsetX - rise)) //Point is below right line; inside SouthEast neighbor.
                Y -= 1;
        }
        else
        {
            //Section type B
            if (OffsetX >= halfWidth) //Is the point on the right side?
            {
                if (OffsetY < (-slope * OffsetX + rise * 2.0f)) //Point is below bottom line; inside SouthWest neighbor.
                    Y -= 1;
            }
            else //Point is on the left side
            {
                if (OffsetY < (slope * OffsetX)) //Point is below the bottom line; inside SouthWest neighbor.
                    Y -= 1;
                else //Point is above the bottom line; inside West neighbor.
                    X -= 1;
            }
        }

        return new Vector2(X, Y);
    }
    /*
public void makeHoneyComb(int radius){

    makeCell(ta, 0, 0);
    for (int r = 0; r > -radius; r--)
        for (int q = -r - 1; q > -radius - r; q--)
            makeCell( q, r);

    for (int r = 1; r < radius; r++)
        for (int q = 0; q > -radius; q--)
            makeCell( q, r);

    for (int q = 1; q < radius; q++)
        for (int r = -q; r < radius - q; r++)
            makeCell( q, r);
    }
    public Vector3 calcInitPos()
    {
        Vector3 initPos;
        //the initial position will be in the left upper corner
        initPos = new Vector3(-width * gridWidthInHexes / 2f +width / 2, 0,
                gridHeightInHexes / 2f * rowHeight - rowHeight / 2);

        return initPos;
    }
    //method used to convert hex grid coordinates to game world coordinates
    public Vector3 calcWorldCoord(Vector2 gridPos)
    {
        //Position of the first hex tile
        Vector3 initPos = calcInitPos();
        //Every second row is offset by half of the tile width
        float offset = 0;
        if (gridPos.y % 2 != 0)
            offset = hexWidth / 2;

        float x =  initPos.x + offset + gridPos.x * hexWidth;
        //Every new line is offset in z direction by 3/4 of the hexagon height
        float z = initPos.z - gridPos.y * hexHeight * 0.75f;
        return new Vector3(x, 0, z);
    }
    */

/*
    public enum Direction
    {
        NorthEast,
        East,
        SouthEast,
        SouthWest,
        West,
        NorthWest,
        NumberOfDirections,
    }

    public static Direction RotateDirection(Direction direction, int amount)
    {
        //Let's make sure our directions stay within the enumerated values.
       // if (direction < Direction.NorthEast || direction > Direction.NorthWest || FastMath.abs(amount) > (int)Direction.NorthWest)
       //     throw new InvalidOperationException("Directions out of range.");

        direction += amount;

        //Now we need to make sure direction stays within the proper range.
        //C# does not allow modulus operations on enums, so we have to convert to and from int.

        int n_dir = (int)direction % (int)Direction.NumberOfDirections;
        if (n_dir < 0) n_dir = (int)Direction.NumberOfDirections + n_dir;
        direction = (Direction)n_dir;

        return direction;
    }

    public static Direction Opposite(Direction direction) { return RotateDirection(direction, 3); }

    public static Vector2f Neighbor(Vector2f tile, Direction direction)
    {
        if (tile.Y % 2 == 0) //Is this row even?
        {
            switch (direction)
            {
                case Direction.NorthEast: tile.Y += 1; break;
                case Direction.East: tile.X += 1; break;
                case Direction.SouthEast: tile.Y -= 1; break;
                case Direction.SouthWest: tile.Y -= 1; tile.X -= 1; break;
                case Direction.West: tile.X -= 1; break;
                case Direction.NorthWest: tile.X -= 1; tile.Y += 1; break;
                default: throw new InvalidOperationException("Invalid direction");
            }
        }
        else //This is an odd row.
        {
            switch (direction)
            {
                case Direction.NorthEast: tile.X += 1; tile.Y += 1; break;
                case Direction.East: tile.X += 1; break;
                case Direction.SouthEast: tile.X += 1; tile.Y -= 1; break;
                case Direction.SouthWest: tile.Y -= 1; ; break;
                case Direction.West: tile.X -= 1; break;
                case Direction.NorthWest: tile.Y += 1; break;
                default: throw new InvalidOperationException("Invalid direction");
            }
        }

        return tile;
    }

*/
}
