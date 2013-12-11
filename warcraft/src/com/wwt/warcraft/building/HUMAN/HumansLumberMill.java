package com.wwt.warcraft.building.HUMAN;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.HUMAN.UpgradeArrow;

public class HumansLumberMill extends ModelBuilding{
	public HumansLumberMill(Map map,ResourceHandler resource,EntryHandler handler){
		super(map,resource,resource.get("HUMANS_LUMBERHILL"),resource.get("CONSTRUCTION").ressource,handler);
		this.addSkill(new UpgradeArrow(0,this));
	}
}
