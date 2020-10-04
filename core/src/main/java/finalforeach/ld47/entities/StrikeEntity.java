package finalforeach.ld47.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class StrikeEntity extends Entity
{
	public Entity owner;
	public double timer;
	public Vector2 dir;
	TextureRegion texReg1;
	TextureRegion texReg2;
	TextureRegion texReg3;
	float speed =(float) (200);
	
	public StrikeEntity(Vector2 dir, Entity owner) {
		super(owner.x, owner.y, 16, 16);
		this.dir =dir;
		this.owner=owner;
		rot = dir.angle();
		x+=dir.x*8;
		y+=dir.y*8;
		speed+=owner.vel.len();
		glowColor = new Color(1,1,1,0.1f);
		texReg1 = new TextureRegion(tex, 0, 64, 16, 16);
		texReg2 = new TextureRegion(tex, 16, 64, 16, 16);
		texReg3 = new TextureRegion(tex, 32, 64, 16, 16);
		
		texReg = texReg1;
	}
	@Override
	public float getGlowRadius() {
		return 32;
	}
	@Override
	public void update(double deltaTime) {
		super.update(deltaTime);
		timer+=deltaTime;
		if(timer>0.25f) 
		{
			texReg=texReg2;
		}
		if(timer>0.5f) 
		{
			texReg=texReg3;
		}
		if(timer>0.75f) 
		{
			dead=true;
		}
		x+=dir.x*speed*deltaTime;
		y+=dir.y*speed*deltaTime;
	}
	@Override
	public void onIntersect(Entity entityB) {
		super.onIntersect(entityB);
		if(entityB.canHit() && entityB!=owner) 
		{
			entityB.hit(2f);
			dead=true;
			hitSound.play();
		}
	}
}
