package com.wwt.warcraft.unit.HUMAN;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.Arrow;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.HUMAN.AttackArrow;
import com.wwt.warcraft.unit.ModelAttackerDistance;

public class Archer extends ModelAttackerDistance{
	public Archer(Map map,ResourceHandler resource){
		super(map,resource,resource.get("ARCHER"),new Arrow(resource));
		this.addSkill(new AttackArrow(2,this));
		this.attacker.setAttackFrame(2);
	}
}
