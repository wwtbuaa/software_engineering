package com.wwt.warcraft.skill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelUnit;

public class Stop extends ModelSkill{

	public Stop(int priority, AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("STOP", priority, owner);
        this.setOrder(false);
        this.setUnlocked(true);
        this.setLvl(this.getLvl(), true);
	}
	
	public void action(){
		if(this.owner instanceof ModelUnit){
			ModelUnit unit = (ModelUnit) this.owner;
            unit.stop();
            ControlPanel.playSfx(unit.getOwnerID(), unit.faction, SFX.confirm);
		}
	}
}
