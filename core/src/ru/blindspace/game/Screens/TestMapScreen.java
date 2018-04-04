package ru.blindspace.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ru.blindspace.game.DefaultLayerMap;
import ru.blindspace.game.Hexagon;
import ru.blindspace.game.Hexagon4;
import ru.blindspace.game.LevelMap;

import ru.blindspace.game.MapTile;
import ru.blindspace.game.TileTriangle;
import ru.blindspace.game.Utils.GameObject;
import ru.blindspace.game.HexGridCell;

import static com.badlogic.gdx.utils.JsonWriter.*;

/**
 * Created by ihzork on 14.12.17.
 */

public class TestMapScreen  implements Screen, InputProcessor {

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    ShapeRenderer shapeRenderer;

    //private CameraInputController controller;
    //private ModelInstance map;
    //LevelMap map;


    private Hexagon4 h;
    public  int radius =12;
    public  int mapradius =8;
    public Vector2 selectCoordinate;
    /*int [] map = {
            -2, -2,-1,-2, 0,-2, 1,-2, 2,-2,
            -2, -1,-1,-1, 0,-1, 1,-1, 2,-1,
            -2,  0,-1, 0, 0, 0, 1, 0, 2, 0,
            -2,  1,-1, 1, 0, 1, 1, 1, 2, 1,
            -2,  2,-1, 2, 0, 2, 1, 2, 2, 2,

    };*/
    Ray collisionRay;
    Vector3 touchPoint,position;
    Vector3 touch;
    MapTile sel=null;
   // BoundingBox box;
    ArrayList<Vector3> tempVector = new ArrayList<Vector3>();
    ArrayList<Vector2> tileCoordinates;
    ArrayList<ModelInstance> tiles;
    ArrayList<HexGridCell> hexGridCells;
    ArrayList<HexGridCell> mapGridCells;

    Environment environment;

    private static final int BOARD_WIDTH = 5;
    private static final int BOARD_HEIGHT =5;

    private static final int L_ON = 1;
    private static final int L_OFF = 2;

    private static final int NUM_HEX_CORNERS = 6;
    private static final int CELL_RADIUS = 5;

    HexGridCell selected=null;



    //ModelInstance testTank,testTank1,treeInstance;




    ModelInstance environmentInstance;
    DecalBatch batch;
    ArrayList<Decal> decals = new ArrayList<Decal>();
    Vector3 xyz;
    Stage stage;
    Skin skin;
    Window window;
    CheckBox checkBox;
    Label tileIndex;
    ChangeListener changeListener;
    TextButton saveButton,loadButton;
    SelectBox placedObject,orientationObject;
    ArrayList<String> nameObjects;
    ArrayList<GameObject> gameObjects;


    Integer lastSelectedIndex=-1;
    Integer lIndex=-1;
    //ArrayList<Integer> path;
    ArrayList<Integer> walkable;

    //private final Vector3 dir = new Vector3();

    private float startX, startY;
    ArrayList<Vector3> directions = new ArrayList<Vector3>();
    DefaultLayerMap defaultLayerMap =null;


    ArrayList<HexGridCell> gridCells = new ArrayList<HexGridCell>();

    //HashMap<String,GameObject> aviableModelInstances = new HashMap<String,GameObject>();

    BitmapFont font;
    Label coodLabel ;

    ModelInstance mapInstance;
    boolean rotationChange=false;

