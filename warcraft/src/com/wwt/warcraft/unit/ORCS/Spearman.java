package com.wwt.warcraft.unit.ORCS;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.Arrow;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.ORCS.AttackSpear;
import com.wwt.warcraft.unit.ModelAttackerDistance;

public class Spearman extends ModelAttackerDistance{
	public Spearman(Map map,ResourceHandler resource){
		super(map,resource,resource.get("SPEARMAN"),new Arrow(resource));
		this.addSkill(new AttackSpear(2,this));
		this.attacker.setAttackFrame(3);
	}
}

