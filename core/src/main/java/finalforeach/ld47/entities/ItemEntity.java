package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class ItemEntity extends Entity
{

	float timer;
	public float anchorY;
	public ItemEntity(float x, float y) 
	{
		super(x, y, 16, 16);
		anchorY = y;
		glowColor = new Color(1,1,1,1);
	}
	public ItemEntity() {
		this(0, 0);
	}
	@Override
	public void update(double deltaTime) 
	{
		super.update(deltaTime);
		timer+=deltaTime;
		y = 4*MathUtils.sin(timer) + anchorY;
		updateBoundingBox();
	}
	@Override
	public void onIntersect(Entity entityB) {
		super.onIntersect(entityB);
		if(entityB instanceof Player) 
		{
			if(pickupItem((Player)entityB)) 
			{
				dead = true;
				pickupSound.play();
			}
		}
	}
	/**
	 * @return true if picked up, and should destroy entity
	 * */
	public boolean pickupItem(Player player) 
	{
		return true;
	}
	@Override
	public float getGlowRadius() {
		return 32;
	}

	public boolean canHit() {
		return false;
	}
}
