package com.wwt.warcraft.skill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.ModelWorker;

public class Repair extends ModelSkill {

    public Repair(int priority, AbstractEntry<Tile, ModelSkill, Attributes> owner) {
        super("REPAIR", priority, owner);
        this.setUnlocked(true);
        Race fac = null;
        if (this.owner instanceof ModelUnit) {
            fac = ((ModelUnit) this.owner).faction;
        }
        if (fac == Race.orcs) {
            this.setIcon(this.getIcon() + 45);
        }
    }

    public void action() {
        if (this.owner instanceof ModelWorker) {
            ModelWorker worker = (ModelWorker) this.owner;
            int id = worker.map.getRef(this.destY, this.destX);
            if (id > 0) {
                ModelBuilding e = ModelBuilding.get(id);
                if (e != null && e.getOwnerID() == worker.getOwnerID()) {
                    if (e.life.getCurrent() < e.life.getMax()) {
                        worker.repair(e);
                        worker.assignDestination(e.getXInTile(), e.getYInTile());
                        ControlPanel.playSfx(worker.getOwnerID(), worker.faction, SFX.confirm);
                    }
                }
            }
        }
    }
}

