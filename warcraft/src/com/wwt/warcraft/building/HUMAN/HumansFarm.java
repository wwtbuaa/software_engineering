package com.wwt.warcraft.building.HUMAN;

import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;

public class HumansFarm extends ModelBuilding{
	public HumansFarm(Map map,ResourceHandler resource,EntryHandler handler){
		super(map,resource,resource.get("HUMANS_FARM"),resource.get("CONSTRUCTION").ressource,handler);
	}
}
