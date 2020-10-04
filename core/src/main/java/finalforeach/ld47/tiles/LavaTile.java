package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.entities.Entity;

public class LavaTile extends FloorTile
{
	public LavaTile()
	{
		texReg = new TextureRegion(tex, 32, 32, 16, 16);
		glowColor = Color.valueOf("d88b1a11");
	}

	@Override
	public void update(TileMap tileMap) {
	}
	@Override
	public void onEntered(Entity entity) {
		super.onEntered(entity);
	}
	@Override
	public void onStandUpdate(Entity entity, double deltaTime) {
		super.onStandUpdate(entity, deltaTime);
		entity.hit(1f);
		
	}
}