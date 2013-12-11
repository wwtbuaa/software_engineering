package com.wwt.warcraft.skill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelAttackerMelee;
import com.wwt.warcraft.unit.ModelUnit;

public abstract class AttackMelee extends ModelSkill{
	public AttackMelee(String name, int priority, AbstractEntry<Tile, ModelSkill, Attributes> owner) {
        super(name, priority, owner);
        this.setOrder(true);
        this.setUnlocked(true);
    }
	
	 public void action() {
	        if (this.owner instanceof ModelAttackerMelee) {
	            ModelAttackerMelee attacker = (ModelAttackerMelee) this.owner;
	            int id = attacker.map.getRef(this.destY, this.destX);
	            if (id > 0) {
	                AbstractEntry<Tile, ModelSkill, Attributes> t = (AbstractEntry<Tile, ModelSkill, Attributes>) ModelUnit.get(id);
	                attacker.attack(t);
	                ControlPanel.playSfx(attacker.getOwnerID(), attacker.faction, SFX.confirm);
	            }
	        }
	 }
}
