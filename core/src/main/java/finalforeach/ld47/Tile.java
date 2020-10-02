package finalforeach.ld47;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Tile
{
	public TextureRegion texReg;
	public static Texture image;
	
	int i, j;
	

	public static void load() 
	{
		image = new Texture("tiles.png");
	}

	public static void dispose() 
	{
		image.dispose();
	}
	public abstract void draw(SpriteBatch batch);
}
class DebugTile extends Tile
{
	DebugTile()
	{
		texReg = new TextureRegion(image,0,0,16,16);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(texReg, i * 16, j * 16);
	}
}
