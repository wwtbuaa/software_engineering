package com.wwt.warcraft.building.HUMAN;

import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.HUMAN.ProdArcher;
import com.wwt.warcraft.skill.HUMAN.ProdFootman;

public class HumansBarracks extends ModelProductor{
	public HumansBarracks(Map map,ResourceHandler resource,EntryHandler handler,EntryFactory factory){
		super(map,resource,resource.get("HUMANS_BARRACKS"),resource.get("CONSTRUCTION").ressource,handler,factory);
		this.addSkill(new ProdFootman(0,this));
		this.addSkill(new ProdArcher(1,this));
	}
}
