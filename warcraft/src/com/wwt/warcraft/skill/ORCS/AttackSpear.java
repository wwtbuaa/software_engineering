package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.AttackDistance;
import com.wwt.warcraft.skill.ModelSkill;

public class AttackSpear extends AttackDistance{
	public AttackSpear(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("ATTACK_SPEAR",priority,owner);
	}
}
