package finalforeach.ld47;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private TileMap tileMap;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		Tile.load();
		tileMap = new TileMap();
		tileMap.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		tileMap.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		Tile.dispose();
	}
}