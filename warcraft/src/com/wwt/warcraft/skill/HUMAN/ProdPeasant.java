package com.wwt.warcraft.skill.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.ProdModel;

public class ProdPeasant extends ProdModel{
	public ProdPeasant(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("HUMAN_WORKER",priority,owner);
		this.setUnlocked(true);
	}
}
