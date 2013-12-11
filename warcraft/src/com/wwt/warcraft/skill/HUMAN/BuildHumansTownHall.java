package com.wwt.warcraft.skill.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.BuildModel;
import com.wwt.warcraft.skill.ModelSkill;

public class BuildHumansTownHall extends BuildModel{

	public BuildHumansTownHall( int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("HUMANS_TOWNHALL", priority, owner);
		this.setUnlocked(true);
	}
}