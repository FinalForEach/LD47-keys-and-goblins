package finalforeach.ld47;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import finalforeach.ld47.entities.Entity;
import finalforeach.ld47.entities.StrikeEntity;


public class InputHandler implements InputProcessor
{
	double scrollAmount;
	public void update() 
	{
		Game.camera.zoom= (float) MathUtils.clamp(Game.camera.zoom + scrollAmount/35f, 0.05f, 0.5f);
		scrollAmount=scrollAmount*0.7 - 0.001f;
		scrollAmount=Math.max(scrollAmount, 0);
		
		if(Gdx.input.isTouched()) 
		{
			Vector3 mVec = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
			mVec = Game.viewport.unproject(mVec);
			Game.tileMap.clickTile(MathUtils.floor(mVec.x/16f), MathUtils.floor(mVec.y/16f));
			
			float angle = MathUtils.atan2(mVec.y-Game.player.bb.getCenterY(), mVec.x-Game.player.bb.getCenterX());
			Vector2 dir = new Vector2(1,0).rotateRad(angle);
			if(Gdx.input.justTouched()) 
			{
				Entity.entities.add(new StrikeEntity(dir, Game.player));	
			}
		}
	}
	@Override
	public boolean keyDown(int keycode) {
		if(Keys.F5 == keycode) 
		{
			Game.tileMap.generateLevel();
			Game.tileMap.update();
			Game.player.x = Game.tileMap.spawnLoc.x;
			Game.player.y = Game.tileMap.spawnLoc.y;
			Game.player.updateBoundingBox();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		scrollAmount += amount;
		return true;
	}

}
