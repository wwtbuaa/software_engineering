package com.wwt.warcraft.unit;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import com.b3dgs.lionengine.game.strategy.AbstractBuilding;
import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.ability.AttackerAbility;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.b3dgs.lionengine.utility.Maths;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.gameplay.AI;
import com.wwt.warcraft.skill.ModelSkill;

public abstract class ModelAttacker extends ModelUnit implements AttackerAbility<Tile, ModelSkill, Attributes> {

	final List<AbstractEntry<Tile, ModelSkill, Attributes>> guards;
	int orderx,ordery;
	boolean assault,riposte,defend;
	long guardtimer;
	
	public ModelAttacker(Map map,ResourceHandler resource,MediaRessource<BufferedImage>src){
		super(map,resource,src.file,src.ressource);
		this.guards=new ArrayList<AbstractEntry<Tile, ModelSkill, Attributes>>(1);
		this.damages.setMin(this.getDataInt("DMG_MIN"));
		this.damages.setMax(this.getDataInt("DMG_MAX"));
		this.riposte=true;
		this.assault=false;
		this.defend=false;
		this.orderx=-1;
		this.ordery=-1;
	}
	
	public void setDamages(int min,int max){
		this.damages.setMin(min);
		this.damages.setMax(max);
	}

	public void update(Keyboard keyboard,Mouse mouse,float f){
		super.update(keyboard,mouse,f);
		this.updateAttack(f);
		if(this.isAlive()&&(!this.hasTarget()||this.isMoving())){
			if(this.isDefending()&&!this.isAttacking()){
				this.defend=false;
				this.assault=false;
			}
			 if (!this.isDefending() && (this.assault || (!this.isAttacking() && !this.isMoving()))) {
	                if (Maths.time() - this.guardtimer > 500) {
	                    if (this.player instanceof AI) {
	                        this.guard();
	                    } else {
	                        if (!this.isMoving()) {
	                            this.guard();
	                        }
	                    }
	                    this.guardtimer = Maths.time();
	                }
			 }
		}
	}
	
	public boolean assignDestination(int x,int y){
		boolean found=super.assignDestination(x, y);
		if(this.orderx==-1&&this.assault){
			this.orderx=x;
			this.ordery=y;
		}
		return found;
	}
	
	public void reAssignDestination() {
        if (this.orderx != -1 && this.ordery != -1) {
            this.stopAttack();
            this.setTarget(null);
            super.assignDestination(this.orderx, this.ordery);
        } else {
            this.stopAttack();
            this.stopMoves();
        }
    }
	
	public void stop(){
		this.stopAttack();
		super.stop();
	}
	
	public void guard(){
		int view=this.getFieldOfView()-1;
		for(int y=this.getYInTile()-view;y<=this.getYInTile()+view;y++){
			for(int x=this.getXInTile()-view;x<=this.getXInTile()+view;x++){
				try{
					int k=this.map.getRef(y,x);
					if(k>0&&k!=this.id){
						 AbstractEntry<Tile, ModelSkill, Attributes> e = ModelUnit.get(k);
						 if(e==null){
							 e=ModelBuilding.get(k);
						 }
						 if(e.isAlive()&&e.isVisible()&&e.getOwnerID() != this.getOwnerID() && e.getOwnerID() > 0 && e.isActive()){
							 this.guards.add(e);
						 }
					}
				}catch(ArrayIndexOutOfBoundsException e){
					continue;
				}
			}
		}
		int min=Integer.MAX_VALUE;
		AbstractEntry<Tile, ModelSkill, Attributes> closest = null;
		for (AbstractEntry<Tile, ModelSkill, Attributes> e : this.guards) {
			int d=this.getDistance(e);
		 if (closest instanceof AbstractBuilding && e instanceof AbstractUnit) {
             min = d;
             closest=e;
		 }else if (!(closest instanceof AbstractUnit && e instanceof AbstractBuilding) || closest == null) {
			 if(d<min){
				 min=d;
				 closest=e;
				 }
			 }
		}
		this.guards.clear();
		if(closest!=null){
			this.guardAction(closest);
		}
	}
	
	public void guardAction(AbstractEntry<Tile, ModelSkill, Attributes> e){
		if (this.getTarget() instanceof ModelAttacker && !(e instanceof ModelAttacker)) {
            return;
		}
		this.attack(e);
	}
	
	public void onHit(AbstractEntry<Tile, ModelSkill, Attributes> e){
		super.onHit(e);
		if(this.isAlive()&&this.riposte){
			if (e instanceof AbstractUnit && this.getTarget() instanceof AbstractBuilding && this.player instanceof AI) {
                this.attack(e);
                return;
            }
			boolean closest=false;
			if(this.hasTarget()){
				closest = this.getDistance(e) < this.getDistance(this.getTarget());
			}
			if ((this.hasTarget() || closest) && this.getOwnerID() != e.getOwnerID()) {
                this.attack(e);
			}
		}
	}
	
	public void onKilled(AbstractEntry<Tile, ModelSkill, Attributes> e) {
		if (this.assault) {
			this.reAssignDestination();
		}
	}
	
	public void setRiposte(boolean state) {
		this.riposte = state;
	}
	
	public void setAssault(boolean state) {
		this.assault = state;
	}

	public boolean getAssault() {
		return this.assault;
	}

	public void setDefend(boolean state) {
		this.defend = state;
	}

	public boolean isDefending() {
		return this.defend;
	}
	
	public boolean isPassive() {
        return super.isPassive() && !this.isAttacking();
    }
	
	public boolean hasTarget() { 
		return this.getTarget() != null;
	}
}