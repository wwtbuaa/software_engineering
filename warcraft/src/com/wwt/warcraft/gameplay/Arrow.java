package com.wwt.warcraft.gameplay;

import com.b3dgs.lionengine.game.Projectile;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.unit.ModelUnit;

public class Arrow extends Projectile<Arrow>{
	public Arrow(ResourceHandler resource){
		super(resource.get("ARROW").ressource,4,2);
		this.setSpeed(3.0f);
		this.setSize(4,4);
		this.setLifeTime(1000);
		this.setRelativeHit(true);
	}
	
	public Arrow(Arrow arrow){
		super(arrow);
	}
	
	public Arrow createInstance(){
		return new Arrow(this);
	}
	
	public void onMove(){
	}
	
	public void onHitTarget(AbstractEntry<?,?,?> target){
		target.life.decrease(this.damages.getRandomDmg());
		if ((target instanceof ModelBuilding && ((ModelBuilding) target).isOnSceen()) || target instanceof ModelUnit && ((ModelUnit) target).isOnScreen()) {
			ControlPanel.playSfx(SFX.arrow_hit);
		}
	}

	public float getRealFrame() {
		return this.sprite.getRealFrame();
	}

	public void setSkipLastFrameOnReverse(boolean arg0) {
		this.sprite.setSkipLastFrameOnReverse(arg0);
	}

	public void onDestoyed() {
	}
}
