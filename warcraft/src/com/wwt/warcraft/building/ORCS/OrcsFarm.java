package com.wwt.warcraft.building.ORCS;

import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.EntryHandler;

public class OrcsFarm extends ModelBuilding {

    public OrcsFarm(Map map, ResourceHandler rsch, EntryHandler handler) {
        super(map, rsch, rsch.get("ORCS_FARM"), rsch.get("CONSTRUCTION").ressource, handler);
    }
}