    public TestMapScreen() {
        /*
        new Vector3( 0, 1, -90);
        new Vector3(-1, 1, -45);
        new Vector3(-1, 0, 0);
        new Vector3( 0,-1, 90);
        new Vector3( 1,-1, 135);
        new Vector3( 1, 0, 180);
        */

        directions.add(new Vector3(-1,  0,   0));
        directions.add(new Vector3( 0, -1,  60));
        directions.add(new Vector3(+1, -1, 135));
        directions.add(new Vector3(+1,  0, 180));
        directions.add(new Vector3( 0,  1, 270));
        directions.add(new Vector3( 0, +1, 315));

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.near = 2f;
        camera.far = 1000f;


        selectCoordinate = new Vector2();
        touchPoint = new Vector3();
        touch = new Vector3();
        position = new Vector3();
        h = new Hexagon4(5);

        tileCoordinates = new ArrayList<Vector2>();

        tiles = new ArrayList<ModelInstance>();


        modelBatch = new ModelBatch();

        shapeRenderer = new ShapeRenderer();

        JsonReader jsonReader = new JsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        //Model mapModel = modelLoader.loadModel(Gdx.files.internal("levelMap01.g3dj"));
        Model mapModel = modelLoader.loadModel(Gdx.files.internal("testmap3.g3dj"));

        /*{
            "id": "MaterialMap",
                "filename": "Material_Base_Color.png",
                "type": "DIFFUSE"
        }*/
        //map = new ModelInstance(mapModel);
        Texture texture = new Texture("Material_Base_Color.png");


        /*
        map = new LevelMap(new ModelInstance(mapModel));
        for(Material mat:map.getMap().materials)
        {
            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
            mat.set(TextureAttribute.createDiffuse(texture));
        }
       // xyz= new Vector3();
        map.constructMap();*/

        mapInstance = new ModelInstance(mapModel);

        for(Material mat:mapInstance.materials)
        {
            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
            mat.set(TextureAttribute.createDiffuse(texture));
        }
        gameObjects = new ArrayList<GameObject>();
        //constructMap(mapInstance);
        //saveDefaulMap();

        loadDefaultMap();
        //loadMap();
        if(defaultLayerMap==null)
        {

            constructMap(mapInstance);
            saveDefaulMap();
        }


        //map.getMap().transform.getTranslation(xyz);
        //xyz.set(map.getCenterMap());
        //Gdx.app.log("tankGenerator log ", "centerMap : "+xyz);
        //map.getMap().transform.translate(xyz.x,xyz.y,xyz.z);


        camera.position.set(0, 60f, 0);
        camera.lookAt(0,0,0);//map.getCenterMap().x, 0f, map.getCenterMap().z);

        //camera.translate(tmpV1.set(camera.direction).scl(50f));

        camera.update();
        //controller = new CameraInputController(camera);
        //Gdx.input.setInputProcessor(controller);
        //for(HexGridCell t:map.getHexCells())

                //Gdx.app.log("tankGenerator log "," "+t.getWorldPosition());


        //Model selectorModel = modelLoader.loadModel(Gdx.files.internal("selector.g3dj"));



        //loadMap();


        Model testTankModel = modelLoader.loadModel(Gdx.files.internal("panzerjager.g3dj"));
        Model testTankModel1 = modelLoader.loadModel(Gdx.files.internal("PZIIC.g3dj"));

/*
        testTank = new ModelInstance(testTankModel);

        testTank1 = new ModelInstance(testTankModel1);
        //testTank1.transform.translate(map.getHexagons().get(10).getCenter());
        //controller.target.set(map.getHexagons().get(10).getCenter());
        //controller.update();
        testTank.transform.translate(map.getHexGridCells().get(346).getWorldPosition());
        testTank1.transform.translate(map.getHexGridCells().get(344).getWorldPosition());
        map.getHexGridCells().get(344).setWalkable(false);
        testTank.transform.scale(0.01f,0.01f,0.01f);
        testTank1.transform.scale(0.01f,0.01f,0.01f);


        testTank.transform.rotate(new Vector3(0,1,0),30);
        testTank1.transform.rotate(new Vector3(0,1,0),30);*/

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1.0f));
        environment.add(new PointLight().set(1f, 1f, 1f, 0f, 100f, 100f,10f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));




        //Label l = new Label("dfas");

        //Decal decal = Decal.newDecal(1, 1, textures[1]);
        //decal.setPosition(0, 0, 0);
        //decals.add(decal);



        stage = new Stage();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("Walkable",skin);
        saveButton = new TextButton("Save",skin);
        saveButton.setPosition(Gdx.graphics.getWidth()-100,20);






        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //map.saveMap();
                saveMap();
            }
        });




        loadButton = new TextButton("Load",skin);
        loadButton.setPosition(Gdx.graphics.getWidth()-40,20);
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadMap();
            }
        });
        TextButton xButton = new TextButton("X",skin);
        xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.setVisible(false);
            }
        });
        window.getTitleTable().add(xButton).height(window.getPadTop());
        checkBox = new CheckBox("walkable!? : ",skin);
        tileIndex = new Label("",skin);


        placedObject = new SelectBox(skin);
        orientationObject = new SelectBox(skin);
        placedObject.setItems("null","HouseType_0003","firTreeForest","respawnTeamRed","respawnTeamBlue");

        font = skin.getFont("default-font");
       // coodLabel =  new Label("",skin);

        int i=0;

        orientationObject.setItems((i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30,(i++)*30);


        placedObject.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if(placedObject.getSelected() instanceof String) {
                    String string = (String) placedObject.getSelected();
                    if (!string.equals("null")) {
                        //System.out.println(placedObject.getSelected());
                        //Gdx.app.log("tankGenerator log ", "selected " + placedObject.getSelected()+" "+map.getHexGridCells().get(selected.getIndex()).getIndex()+ " "+ selected.getIndex());

                        int setId = -1;
                        for (int i = 0; i < gameObjects.size(); i++) {
                            HexGridCell hex = defaultLayerMap.getCells().get(selected.getIndex());
                            if (hex != null) {

                                if (selected.getIndex() == gameObjects.get(i).getId() )//&& gameObjects.get(i).isMe(hex.getPlacedObject())) {
                                {
                                    setId = i;
                                }
                            }
                        }
                        if (setId > -1) {

                        } else {
                            HexGridCell hex = defaultLayerMap.getCells().get(selected.getIndex());
                            hex.setPlacedObject(string);

                            gameObjects.add(loadGameObject(hex));
                            Gdx.app.log("tankGenerator log ", "Now game objects size is " + gameObjects.size());

                        }
                        //gameObjects.set().remove(selected.getIndex());
                        //defaultLayerMap.getCells().get(selected.getIndex()).setPlacedObject(string);
                        //map.getHexGridCells().get(selected.getIndex()).setPlacedObject(string);//.setWalkable(checkBox.isChecked());
                    } else {

                        int removeId = -1;
                        for (int i = 0; i < gameObjects.size(); i++) {
                            HexGridCell hex = defaultLayerMap.getCells().get(selected.getIndex());

                            if (hex != null) {

                                if (selected.getIndex() == gameObjects.get(i).getId() && gameObjects.get(i).isMe(hex.getPlacedObject())) {
                                    removeId = i;
                                }
                            }
                        }
                        if (removeId > -1) {
                            gameObjects.remove(removeId);

                            defaultLayerMap.getCells().get(selected.getIndex()).setPlacedObject(null);
                        }
                    }
                }
            }
        });
        orientationObject.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if(orientationObject.getSelected() instanceof Integer) {

                    Integer orient =  (Integer)orientationObject.getSelected();
                        //map.getHexGridCells().get(selected.getIndex()).setOrientation(orient);
                        defaultLayerMap.getCells().get(selected.getIndex()).setOrientation(orient);
                    if(selected.getOrientation()==(int)orient)
                        rotationChange =true;


                }
            }
        });
        window.add(tileIndex);
        window.row();
        window.add(checkBox);
        window.row();
        window.add(placedObject);
        window.row();
        window.add(orientationObject);
        window.setWidth(300);
        stage.addActor(saveButton);
        stage.addActor(loadButton);
        stage.addActor(window);

        changeListener =new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(selected!=null)
                {
                    defaultLayerMap.getCells().get(selected.getIndex()).setWalkable(checkBox.isChecked());
                    //map.getHexGridCells().get(selected.getIndex()).setWalkable(checkBox.isChecked());
                    //selected.setWalkable(checkBox.isChecked());
                }

            }
        };
        checkBox.addListener(changeListener);

        InputMultiplexer multiplexer = new InputMultiplexer();
        //controller.scrollFactor=0f;

        multiplexer.addProcessor(stage);
        //multiplexer.addProcessor(controller);


        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);

        //path = map.cube_reachable(396, 6);
        //testTank.transform.set(map.getHexGridCells().get(346).getWorldPosition(),new Quaternion(),new Vector3(0.01f,0.01f,0.01f));

       // Gdx.app.log("tankGenerator log ","index "+map.getHexagons().get(346).getCenter()+" "+map.getHexagons().get(366).getCenter());

        //walkable =map.calculateMovementRange(map.getHexGridCells().get(391), 5);

        //path = map.calculateMovementRangeFrom(map.getHexGridCells().get(391),map.getHexGridCells().get(323),5);
        /*path = new ArrayList<Integer>();
        path.add(391);
        path.add(383);
        path.add(368);
        path.add(347);
        path.add(348);
        path.add(322);
        path.add(323);
        path.add(349);
        path.add(350);
        path.add(371);
        path.add(386);
        path.add(393);
        path.add(396);
        path.add(390);
        path.add(381);
        path.add(364);
        path.add(380);
        path.add(362);
        path.add(380);
        path.add(364);
        path.add(381);
        path.add(391);
        path.add(396);
        path.add(391);*/

        //testTank.transform.setTranslation(map.getHexGridCells().get(path.get(0)).getWorldPosition());


