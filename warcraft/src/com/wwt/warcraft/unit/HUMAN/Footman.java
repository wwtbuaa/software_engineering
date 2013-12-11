package com.wwt.warcraft.unit.HUMAN;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.HUMAN.AttackSword;
import com.wwt.warcraft.unit.ModelAttackerMelee;

public class Footman extends ModelAttackerMelee{
	public Footman(Map map,ResourceHandler resource){
		super(map,resource,resource.get("FOOTMAN"));
		this.addSkill(new AttackSword(2,this));
	}
}
