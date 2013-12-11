package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.ProdModel;

public class ProdSpearman extends ProdModel{
	public ProdSpearman(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("ORCS_THROWER",priority,owner);
	}
}
