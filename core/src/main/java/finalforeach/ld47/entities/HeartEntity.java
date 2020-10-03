package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class HeartEntity extends Entity 
{
	float timer;
	float anchorY;
	public HeartEntity(float x, float y) 
	{
		super(x, y, 16, 16);
		anchorY = y;
		texReg = new TextureRegion(tex, 0, 16, 16, 16);
	}
	@Override
	public void update(double deltaTime) 
	{
		super.update(deltaTime);
		timer+=deltaTime;
		y = 4*MathUtils.sin(timer) + anchorY;
	}
}
