package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CompassItem extends ItemEntity 
{
	public static TextureRegion pointTexReg;
	public static TextureRegion pointExitTexReg;
	public CompassItem() 
	{
		super();
		texReg = new TextureRegion(tex, 48, 32, 16, 16);
		pointTexReg = new TextureRegion(tex, 64, 32, 16, 16);
		pointExitTexReg = new TextureRegion(tex, 80, 32, 16, 16);
	}
	@Override
	public boolean pickupItem(Player player) 
	{
		for(ItemEntity i : player.inventory) 
		{
			if(i instanceof CompassItem)
				return false;
		}
		player.inventory.add(this);
		return true;
	}
}
