package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import finalforeach.ld47.Game;

public class Enemy extends Entity
{
	public static Array<Enemy> allEnemies = new Array<Enemy>();
	public Array<ItemEntity> inventory = new Array<ItemEntity>();
	private TextureRegion upTexReg;
	private TextureRegion downTexReg;
	private TextureRegion leftTexReg;
	private TextureRegion rightTexReg;
	private Vector2 dir = new Vector2();
	public Enemy(float x, float y)
	{
		super(x, y, 16, 16);
		downTexReg = new TextureRegion(tex, 0, 48, 16, 16);
		upTexReg = new TextureRegion(tex, 16, 48, 16, 16);
		rightTexReg = new TextureRegion(tex, 32, 48, 16, 16);
		leftTexReg = new TextureRegion(tex, 48, 48, 16, 16);
		
		texReg = upTexReg;
		vel = new Vector2(0,0);
	}
	public static void spawnEnemy(Enemy e) 
	{
		allEnemies.add(e);
		Entity.entities.add(e);
	}
	@Override
	public void onDeath() {
		super.onDeath();
		allEnemies.removeValue(this,true);
	}
	@Override
	public void updateMovement(double deltaTime) {
		super.updateMovement(deltaTime);
		
		
		dir.set(Game.player.x - x, Game.player.y - y).nor();
		if(dir.y>Math.abs(dir.x)) 
		{
			texReg = upTexReg;
		}
		if(-dir.y>Math.abs(dir.x)) 
		{
			texReg = downTexReg;
		}
		if(-dir.x>Math.abs(dir.y)) 
		{
			texReg = leftTexReg;
		}
		if(dir.x>Math.abs(dir.y))
		{
			texReg = rightTexReg;
		}
		float speed = (float) (deltaTime * 4);
		dir.scl(speed);
		
		vel.add(dir);
		x+=vel.x;
		y+=vel.y;
		vel.scl(0.9f);
		
	}
	@Override
	public void onIntersect(Entity entityB) {
		super.onIntersect(entityB);
		if(entityB instanceof Player) 
		{
			entityB.hit(0.1f);
		}
	}
}
