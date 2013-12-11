package com.wwt.warcraft.unit.ORCS;

import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.ORCS.BuildOrcsBarracks;
import com.wwt.warcraft.skill.ORCS.BuildOrcsFarm;
import com.wwt.warcraft.skill.ORCS.BuildOrcsLumberMill;
import com.wwt.warcraft.skill.ORCS.BuildOrcsTownHall;
import com.wwt.warcraft.skill.ORCS.CancelOrcsBuild;
import com.wwt.warcraft.skill.ORCS.stdOrcsBuild;
import com.wwt.warcraft.unit.ModelWorker;

public class Peon extends ModelWorker{
	public Peon(Map map,ResourceHandler resource,AbstractEntryHandler<Tile,ModelSkill,Attributes> handler,EntryFactory factory){
		super(map,resource,resource.get("PEON"),handler,factory);
		this.addSkill(new BuildOrcsTownHall(0,this));
		this.addSkill(new BuildOrcsFarm(1,this));
		this.addSkill(new BuildOrcsBarracks(2,this));
		this.addSkill(new BuildOrcsLumberMill(3,this));
		this.addSkill(new stdOrcsBuild(4,this));
		this.addSkill(new CancelOrcsBuild(4,this));
	}
}