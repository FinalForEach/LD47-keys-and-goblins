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
		switch(tileMap.levelTheme) 
		{
		case NORMAL:
			hiddenTexReg = new TextureRegion(tex,112,0,16,16);
			readyTexReg = new TextureRegion(tex,128,0,16,16);
			attackTexReg = new TextureRegion(tex,144,0,16,16);
			break;
		case OVERGROWN:
			hiddenTexReg = new TextureRegion(tex,112,16,16,16);
			readyTexReg = new TextureRegion(tex,128,16,16,16);
			attackTexReg = new TextureRegion(tex,144,16,16,16);
			break;
		case HOT:
			hiddenTexReg = new TextureRegion(tex,112,32,16,16);
			readyTexReg = new TextureRegion(tex,128,32,16,16);
			attackTexReg = new TextureRegion(tex,144,32,16,16);
			break;
		};
		texReg = hiddenTexReg;
	}
	@Override
	public void onEntered(Entity entity) {
		super.onEntered(entity);
		for(Tile t : Game.tileMap.getSurroundingTiles(i, j, true, 2)) 
		{
			if(t instanceof SpikeTile) 
			{
				SpikeTile spike = (SpikeTile) t;
				if(spike.timer==-1) 
				{
					spike.texReg = spike.readyTexReg;
					spike.timer=0;
					Game.tileMap.updatingTiles.add(spike);
				}
			}
		}
	}
	@Override
	public void onStandUpdate(Entity entity, double deltaTime) {
		super.onStandUpdate(entity, deltaTime);
		if(timer>0.5f) 
		{
			entity.hit(0.2f);
		}
	}
	@Override
	public void update(double deltaTime)
	{
		timer+=deltaTime;
		if(timer>0.3f) 
		{
			texReg = attackTexReg;
			if(timer>1.3f) 
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