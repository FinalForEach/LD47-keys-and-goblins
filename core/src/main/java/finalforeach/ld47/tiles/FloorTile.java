package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FloorTile extends BasicTile
{
	public FloorTile()
	{
		texReg = new TextureRegion(tex,16,0,16,16);
	}
	@Override
	public void update(TileMap tileMap) {
		super.update(tileMap);
		switch(tileMap.levelTheme) 
		{
		case NORMAL:
			texReg = new TextureRegion(tex,16,0,16,16);
			break;
		case OVERGROWN:
			texReg = new TextureRegion(tex,16,16,16,16);
			break;
		case HOT:
			texReg = new TextureRegion(tex,16,32,16,16);
			break;
		default:
			break;
		
		}
	}
}