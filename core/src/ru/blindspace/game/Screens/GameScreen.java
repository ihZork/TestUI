package ru.blindspace.game.Screens;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Files;
import  com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.Node;

import java.util.ArrayList;

import ru.blindspace.game.Hexagon4;

public class GameScreen implements Screen, InputProcessor {


    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private Model modelTurret;
    private ModelInstance modelInstance;
    private ArrayList<ModelInstance> modelTuretInstance;
    private Environment environment;





    private FrameBuffer fbo;
    private SpriteBatch fboBatch;

    private CameraInputController controller;


    private Hexagon4 h;
    public  int radius =5;
    int [] map = {
            -2, -2,-1,-2, 0,-2, 1,-2, 2,-2,
            -2, -1,-1,-1, 0,-1, 1,-1, 2,-1,
            -2,  0,-1, 0, 0, 0, 1, 0, 2, 0,
            -2,  1,-1, 1, 0, 1, 1, 1, 2, 1,
            -2,  2,-1, 2, 0, 2, 1, 2, 2, 2,

    };




    SpriteBatch batch;
    Texture texture;
    TextureRegion buffer;
    Sprite sprite;

    ShaderProgram shaderProgram;


    public void initializeFBO() {
        if(fbo != null) fbo.dispose();
        fbo = new FrameBuffer(Pixmap.Format.RGBA4444, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
       // fbo.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        buffer = new TextureRegion( fbo.getColorBufferTexture());
        buffer.flip(false,true);
        if(fboBatch != null) fboBatch.dispose();
        fboBatch = new SpriteBatch();
    }


    public void ModelInstanceLog(Node _modelInstance,String log)
    {

        log+="     ";
        for(int j=0;j<_modelInstance.getChildCount();j++) {

            Gdx.app.log("tankGenerator log ", log+"|___ " + _modelInstance.getChild(j).id);
            Gdx.app.log("tankGenerator log ", log+"|___ " + _modelInstance.getChild(j).translation);
            ModelInstanceLog(_modelInstance.getChild(j), log);
        }

    }

    public GameScreen() {

        camera = new PerspectiveCamera(75,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 300f);
        camera.lookAt(0f, 0f, 0f);
        camera.near=0.1f;
        camera.far=1500f;
        camera.update();
        controller = new CameraInputController(camera);

        initializeFBO();

/*
        batch = new SpriteBatch();

        sprite = new Sprite();
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shaderProgram.pedantic = false;
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("vertex.glsl").readString(),
                Gdx.files.internal("fragment.glsl").readString()
        );

        if(!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }

*/
        h = new Hexagon4(10.2f);

        ArrayList<Vector3> tileCoordinates = new ArrayList<Vector3>();

        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {
                //map.insert(Hex(q, r, -q-r));
                tileCoordinates.add(new Vector3(q + (r >> 1), 0, r));
            }
        }

        shaderProgram.pedantic = false;
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("vertex.glsl").readString(),
                Gdx.files.internal("fragment.glsl").readString()
        );

        modelBatch =new ModelBatch();
        // UBJsonReader jsonReader = new UBJsonReader();
        JsonReader jsonReader = new JsonReader();
        G3dModelLoader  modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.internal("frigate_v1_0004_0000_16.g3dj"));




        modelInstance = new ModelInstance(model);

        //modelInstance.transform.scl(0.01f);

        for (Material mat : modelInstance.materials) {
            //mat.remove(IntAttribute.CullFace);
            mat.set(new IntAttribute(IntAttribute.CullFace, 0));
            mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        }


        Gdx.app.log("FPS",""+ modelInstance.getNode("pivot_0001").localTransform+" "+modelInstance.getNode("pivot_0001").calculateWorldTransform());

        modelTurret = modelLoader.loadModel(Gdx.files.internal("untitled.g3dj"));



        modelTuretInstance = new ArrayList<ModelInstance>();
        modelTuretInstance.add(new ModelInstance(modelTurret));
        modelTuretInstance.add(new ModelInstance(modelTurret));
        modelTuretInstance.add(new ModelInstance(modelTurret));
        //modelTuretInstance.add(new ModelInstance(modelTurret));
/*
        modelTuretInstance.get(0).transform.set(modelInstance.getNode("pivot_0001").globalTransform);

        modelTuretInstance.get(1).transform.set(modelInstance.getNode("pivot_0002").globalTransform);
        modelTuretInstance.get(2).transform.set(modelInstance.getNode("pivot_0003").globalTransform);
       // modelTuretInstance.get(3).transform.set(modelInstance.getNode("pivot_0004_left").globalTransform);

        //modelTuretInstance.get(3).transform.rotate(0,0,1,90f);
*/

        for(int i=0;i<3;i++) {
          //  modelTuretInstance.get(i).transform.scl(0.4f);
            for (Material mat : modelTuretInstance.get(i).materials) {
                //mat.remove(IntAttribute.CullFace);
                mat.set(new IntAttribute(IntAttribute.CullFace, 0));
                mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            }
        }



        modelInstance.getNode("pivot_0001").addChild(modelTuretInstance.get(0).getNode("protoTurret_001_low"));
        modelInstance.getNode("pivot_0002").addChild(modelTuretInstance.get(1).getNode("protoTurret_001_low"));

        Gdx.app.log("modelInstance.getNode(pivot_0001)", " m1 : " + modelInstance.getNode("pivot_0001").translation);
        Gdx.app.log("modelInstance.getNode(pivot_0001)", " m1 : " + modelInstance.getNode("pivot_0001").rotation);



        //modelInstance.getNode("pivot_0001").getChild(0).globalTransform.mul(modelInstance.getNode("pivot_0001").localTransform);
        modelInstance.getNode("pivot_0001").getChild(0).globalTransform.set(modelInstance.getNode("pivot_0001").translation,modelInstance.getNode("pivot_0001").getChild(0).rotation,new Vector3(0.5f,0.5f,0.5f));
        modelInstance.getNode("pivot_0002").getChild(0).globalTransform.set(modelInstance.getNode("pivot_0002").translation, modelInstance.getNode("pivot_0002").getChild(0).rotation, new Vector3(0.5f,0.5f,0.5f));

        //modelInstance.getNode("pivot_0001").getChild(0).localTransform.scl(0.4f);
       modelInstance.getNode("pivot_0001").calculateWorldTransform();
       // modelInstance.getNode("pivot_0001").getChild(0).scale.scl(0.4f);

        Gdx.app.log("Matrixes", " m1 : " + modelInstance.getNode("pivot_0001").getChild(0).globalTransform + " \r\nm2: " + modelInstance.getNode("pivot_0001").getChild(0).localTransform + " "
                + modelInstance.getNode("pivot_0001").getChild(0).globalTransform.getScaleX());


        //modelInstance.getNode("pivot_0003").addChild(modelTuretInstance.get(1).getNode("FrigateTurretLowPower_low"));
       // modelInstance.getNode("FrigateTurretLowPower_low").globalTransform.rotate(Vector3.Y, 0.2f);;
