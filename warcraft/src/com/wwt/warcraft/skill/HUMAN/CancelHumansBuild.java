package com.wwt.warcraft.skill.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class CancelHumansBuild extends ModelSkill {

	public CancelHumansBuild(int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("HUMANS_CANCEL", priority, owner);
		this.setOrder(false);
		this.setUnlocked(true);
		this.setIgnore(true);
	}

	public void action(){
		this.owner.getSkill("MOVE").setIgnore(false);
		this.owner.getSkill("STOP").setIgnore(false);
		this.owner.getSkill("EXTRACT").setIgnore(false);
		this.owner.getSkill("REPAIR").setIgnore(false);
		this.owner.getSkill("HUMANS_STDBUILD").setIgnore(false);
		this.owner.getSkill("HUMANS_TOWNHALL").setIgnore(true);
		this.owner.getSkill("HUMANS_FARM").setIgnore(true);
		this.owner.getSkill("HUMANS_BARRACKS").setIgnore(true);
		this.owner.getSkill("HUMANS_LUMBERMILL").setIgnore(true);
		this.owner.getSkill("HUMANS_CANCEL").setIgnore(true);
	}
}

