package finalforeach.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity
{
	public Array<ItemEntity> inventory = new Array<ItemEntity>();
	private Vector2 tmp;
	private TextureRegion upTexReg;
	private TextureRegion downTexReg;
	private TextureRegion leftTexReg;
	private TextureRegion rightTexReg;
	public Player(float x, float y)
	{
		super(x, y, 16, 16);
		downTexReg = new TextureRegion(tex, 0, 0, 16, 16);
		upTexReg = new TextureRegion(tex, 16, 0, 16, 16);
		rightTexReg = new TextureRegion(tex, 32, 0, 16, 16);
		leftTexReg = new TextureRegion(tex, 48, 0, 16, 16);
		
		texReg = upTexReg;
		vel = new Vector2(0,0);
		tmp = new Vector2(0,0);
	}
	@Override
	public void updateMovement(double deltaTime) {
		super.updateMovement(deltaTime);
		
		tmp.set(0,0);
		if(Gdx.input.isKeyPressed(Keys.W)) 
		{
			tmp.y+=1;
			texReg = upTexReg;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) 
		{
			tmp.y-=1;
			texReg = downTexReg;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) 
		{
			tmp.x-=1;
			texReg = leftTexReg;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) 
		{
			tmp.x+=1;
			texReg = rightTexReg;
		}
		float speed = (float) (deltaTime * 16);
		tmp.nor();
		tmp.scl(speed);
		
		vel.add(tmp);
		x+=vel.x;
		y+=vel.y;
		vel.scl(0.9f);
	}
	@Override
	public void onDeath() {
		super.onDeath();
	}
}
