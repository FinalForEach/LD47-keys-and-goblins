package finalforeach.ld47;

import java.util.function.Predicate;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.Player;
import finalforeach.ld47.tiles.IUpdateDelta;
import finalforeach.ld47.tiles.Tile;
import finalforeach.ld47.tiles.TileMap;
import finalforeach.ld47.tiles.TileMap.LevelTheme;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter 
{
	public static TileMap tileMap;
	public static OrthographicCamera camera;
	public static OrthographicCamera uiCamera;
	public static SpriteBatch batch;
	public static SpriteBatch batchDebug;

	public static Viewport viewport;
	private Viewport uiViewport;
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
		
		uiCamera = new OrthographicCamera();
		uiViewport = new FitViewport(1280, 786, uiCamera);
		uiCamera.setToOrtho(false);
		uiCamera.position.set(1280/2,-300,0);
		//uiCamera.zoom =0.15f;
		uiCamera.update();
		
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		
		player = new Player(tileMap.spawnLoc.x,tileMap.spawnLoc.y);
		Entity.entities.add(player);
		
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		uiViewport.update(width, height);
	}
	public void handleInput(double deltaTime) 
	{
		inputHandler.update();
	}
	@Override
	public void render() 
	{
		uiCamera.position.set(1280/2,-768/2,0);
		double deltaTime = Gdx.graphics.getDeltaTime();
		handleInput(deltaTime);
		Entity.updateAllEntities(deltaTime);
		Array<Entity> deadEntities = null;
		for(int i=0; i< Entity.entities.size; i++) 
		{
			Entity e = Entity.entities.get(i);
			if(e.dead) 
			{
				if(deadEntities==null)deadEntities = new Array<>();
				deadEntities.add(e);
			}
		}
		if(deadEntities!=null) 
		{
			Entity.entities.removeAll(deadEntities, true);	
		}
		
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
		

		uiCamera.update();
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		// Full hearts
		for(int i=0; i< player.getHP(); i++) 
		{
			batch.draw(Entity.tex, 2 + (i*1.05f)*64, -64, 
					64, 64, 0, 16, 16, 16, false, false);
		}
		if(player.getHP() - Math.floor(player.getHP()) > 0) 
		{
			// Half heart
			batch.draw(Entity.tex, 2 + (((int) player.getHP())*1.05f)*64, -64, 
					64, 64, 16, 16, 16, 16, false, false);	
		}
		
		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
		Tile.dispose();
		Entity.dispose();
	}
	public static void nextLevel() {
		Entity.entities.clear();
		Entity.entities.add(player);
		tileMap.updatingTiles.clear();
		
		switch(tileMap.levelTheme) 
		{
		case NORMAL:
			tileMap.levelTheme = LevelTheme.OVERGROWN;
			break;
		case OVERGROWN:
			tileMap.levelTheme = LevelTheme.HOT;
			break;
		case HOT:
			tileMap.levelTheme = LevelTheme.NORMAL;
			break;
		
		}
		tileMap.generateLevel();
		player.x = Game.tileMap.spawnLoc.x;
		player.y = Game.tileMap.spawnLoc.y;
		player.vel.set(0, 0);
		player.updateBoundingBox();
		
	}
}