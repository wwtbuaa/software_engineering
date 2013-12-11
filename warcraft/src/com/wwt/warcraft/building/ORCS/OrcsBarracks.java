package com.wwt.warcraft.building.ORCS;

import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.ORCS.ProdGrunt;
import com.wwt.warcraft.skill.ORCS.ProdSpearman;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.EntryHandler;

public class OrcsBarracks extends ModelProductor {

    public OrcsBarracks(Map map, ResourceHandler rsch, EntryHandler handler, EntryFactory factory) {
        super(map, rsch, rsch.get("ORCS_BARRACKS"), rsch.get("CONSTRUCTION").ressource, handler, factory);
        this.addSkill(new ProdGrunt(0, this));
        this.addSkill(new ProdSpearman(1, this));
    }
}
