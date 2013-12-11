package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class CancelOrcsBuild extends ModelSkill {

	public CancelOrcsBuild(int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("ORCS_CANCEL", priority, owner);
		this.setOrder(false);
		this.setUnlocked(true);
		this.setIgnore(true);
	}

	public void action(){
		this.owner.getSkill("MOVE").setIgnore(false);
		this.owner.getSkill("STOP").setIgnore(false);
		this.owner.getSkill("EXTRACT").setIgnore(false);
		this.owner.getSkill("REPAIR").setIgnore(false);
		this.owner.getSkill("ORCS_STDBUILD").setIgnore(false);
		this.owner.getSkill("ORCS_TOWNHALL").setIgnore(true);
		this.owner.getSkill("ORCS_FARM").setIgnore(true);
		this.owner.getSkill("ORCS_BARRACKS").setIgnore(true);
		this.owner.getSkill("ORCS_LUMBERMILL").setIgnore(true);
		this.owner.getSkill("ORCS_CANCEL").setIgnore(true);
	}
}