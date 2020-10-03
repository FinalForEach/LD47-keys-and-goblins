package finalforeach.ld47;

import java.util.function.Predicate;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.Player;
import finalforeach.ld47.tiles.IUpdateDelta;
import finalforeach.ld47.tiles.Tile;
import finalforeach.ld47.tiles.TileMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter 
{
	public static TileMap tileMap;
	public static OrthographicCamera camera;
	public static SpriteBatch batch;
	public static SpriteBatch batchDebug;

	private Viewport viewport;
	private InputHandler inputHandler;
	
	public static Player player;

	@Override
	public void create() {
		batch = new SpriteBatch();
		batchDebug = new SpriteBatch();
		Tile.load();
		Entity.load();
		tileMap = new TileMap();

		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 786, camera);
		camera.setToOrtho(false);
		camera.position.set(0,0,0);
		camera.zoom =0.15f;
		camera.update();
		
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		
		player = new Player(tileMap.spawnLoc.x,tileMap.spawnLoc.y);
		Entity.entities.add(player);
		
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	public void handleInput(double deltaTime) 
	{
		inputHandler.update();
	}
	@Override
	public void render() 
	{
		double deltaTime = Gdx.graphics.getDeltaTime();
		handleInput(deltaTime);
		Entity.updateAllEntities(deltaTime);
		for(IUpdateDelta t : tileMap.updatingTiles) 
		{
			t.update(deltaTime);
		}
		tileMap.updatingTiles.removeIf(new Predicate<IUpdateDelta>() {
			@Override
			public boolean test(IUpdateDelta u) {
				return !u.IsActive();
			}

		});
		
		camera.position.set(player.bb.getCenterX(),player.bb.getCenterY(),0);
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tileMap.draw(batch);
		Entity.drawAllEntities(batch);
		
		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
		Tile.dispose();
		Entity.dispose();
	}
}