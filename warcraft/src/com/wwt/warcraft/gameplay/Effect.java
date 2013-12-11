package com.wwt.warcraft.gameplay;

import java.awt.Graphics2D;

import com.b3dgs.lionengine.drawable.AnimatedSprite;
import com.b3dgs.lionengine.game.Camera;

public class Effect {
	int x1,x2,y1,y2;
	AnimatedSprite eff1,eff2;
	boolean active1,active2;
	
	public Effect(AnimatedSprite eff1,int x1,int y1,AnimatedSprite eff2,int x2,int y2){
		this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.eff1 = eff1;
        this.eff2 = eff2;
        this.active1 = false;
        this.active2 = false;
	}
	
	public void render(Graphics2D graph,Camera camera){
		if(this.active1){
			this.eff1.render(graph,this.x1-camera.getX(),this.y1-camera.getY());
		}
		if(this.active2){
			this.eff2.render(graph,this.x1-camera.getX(),this.y1-camera.getY());
		}
	}
	
    public void activate1(boolean state) {
        this.active1 = state;
    }

    public void activate2(boolean state) {
        this.active2 = state;
    }
}
