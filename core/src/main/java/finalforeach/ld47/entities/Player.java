package finalforeach.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity
{
	private Vector2 tmp;
	public Player(float x, float y)
	{
		super(x, y, 16, 16);
		texReg = new TextureRegion(tex, 0, 0, 16, 16);
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
		}
		if(Gdx.input.isKeyPressed(Keys.S)) 
		{
			tmp.y-=1;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) 
		{
			tmp.x-=1;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) 
		{
			tmp.x+=1;
		}
		float speed = (float) (deltaTime * 16);
		tmp.nor();
		tmp.scl(speed);
		
		vel.add(tmp);
		x+=vel.x;
		y+=vel.y;
		vel.scl(0.9f);
		
	}
}
