package com.wwt.warcraft.unit.ORCS;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.ORCS.AttackAxe;
import com.wwt.warcraft.unit.ModelAttackerMelee;

public class Grunt extends ModelAttackerMelee{
	public Grunt(Map map,ResourceHandler resource){
		super(map,resource,resource.get("FOOTMAN"));
		this.addSkill(new AttackAxe(2,this));
	}
}
