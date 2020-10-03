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
	public void update(TileMap tileMap) {}
	public abstract void draw(SpriteBatch batch);
}
class BasicTile extends Tile
{
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(texReg, i * 16, j * 16);
	}
}
class DebugTile extends BasicTile
{
	DebugTile()
	{
		texReg = new TextureRegion(image,0,0,16,16);
	}
}
class FloorTile extends BasicTile
{
	FloorTile()
	{
		texReg = new TextureRegion(image,16,0,16,16);
	}
}
class WallTile extends BasicTile
{
	WallTile()
	{
		texReg = new TextureRegion(image,32,0,16,16);
	}
	@Override
	public void update(TileMap tileMap) 
	{
		boolean wallUp = tileMap.getTile(i, j+1) instanceof WallTile;
		boolean wallDown = tileMap.getTile(i, j-1) instanceof WallTile;
		boolean wallLeft = tileMap.getTile(i-1, j) instanceof WallTile;
		boolean wallRight = tileMap.getTile(i+1, j) instanceof WallTile;

		// Basic wall
		if(!wallUp && wallDown && !wallLeft && !wallRight) 
		{
			texReg = new TextureRegion(image,0,16,16,16);
		}
		if(wallUp && !wallDown && !wallLeft && !wallRight) 
		{
			texReg = new TextureRegion(image,0,32,16,16);
		}
		
		// Horizontal walls
		if(wallUp && !wallDown && !wallLeft && wallRight) 
		{
			texReg = new TextureRegion(image,16,32,16,16);
		}
		if(wallUp && !wallDown && wallLeft && !wallRight) 
		{
			texReg = new TextureRegion(image,32,32,16,16);
		}
		if(wallUp && !wallDown && wallLeft && wallRight) 
		{
			texReg = new TextureRegion(image,24,32,16,16);
		}
		if(!wallUp && wallDown && !wallLeft && wallRight) 
		{
			texReg = new TextureRegion(image,16,16,16,16);
		}
		if(!wallUp && wallDown && wallLeft && !wallRight) 
		{
			texReg = new TextureRegion(image,32,16,16,16);
		}
		if(!wallUp && wallDown && wallLeft && wallRight) 
		{
			texReg = new TextureRegion(image,24,16,16,16);
		}
	}
}
