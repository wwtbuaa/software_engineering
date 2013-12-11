package com.wwt.warcraft.building.ORCS;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.ORCS.UpgradeSpear;

public class OrcsLumberMill extends ModelBuilding{
	public OrcsLumberMill(Map map,ResourceHandler resource,EntryHandler handler){
		super(map,resource,resource.get("ORCS_LUMBERHILL"),resource.get("CONSTRUCTION").ressource,handler);
		this.addSkill(new UpgradeSpear(0,this));
	}
}

