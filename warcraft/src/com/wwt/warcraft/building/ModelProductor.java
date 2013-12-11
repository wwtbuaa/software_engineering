package com.wwt.warcraft.building;

import java.awt.image.BufferedImage;

import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.strategy.ability.ProducerAbility;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.ability.ProducerAbilityImpl;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class ModelProductor extends ModelBuilding implements ProducerAbility<Tile, ModelSkill, Attributes> {
	final ProducerAbility<Tile, ModelSkill, Attributes> producer;
	
	public ModelProductor(Map map,ResourceHandler resource,MediaRessource<BufferedImage> media,BufferedImage construction, EntryHandler handler, EntryFactory factory){
		super(map,resource,media,construction,handler);
		this.producer=new ProducerAbilityImpl(this,handler,factory);
	}
	
	public void update(Keyboard keyboard,Mouse mouse,float f){
		super.update(keyboard,mouse,f);
		this.updateProduction();
	}

	public void addToProductionQueue(String name, int time) {
		this.producer.addToProductionQueue(name, time);
	}

	public String getProducingElement() {
		return this.producer.getProducingElement();
	}

	public int getProductionProgress() {
		return this.producer.getProductionProgress();
	}

	public int getQueueLength() {
		return this.producer.getQueueLength();
	}

	public void setRallyLocation(int x, int y) {
		this.producer.setRallyLocation(x, y);
	}

	public void stopProduction() {
		this.producer.stopProduction();
	}

	public void updateProduction() {
		this.producer.updateProduction();
	}
	
	public void onDestroyed(){
		super.onDestroyed();
		this.stopProduction();
	}
}
