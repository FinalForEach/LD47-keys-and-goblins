package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.tiles.LevelTheme;

public class KeyItem extends ItemEntity 
{
	LevelTheme levelTheme;
	TextureRegion normalKeyTexReg;
	TextureRegion overgrownKeyTexReg;
	TextureRegion hotKeyTexReg;
	public KeyItem(LevelTheme levelTheme) 
	{
		super();
		this.levelTheme=levelTheme;
		normalKeyTexReg = new TextureRegion(tex, 0, 32, 16, 16);
		overgrownKeyTexReg = new TextureRegion(tex, 16, 32, 16, 16);
		hotKeyTexReg = new TextureRegion(tex, 32, 32, 16, 16);
		switch (levelTheme) {
		case NORMAL:
			texReg = normalKeyTexReg;
			break;
		case OVERGROWN:
			texReg = overgrownKeyTexReg;
			break;
		case HOT:
			texReg = hotKeyTexReg;
			break;
		}
	}
	@Override
	public boolean pickupItem(Player player) 
	{
		player.inventory.add(this);
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KeyItem) 
		{
			return levelTheme==((KeyItem)obj).levelTheme;
		}
		return false;
	}
}