/*
        for (Integer p:path)
        {

            Gdx.app.log("tankGenerator log ","index "+p+ " " + map.getHexGridCells().get(p).getMapPosition());
        }*/

        //Decal d = new Decal();
/*
        for(HexGridCell hexagon:map.getHexGridCells()) {

            Vector3 pos = hexagon.getWorldPosition();

            Vector3 positionHUD = camera.project(pos);

            coodLabel = new Label(""+hexagon.getMapPosition(),skin);
            coodLabel.setPosition(positionHUD.x-20, positionHUD.y);
            //stage.addActor(coodLabel);

        }*/

    }
    private void saveDefaulMap() {
        Json json = new Json();

        json.setIgnoreUnknownFields(false);
        json.setUsePrototypes(false);
        json.setOutputType(OutputType.json);

        String jsonString = json.prettyPrint(defaultLayerMap);

        FileHandle file = Gdx.files.local("defaultMap.json");
        file.writeString(jsonString, false);
    }
    private void saveMap() {
        Json json = new Json();

        json.setIgnoreUnknownFields(false);
        json.setUsePrototypes(false);
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(defaultLayerMap );

        FileHandle file = Gdx.files.local("levelMap01.json");
        file.writeString(jsonString , false);
        /*

        try {

            FileWriter writer= null;
            try {
                writer = new FileWriter("levelMap01.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //json.setIgnoreUnknownFields(false);
            //json.setUsePrototypes(false);

            //json.setOutputType(OutputType.json);
            //SaveHexGridCellsMapClass saveHexGridCellsMapClass =  new SaveHexGridCellsMapClass();
            //HexGridCellsMapClass hh =  new HexGridCellsMapClass();
            //ArrayList<HexGridCell> outHex = new ArrayList<HexGridCell>();

            writer.write(json.prettyPrint(hexGridCells));
           // writer.write(json.prettyPrint(map.getHexGridCells()));


            writer.flush();
        } catch (IOException se) {
            // TODO Auto-generated catch block
            se.printStackTrace();
        }*/
    }



    private void loadDefaultMap() {

        ///phrasesMap = new LanguagePhrases();

        Json json = new Json();


        //File f = new File("map.json");
        FileHandle defaultDataFile = Gdx.files.internal("defaultMap.json");
        if (defaultDataFile.exists()) {

            //try {
            String profileAsText = defaultDataFile.readString().trim();
            //FileReader file = new FileReader("map.json");
            json.setIgnoreUnknownFields(false);
            json.setUsePrototypes(false);
            //json.setOutputType(JsonWriter.OutputType.json);

            Gdx.app.log("tankGenerator log ", "File FOUND");
            defaultLayerMap = json.fromJson(DefaultLayerMap.class, defaultDataFile);


        } else {
            Gdx.app.log("tankGenerator log ", "Not FOUND!!! " + defaultDataFile);
        }


    }
    private void loadMap() {

        ///phrasesMap = new LanguagePhrases();

        Json json = new Json();


        //File f = new File("map.json");
        FileHandle defaultDataFile = Gdx.files.internal("levelMap01.json");
        if (defaultDataFile.exists()) {

            //try {
            String profileAsText = defaultDataFile.readString().trim();
            //FileReader file = new FileReader("map.json");
            json.setIgnoreUnknownFields(false);
            json.setUsePrototypes(false);
            //json.setOutputType(JsonWriter.OutputType.json);

            Gdx.app.log("tankGenerator log ", "File FOUND");
            defaultLayerMap = json.fromJson(DefaultLayerMap.class, defaultDataFile);


        } else {
            Gdx.app.log("tankGenerator log ", "Not FOUND!!! " + defaultDataFile);
        }
        if (defaultLayerMap != null) {

            Gdx.app.log("tankGenerator log ", "game objects create");
            gameObjects.clear();
            /*if (gameObjects != null) {
                gameObjects.clear();
            } else {
                Gdx.app.log("tankGenerator log ", "game objects NULL");
                gameObjects = new ArrayList<GameObject>();
            }*/
            JsonReader jsonReader = new JsonReader();
            G3dModelLoader loadermodel = new G3dModelLoader(jsonReader);
                for (HexGridCell hexagon : defaultLayerMap.getCells()) {


                    if (hexagon.getPlacedObject() != null) {

                        Gdx.app.log("tankGenerator log ", "get game object model "+ hexagon.getPlacedObject());
                        Model tModel = loadermodel.loadModel(Gdx.files.internal(hexagon.getPlacedObject() + ".g3dj"));

                        ModelInstance tModelInstance = new ModelInstance(tModel);
                        Gdx.app.log("tankGenerator log ", "set game object position to "+ defaultLayerMap.getCells().get(hexagon.getIndex()).getWorldPosition()+" "+defaultLayerMap.getCells().get(hexagon.getIndex()).getOrientation());
                        tModelInstance.transform.translate(defaultLayerMap.getCells().get(hexagon.getIndex()).getWorldPosition());
                        tModelInstance.transform.rotate(new Vector3(0, 1, 0), hexagon.getOrientation());


                        for (Material mat : tModelInstance.materials) {
                            mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
                            mat.set(new FloatAttribute(FloatAttribute.AlphaTest, 0.1f));

                        }
                        //aviableModelInstances.put(hexagon.getPlacedObject(),new GameObject(tModelInstance,hexagon.getIndex(), hexagon.getPlacedObject()));

                        gameObjects.add(new GameObject(tModelInstance,hexagon.getIndex(), hexagon.getPlacedObject()));
                    }

                }
            }
        Gdx.app.log("tankGenerator log ", "total game objects size "+ gameObjects.size());
    }



    /*void updateTreeCamera(){
        Vector3 camPosition = camera.position;
        camPosition.set(5, 2, 0); //Move camera to default location on circle centered at origin
        camPosition.rotate(Vector3.Y, camPathAngle); //Rotate the position to the angle you want. Rotating this vector about the Y axis is like walking along the circle in a counter-clockwise direction.
        camPosition.add(treeCenterPosition); //translate the circle from origin to tree center
        camera.up.set(Vector3.Y); //Make sure camera is still upright, in case a previous calculation caused it to roll or pitch
        camera.lookAt(treeCenterPosition);
        camera.update(); //Register the changes to the camera position and direction
    }*/



    int lastIndex=0;
    float lastAngle=0;
    Quaternion lastQaut = new Quaternion();
    Vector3 nextPos = new Vector3();
    Vector3 nextVel = new Vector3();

    float speed =0.8f;
    float rotSpeed = 30f;
    Matrix4 mat = new Matrix4();
    float angle,dAngle;

    int index = 0;

    Vector3 pos = new Vector3();
    Vector3 vel = new Vector3();
    Vector3 dir = new Vector3();

    Vector2 v2Pos = new Vector2();
    Vector2 v2Vel = new Vector2();
    Vector2 v2LastPos = new Vector2();
    Vector2 v2LastVel = new Vector2();
    Vector2 v2Tmp = new Vector2();


    boolean walk=true;
    boolean rotate=false;
    boolean stop=true;
    boolean start = false;
    boolean loop = false;
    float deltaX=0,deltaY;


    float tolerance =0.1f;

    /*
    public boolean isWayPointReached(Vector3 p ,int index,float delta)
    {

        return Math.abs(map.getHexGridCells().get(path.get(index)).getWorldPosition().z-p.z)<=speed/tolerance *delta && Math.abs(map.getHexGridCells().get(path.get(index)).getWorldPosition().x-p.x)<=speed/tolerance *delta;
    }
    public boolean isRotateReached(float lastAngle , float angle,float delta)
    {

        return Math.abs(lastAngle-angle)<=rotSpeed/tolerance *delta;
    }
*/

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        /*if(selected!=null) {
            camera.lookAt(selected.getCenter().x,0,selected.getCenter().z);
            camera.position.set(selected.getCenter().x, 20, selected.getCenter().z+20);
            //camera.update();
        }
        camera.update();*/



        /*if(selected!=null)
            camera.position.add(camera.direction.x * 0.1f, camera.direction.y * 0.1f, camera.direction.z * 0.1f);
            //camera.position.add(tmpV.set(camera.direction).scl(0.1f));
        camera.update();
*/

        //controller.update();

        //camera.rotateAround();

        //updateUnitCamera(pos,deltaX,deltaY);
        if(selected!=null)
            updateTreeCamera(selected,deltaX,deltaY);
        modelBatch.begin(camera);
        //modelBatch.render(map.getMap(),environment);
        modelBatch.render(mapInstance,environment);




        //// seek path waypoint & walk

        /*
        if(start)
        {
            //testTank.transform.getRotation(lastQaut);
            //angle =  lastQaut.getAngle();//MathUtils.atan2(vel.x, vel.z)* MathUtils.radiansToDegrees;
            //if (angle < 0) angle += 360;
            start = false;
            stop = false;
            loop =false;
            walk = false;
            rotate =false;
            index=0;
            testTank.transform.getTranslation(pos);
        }

        if(!stop) {

        //



            if(walk) {

                pos.mulAdd(vel, speed * delta);
                testTank.transform.setTranslation(pos);
            }


            if(rotate)
            {
                   // Gdx.app.log("tankGenerator log ", " Check! " +  " a "  +angle + " la " + lastAngle);
                        if(lastAngle<=-148 && angle>=150) { lastAngle =360+lastAngle; }


                        if (lastAngle < angle) {
                            dAngle=1;
                        }

                        if (lastAngle > angle)// && lastAngle>0 && angle>0)
                        {
                           dAngle=-1;
                        }


                    lastAngle +=dAngle* rotSpeed * delta;
                    //Gdx.app.log("tankGenerator log ", " ---> Check!? " +  " a "  +angle + " la " + lastAngle);
                    if (isRotateReached(lastAngle, angle, delta)) {
                        walk = true;
                        rotate = false;
                        lastAngle = angle;

                    }

                    testTank.transform.setToRotation(Vector3.Y, lastAngle);
                    testTank.transform.scale(0.01f, 0.01f, 0.01f);
                    testTank.transform.setTranslation(pos);
               // }

            }

        if (isWayPointReached(pos, index,delta)) {


            if(index<path.size()-1)
            {
                index++;
                vel.set(map.getHexGridCells().get(path.get(index)).getWorldPosition()).sub(pos);
                angle = MathUtils.atan2(vel.x, vel.z)* MathUtils.radiansToDegrees;
                angle =  new BigDecimal(angle).setScale(0, RoundingMode.HALF_UP).intValue();
                walk = false;
                rotate =true;
            }
            else
            {
                stop=true;

            }
        }
            //Gdx.app.log("tankGenerator log ", " ==========>  Check! " +index+" "+pos + " a "  +angle + " la " + lastAngle);
    }else
        {
            loop = true;
        }
    if(loop){
        start=true;
    }
*/


        /*
        modelBatch.render(testTank,environment);
        modelBatch.render(testTank1,environment);
*/


        //for(HexGridCell hexagon:map.getHexGridCells())
        //{
            for(GameObject go:gameObjects) {
                modelBatch.render(go.getGameObjectInstance(),environment);
            }
        //}
        //modelBatch.render(environmentInstance,environment);
        //modelBatch.render(treeInstance,environment);


       /* for(ModelInstance t:tiles)
        {
            modelBatch.render(t);
        }*/
        //if(!touch.isZero())
                              // modelBatch.render(selector);




        modelBatch.end();



        // if(sel!=null) sel.debug();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        /*
        shapeRenderer.setColor(Color.GREEN);

        for(HexGridCell hexagon:map.getHexGridCells())
        {


            if(hexagon.isWalkable())
            {
                Hexagon hex = map.getHexagons().get(hexagon.getIndex());
                for (int i = 0; i < hex.getTriangles().size(); i++) {
                    TileTriangle tileTriangle = hex.getTriangles().get(i);
                    shapeRenderer.line(tileTriangle.getA(), tileTriangle.getB());
                }
            }

        }*/
        shapeRenderer.setColor(Color.WHITE);



        for(HexGridCell hexagon:defaultLayerMap.getCells())
        {


           // if(!hexagon.isWalkable()){
                int size= defaultLayerMap.getCells().get(hexagon.getIndex()).getTriangles().size();
                //Hexagon hex = map.getHexagons().get(hexagon.getIndex());
                for (int i = 0; i < size; i++) {
                    TileTriangle tileTriangle = defaultLayerMap.getCells().get(hexagon.getIndex()).getTriangles().get(i);
                    shapeRenderer.line(tileTriangle.getA(), tileTriangle.getB());
                }
           // }

        }

