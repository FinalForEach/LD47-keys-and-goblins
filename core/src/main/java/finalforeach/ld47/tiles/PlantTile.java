package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class PlantTile extends BasicTile {

	public PlantTile()
	{
		texReg = new TextureRegion(tex,32,16,16,16);
		glowColor = new Color(0,MathUtils.random(0.6f, 0.9f),MathUtils.random(0f, 0.2f),MathUtils.random(0.15f, 0.5f));
	}
	
}
