package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.ProdModel;

public class ProdPeon extends ProdModel {

    public ProdPeon(int priority, AbstractEntry<Tile, ModelSkill, Attributes> owner) {
    	super("ORCS_WORKER", priority, owner);
    	this.setUnlocked(true);
    }
}