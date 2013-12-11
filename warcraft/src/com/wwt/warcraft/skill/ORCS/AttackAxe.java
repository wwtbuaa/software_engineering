package com.wwt.warcraft.skill.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.AttackMelee;
import com.wwt.warcraft.skill.ModelSkill;

public class AttackAxe extends AttackMelee{
	public AttackAxe(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("ATTACK_Axe",priority,owner);
	}
}
