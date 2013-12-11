package com.wwt.warcraft.building;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;

import com.b3dgs.lionengine.Drawable;
import com.b3dgs.lionengine.drawable.*;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.CollisionArea;
import com.b3dgs.lionengine.game.MediaRessource;
import com.b3dgs.lionengine.game.strategy.*;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.*;
import com.wwt.warcraft.map.*;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.skill.*;
import com.wwt.warcraft.unit.ModelWorker;

public abstract class ModelBuilding extends AbstractBuilding<Tile,ModelSkill,Attributes>{
	public static final TreeMap<Integer, ModelBuilding> ENTRYS = new TreeMap<Integer, ModelBuilding>();
	public final Map map;
	public final BuildingType type;
	public final Race faction;
	public Player player;
	public final EntryHandler handler;
	public final TiledSprite construction;
	public final AnimatedSprite burning, explode;
	public boolean destroyed, isOnScreen;
	
	public ModelBuilding(Map m,ResourceHandler r,MediaRessource<BufferedImage> b, BufferedImage construction, EntryHandler e) {
		super(b.file,m,b.ressource,new Attributes());
		this.handler=e;
		this.map=m;
		this.type = BuildingType.valueOf(this.getDataString("TYPE").toUpperCase());
        this.setFieldOfView(this.getDataInt("FOV"));
        this.setFrame(this.getDataInt("DEFAULT_FRAME"));
        this.faction = Race.valueOf(this.getDataString("FACTION").toLowerCase());
        this.life.setMax(this.getDataInt("MAX_LIFE"));
        this.life.set(this.life.getMax());
        this.construction = Drawable.DRAWABLE.loadTiledSprite(construction, 48, 48);
        this.construction.load(false);
        this.burning = Drawable.DRAWABLE.loadAnimatedSprite(r.get("BURNING").ressource, 4, 2);
        this.burning.load(false);
        this.explode = Drawable.DRAWABLE.loadAnimatedSprite(r.get("EXPLODE").ressource, 6, 3);
        this.explode.load(false);
        this.destroyed = false;
        this.setLayer(1);
        this.map.fogofwar.updateEntryFOV(this);
        this.manage();
	}

	public static ModelBuilding get(int id) {
	        return ENTRYS.get(id);
	        }

	public static void clear() {
	        ENTRYS.clear();
	    }

	public static List<ModelBuilding> getByOwner(int id){
		List<ModelBuilding> list = new ArrayList<ModelBuilding>(1);
		Collection<ModelBuilding> c=ENTRYS.values();
		for(ModelBuilding i:c){
			if(i.getOwnerID()==id){
				list.add(i);
			}
		}
		return list;
	}
	
	public void manage(){
		ENTRYS.put(this.id, this);
	}
	
	public void place(int x,int y){
		super.place(x,y);
		this.map.fogofwar.updateEntryFOV(this);
		this.handler.addEffect(this.id,this.burning,this.getX()+this.getWidth()/2-8,this.getY()-16,this.explode,this.getX(),this.getY()+this.getWidth()-64);
	}
	
	public void update(Keyboard keyboard,Mouse mouse,float f){
		super.update(keyboard, mouse, f);
		if(!this.isAlive()&&!this.destroyed){
			this.explode.updateAnimation(f);
			this.explode.play(1,18,0.12f,false,false);
			if(this.explode.getFrame()>5){
				this.setVisibility(false);
			}
			if(this.explode.getFrame()>17){
				this.remove();
			}
			if(this.explode.getAnimState()==AnimState.FINISHED){
				this.destroyed=true;
			}
		}
		if(this.isAlive()&&this.life.getPercent()<=50){
			this.burning.updateAnimation(f);
			if(this.life.getPercent()>25){
				this.burning.play(1,4,0.2f,false,true);
			}else{
				this.burning.play(5,8,0.2f,false,true);
			}
		}
		if(this.animName!=null){
			CollisionArea area=this.getCollArea(this.animName);
			this.updateCollision(area.getX(),area.getY(),area.getWidth(),area.getHeight());
		}
	}
	
	public void render(Graphics2D graph,Camera camera){
		if(this.isUnderConstruction()){
			int progress = this.getBuildingProgress();
			int tile=0,x,y;
			if(progress>25){
				tile=1;
				x=18-this.getWidth()/2;
				y=18-this.getHeight()/2;
			}else{
				x=16-this.getWidth()/2;
				y=16-this.getHeight()/2;
			}
			if(progress<=50){
				this.construction.render(graph, tile,this.getX() - camera.getX() - x, this.getY() - camera.getY() - y);
			}else{
				super.render(graph,camera);
			}
		}else{
			super.render(graph, camera);
		}
		if(this.explode.getFrame()<18){
			if(!this.isActive()&&this.explode.getFrame()<18){
				this.handler.activateEffect(this.id,1,true);
			}else{
				this.handler.activateEffect(this.id,1,false);
			}
			if(this.isAlive()&&this.life.getPercent()<=50){
				this.handler.activateEffect(this.id,0,true);
			}else{
				this.handler.activateEffect(this.id,0,false);
			}
			if(this.getX()>camera.getX()&& this.getX() <= camera.getX() + 320 && this.getY() >= camera.getY() && this.getY() <= camera.getY() + 200){
				this.isOnScreen=true;
			}else{
				this.isOnScreen=false;
			}
		}
	}
	
	public void setOwnerID(int id){
		super.setOwnerID(id);
		if(id>0){
			this.player=(Player)AbstractPlayer.get(id);
		}
	}
	
	public Player player(){
		return this.player;
	}
	
	public void onConstructed() {
		this.setFrame(2);
		this.setActive(true);
		this.map.fogofwar.updateEntryFOV(this);
	}

	public void onConstructing() {
		if(this.getBuildingProgress()>=50){
			this.setVisibility(true);
		}
	}

	public void onDestroyed() {
		if(this.isOnScreen){
			ControlPanel.playSfx(SFX.deconstruction);
		}
		this.setActive(false);
		this.destroyed=false;
		this.explode.setFrame(1);
		if(this.player!=null){
			this.player.removeBuilding(this);
		}
		this.handler.removeEffect(this.id);
	}

	public void onHit(AbstractEntry<Tile,ModelSkill,Attributes>attacker){
		super.onHit(attacker);
		this.player().defendMe(this,attacker);
	}
	
	public void onStartConstruction() {
		
	}

	public void onKilled(AbstractEntry<Tile, ModelSkill, Attributes> arg0) {
		
	}

	public void onOrderedFail(ModelSkill arg0) {
		
	}

	public void onSelection() {
		if(this.isUnderConstruction()){
			ControlPanel.playSfx(this.getOwnerID(),null,SFX.construction);
		}else{
			ControlPanel.playSfx(this.getOwnerID(), null, SFX.click);
		}
	}

	public ModelWorker getBuilder(){
		if(this.builder!=null){
			return (ModelWorker)this.builder;
		}else{
			return null;
		}
	}
	
	public boolean isOnSceen(){
		return this.isOnScreen;
	}
}
