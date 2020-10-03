package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BasicTile extends Tile
{
	@Override
	public void draw(SpriteBatch batch) {
		float epsilon = 0.0001f;
		float u = texReg.getU()+epsilon;
		float v = texReg.getV()+epsilon;
		float u2 = texReg.getU2()-epsilon;
		float v2 = texReg.getV2()-epsilon;
		batch.draw(tex,getI() * 16, getJ() * 16, 16, 16, u,v2,u2,v);
	}
}