package finalforeach.ld47;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class InputHandler implements InputProcessor
{
	double scrollAmount;
	public void update() 
	{
		Game.camera.zoom= (float) MathUtils.clamp(Game.camera.zoom + scrollAmount/40f, 0.01f, 2);
		scrollAmount=scrollAmount*0.7 - 0.001f;
		scrollAmount=Math.max(scrollAmount, 0);
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
