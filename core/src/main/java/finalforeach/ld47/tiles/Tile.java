package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import finalforeach.ld47.entities.Entity;

public abstract class Tile
{
	public TextureRegion texReg;
	public static Texture tex;
	
	/**The bounding box used for hard collisions*/
	public BoundingBox bb;
	/**The larger bounding box used for pushing slightly*/
	public BoundingBox lbb;
	protected int i;
	protected int j;
	
	public Tile() 
	{
		bb = new BoundingBox(new Vector3(0,0,0), new Vector3(16,16,0));
		lbb = new BoundingBox(new Vector3(), new Vector3());
	}

	public static void load() 
	{
		tex = new Texture("tiles.png");
	}

	public static void dispose() 
	{
		tex.dispose();
	}
	public void update(TileMap tileMap) {}
	public abstract void draw(SpriteBatch batch);

	public int getI() {return i;}
	public int getJ() {return j;}

	public void setTilePos(int i, int j) 
	{
		this.i = i;
		this.j = j;
		bb.set(bb.min.set(i*16, j*16, 0),bb.max.set(i*16+16,j*16+16,0));
		lbb.set(lbb.min.set(bb.min).sub(0.1f), lbb.max.set(bb.max).add(0.1f));
	}

	public boolean IsSolid() {
		return false;
	}

	public void click() {
	}

	public void onEntered(Entity entity) {
	}

}
