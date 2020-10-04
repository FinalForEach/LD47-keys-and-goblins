package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class HeartItem extends ItemEntity 
{
	public HeartItem(float x, float y) 
	{
		super(x, y);
		texReg = new TextureRegion(tex, 0, 16, 16, 16);
	}
	public HeartItem() 
	{
		this(0,0);
	}
	@Override
	public boolean pickupItem(Player player) 
	{
		if(player.getHP()<player.getMaxHP()) 
		{
			player.heal(1);
			return true;	
		}
		return false;
	}
}
