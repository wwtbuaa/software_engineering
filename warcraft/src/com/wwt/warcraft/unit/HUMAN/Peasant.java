package com.wwt.warcraft.unit.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.HUMAN.BuildHumansBarracks;
import com.wwt.warcraft.skill.HUMAN.BuildHumansFarm;
import com.wwt.warcraft.skill.HUMAN.BuildHumansLumberMill;
import com.wwt.warcraft.skill.HUMAN.BuildHumansTownHall;
import com.wwt.warcraft.skill.HUMAN.CancelHumansBuild;
import com.wwt.warcraft.skill.HUMAN.stdHumansBuild;
import com.wwt.warcraft.unit.ModelWorker;

public class Peasant extends ModelWorker{
	public Peasant(Map map,ResourceHandler resource,AbstractEntryHandler<Tile,ModelSkill,Attributes> handler,EntryFactory factory){
		super(map,resource,resource.get("PEASANT"),handler,factory);
		this.addSkill(new BuildHumansTownHall(0,this));
		this.addSkill(new BuildHumansFarm(1,this));
		this.addSkill(new BuildHumansBarracks(2,this));
		this.addSkill(new BuildHumansLumberMill(3,this));
		this.addSkill(new stdHumansBuild(4,this));
		this.addSkill(new CancelHumansBuild(4,this));
	}
}
