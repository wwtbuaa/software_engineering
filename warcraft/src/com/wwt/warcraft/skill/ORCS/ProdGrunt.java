package com.wwt.warcraft.skill.ORCS;

import com.wwt.warcraft.skill.ModelSkill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ProdModel;

public class ProdGrunt extends ProdModel{
	public ProdGrunt(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("ORCS_WARRIOR",priority,owner);
		this.setUnlocked(true);
	}
}
