package com.wwt.warcraft.ability;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.ability.AbstractAttackerMeleeAbility;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelAttacker;

public class AttackerMeleeAbilityImpl extends AbstractAttackerMeleeAbility<Tile, ModelSkill, Attributes>{
	final ModelAttacker attacker;
	
	public AttackerMeleeAbilityImpl(AbstractUnit<Tile, ModelSkill, Attributes> attacker){
		super(attacker);
		this.attacker=(ModelAttacker) attacker;
	}
	
	public void updateAttack(float f){
		super.updateAttack(f);
	}
	
	public void onHitTarget(AbstractEntry<Tile, ModelSkill, Attributes> target) {
		target.life.decrease(this.attacker.damages.getRandomDmg());
		if (this.attacker.isOnScreen()) {
			ControlPanel.playSfx(SFX.hit);
		}
	}
	
	public boolean onStartAttacker(){
		boolean start=super.onStartAttack();
		if(start){
			this.attacker.setAnimation("ATTACK");
		}
		return start;
	}
	
	public void onAttacked() {
        this.attacker.setAnimation("IDLE");
    }
	
	public void onReaching() {
	}

	public void onPause(){
	}
	
	public void onLostTarget() {
		this.attacker.reAssignDestination();
	}
}
