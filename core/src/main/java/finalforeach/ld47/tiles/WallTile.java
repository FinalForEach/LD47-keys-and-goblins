package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WallTile extends BasicTile
{
	public WallTile()
	{
		texReg = new TextureRegion(tex,48,0,16,16);
	}

	@Override
	public void update(TileMap tileMap) {
		super.update(tileMap);
		switch(tileMap.levelTheme) 
		{
		case NORMAL:
			texReg = new TextureRegion(tex,48,0,16,16);
			break;
		case OVERGROWN:
			texReg = new TextureRegion(tex,48,16,16,16);
			break;
		case HOT:
			texReg = new TextureRegion(tex,48,32,16,16);
			break;
		case LOVE:
			texReg = new TextureRegion(tex,48,48,16,16);
			break;
		default:
			break;
		
		}
	}
	public boolean IsSolid() {
		return true;
	}
}