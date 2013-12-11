package com.wwt.warcraft.skill;

import java.awt.*;

import com.b3dgs.lionengine.core.*;
import com.b3dgs.lionengine.drawable.*;
import com.b3dgs.lionengine.game.strategy.*;
import com.b3dgs.lionengine.input.*;
import com.b3dgs.lionengine.utility.*;
import com.wwt.warcraft.*;
import com.wwt.warcraft.gameplay.*;
import com.wwt.warcraft.map.*;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.building.*;
import static com.wwt.warcraft.gameplay.ControlPanel.TEXT;

public abstract class ModelSkill extends AbstractSkill<Tile,ModelSkill,Attributes>{

	String alert;
	long alertTimer;
	int icon;
	
	public ModelSkill(String name, int priority,AbstractEntry<Tile, ModelSkill, Attributes> owner) {
		super(Media.get("skills.txt"), name, priority, owner);
		this.icon=this.getIcon();
		this.alert=null;
	}

	public void action() {
	}

	public void onClick() {
		ControlPanel.playSfx(SFX.click);
	}

	public void renderOnMap(Graphics2D arg0,AbstractControlPanel<Tile, ModelSkill, Attributes> arg1,StrategyCursor arg2) {
	}

	@Override
	public void renderOnPanel(Graphics2D arg0,AbstractControlPanel<Tile, ModelSkill, Attributes> arg1,StrategyCursor arg2, TiledSprite arg3, int arg4, int arg5,int arg6, int arg7) {
		int x=arg2.getScreenX();
		int y=arg2.getScreenY();
		boolean over=(x>=arg4 && x<=arg4+arg6 && y >=arg5 && y <=arg5+arg7);
		if(this.isUnlocked()){
			if(arg2.getClick()==Mouse.LEFT && over){
				arg3.render(arg0,this.getIcon(),arg4,arg5+1);
			}else{
				arg3.render(arg0,this.getIcon(),arg4,arg5);
				if(over && this.alert == null){
					TEXT.setColor(new Color(255, 255, 235));
					TEXT.draw(arg0,this.getDisplayName(),72,192,Alignment.LEFT);
				}
			}
		}else{
			arg3.render(arg0, this.getIcon(), arg4,arg5);
			arg0.setColor(new Color(128, 64, 64, 192));
			arg0.fill3DRect(arg4, arg5, arg6, arg7, false);
			}
		if(this.alert!=null){
			TEXT.setColor(new Color(255, 255, 235));
			TEXT.draw(arg0,this.alert,72,192,Alignment.LEFT);
		}
	}

	public void updateOnMap(float arg0) {
	}

	public void updateOnPanel(AbstractControlPanel<Tile, ModelSkill, Attributes> arg0) {
		if(this.alert!=null && Maths.time()-this.alertTimer>3000){
			this.alert=null;
		}
	}

	public void setLvl(int lvl){
		super.setLvl(lvl);
		this.setIcon(this.icon+((lvl-1)*3));
	}
	
	public void setLvl(int lvl,boolean faction){
		super.setLvl(lvl);
		Race r;
		if(this.owner instanceof ModelUnit){
			r=((ModelUnit)this.owner).faction;
		}else{
			r=((ModelBuilding)this.owner).faction;
		}
		if(r==Race.orcs){
			this.setIcon(this.icon+((lvl-1)*3)+45);
		}else{
			this.setIcon(this.icon+((lvl-1)*3));
		}
	}
	
	public void setAlert(String alert){
		this.alert=alert;
		this.alertTimer=Maths.time();
	}
}