/*
        for(int i=0;i<map.getPoints().size();i++)
        {

            shapeRenderer.line(map.getPoints().get(i), new Vector3(map.getPoints().get(i).x,10,map.getPoints().get(i).z));
        }
*/
/*
        shapeRenderer.setColor(Color.WHITE);

        for (Integer pa:walkable)
        {
            //Hexagon hex = map.getHexagons().get(pa);
            int size= defaultLayerMap.getCells().get(pa).getTriangles().size();

            for (int i = 0; i < size; i++) {
                TileTriangle tileTriangle = defaultLayerMap.getCells().get(pa).getTriangles().get(i);
                shapeRenderer.line(tileTriangle.getA(), tileTriangle.getB());
            }
            // Gdx.app.log("tankGenerator log ","index "+p+ " " + map.getHexGridCells().get(p).getMapPosition());
        }
/*
        shapeRenderer.setColor(Color.GREEN);

        for (Integer pa:path)
        {
            Hexagon hex = map.getHexagons().get(pa);
            for (int i = 0; i < hex.getTriangles().size(); i++) {
                TileTriangle tileTriangle = hex.getTriangles().get(i);
                shapeRenderer.line(tileTriangle.getA(), tileTriangle.getB());
            }
           // Gdx.app.log("tankGenerator log ","index "+p+ " " + map.getHexGridCells().get(p).getMapPosition());
        }

*/

        shapeRenderer.setColor(Color.YELLOW);
        if(selected!=null)
        {

            for(int i=0;i<selected.getTriangles().size();i++)
            {
                TileTriangle tileTriangle = selected.getTriangles().get(i);
                shapeRenderer.line(tileTriangle.getA(), tileTriangle.getB());
                //shapeRenderer.line(tileTriangle.getA(), tileTriangle.getC());

                shapeRenderer.line(tileTriangle.getC(), new Vector3(tileTriangle.getC().x,tileTriangle.getC().y+4,tileTriangle.getC().z));

            }
        }


        shapeRenderer.end();




        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();


    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {



/*
        float distance = -collisionRay.origin.y / collisionRay.direction.y;
        Vector3 tmpVector = new Vector3();
        tmpVector.set(collisionRay.direction).scl(distance).add(collisionRay.origin);
        Vector2 a= new Vector2(tmpVector.x,tmpVector.z);
        selectCoordinate = h.TileCenter<=(a);
        selector.transform.setTranslation(tmpVector.x,0.5f,tmpVector.z);
        */

        //boolean intersectionOccured = false;


       /* public boolean intersectRayMeshes(Ray ray, List meshes, Vector3 globalIntersection) {

boolean intersectionOccured = false;

// for all Meshes in List
        for (float[] mesh : meshes) {
            if (Intersector.intersectRayTriangles(ray, mesh, localIntersection))
            { intersectionOccured = true; Log.out("Intersection Occured!"); // update globalIntersection only if // it is closer to the screen as the // intersection point we got earlier // and there was an intersection yet at all if (globalIntersection != null) { Log.out("Local intersection occured!"); if (ray.origin.sub(localIntersection).len() < ray.origin .sub(globalIntersection).len()) { Log.out("updated global intersection"); globalIntersection.set(localIntersection); } } else { Log.out("First time setting global intersection!"); globalIntersection.set(localIntersection); } } }

            if (intersectionOccured) { return true; } else { return false; } }
            public boolean intersectRayMeshes(Ray ray, List meshes, Vector3 intersection, String meshId) {
                boolean intersectionOccured = false;
                Vector3 localIntersection = new Vector3(0, 0, 0);
                intersection = new Vector3(0, 0, 0);
                meshId = null;
*/
// for all Meshes in List
        if(lastSelectedIndex==-1) {
            collisionRay = camera.getPickRay(screenX, screenY);
            selected = intersectRayWithTerrain(collisionRay);
            if (selected != null) {

                lastSelectedIndex = selected.getIndex();
                window.setVisible(true);
                HexGridCell cell = defaultLayerMap.getCells().get(selected.getIndex());
                String pObject = cell.getPlacedObject();


                if(pObject!=null) {
                    Array<String> array = placedObject.getItems();
                    int placedObjectIndex = 0;


                    for (int i = 0; i < array.size; i++) {
                        if (array.get(i).equalsIgnoreCase(pObject)) {
                            placedObjectIndex = i;
                        }
                    }
                    placedObject.setSelectedIndex(placedObjectIndex);
                }
                else
                {
                    placedObject.setSelectedIndex(0);
                }
                Integer oObject =(int)cell.getOrientation();
                if(oObject!=null && rotationChange) {

                    Array<Integer> arrayOfOrientations = orientationObject.getItems();
                    int orientationObjectIndex = 0;
                    for (int i = 0; i < arrayOfOrientations.size; i++) {
                        {

                           Integer item =arrayOfOrientations.get(i);
                            if (item.equals(oObject)) {
                                orientationObjectIndex = i;
                            }
                        }

                    }

                    orientationObject.setSelectedIndex(orientationObjectIndex);
                    for(GameObject go: gameObjects)
                    {
                        Gdx.app.log("tankGenerator log ",go.getId()+" : "+ cell.getIndex());
                        if(go.getId()==cell.getIndex())
                        {
                           // if(rotationChange)
                                go.getGameObjectInstance().transform.rotate(new Vector3(0, 1, 0), cell.getOrientation());
                        }
                    }
                    rotationChange=false;
                }else

                {
                    orientationObject.setSelectedIndex(0);
                }
                        checkBox.setChecked(defaultLayerMap.getCells().get(selected.getIndex()).isWalkable());//map.getHexGridCells().get(selected.getIndex()).isWalkable());

                        tileIndex.setText("Index: " + selected.getIndex() + " " + selected.getMapPosition() + " " + selected.getWorldPosition());
                        if (lIndex > -1) {
                            // v2Tmp.set((map.getHexGridCells().get(lIndex).getMapPosition()).y-selected.getHexPosition().y, (map.getHexGridCells().get(lIndex).getMapPosition()).x-selected.getHexPosition().x);
                            // angle = MathUtils.atan2((map.getHexGridCells().get(lIndex).getMapPosition()).y-selected.getHexPosition().y, (map.getHexGridCells().get(lIndex).getMapPosition()).x-selected.getHexPosition().x)* MathUtils.radiansToDegrees;
                            //Gdx.app.log("tankGenerator log ", "selected " + selected.getIndex() + " " + selected.isWalkable() + " " + selected.getHexPosition() + " " + (map.getHexGridCells().get(lIndex).getMapPosition())+" "+v2Tmp);// + " " + selected.getCenter().toString() + " " + pObject);
                            // Gdx.app.log("tankGenerator log ", "selected " + selected.getIndex() + " " + v2Tmp+ " "+ (int)angle);// + " " + selected.getCenter().toString() + " " + pObject);
                        }
                        //window.setVisible(true);
                        lIndex = selected.getIndex();
                    }

        }

        return true;


    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

       // if(selected!=null)
        //controller.target.set(selected.getCenter());
        lastSelectedIndex=-1;

        return true;
    }
    void updateUnitCamera(Vector3 pos,float camPathAngle,float camPathAngle1){

        camera.position.set(12f, 8f+camPathAngle1, 2f)
                .rotate(Vector3.Y, camPathAngle).add(pos);//.rotate(Vector3.X,camPathAngle1)
        camera.up.set(Vector3.Y);
        camera.lookAt(pos);
        camera.update();

    }

    void updateTreeCamera(HexGridCell pos,float camPathAngle,float camPathAngle1){

        camera.position.set(12f, 8f+camPathAngle1, 2f)
                .rotate(Vector3.Y, camPathAngle).add(pos.getWorldPosition());//.rotate(Vector3.X,camPathAngle1)
        camera.up.set(Vector3.Y);
        camera.lookAt(pos.getWorldPosition());
        camera.update();
    }


