package com.wwt.warcraft.unit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.Projectile;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.ability.AttackerAbility;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.ability.AttackerDistanceAbilityImpl;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public abstract class ModelAttackerDistance extends ModelAttacker{
	public final AttackerAbility<Tile, ModelSkill, Attributes> attacker;
    final Projectile<?> projectile;
    
    public ModelAttackerDistance(Map map,ResourceHandler resource,MediaRessource<BufferedImage> media,Projectile<?> projectile){
    	super(map,resource,media);
    	this.attacker=new AttackerDistanceAbilityImpl(this, projectile);
    	this.projectile=projectile;
    	this.setAttackTimer(this.getDataInt("ATT_TIME"));
        this.attacker.setMinimalAttackDistance(1);
        this.attacker.setMaximalAttackDistance(this.getFieldOfView());
        projectile.damages.setMin(this.getDataInt("DMG_MIN"));
        projectile.damages.setMax(this.getDataInt("DMG_MAX"));
    }
    
    public void setDamages(int min, int max) {
        this.damages.setMin(min);
        this.damages.setMax(max);
        this.projectile.damages.setMin(min);
        this.projectile.damages.setMax(max);
    }
    
    public void render(Graphics2D graph,Camera camera){
    	super.render(graph,camera);
    	this.projectile.render(graph, camera);
    }
    
    public final void updateAttack(float f) {
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
