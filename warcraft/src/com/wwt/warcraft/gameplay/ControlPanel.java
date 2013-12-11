package com.wwt.warcraft.gameplay;
import static com.b3dgs.lionengine.Drawable.DRAWABLE;
import static com.wwt.warcraft.ResourceHandler.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import com.b3dgs.lionengine.audio.Sound;
import com.b3dgs.lionengine.core.Alignment;
import com.b3dgs.lionengine.drawable.Text;
import com.b3dgs.lionengine.drawable.TiledSprite;
import com.b3dgs.lionengine.game.strategy.AbstractControlPanel;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.AbstractPlayer;
import com.b3dgs.lionengine.game.strategy.StrategyCursor;
import com.b3dgs.lionengine.utility.Maths;
import com.b3dgs.lionengine.utility.Media;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.*;
	
public class ControlPanel extends AbstractControlPanel<Tile,ModelSkill,Attributes>{
	
    public static long sndTimer=0;
    public static int playerID;
	public static final Text TEXT = DRAWABLE.createText(Font.SANS_SERIF, 9, Text.NORMAL);
    final TiledSprite skillBackground,stats;
    
	public ControlPanel(Race r) {
		super(DRAWABLE.loadSprite(getSprite("hud_" + r.name().toLowerCase() + ".png")), DRAWABLE.loadTiledSprite(getSprite("icons.png"), 27, 19));
		this.setClickableArea(72, 12,240,176);
		this.icons.load(false);
		this.panel.load(false);
		DRAWABLE.loadSprite(Media.get("sprites", "gold.png")).load(false);
		DRAWABLE.loadSprite(Media.get("sprites", "wood.png")).load(false);
		this.skillBackground=DRAWABLE.loadTiledSprite(getSprite("skill_background.png"), 31, 23);
		this.skillBackground.load(false);
		this.stats=DRAWABLE.loadTiledSprite(getSprite("stats.png"), 66, 34);
		this.stats.load(false);
	}

	public static Sound getRandSfx(SFX s){
		switch(s){
		case hit:
			return getRandSfx(SND_HIT);
		case arrow_hit:
            return getRandSfx(SND_ARROW_HIT);
        case arrow_thrown:
            return getRandSfx(SND_ARROW_THROWN);
        case construction:
            return getRandSfx(SND_CONSTRUCTION);
        case deconstruction:
            return getRandSfx(SND_DECONSTRUCTION);
        case click:
            return getRandSfx(SND_CLICK);
        case valided:
            return getRandSfx(SND_VALIDED);
    }
		return null;
	}
	
	private static Sound getRandSfx(Sound[] sndClick) {
		return sndClick[Maths.random(0,sndClick.length-1)];
	}

	public static Sound getRandSfx(Race race, SFX name) {
		if (Maths.time() - sndTimer > 300) {
			sndTimer = Maths.time();
			int n=0;
	        if (race == Race.orcs) {
	        	n = 1;
	        	}
	        switch (name) {
	        case select:
	        	return getRandSfx(SND_SELECT[n]);
	        case confirm:
	            return getRandSfx(SND_CONFIRM[n]);
	        case die:
	            return getRandSfx(SND_DIE[n]);
	        default:
	            return null;
	            }
	        } else {
	            return null;
	            }
	    }
	
	public static void playSfx(SFX s){
		playSfx(0,null,s);
	}
	
	public static void playSfx(int n,Race r,SFX s){
		try{
			if(r==null){
				if(n==0||n==playerID){
					getRandSfx(s).play();
				}
			}else if(n==playerID||n==0){
				getRandSfx(r,s).play();
			}
		}catch(NullPointerException e){
		}
	}
	
	protected void renderMultipleEntry(Graphics2D arg0, StrategyCursor arg1,Set<AbstractEntry<Tile, ModelSkill, Attributes>> arg2) {
		Set<String> ignore = new HashSet<String>(1);
        ignore.add(((Player) this.player).race + "_STDBUILD");
        this.renderEntrysSkills(arg0,arg1,arg2, 4, 118, 7, 4, 2, ignore);
	}

	protected void renderSingleEntry(Graphics2D arg0, StrategyCursor arg1,AbstractEntry<Tile, ModelSkill, Attributes> arg2) {
		int per=arg2.life.getPercent();
		boolean building =false;
		if(arg2 instanceof ModelBuilding){
			ModelBuilding b = (ModelBuilding) arg2;
			if(b.isUnderConstruction()){
				per=b.getBuildingProgress();
				building = true;
			}
			if(!b.isUnderConstruction()&&b.type==BuildingType.FARM&&b.getOwnerID()==this.player.id){
				TEXT.setColor(new Color(255, 255, 235));
				TEXT.draw(arg0, "Food usage",5,118,Alignment.LEFT);
				TEXT.draw(arg0, "Growth:"+b.player().getFarmGrowth(),10,30,Alignment.LEFT);
				TEXT.draw(arg0,"Used:"+b.player().getFarmUsed(),10,142,Alignment.LEFT);
				}
		}
		if(arg2.getOwnerID()==this.player.id&&!building){
			this.renderEntrySkills(arg0,arg1,arg2,4,118,7,4,2);
		}
		this.stats.render(arg0, 0,2,72);
		this.icons.render(arg0, arg2.icon,6,76);
		TEXT.setColor(new Color(255, 255, 235));
		TEXT.draw(arg0,arg2.getDataString("NAME"),5,97,Alignment.LEFT);
		if(arg2 instanceof GoldMine){
			GoldMine gd = (GoldMine) arg2;
			TEXT.setColor(new Color(255, 255, 235));
			TEXT.draw(arg0, "Gold left:", 5,118,Alignment.LEFT);
			TEXT.draw(arg0,""+(int)gd.gold.getValue(),10,130,Alignment.LEFT);
			}
		if (per > 50) {
            arg0.setColor(Color.GREEN);
        } else if (per > 25) {
            arg0.setColor(Color.YELLOW);
        } else {
            arg0.setColor(Color.RED);
        }
        arg0.fillRect(37, 92, (int) Math.ceil(per * 0.265f), 3);
	}
	
	public void setPlayer(AbstractPlayer player) {
        super.setPlayer(player);
        playerID = player.id;
    }
	
	public void updateSkill(Graphics2D graph,StrategyCursor c,ModelSkill skill,int i,int x,int y,int w,int h){
		int cx=c.getScreenX(),cy=c.getScreenY();
		if(skill.isUnlocked()){
			if(skill.isSelected()&&cx>=x&&cx<=x+w&&cy>=y&&cy<=y+h){
				this.skillBackground.render(graph, 1, x - 2, y - 2);
				super.updateSkill(graph,c,skill,i,x,y+1,w,h);
			}else{
				this.skillBackground.render(graph,0,x-2,y-2);
				super.updateSkill(graph,c,skill,i,x,y,h,w);
				}
		}else{
			this.skillBackground.render(graph, 0,x-2,y-2);
			}
	}
}