/*
        for(int i=0;i<modelInstance.nodes.size;i++) {
       //     Gdx.app.log("FPS", "" + modelInstance.nodes.items.length);
            Gdx.app.log("FPS", "" + modelInstance.nodes.get(i).id);
                    for(int j=0;j<modelInstance.nodes.get(i).getChildCount();j++) {
                        Gdx.app.log("FPS", " |___ " + modelInstance.nodes.get(i).getChild(j).id);

                        Gdx.app.log("FPS", " |___ " + modelInstance.nodes.get(i).getChild(j).globalTransform);
                    }
        }
*/
        for(int i=0;i<modelInstance.nodes.size;i++) {

            Gdx.app.log("testUIGame log ", "-" + modelInstance.nodes.get(i).id);
            //for (int j = 0; j < modelInstance.nodes.get(i).getChildCount(); j++) {

                ModelInstanceLog(modelInstance.nodes.get(i), " ");
            //}
        }

        // modelInstance.getMaterial("Diffuse").set(new TextureAttribute((TextureAttribute.Diffuse),new Texture("fv1_0004_00014_texture.png")));
        environment = new Environment();
        //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 08f, 1f));
        environment.add(new DirectionalLight().set(Color.WHITE, 300f, -300f, 0f));
        //environment.add(new PointLight().set(Color.WHITE, 0f, 0f, 0f, 5));


        Gdx.input.setInputProcessor(controller);
        //Gdx.input.setInputProcessor(this);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


       // Gdx.app.log("FPS", "" + Gdx.graphics.getFramesPerSecond() + " " + Gdx.graphics.getDeltaTime());
        modelInstance.transform.rotate(Vector3.X, 0.02f);
       // modelInstance.transform.translate(0.0f,0.0f,0.1f);
       // modelInstance.getNode("FrigateTurretLowPower_low").globalTransform.rotate(Vector3.Y, 0.2f);
        modelInstance.getNode("pivot_0001").getChild(0).globalTransform.rotate(Vector3.Y, 0.2f);
        modelInstance.getNode("pivot_0002").getChild(0).globalTransform.rotate(Vector3.Y, -0.2f);
        controller.update();
        //camera.update();

       // sprite.setRegion(scene.getColorBufferTexture());
       // sprite.flip(false, true);


      /*  batch.begin();
        sprite.draw(batch);
        batch.end();
*/

//        texture.bind();

    /*    for(int i=0;i<3;i++)
            modelTuretInstance.get(i).transform.rotate(Vector3.Y, 0.2f);
        //modelTuretInstance.get(3).transform.rotate(Vector3.Y, 0.2f);
*/
        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        fboBatch.setShader(shaderProgram);

       // texture.bind();
       //     shaderProgram.begin();
     //   shaderProgram.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
      //  shaderProgram.setUniformi("u_texture", 0);
                modelBatch.begin(camera);
                modelBatch.render(modelInstance);//, environment);
             //   modelBatch.render(modelTuretInstance);//, environment);
                modelBatch.end();

      //      shaderProgram.end();

        fbo.end();
        fboBatch.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        fboBatch.draw(buffer, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //fboBatch.draw(fbo.getColorBufferTexture(),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        fboBatch.end();




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

        modelBatch.dispose();
        model.dispose();

        fbo.dispose();
        fboBatch.dispose();
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    private int dragX, dragY;
    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        dragX = x;
        dragY = y;
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
/*
        float dX = (float)(x-dragX)/(float)Gdx.graphics.getWidth();
        float dY = (float)(dragY-y)/(float)Gdx.graphics.getHeight();
        dragX = x;
        dragY = y;
        camera.position.add(dX * 10f, dY * 10f, 0f);
       // camera.rotate(camera.up,);
        camera.update();
        return true;
/*
        int diffX = dragX - x;
        int diffY = dragY - y;

        Vector3 lookAtPoint = new Vector3();

        float orbitRadius = lookAtPoint.dst(camera.position);

        camera.position.set(lookAtPoint);
        camera.rotate(diffX, 0, 1, 0);
        camera.rotate(diffY, 1, 0, 0);

        Vector3 orbitReturnVector = new Vector3();
        orbitReturnVector.set(camera.direction.tmp().mul(-orbitRadius));

        camera.translate(orbitReturnVector.x, orbitReturnVector.y, orbitReturnVector.z);


        dragX = x;
        dragY = y;
        return true;*/
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }





}
