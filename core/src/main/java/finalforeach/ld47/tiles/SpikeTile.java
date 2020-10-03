package finalforeach.ld47.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import finalforeach.ld47.Game;
import finalforeach.ld47.entities.Entity;

public class SpikeTile extends FloorTile implements IUpdateDelta
{
	double timer;
	TextureRegion hiddenTexReg;
	TextureRegion readyTexReg;
	TextureRegion attackTexReg;
	public SpikeTile()
	{
		hiddenTexReg = new TextureRegion(tex,112,0,16,16);
		readyTexReg = new TextureRegion(tex,128,0,16,16);
		attackTexReg = new TextureRegion(tex,144,0,16,16);
		texReg = hiddenTexReg;
		timer = -1;
	}
	@Override
	public void update(TileMap tileMap) {
		super.update(tileMap);
	}
	@Override
	public void onEntered(Entity entity) {
		super.onEntered(entity);
		if(timer==-1) 
		{
			texReg = readyTexReg;
			timer=0;
			Game.tileMap.updatingTiles.add(this);
		}
	}
	@Override
	public void update(double deltaTime)
	{
		timer+=deltaTime;
		if(timer>0.5f) 
		{
			texReg = attackTexReg;
			if(timer>1.5f) 
			{
				texReg = hiddenTexReg;
				timer = -1;
			}
		}
	}
	@Override
	public boolean IsActive() {
		return timer>=0;
	}
}