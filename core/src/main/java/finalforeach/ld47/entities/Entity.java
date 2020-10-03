package finalforeach.ld47.entities;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import finalforeach.ld47.Game;
import finalforeach.ld47.tiles.DebugTile;
import finalforeach.ld47.tiles.Tile;

public class Entity
{
	public static Array<Entity> entities = new Array<Entity>();
	public static Texture tex;

	public TextureRegion texReg;
	public float x,y;
	public float lastX,lastY;
	public Vector2 vel;
	public BoundingBox bb;
	public Entity(float x, float y, float w, float h) 
	{
		this.x=x;
		this.y=y;
		bb = new BoundingBox(new Vector3(x,y,0), new Vector3(x+w,y+h,0));
	}

	public static void load() 
	{
		tex = new Texture("entities.png");
	}
	public static void dispose() 
	{
		tex.dispose();
	}
	public static void updateAllEntities(double deltaTime) 
	{
		for(Entity e : entities) 
		{
			e.update(deltaTime);
		}
	}
	public static void drawAllEntities(SpriteBatch batch) 
	{

		for(Entity e : entities) 
		{
			e.draw(batch);
		}
	}
	public void updateBoundingBox() 
	{
		float w = bb.getWidth();
		float h = bb.getHeight();
		bb.set(bb.min.set(x, y, 0), bb.max.set(x+w, y+h, 0));
	}
	public void update(double deltaTime) 
	{
		Array<Tile>ts = Game.tileMap.getAdjacentTiles(getTileI(), getTileJ(),true);
		ts.sort((Tile t1, Tile t2) -> 
		{
			float dx1 = t1.bb.getCenterX() - bb.getCenterX();
			float dy1 = t1.bb.getCenterY() - bb.getCenterY();
			float dx2 = t2.bb.getCenterX() - bb.getCenterX();
			float dy2 = t2.bb.getCenterY() - bb.getCenterY();

			float s1 = Math.abs(dx1) + Math.abs(dy1);
			float s2 = Math.abs(dx2) + Math.abs(dy2);
			if(s1 < s2) return -1;
			if(s1 > s2) return 1;
			return 0;
		});
		lastX = x;
		lastY = y;

		updateMovement(deltaTime);
		updateBoundingBox();

		//Tile-Entity collisions
		for(Tile t : ts) 
		{
			if(t.IsSolid() && bb.intersects(t.bb)) 
			{
				float xIntersect = Math.max(t.bb.max.x - bb.max.x, bb.min.x - t.bb.min.x);
				float yIntersect = Math.max(t.bb.max.y - bb.max.y, bb.min.y - t.bb.min.y);
				if(xIntersect>0 && xIntersect>yIntersect) 
				{
					x = lastX;
					vel.x=0;
					updateBoundingBox();
				}
				if(yIntersect>0 && yIntersect>xIntersect) 
				{
					y = lastY;
					vel.y=0;
					updateBoundingBox();
				}
			}

		}
	}
	public void updateMovement(double deltaTime) {}
	public int getTileI() 
	{
		return MathUtils.floor(bb.getCenterX()/16f);
	}
	public int getTileJ() 
	{
		return MathUtils.floor(bb.getCenterY()/16f);
	}
	public void draw(SpriteBatch batch) 
	{/*
		Tile debug = new DebugTile();
		debug.setTilePos(getTileI(), getTileJ());
		debug.draw(batch);*/

		float epsilon = 0.0001f;
		float u = texReg.getU()+epsilon;
		float v = texReg.getV()+epsilon;
		float u2 = texReg.getU2()-epsilon;
		float v2 = texReg.getV2()-epsilon;
		batch.draw(tex,x, y, bb.getWidth(), bb.getHeight(), u,v2,u2,v);
	}
}
