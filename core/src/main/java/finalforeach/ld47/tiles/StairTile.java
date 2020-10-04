package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.Game;
import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.KeyItem;
import finalforeach.ld47.entities.Player;

public class StairTile extends BasicTile
{
	public StairTile()
	{
		texReg = new TextureRegion(tex,160,0,16,16);
	}
	@Override
	public void onEntered(Entity entity) 
	{
		super.onEntered(entity);
		if(entity instanceof Player) 
		{
			Game.nextLevel();
		}
	}
}