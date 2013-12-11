package com.wwt.warcraft.ability;

import com.b3dgs.lionengine.game.Projectile;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.ability.AbstractAttackerDistanceAbility;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelAttacker;

public class AttackerDistanceAbilityImpl extends AbstractAttackerDistanceAbility<Tile, ModelSkill, Attributes>{
	final ModelAttacker attacker;
	
	public AttackerDistanceAbilityImpl(AbstractUnit<Tile, ModelSkill, Attributes> attacker, Projectile<?> projectile) {
        super(attacker, projectile);
        this.attacker = (ModelAttacker) attacker;
    }
	
	public void onThrown(){
		if(this.attacker.isOnScreen()){
			ControlPanel.playSfx(SFX.arrow_thrown);
		}
	}
	
	public void onAttacked(){
		this.attacker.setAnimation("IDLE");
	}
	
	public void onReaching(){
	}
	
    public boolean onStartAttack() {
        boolean start = super.onStartAttack();
        this.attacker.setAnimation("ATTACK");
        return start;
    }
    
    public void onPause() {
    }
    
    public void onLostTarget() {
        this.attacker.reAssignDestination();
    }
}
