package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.ItemEntity;

public class ChestTile extends BasicTile
{
	TextureRegion openTexReg;
	TextureRegion closedTexReg;
	ItemEntity item;
	boolean isOpen;
	public ChestTile(ItemEntity item)
	{
		this.item = item;
		closedTexReg = new TextureRegion(tex,80,0,16,16);
		openTexReg = new TextureRegion(tex,96,0,16,16);
		
		texReg = closedTexReg;
	}
	
	public void click() 
	{
		if(!isOpen) 
		{
			item.x = i * 16;
			item.anchorY = j * 16 + 8;
			item.updateBoundingBox();
			Entity.entities.add(item);
			System.out.println("Spawned " + item + " @ " + item.x + ", " + item.y);
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
		case LOVE:
			closedTexReg = new TextureRegion(tex,80,48,16,16);
			openTexReg = new TextureRegion(tex,96,48,16,16);
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