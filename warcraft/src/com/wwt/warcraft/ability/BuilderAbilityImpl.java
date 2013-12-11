package com.wwt.warcraft.ability;

import com.b3dgs.lionengine.game.strategy.AbstractBuilding;
import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.ability.AbstractBuilderAbility;
import com.b3dgs.lionengine.game.strategy.ability.BuilderAbility;
import com.b3dgs.lionengine.geometry.Point2D;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.AI;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.Cost;
import com.wwt.warcraft.gameplay.Costs;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelWorker;

public class BuilderAbilityImpl extends AbstractBuilderAbility<Tile,ModelSkill,Attributes> implements BuilderAbility<Tile,ModelSkill,Attributes>{
	final AbstractEntryHandler<Tile,ModelSkill,Attributes> handler;
	final EntryFactory factory;
	final ModelWorker worker;
	
	public BuilderAbilityImpl(AbstractUnit<Tile, ModelSkill, Attributes> builder,AbstractEntryHandler<Tile, ModelSkill, Attributes> handler,EntryFactory factory){
		super(builder,handler);
		this.handler=handler;
		this.factory=factory;
		this.worker=(ModelWorker) builder;
	}
	
	public AbstractBuilding<Tile,ModelSkill,Attributes> getBuildingToBuild(String name){
		int i =name.indexOf('_');
		Race race=Race.valueOf(name.substring(0,i).toLowerCase());
		BuildingType type=BuildingType.valueOf(name.substring(i+1,name.length()));
		return this.factory.createBuilding(type,race);
	}
	
	public void startConstruction(String name){
		Player p=this.worker.player();
		Cost cost=Costs.get(name);
		boolean free = this.worker.map.checkFreePlace(this.worker.getXInTile(), this.worker.getYInTile(), cost.w, cost.h, this.worker.id);
		if (free && p.gold.canSpend(cost.gold.get()) && p.wood.canSpend(cost.wood.get())) {
			p.gold.spend(cost.gold.get());
	        p.wood.spend(cost.wood.get());
	        super.startConstruction(name);
	        this.worker.setVisibility(false);
	        this.worker.setActive(false);
	        this.worker.unselect();
	        this.worker.setOver(false);
	        this.handler.selection.remove(this.worker);
	        ControlPanel.playSfx(this.worker.getOwnerID(), null, SFX.construction);
		}else{
			this.constructionRefused(this.cur);
		}
	}
	
	
	public boolean canBuild(Constructible c){
		if(this.worker.player() instanceof AI){
			if (!this.worker.map.checkFreePlace(c.tx - 1, c.ty - 1, c.tw + 2, c.th + 2, this.worker.id)) {
                return false;
            }
		}
		return true;
	}

	public void constructionRefused(Constructible c){
		if (this.worker.player() instanceof AI) {
			Point2D free = this.worker.map.getFreePlaceArround(c.tx - 1, c.ty - 1, c.tw + 2, c.th + 2, this.worker.id, 32);
	        if (free != null) {
	        	c.tx = free.getX() + 1;
	        	c.ty = free.getY() + 1;
	        	this.worker.assignDestination(c.tx, c.ty);
	        }
		}else {
			this.worker.player().incWorkersOnConstructing(-1);
			this.worker.stop();
		}
	}

	public void stopBuild(){
		if(this.cur!=null){
			this.adjustWorkersCount(this.cur.name,-1);
		}
		super.stopBuild();
	}
	
	public void adjustWorkersCount(String name,int n){
		BuildingType type=BuildingType.valueOf(name.substring(this.worker.getDataString("FACTION").length()+1));
		switch (type) {
		case TOWNHALL:
			this.worker.player().incWorkersOnTownHall(n);
            break;
        case FARM:
            this.worker.player().incWorkersOnFarm(n);
            break;
        case BARRACKS:
            this.worker.player().incWorkersOnBarracks(n);
            break;
        case LUMBERMILL:
            this.worker.player().incWorkersOnLumberMill(n);
            break;
		}
	}
	
	public void willBuild(String arg0) {
		this.adjustWorkersCount(arg0,1);
	}
	
    public void onConstructed(AbstractBuilding<Tile, ModelSkill, Attributes> building) {
        ModelBuilding build = (ModelBuilding) building;
        Map map = this.worker.map;
        Tile tile = map.getTileArround(this.worker, build.getYInTile(), build.getXInTile(), build.getWidthInTile(), build.getHeightInTile());
        if (tile != null) {
            this.worker.place(tile.getX() / map.getTileWidth(), -tile.getY() / map.getTileHeight());
        }
        this.worker.setVisibility(true);
        this.worker.setActive(true);
        this.worker.setAvailable(true);
        this.worker.player().incWorkersOnConstructing(-1);
        this.adjustWorkersCount(this.cur.name, -1);
        build.player().onBuilt(build);
        super.onConstructed(building);
    }
}
