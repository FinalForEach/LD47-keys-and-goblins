package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.HeartEntity;

public class ChestTile extends BasicTile
{
	TextureRegion openTexReg;
	TextureRegion closedTexReg;
	boolean isOpen;
	public ChestTile()
	{
		closedTexReg = new TextureRegion(tex,80,0,16,16);
		openTexReg = new TextureRegion(tex,96,0,16,16);
		
		texReg = closedTexReg;
	}
	
	public void click() 
	{
		if(!isOpen) 
		{
			Entity.entities.add(new HeartEntity(i*16, j*16 + 8));	
		}
		isOpen = true;
		texReg = openTexReg;
		
	}
	
	@Override
	public void update(TileMap tileMap) {
		super.update(tileMap);
		switch(tileMap.levelTheme) 
		{
		case NORMAL:
			closedTexReg = new TextureRegion(tex,80,0,16,16);
			openTexReg = new TextureRegion(tex,96,0,16,16);
			break;
		case OVERGROWN:
			closedTexReg = new TextureRegion(tex,80,16,16,16);
			openTexReg = new TextureRegion(tex,96,16,16,16);
			break;
		case HOT:
			closedTexReg = new TextureRegion(tex,80,32,16,16);
			openTexReg = new TextureRegion(tex,96,32,16,16);
			break;
		}
		if(isOpen) 
		{
			texReg = openTexReg;
		}else 
		{
			texReg = closedTexReg;
		}
	}
}