package com.wwt.warcraft.gameplay;

import com.b3dgs.lionengine.game.strategy.Ressource;
import com.b3dgs.lionengine.utility.Maths;

public class Resource extends Ressource{
	float value;
	
	public Resource(int n){
		super(n);
		this.value=n;
	}
	
	public void update(float f,float s){
		this.value=Maths.curveValue(this.value,this.get(),s/f);
		if (this.value >= this.get() - 0.1f && this.value <= this.get() + 0.1f) {
		    this.value = this.get();
		}
	}
	
	public float getValue(){
		return this.value;
	}
}
