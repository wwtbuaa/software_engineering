package com.wwt.warcraft.building.HUMAN;

import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.HUMAN.ProdPeasant;

public class HumansTownHall extends ModelProductor{
	public HumansTownHall(Map map,ResourceHandler resource,EntryHandler handler,EntryFactory factory){
		super(map,resource,resource.get("HUMANS_TOWNHALL"),resource.get("CONSTRUCTION").ressource,handler,factory);
		this.addSkill(new ProdPeasant(0,this));
	}
}
