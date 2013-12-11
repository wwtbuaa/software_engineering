package com.wwt.warcraft.skill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.ResourceType;
import com.wwt.warcraft.map.CollisionType;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelWorker;

public class Extract extends ModelSkill{
	public Extract(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("EXTRACT",priority,owner);
		this.setUnlocked(true);
	}

	public void action(){
		if(this.owner instanceof ModelWorker){
			ModelWorker worker=(ModelWorker)this.owner;
			ModelBuilding b=worker.player().getClosestBuilding(worker, BuildingType.TOWNHALL);
			if(b!=null){
				worker.stop();
				int x=this.destX;
				int y=this.destY;
				int id=worker.map.getRef(y,x);
				if(id>0){
					ModelBuilding e=ModelBuilding.get(id);
					if(e instanceof GoldMine){
						worker.ignoreID(id, true);
						worker.setRessourceType(ResourceType.GOLD);
						worker.player().incWorkerOnGold(1);
						x=e.getXInTile()+e.getWidthInTile()/2;
						y=e.getYInTile()+e.getHeightInTile()/2;
					}
				}else{
					if(worker.map.getTile(y, x).getCollType()==CollisionType.TREE){
						worker.setRessourceType(ResourceType.WOOD);
						worker.player().incWorkersOnWood(1);
					}
				}
				worker.setRessourceLocation(x,y);
				worker.setWarehouse(b.getXInTile()+b.getWidthInTile()/2,b.getYInTile()+b.getHeightInTile()/2);
				worker.startExtraction();
				ControlPanel.playSfx(worker.getOwnerID(),worker.faction,SFX.confirm);
			}
		}
	}
}
