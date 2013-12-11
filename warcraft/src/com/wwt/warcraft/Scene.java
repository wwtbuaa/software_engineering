package com.wwt.warcraft;

import java.awt.Color;
import java.awt.Graphics2D;

import com.b3dgs.lionengine.Audio;
import com.b3dgs.lionengine.audio.Midi;
import com.b3dgs.lionengine.core.Alignment;
import com.b3dgs.lionengine.engine.Loader;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.utility.Media;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Costs;
import com.wwt.warcraft.unit.ModelUnit;

public final class Scene extends AbstractSequence{
	World world;
	Midi music;
	
	public Scene(Loader loader){
		super(loader);
		this.fpsOffsetX=72;
		this.fpsOffsetY=72;
	}

	public void load() {
		ModelUnit.clear();
		ModelBuilding.clear();
		Costs.initializeCosts(Media.get("costs.xml"));
		this.world=new World(this,this.screen);
		this.world.loadFromFile(Media.get("levels",GameConfig.getMap()));
	}
	
	public void update(float f){
		super.update(1.0f);
		this.world.update(this.keyboard,this.mouse,1.0f);
		if(this.keyboard.isPressed(Keyboard.ESCAPE)){
			this.music.stop();
			this.end(new Menu(this.loader));
		}
	}
	
	
	public void render(Graphics2D graph){
		this.world.render(graph);
		if(this.fps){
			this.text.draw(graph, "Coord = [" + (this.world.camera.getX() /16+ 4) + " | " + this.world.camera.getY() / 16 + "]", this.fpsOffsetX, this.fpsOffsetY + 24, Alignment.LEFT);
		}
		this.text.setColor(new Color(208,208,240,120));
		this.text.draw(graph,"warcraft"+""+"0.0.6",(int)(315*this.wide)-2,181,Alignment.RIGHT);
		this.text.draw(graph,"wwt",74,181,Alignment.LEFT);
		super.render(graph);
	}
	
	public void playMusic(String file){
		this.music=Audio.AUDIO.loadMidi(Media.get("musics",file));
		if(file.equals("humans.mid")){
			this.music.setLoop(600,this.music.getTicks()-760);
		}else{
			this.music.setLoop(13000, this.music.getTicks()-575);
		}
		this.music.play(true);
	}
}
