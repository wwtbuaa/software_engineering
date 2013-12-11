package com.wwt.warcraft.skill.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.AttackMelee;
import com.wwt.warcraft.skill.ModelSkill;

public class AttackSword extends AttackMelee{
	public AttackSword(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("ATTACK_SWORD",priority,owner);
	}
}
