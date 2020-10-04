package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import finalforeach.ld47.Game;
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
	public Tile curTile;
	public double invulnerableTimer;
	private double hp;
	private double maxHP;
	public boolean dead;
	public Entity(float x, float y, float w, float h) 
	{
		this.x=x;
		this.y=y;
		bb = new BoundingBox(new Vector3(x,y,0), new Vector3(x+w,y+h,0));
		hp = 3f;
		maxHP = 5f;
		dead = false;
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
		for(int i = 0; i< entities.size; i++) 
		{
			Entity eA = entities.get(i);
			for(int j = i+1; j< entities.size; j++) 
			{
				if(i==j)continue;
				Entity eB = entities.get(j);
				if(eA.bb.intersects(eB.bb)) 
				{
					eA.onIntersect(eB);
					eB.onIntersect(eA);
				}
			}
		}
	}
	public void onIntersect(Entity entityB) {
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
		invulnerableTimer=Math.max(0, invulnerableTimer-deltaTime);
		Array<Tile>ts = Game.tileMap.getSurroundingTiles(getTileI(), getTileJ(),true);
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
			boolean b = false;
			if(t.IsSolid() && bb.intersects(t.bb)) 
			{
				boolean intersectBot =bb.getCenterY() - bb.getHeight()/2 < t.bb.getCenterY();
				boolean intersectTop =bb.getCenterY() + bb.getHeight()/2 > t.bb.getCenterY();
				double botDiff = Math.abs(bb.getCenterY() - bb.getHeight()/2 - t.bb.getCenterY());
				double topDiff = Math.abs(bb.getCenterY() + bb.getHeight()/2 - t.bb.getCenterY());

				boolean intersectR =bb.getCenterX() - bb.getWidth()/2 < t.bb.getCenterX();
				boolean intersectL =bb.getCenterX() + bb.getWidth()/2 > t.bb.getCenterX();
				double rDiff = Math.abs(bb.getCenterX() - bb.getWidth()/2 - t.bb.getCenterX());
				double lDiff = Math.abs(bb.getCenterX() + bb.getWidth()/2 - t.bb.getCenterX());
				
				double vertDiff = Math.max(topDiff, botDiff);
				double horizDiff = Math.max(lDiff, rDiff);
				if(vertDiff>horizDiff) 
				{
					if(intersectBot && botDiff > topDiff) 
					{
						y = t.bb.getCenterY()- bb.getHeight() *1.5f;
					}
					if(intersectTop && topDiff > botDiff) 
					{
						y = t.bb.getCenterY()+ bb.getHeight()/2 ;
					}
				}else 
				{
					if(intersectR && rDiff > lDiff) 
					{
						x = t.bb.getCenterX()- bb.getWidth() *1.5f;
					}
					if(intersectL && lDiff > rDiff) 
					{
						x = t.bb.getCenterX()+ bb.getWidth()/2 ;
					}
					
				}
				

				
				b=true;
			}
			if(b)break;
		}

		updateBoundingBox();
		Tile t = Game.tileMap.getTile(getTileI(), getTileJ());
		if(t!=null) 
		{
			if(t!=curTile) 
			{
				curTile = t;
				curTile.onEntered(this);	
			}
			curTile.onStandUpdate(this, deltaTime);
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
	{
		draw(batch, x, y);
	}

	public void draw(SpriteBatch batch, float x, float y) 
	{
		float epsilon = 0.0001f;
		float u = texReg.getU()+epsilon;
		float v = texReg.getV()+epsilon;
		float u2 = texReg.getU2()-epsilon;
		float v2 = texReg.getV2()-epsilon;
		if(invulnerableTimer>0) 
		{
			batch.setColor(1, 0, 0, 1);	// When hit
		}
		
		batch.draw(tex,x, y, bb.getWidth(), bb.getHeight(), u,v2,u2,v);
		
		if(invulnerableTimer>0) 
		{
			batch.setColor(1, 1, 1, 1); // Return colour to normal	
		}
	}
	public void hit(double dmg) 
	{
		if(invulnerableTimer==0) 
		{
			hp-=dmg;
			invulnerableTimer = 0.2f;
		}
	}
	public double getHP() {return hp;}

	public void heal(double hp) {
		this.hp=Math.min(maxHP, this.hp+hp);
	}

	public double getMaxHP() {
		return maxHP;
	}
}
