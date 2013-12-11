package com.wwt.warcraft.unit;

import java.awt.image.BufferedImage;

import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.ability.AttackerAbility;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.ability.AttackerMeleeAbilityImpl;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public abstract class ModelAttackerMelee extends ModelAttacker{
	final AttackerAbility<Tile, ModelSkill, Attributes> attacker;
	
	public ModelAttackerMelee(Map map,ResourceHandler resource,MediaRessource<BufferedImage> media){
		super(map,resource,media);
		this.attacker = new AttackerMeleeAbilityImpl(this);
        this.setAttackTimer(this.getDataInt("ATT_TIME"));
        this.attacker.setAttackFrame(5);
        this.attacker.setMinimalAttackDistance(1);
        this.attacker.setMaximalAttackDistance(1);
	}
	
	public final void updateAttack(float f){
		this.attacker.updateAttack(f);
	}
	
	public final void attack(AbstractEntry<Tile, ModelSkill, Attributes> entry) {
		this.attacker.attack(entry);
	}
	
    public final void stopAttack() {
        this.attacker.stopAttack();
    }
    
    public final void setAttackTimer(int time) {
        this.attacker.setAttackTimer(time);
    }
    
    public void setAttackFrame(int frame) {
        this.attacker.setAttackFrame(frame);
    }
    
    public final void setMinimalAttackDistance(int min) {
        this.attacker.setMinimalAttackDistance(min);
    }
    
    public final void setMaximalAttackDistance(int max) {
        this.attacker.setMaximalAttackDistance(max);
    }
    
    public final boolean isAttacking() {
        return this.attacker.isAttacking();
    }
}