Vector3 tmpV1 = new Vector3();
    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {

        //final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        //deltaY += (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
         deltaX += Gdx.input.getDeltaX() * 0.5f;
         deltaY += Gdx.input.getDeltaY() * 0.1f;
        if(deltaY<-1.3f) deltaY=-1.3f;
        if(deltaY>60f) deltaY=60f;

       if(lastSelectedIndex!=-1) {
           if (selected != null) {
               //if (map.getHexGridCells().get(selected.getIndex()).getPlacedObject() != null) {


                    //camera.translate(tmpV1.set(camera.direction).scl(amount));
                   //camera.position.set(selected.getCenter().x, 30f, selected.getCenter().z);
                   //camera.lookAt(selected.getCenter());
                   //camera.rotateAround(selected.getCenter(), new Vector3(0,1,0), deltaX);
                   //camera.rotateAround(selected.getCenter(), new Vector3(0,0,1), deltaX);
               //updateTreeCamera(selected,deltaX,deltaY);
                  /* dir.set(camera.direction).crs(camera.up).y = 0f;

                   camera.rotateAround(selected.getCenter(), dir, deltaY * 360f);
                   camera.rotateAround(selected.getCenter(), Vector3.Y, deltaX * 360f);
*/

                   //camera.rotate(new Vector3(1,0,1),deltaX);

                   // camera.rotateAround(selected.getCenter(), new Vector3(0, 0, 1), deltaY);
                   //camera.update();
               //}
           }
       }


        return true;
    }

    public GameObject loadGameObject(HexGridCell world)
    {
        JsonReader jsonReader = new JsonReader();
        G3dModelLoader loadermodel = new G3dModelLoader(jsonReader);
        Model tModel = loadermodel.loadModel(Gdx.files.internal(world.getPlacedObject() + ".g3dj"));

        ModelInstance tModelInstance = new ModelInstance(tModel);

        tModelInstance.transform.translate(world.getWorldPosition());
        tModelInstance.transform.rotate(new Vector3(0, 1, 0), world.getOrientation());


        for (Material mat : tModelInstance.materials) {
            mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
            mat.set(new FloatAttribute(FloatAttribute.AlphaTest, 0.1f));

        }
        GameObject newObj = new GameObject();
        newObj.setId(world.getIndex());
        newObj.setGameObjectInstance(tModelInstance);
        newObj.setNameOfObject(world.getPlacedObject());

        return newObj;
    }



    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


