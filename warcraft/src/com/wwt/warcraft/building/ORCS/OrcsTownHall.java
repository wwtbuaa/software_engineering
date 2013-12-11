package com.wwt.warcraft.building.ORCS;

import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.ORCS.ProdPeon;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.EntryHandler;

public class OrcsTownHall extends ModelProductor {

    public OrcsTownHall(Map map, ResourceHandler rsch, EntryHandler handler, EntryFactory factory) {
        super(map, rsch, rsch.get("ORCS_TOWNHALL"), rsch.get("CONSTRUCTION").ressource, handler, factory);
        this.addSkill(new ProdPeon(0, this));
    }
}
