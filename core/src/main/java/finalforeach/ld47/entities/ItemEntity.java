package finalforeach.ld47.entities;

import com.badlogic.gdx.math.MathUtils;

public class ItemEntity extends Entity
{

	float timer;
	public float anchorY;
	public ItemEntity(float x, float y) 
	{
		super(x, y, 16, 16);
		anchorY = y;
	}
	public ItemEntity() {
		super(0, 0, 16, 16);
	}
	@Override
	public void update(double deltaTime) 
	{
		super.update(deltaTime);
		timer+=deltaTime;
		y = 4*MathUtils.sin(timer) + anchorY;
		System.out.println(this.getClass().getSimpleName()+" y:" + y);
	}
	@Override
	public void onIntersect(Entity entityB) {
		super.onIntersect(entityB);
		if(entityB instanceof Player) 
		{
			dead = pickupItem((Player)entityB);
		}
	}
	/**
	 * @return true if picked up, and should destroy entity
	 * */
	public boolean pickupItem(Player player) 
	{
		return true;
	}
}
