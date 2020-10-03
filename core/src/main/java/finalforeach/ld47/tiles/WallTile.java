package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WallTile extends BasicTile
{
	public WallTile()
	{
		texReg = new TextureRegion(tex,48,0,16,16);
	}

	public boolean IsSolid() {
		return true;
	}
}