package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.BuildModel;
import com.wwt.warcraft.skill.ModelSkill;

public class BuildOrcsLumberMill extends BuildModel{

	public BuildOrcsLumberMill( int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super("ORCS_LUMBERHILL", priority, owner);
		this.setUnlocked(true);
	}
}