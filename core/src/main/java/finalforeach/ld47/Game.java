package finalforeach.ld47;

import java.util.function.Predicate;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import finalforeach.ld47.entities.CompassItem;
import finalforeach.ld47.entities.Enemy;
import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.ItemEntity;
import finalforeach.ld47.entities.KeyItem;
import finalforeach.ld47.entities.Player;
import finalforeach.ld47.tiles.DebugTile;
import finalforeach.ld47.tiles.IUpdateDelta;
import finalforeach.ld47.tiles.LevelTheme;
import finalforeach.ld47.tiles.Tile;
import finalforeach.ld47.tiles.TileMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter 
{
	public static TileMap tileMap;
	public static OrthographicCamera camera;
	public static OrthographicCamera uiCamera;
	public static OrthographicCamera ppCamera;
	public static SpriteBatch batch;
	public static SpriteBatch batchPostProcessing;
	public static SpriteBatch batchDebug;
	public static FrameBuffer postProcessingFBO;
	public static TextureRegion postProcessingTexReg;
	
	public static Viewport viewport;
	private Viewport ppViewport;
	private Viewport uiViewport;
	private InputHandler inputHandler;
	private static Texture blankTex;
	private static Texture fovTex;
	private static Texture glowTileTex;

	public static TextureRegion gameOverTexReg;
	public static TextureRegion youWinTexReg;
	public static Player player;

	@Override
	public void create() {
		batch = new SpriteBatch();
		batchPostProcessing = new SpriteBatch();
		batchPostProcessing.setShader(createPostProcessShader());
		batchDebug = new SpriteBatch();
		Tile.load();
		Entity.load();
		gameOverTexReg = new TextureRegion(Entity.tex, 0, 80, 160, 56);
		youWinTexReg = new TextureRegion(Entity.tex, 0 , 136, 160, 56);
		blankTex = new Texture("blankTex.png");
		fovTex = new Texture("FOV.png");
		glowTileTex = new Texture("glowTile.png");

		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		camera.setToOrtho(false);
		camera.position.set(0,0,0);
		camera.zoom =0.2f;
		camera.update();
		

		ppCamera = new OrthographicCamera();
		ppViewport = new FitViewport(1280, 786, ppCamera);
		ppCamera.setToOrtho(false);
		//ppCamera.position.set(0, 0,0);
		ppCamera.update();
		
		uiCamera = new OrthographicCamera();
		uiViewport = new FitViewport(1280, 786, uiCamera);
		uiCamera.setToOrtho(false);
		uiCamera.position.set(1280/2,-786/2,0);
		//uiCamera.zoom =0.15f;
		uiCamera.update();
		
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);

		restart();
		
	}
	public static void restart() 
	{
		Enemy.allEnemies.clear();
		Entity.entities.clear();
		tileMap = new TileMap();
		player = new Player(tileMap.spawnLoc.x,tileMap.spawnLoc.y);
		Entity.entities.add(player);
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		uiViewport.update(width, height);
		ppViewport.update(width, height);
		

    	postProcessingFBO = new FrameBuffer(Format.RGB565, (int)(width), (int)(height), false);
        postProcessingTexReg = new TextureRegion(postProcessingFBO.getColorBufferTexture());
        postProcessingTexReg.flip(false, true);
	}
	public void handleInput(double deltaTime) 
	{
		inputHandler.update();
	}
	public void update(double deltaTime) 
	{
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
		if(Enemy.allEnemies.size<10 && tileMap.levelTheme!=LevelTheme.LOVE) 
		{
			Tile t =(Tile) tileMap.floorTiles.toArray()[MathUtils.random(tileMap.floorTiles.size()-1)];
			if(t!=null) 
			{
				Enemy.spawnEnemy(new Enemy(t.getI()*16, t.getJ()*16));
			}
		}
	}
	@Override
	public void render() 
	{
		uiCamera.position.set(1280/2,-768/2,0);
		double deltaTime = Gdx.graphics.getDeltaTime();
		update(deltaTime);

		
		// Post processing
	    float width = Gdx.graphics.getWidth();
	    float height = Gdx.graphics.getHeight();

	                     
        if(postProcessingFBO == null)
        {
        	postProcessingFBO = new FrameBuffer(Format.RGB565, (int)(width), (int)(height), false);
            postProcessingTexReg = new TextureRegion(postProcessingFBO.getColorBufferTexture());
            postProcessingTexReg.flip(false, true);
        }
        
        postProcessingFBO.begin();

		viewport.apply();
		camera.position.set(player.bb.getCenterX(),player.bb.getCenterY(),0);
        float z = camera.zoom;
        camera.zoom=z;
		camera.update();

		// Lights

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_DST_ALPHA, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        // FOV sphere
        float fovRadius = 128;
        batch.setColor(1,1,1,1);
        batch.draw(fovTex, player.bb.getCenterX()-fovRadius, player.bb.getCenterY()-fovRadius,fovRadius*2,fovRadius*2);
        for(Entity e : Entity.entities) 
        {
        	if(e.getGlowRadius()>0) 
        	{
            	batch.setColor(e.getGlowColor());
            	fovRadius = e.getGlowRadius();
            	batch.draw(fovTex, e.bb.getCenterX()-fovRadius, e.bb.getCenterY()-fovRadius,fovRadius*2,fovRadius*2);
        	}
        }
        
        for(int i=0;i<TileMap.MAP_SIZE;i++) 
        {
        	for(int j=0;j<TileMap.MAP_SIZE;j++) 
            {
            	Tile t = tileMap.getTile(i, j);
            	if(t!=null) 
            	{
            		Color glow = t.getGlowColor();
            		if(glow!=null) 
            		{
            			batch.setColor(t.getGlowColor());
            			float glowRadius = 128;
            			batch.draw(fovTex, t.bb.getCenterX()-glowRadius, t.bb.getCenterY()-glowRadius,glowRadius*2,glowRadius*2);
            		}
            	}
            }
        }
        
        batch.setColor(1, 1, 1, 1);
        batch.end();

		
	    
        postProcessingFBO.end();

		
        // Main drawing

		viewport.apply();
		camera.position.set(player.bb.getCenterX(),player.bb.getCenterY(),0);
		camera.zoom = z;
		camera.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tileMap.draw(batch);
		Entity.drawAllEntities(batch);
				
		batch.end();
		
        

		camera.zoom = z;
		// Finish post processing
        viewport.apply();
		camera.position.set(player.bb.getCenterX(),player.bb.getCenterY(),0);
		camera.update();
		batchPostProcessing.setBlendFunction( GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batchPostProcessing.setProjectionMatrix(camera.combined);
        batchPostProcessing.begin();         
		        
		Vector2 vA = viewport.unproject(new Vector2(0,0));
		Vector2 vB = viewport.unproject(new Vector2(width,height));
		
        batchPostProcessing.draw(postProcessingTexReg, 
        		vA.x, 
        		vB.y, 
        		vB.x-vA.x  , 
        		vA.y-vB.y);

        batchPostProcessing.end();

		camera.zoom = z;
		
		// UI
        uiViewport.apply();
		uiCamera.update();

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
				
		for(ItemEntity i : player.inventory) 
		{
			if(i instanceof CompassItem)
			{
				float angle = MathUtils.atan2(tileMap.treasureLoc.y - player.y, tileMap.treasureLoc.x - player.x);
				batch.draw(CompassItem.pointTexReg, 
						uiViewport.getWorldWidth()/2 + 20,
						-uiViewport.getWorldHeight()/2, 
						8 - 20, 8, 16, 16, 2, 2, MathUtils.radiansToDegrees * angle );
				
				angle = MathUtils.atan2(tileMap.exitLoc.y - player.y, tileMap.exitLoc.x - player.x);
				batch.draw(CompassItem.pointExitTexReg, 
						uiViewport.getWorldWidth()/2 + 20,
						-uiViewport.getWorldHeight()/2, 
						8 - 20, 8, 16, 16, 2, 2, MathUtils.radiansToDegrees * angle );
				break;
			}
		}
		
		// Empty hearts

		for(int i=0; i< player.getMaxHP(); i++) 
		{
			batch.draw(Entity.tex, 2 + (i*1.05f)*64, -64, 
					64, 64, 32, 16, 16, 16, false, false);
		}
		// Full hearts
		for(int i=0; i< player.getHP(); i++) 
		{
			batch.draw(Entity.tex, 2 + (i*1.05f)*64, -64, 
					64, 64, 0, 16, 16, 16, false, false);
		}
		if(player.getHP() - Math.floor(player.getHP()) > 0 && player.getHP()>0) 
		{
			// Half heart
			batch.draw(Entity.tex, 2 + (((int) player.getHP())*1.05f)*64, -64, 
					64, 64, 16, 16, 16, 16, false, false);	
		}
		
		for(int i=0; i<player.inventory.size;i++) 
		{
			ItemEntity item = player.inventory.get(i);
			batch.draw(item.texReg, 0, -128-(i*16), 8, 8, 64, 64, 1, 1, 0);
		}
		
		if(player.dead) 
		{
			batch.draw(gameOverTexReg, uiViewport.getWorldWidth()/2 - 256, -256, 512, 170);
		}else 
		{
			if(tileMap.levelTheme==LevelTheme.LOVE) 
			{
				batch.draw(youWinTexReg, uiViewport.getWorldWidth()/2 - 256, -256, 512, 170);
			}
		}
		
		batch.end();
		

	}
	public static ShaderProgram createPostProcessShader () {
		String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "}\n";
		String fragmentShader = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "  vec4 lightColor = v_color * texture2D(u_texture, v_texCoords);\n"
			+ "  gl_FragColor = lightColor;\n" //
			//+ "  gl_FragColor.a = length(lightColor.xzy) / length(vec3(1));\n"
			+ "}";

		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
		return shader;
	}
	@Override
	public void dispose() {
		batch.dispose();
		Tile.dispose();
		Entity.dispose();
		blankTex.dispose();
		fovTex.dispose();
		glowTileTex.dispose();
	}
	public static void nextLevel() {
		Enemy.allEnemies.clear();
		Entity.entities.clear();
		Entity.entities.add(player);
		tileMap.floorTiles.clear();
		tileMap.updatingTiles.clear();

		if(player.inventory.contains(new KeyItem(LevelTheme.NORMAL), false)
				&& player.inventory.contains(new KeyItem(LevelTheme.OVERGROWN), false)
				&& player.inventory.contains(new KeyItem(LevelTheme.HOT), false)) 
		{
			for(ItemEntity i : player.inventory) 
			{
				System.out.println(i);
			}
			tileMap.levelTheme = LevelTheme.LOVE;
			player.inventory.clear();
		}else 
		{
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
			case LOVE:
				restart();
				break;
			
			}
		}
		
		tileMap.generateLevel();
		player.x = Game.tileMap.spawnLoc.x;
		player.y = Game.tileMap.spawnLoc.y;
		player.vel.set(0, 0);
		player.updateBoundingBox();
		
	}
}