package com.wwt.warcraft.skill.HUMAN;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class UpgradeArrow extends ModelSkill{
	public UpgradeArrow(int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super("UPGRADE_ARROW",priority,owner);
		this.setOrder(false);
		this.setUnlocked(true);
		this.setLvl(2);
	}
	
	public final void setLvl(int lvl){
		super.setLvl(lvl);
		if(lvl>=3){
			this.setIgnore(true);
		}
	}
	
	public void action(){
		Player player=(Player) Player.get(this.owner.getOwnerID());
		player.incArrowLvl();
	}
}
