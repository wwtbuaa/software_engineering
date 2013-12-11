package com.wwt.warcraft.unit;

import java.awt.image.BufferedImage;

import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.strategy.AbstractBuilding;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.b3dgs.lionengine.game.strategy.ability.BuilderAbility;
import com.b3dgs.lionengine.game.strategy.ability.ExtractAbility;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.b3dgs.lionengine.utility.Maths;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.ability.BuilderAbilityImpl;
import com.wwt.warcraft.ability.ExtractAbilityImpl;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ResourceType;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.Extract;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.Repair;

public abstract class ModelWorker extends ModelUnit implements BuilderAbility<Tile, ModelSkill, Attributes>, ExtractAbility<Tile, ModelSkill, Attributes, ResourceType>{
	 final BuilderAbility<Tile, ModelSkill, Attributes> builder;
	 final ExtractAbility<Tile, ModelSkill, Attributes, ResourceType> extract;
	 boolean available, repair;
	 ModelBuilding toRepair;
	 long repairTimer;
	 
	 public ModelWorker(Map map,ResourceHandler resource,MediaRessource<BufferedImage> media,AbstractEntryHandler<Tile,ModelSkill,Attributes> handler,EntryFactory factory){
		 super(map,resource,media.file,media.ressource);
		 this.builder = new BuilderAbilityImpl(this, handler, factory);
		 this.extract = new ExtractAbilityImpl(this);
		 this.addSkill(new Repair(2, this));
		 this.addSkill(new Extract(3, this));
		 this.available = true;
		 this.toRepair = null;
	 }
	 
	 public void update(Keyboard keyboard,Mouse mouse,float f){
		 super.update(keyboard, mouse,f);
		 this.updateConstruction(f);
		 this.updateExtraction(f);
		 if(this.isAlive()){
			 this.checkRepair();
		 }
	 }

	public void checkRepair() {
		if(!this.repair){
			if(this.hasReachedDestination()&&this.toRepair!=null&&this.toRepair.isAlive()){
				this.pointTo(this.toRepair);
				this.setAnimation("EXTRACT");
				this.repairTimer=Maths.time();
				this.repair=true;
			}
		}else{
            if (Maths.time() - this.repairTimer > 500) {
                if (this.player.gold.canSpend(10) && this.player.wood.canSpend(5) && this.toRepair.isAlive()) {
                    this.toRepair.life.increase(5);
                    this.player.gold.spend(10);
                    this.player.wood.spend(5);
                    this.repairTimer = Maths.time();
                    if (this.toRepair.life.getCurrent() >= this.toRepair.life.getMax()) {
                        this.stop();
                    }
                }
            }
            if(this.toRepair!=null&&!this.toRepair.isAlive()){
            	this.stopRepair();
            }
		}
	}

	public void stopRepair() {
		this.repair=false;
		this.toRepair=null;
		this.player.incWorkersOnRepairing(-1);
		this.setAnimation("IDLE");
	}

	public void setVisibility(boolean b) {
		super.setVisibility(b);
		if (!b) {
			this.getSkill(this.getDataString("FACTION").toUpperCase() + "_" + "CANCEL").action();
		}
	}
	
	public void stop(){
		this.stopBuild();
		this.stopExtraction();
		super.stop();
		this.setAvailable(true);
		this.stopRepair();
	}
	
    public boolean isPassive() {
        return (super.isPassive() && !this.isConstructing() && !this.isExtracting() && !this.repair);
    }
    
    public void onMove() {
        if (!this.isExtracting()) {
            super.onMove();
        }
    }
    
    public void onOrderedFail(ModelSkill skill) {
    }
    
    public void onHit(AbstractEntry<Tile, ModelSkill, Attributes> attacker) {
        super.onHit(attacker);
        this.player().defendMe(this, attacker);
    }
    
    public void setAvailable(boolean state) {
        this.available = state;
    }

    public boolean isAvailable() {
        return this.available;
    }
    
    public void repair(ModelBuilding building) {
        this.toRepair = building;
    }
    
    public void buildAt(int x,int y,String building,int w,int h,int time){
    	this.builder.buildAt(x, y, building, w, h, time);
    }
    
    public void updateConstruction(float f){
    	this.builder.updateConstruction(f);
    }
    
    public boolean isConstructing() {
        return this.builder.isConstructing();
    }
    
    public void stopBuild() {
        this.builder.stopBuild();
    }
    
    public AbstractBuilding<Tile, ModelSkill, Attributes> getBuilding() {
        return this.builder.getBuilding();
    }
    
    public void onConstructed(AbstractBuilding<Tile, ModelSkill, Attributes> building) {
        this.builder.onConstructed(building);
    }
    
    public void updateExtraction(float f) {
        this.extract.updateExtraction(f);
    }
    
    public void setRessourceLocation(int x, int y) {
        this.extract.setRessourceLocation(x, y);
    }
    
    public void setWarehouse(int x, int y) {
        this.extract.setWarehouse(x, y);
    }
    
    public boolean hasRessources() {
        return this.extract.hasRessources();
    }
    
    public boolean hasWarehouse() {
        return this.extract.hasWarehouse();
    }
    
    public final void setExtractionTime(long time) {
        this.extract.setExtractionTime(time);
    }
    
    public final void setDropOffTime(long time) {
        this.extract.setDropOffTime(time);
    }
    
    public void startExtraction() {
        this.extract.startExtraction();
    }
    
    public boolean isExtracting() {
        return this.extract.isExtracting();
    }
    
    public void stopExtraction() {
        ResourceType r = this.getRessourceType();
        if (r != null) {
            switch (r) {
                case GOLD:
                    this.player.incWorkerOnGold(-1);
                    break;
                case WOOD:
                    this.player.incWorkersOnWood(-1);
            }
        }
        this.extract.stopExtraction();
        this.clearIgnoredID();
    }
    
    public void setRessourceType(ResourceType type) {
        this.extract.setRessourceType(type);
    }
    
    public ResourceType getRessourceType() {
        return this.extract.getRessourceType();
    }
}
