package com.wwt.warcraft;

import com.b3dgs.lionengine.*;
import com.b3dgs.lionengine.core.*;
import com.b3dgs.lionengine.drawable.*;
import com.b3dgs.lionengine.engine.*;
import com.b3dgs.lionengine.utility.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AbstractSequence extends Sequence{
	
	final Text text=Drawable.DRAWABLE.createText(Font.SERIF,10,Text.NORMAL);
	final Timer memTime;
	final Runtime runtime=Runtime.getRuntime();
	boolean lockMouse,fps;
	int fpsOffsetX,fpsOffsetY;
	int used,max;
	final float wide;
	
	public AbstractSequence(Loader arg0) {
		super(arg0);
		this.text.setColor(new Color(208, 208, 240, 120));
		this.memTime=new Timer();
		this.lockMouse=false;
		this.fps=false;
		this.fpsOffsetX=0;
		this.fpsOffsetY=0;
		float width=this.screen.init.widthRef * (this.screen.display.getHeight()/(float)this.screen.init.heightRef);
		this.wide=this.screen.getWide()?(this.screen.display.getWidth()/width):1.0f;
		this.used=(int)((runtime.totalMemory()-runtime.freeMemory())/1048576L);
		this.max=(int)(runtime.maxMemory()/1048576L);
		this.memTime.start();
	}
	
	public void update(float fp){
		if(this.keyboard.isPressed(KeyEvent.VK_TAB)){
			this.fps=true;
		}else{
			this.fps=false;
		}
	}
	
	public void terminate(){
		
	}
	
	public void render(Graphics2D graph){
		this.text.setColor(new Color(208, 208, 240, 120));
		if(this.fps){
			this.text.draw(graph,"FPS="+this.getFPS(),this.fpsOffsetX,this.fpsOffsetY,Alignment.LEFT);
			if(this.memTime.elapsed(500L)){
				this.used=(int)((runtime.totalMemory())-runtime.freeMemory()/1048576L);
				this.max=(int)(runtime.maxMemory()/1048576L);
			}
			this.text.draw(graph, "Memory: " + this.used + "MB" + " / " + this.max + "MB", this.fpsOffsetX, this.fpsOffsetY + 12, Alignment.LEFT);
		}
	}

	public void lockMouse(boolean bool){
		this.lockMouse=bool;
	}
}