/*
    public void calculateNormals() {
        //Gdx.app.log("Debug", "Indices: " + indices.length + " Vertex: " + vertexsList.size());
        if (indices.length <= 3) {
            return;
        }
        for (int i = 0; i < indices.length / 3; i++) {
            int index1 = indices[i * 3];
            int index2 = indices[i * 3 + 1];
            int index3 = indices[i * 3 + 2];

            Vector3 side1   = this.vertexsList.get(index1).position.cpy().sub(this.vertexsList.get(index3).position);
            Vector3 side2   = this.vertexsList.get(index1).position.cpy().sub(this.vertexsList.get(index2).position);
            Vector3 normal  = side1.crs(side2);

            this.vertexsList.get(index1).normal.add(normal);
            this.vertexsList.get(index2).normal.add(normal);
            this.vertexsList.get(index3).normal.add(normal);
        }
    }
*/
    Vector3 tmp = new Vector3();
    Vector3 tmp1 = new Vector3();
    Vector3 tmp2 = new Vector3();
    Vector3 tmp3 = new Vector3();


    public HexGridCell intersectRayWithTerrain(Ray ray) {
        //defaultLayerMap.getCells().get(selected.getIndex())


        for(HexGridCell hexagon:defaultLayerMap.getCells())//map.getHexagons())
        {


           // for(Hexagon hexagon:map.getHexagons()) {

          /// if(map.getHexGridCells().get(hexagon.getIndex()).isWalkable()) /// to consider when developing
            for (int i = 0; i < hexagon.getTriangles().size(); i++) {
                TileTriangle tr= hexagon.getTriangles().get(i);
                boolean result = Intersector.intersectRayTriangle(ray, tr.getA(), tr.getB(),tr.getC(), tmp);
                if(result) return hexagon;
            }
        }





        return null;

    }


    public ArrayList<Vector3> intersectRayWithMesh(Ray ray, float[] vertices, short[] indices, int vertexSize) {
        float min_dist = Float.MAX_VALUE;
        boolean hit = false;
        ArrayList<Vector3> outArray = new ArrayList<Vector3>(3);
        if (indices.length % 3 != 0)
            throw new RuntimeException("triangle list size is not a multiple of 3");

        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i] * vertexSize;
            int i2 = indices[i + 1] * vertexSize;
            int i3 = indices[i + 2] * vertexSize;

            boolean result = Intersector.intersectRayTriangle(ray, tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]),
                    tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]),
                    tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]), tmp);

            if (result == true) {

                outArray.add(tmp1);
                outArray.add(tmp2);
                outArray.add(tmp3);
                return outArray;
            }
        }


        return null;

    }

    boolean isPosition(int q,int r,float cx,float cz)
    {
        float tileSizeL = 7.5f;
        float tileSizeH = 8.66f;


        float qx= q*tileSizeH;
        float ry = r*tileSizeL;
        float x = (float) (10/2f * Math.sqrt(3f) * (r + q / 2f));
        float z = 10/2f * 3f/2f * q;
        //Gdx.app.log("tankGenerator log ", x + " "+ z + " "+ getCenter().x+ " "+getCenter().z+ " "+ q+ " "+ r);
        // if(xh == q && yh==r)
        //String out = x + " "+ z + " "+ getCenter().x+ " "+getCenter().z+ " "+ q+ " "+ r+"\n";
        if(cx>=(x-0.2f) && cx<=(x+0.2f) && cz>=(z-0.2f) && cz<=(z+0.2f))
        {

            //return new Vector2(q,r);
            return true;
            //return hexPosition;
        }
        return false;
    }

    public void constructMap(ModelInstance mapInstance)
    {

        float[] vertices = new float[0];
        short[] indices = new short[0];
        int vertexSize =0;

        defaultLayerMap = new DefaultLayerMap();

        mapGridCells = new ArrayList<HexGridCell>();
        for(int i=0;i<mapInstance.model.meshes.size;i++)
        {

            vertexSize = mapInstance.model.meshes.get(i).getVertexSize() / 4;
            vertices = new float[mapInstance.model.meshes.get(i).getNumVertices() * mapInstance.model.meshes.get(i).getVertexSize() / 4];
            indices = new short[mapInstance.model.meshes.get(i).getNumIndices()];


            mapInstance.model.meshes.get(i).getIndices(indices);
            mapInstance.model.meshes.get(i).getVertices(vertices);
        }

        Gdx.app.log("tankGenerator log ","indices.length = "+indices.length);
        ArrayList<TileTriangle> triangles = new ArrayList<TileTriangle>();
        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i] * vertexSize;
            int i2 = indices[i + 1] * vertexSize;
            int i3 = indices[i + 2] * vertexSize;
            Vector3 tmp1 = new Vector3();
            Vector3 tmp2 = new Vector3();
            Vector3 tmp3 = new Vector3();
            /*
            tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]);
            tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]);
            tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]);*/

            tmp1.set(vertices[i1], 0, vertices[i1 + 2]);
            tmp2.set(vertices[i2], 0, vertices[i2 + 2]);
            tmp3.set(vertices[i3], 0, vertices[i3 + 2]);

            triangles.add(new TileTriangle(tmp1,tmp2,tmp3));
        }


        ArrayList<Hexagon> hexagons =  new ArrayList<Hexagon>();
        //hexGridCells = new ArrayList<HexGridCell>();
        for(int i=0;i<triangles.size();i+=6)
        {


            HexGridCell hexGridCell = new HexGridCell();
            hexGridCell.setIndex(mapGridCells.size());
                ArrayList<TileTriangle> tileTriangles = new ArrayList<TileTriangle>();
                    tileTriangles.add(triangles.get(i));
                    tileTriangles.add(triangles.get(i+1));
                    tileTriangles.add(triangles.get(i+2));
                    tileTriangles.add(triangles.get(i+3));
                    tileTriangles.add(triangles.get(i+4));
                    tileTriangles.add(triangles.get(i+5));
            hexGridCell.setTriangles(tileTriangles);
            hexGridCell.setType(0);
            hexGridCell.setHeight(0);

            double cx = new BigDecimal(triangles.get(i).getC().x).setScale(1, RoundingMode.UP).doubleValue();
            double cz = new BigDecimal(triangles.get(i).getC().z).setScale(1, RoundingMode.UP).doubleValue();


            hexGridCell.setWorldPosition(new Vector3((float)cx,0,(float)cz));
            hexGridCell.setMapPosition(new Vector2(0,0));
            hexGridCell.setPlacedObject(null);
            mapGridCells.add(hexGridCell);
            //defaultLayerMap.addCell(hexGridCell);


        }
        //defaultLayerMap.setCells(mapGridCells);


        //int hexSize=defaultLayerMap.getCells().size();//mapGridCells.size();

        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {

                //Gdx.app.log("tankGenerator log ",(t++)+" " +q+ " : " +r);
                //Vector3 tmp =GetPositionVector(10f,q,r);
                //points.add(tmp);
                //points.add(new Vector3(q*8.66f,0,r*7.5f));
                for(int i=0;i<mapGridCells.size();i++)
                {

                    HexGridCell hexGridCell = mapGridCells.get(i);
                    if(isPosition(q,r,hexGridCell.getWorldPosition().x,hexGridCell.getWorldPosition().z)) {
                        hexGridCell.setMapPosition(new Vector2(q, r));


                    }

                }

            }
        }
        for(int i=0;i<mapGridCells.size();i++)
        {
            mapGridCells.get(i).getWorldPosition().y=0;
            defaultLayerMap.addCell(mapGridCells.get(i));
        }


        //defaultLayerMap.setCells(mapGridCells);
/*
        PrintWriter outTxt;
        try {
            outTxt = new PrintWriter(new BufferedWriter(new FileWriter("map.txt", false)));
            outTxt.println(out);
            outTxt.close();

        } catch (IOException ex) {

        }

*/
/*
        Json json = new Json();
        //json.addClassTag("DefaultTank", DefaultTank.class); //As above
        json.setIgnoreUnknownFields(false);
        json.setUsePrototypes(false);
        json.setOutputType(OutputType.json);

        String jsonString = json.prettyPrint(defaultLayerMap);

        FileHandle file = Gdx.files.local("levelMap01a.json");
        file.writeString(jsonString , false);
        */


    }


    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
