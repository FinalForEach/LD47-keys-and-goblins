package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class MoneyTile extends BasicTile {

	public MoneyTile()
	{
		texReg = new TextureRegion(tex,32,48,16,16);
		glowColor = Color.valueOf("d88b1a22");
	}
	
}
