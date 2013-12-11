package com.wwt.warcraft.skill;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelAttacker;
import com.wwt.warcraft.unit.ModelAttackerDistance;
import com.wwt.warcraft.unit.ModelUnit;

public abstract class AttackDistance extends ModelSkill{
	ModelAttacker attacker;
	int dmgMin,dmgMax;
	
	public AttackDistance(String name,int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super(name,priority,owner);
		this.setOrder(true);
		this.setUnlocked(true);
		this.dmgMin=this.attacker.damages.getMin();
		this.dmgMax=this.attacker.damages.getMax();
	}
	
	public void action(){
		if(this.owner instanceof ModelAttackerDistance){
			int id=attacker.map.getRef(this.destY, this.destX);
			if(id>0){
				AbstractEntry<Tile, ModelSkill, Attributes> t = (AbstractEntry<Tile, ModelSkill, Attributes>) ModelUnit.get(id);
                this.attacker.attack(t);
                ControlPanel.playSfx(attacker.getOwnerID(), this.attacker.faction, SFX.confirm);
			}
		}
	}
	
	public void setLvl(int lvl) {
		super.setLvl(lvl);
	    switch (lvl) {
	    case 1:
	    	this.attacker.setDamages(this.dmgMin, this.dmgMax);
	        break;
	    case 2:
	        this.attacker.setDamages(this.dmgMin, this.dmgMax + 1);
	        break;
	    case 3:
	        this.attacker.setDamages(this.dmgMin + 1, this.dmgMax + 2);
	        break;
	    }
	}
}
