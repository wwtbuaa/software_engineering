package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.BuildModel;
import com.wwt.warcraft.skill.ModelSkill;

public class BuildOrcsTownHall extends BuildModel{

	public BuildOrcsTownHall( int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("ORCS_TOWNHALL", priority, owner);
		this.setUnlocked(true);
	}
}