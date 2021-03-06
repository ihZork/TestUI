package ru.blindspace.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

import com.badlogic.gdx.Game;
import ru.blindspace.game.Screens.GameScreen;
import ru.blindspace.game.Screens.LoadScreen;
import ru.blindspace.game.Screens.TestMapScreen;

//public class testUIGame extends ApplicationAdapter {
public class testUIGame extends Game {
	GameScreen gameScreen;
	LoadScreen loadScreen;
	TestMapScreen testMapScreen;


	private static testUIGame instance = new testUIGame();

	public static  testUIGame getInstance()
	{
		return instance;
	}



	@Override
	public void create() {

		//gameScreen = new GameScreen();
		//setScreen(gameScreen);
		//gameScreen = new GameScreen();
		//setScreen(gameScreen);
		//loadScreen = new LoadScreen();
		//setScreen(loadScreen);

		testMapScreen = new TestMapScreen();
		setScreen(testMapScreen);

		dispose();

	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	@Override
	public void dispose() {
		super.dispose();
		//manager.dispose();
	}

	@Override
	public void render() {
		super.render();

	}
}

	/*

	SpriteBatch batch;
	Texture texture;
	Sprite sprite;
	Mesh mesh;
	ShaderProgram shaderProgram;

	@Override
	public void create () {
		batch = new SpriteBatch();
		texture = new Texture("badlogic.jpg");
		sprite = new Sprite(texture);
		sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		float[] verts = new float[30];
		int i = 0;
		float x,y; // Mesh location in the world
		float width,height; // Mesh width and height

		x = y = 50f;
		width = height = 300f;

		//Top Left Vertex Triangle 1
		verts[i++] = x;   //X
		verts[i++] = y + height; //Y
		verts[i++] = 0;    //Z
		verts[i++] = 0f;   //U
		verts[i++] = 0f;   //V

		//Top Right Vertex Triangle 1
		verts[i++] = x + width;
		verts[i++] = y + height;
		verts[i++] = 0;
		verts[i++] = 1f;
		verts[i++] = 0f;

		//Bottom Left Vertex Triangle 1
		verts[i++] = x;
		verts[i++] = y;
		verts[i++] = 0;
		verts[i++] = 0f;
		verts[i++] = 1f;

		//Top Right Vertex Triangle 2
		verts[i++] = x + width;
		verts[i++] = y + height;
		verts[i++] = 0;
		verts[i++] = 1f;
		verts[i++] = 0f;

		//Bottom Right Vertex Triangle 2
		verts[i++] = x + width;
		verts[i++] = y;
		verts[i++] = 0;
		verts[i++] = 1f;
		verts[i++] = 1f;

		//Bottom Left Vertex Triangle 2
		verts[i++] = x;
		verts[i++] = y;
		verts[i++] = 0;
		verts[i++] = 0f;
		verts[i] = 1f;

		// Create a mesh out of two triangles rendered clockwise without indices
		mesh = new Mesh( true, 6, 0,
				new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
				new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

		mesh.setVertices(verts);
		shaderProgram.pedantic = false;
		shaderProgram = new ShaderProgram(
				Gdx.files.internal("vertex.glsl").readString(),
				Gdx.files.internal("fragment.glsl").readString()
		);
	}

	@Override
	public void render () {

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		batch.begin();
		sprite.draw(batch);
		batch.end();

		texture.bind();
		shaderProgram.begin();
		shaderProgram.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
		shaderProgram.setUniformi("u_texture", 0);
		mesh.render(shaderProgram, GL20.GL_TRIANGLES);
		shaderProgram.end();
	}

}*/
