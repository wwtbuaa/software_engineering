package com.wwt.warcraft.unit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import com.b3dgs.lionengine.Drawable;
import com.b3dgs.lionengine.drawable.TiledSprite;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.CollisionArea;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.AbstractPlayer;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.Orientation;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.b3dgs.lionengine.utility.Maths;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.skill.Move;
import com.wwt.warcraft.skill.Stop;

public abstract class ModelUnit extends AbstractUnit<Tile, ModelSkill, Attributes>{

	static final TreeMap<Integer, ModelUnit> ENTRYS = new TreeMap<Integer, ModelUnit>();
	static final Orientation[] orientations = Orientation.values();
	public final Map map;
	public final UnitType type;
	public final Race faction;
	public Player player;
	boolean isOnScreen,dead;
	TiledSprite corpse;
	long deadtimer,angletimer,nextangletimer;
	int deadindex,deadoffset;
	
	public ModelUnit(Map map, ResourceHandler resource, String file,BufferedImage ressource) {
		super(file,map,ressource,new Attributes());
		this.map=map;
		this.type=UnitType.valueOf(this.getDataString("TYPE").toUpperCase());
		this.setFieldOfView(this.getDataInt("FOV"));
		this.setFrame(this.getDataInt("DEFAULT_FRAME"));
		this.setSkipLastFrameOnReverse(true);
		this.faction = Race.valueOf(this.getDataString("FACTION").toLowerCase());
        this.life.setMax(this.getDataInt("MAX_LIFE"));
        this.life.set(this.life.getMax());
        this.addSkill(new Move(0, this));
        this.addSkill(new Stop(1, this));
        this.setSpeed(1.5f, 1.5f);this.setLayer(2);
        this.corpse = Drawable.DRAWABLE.loadTiledSprite(resource.get("CORPSE").ressource, 32, 32);
        this.corpse.load(false);
        this.deadtimer=-1;
        this.dead=false;
        this.deadindex=0;
        if(this.faction==Race.orcs){
        	this.deadoffset=8;
        }else{
        	this.deadoffset=0;
        }
        this.map.fogofwar.updateEntryFOV(this);
        this.angletimer = Maths.time();
        this.nextangletimer = Maths.random(0, 2000) + 5000L;
        this.manage();
	}

	public static ModelUnit get(int id){
		return ENTRYS.get(id);
	}
	
	public static void clear(){
		ENTRYS.clear();
	}
	
	public static List<ModelUnit> getByOwner(int id){
		List<ModelUnit> list=new ArrayList<ModelUnit>(1);
		Collection<ModelUnit> c = ENTRYS.values();
		for(ModelUnit u:c){
			if(u.getOwnerID()==id){
				list.add(u);
			}
		}
		return list;
	}

	public void manage(){
		ENTRYS.put(this.id,this);
	}
	
	public void place(int x,int y){
		super.place(x,y);
		this.map.fogofwar.updateEntryFOV(this);
	}
	
	public void updata(Keyboard keyboard,Mouse mouse,float f){
		int x=this.getXInTile();
		int y=this.getYInTile();
		super.update(keyboard,mouse,f);
		Orientation o=this.getOrientation();
		if(o.ordinal()>4){
			if(!this.getMirror()){
				this.mirror(true);
			}
		}else{
			if(this.getMirror()){
				this.mirror(false);
			}
		}
		if(!this.isAlive()){
			if(!this.dead){
				if(this.deadtimer==-1){
					this.deadtimer=Maths.time();
				}
				if (Maths.time() - this.deadtimer > 5000) {
					this.setVisibility(false);
					this.dead=true;
					this.deadindex=0;
					this.deadtimer=Maths.time();
				}
			}else{
					if(this.deadindex<=3 && Maths.time() - this.deadtimer > 5000) {
						this.deadindex++;
						this.deadtimer=Maths.time();
					}
			}
			if(this.deadindex>3){
				this.remove();
			}
		}else{
			if (x!= this.getXInTile() || y != this.getYInTile()) {
                this.map.fogofwar.updateEntryFOV(this);
            }
			if (this.isPassive() && Maths.time() - this.angletimer > this.nextangletimer) {
                this.setAnimation("IDLE");
                this.setOrientation(orientations[Maths.random(0, orientations.length - 1)]);
                this.angletimer = Maths.time();
                this.nextangletimer = Maths.random(0, 2000) + 5000L;
            }
		}
		if(this.animName!=null){
			CollisionArea area = this.getCollArea(this.animName);
            this.updateCollision(area.getX(), area.getY(), area.getWidth(), area.getHeight());
        } else {
            this.updateCollision(16, 16, 0, 0);
        }
	}
	
	public void render(Graphics2D graph,Camera camera){
		super.render(graph, camera);
		if(this.dead&&this.deadindex<=3){
			int o=0;
			if(this.getOrientation().ordinal()>0){
				o=4;
			}
			this.corpse.render(graph,this.deadindex+this.deadoffset + o, this.getX() - camera.getX() - 8,this.getY() - camera.getY() - 8);
		}
		 if (this.getX() >= camera.getX() && this.getX() <= camera.getX() + 320 && this.getY() >= camera.getY() && this.getY() <= camera.getY() + 200) {
	            this.isOnScreen = true;
		 }else{
			 this.isOnScreen=false;
		 }
	}
	
	public void setOwnerID(int id){
		super.setOwnerID(id);
		if(id>0){
			this.player = (Player) AbstractPlayer.get(id);
		}
	}
	
	public Player player(){
		return this.player;
	}
	
	public void stop(){
		super.stop();
		this.clearIgnoredID();
		this.angletimer=Maths.time();
	}
	
	public void onStartMove() {
		this.setAnimation("MOVE");
		}
	
	public void onMove(){
		if(!this.animName.equals("MOVE")){
			this.setAnimation("MOVE");
			}
	}
	
	public void onArrived() {
        this.setAnimation("IDLE");
        this.angletimer = Maths.time();
    }
	
	public void onDied() {
        if (this.getOrientation().ordinal() < 4) {
            this.setOrientation(Orientation.NORTH);
        } else {
            this.setOrientation(Orientation.NORTH_EAST);
        }
        this.setAnimation("DIE");
        this.player.removeUnit(this);
        if (this.isOnScreen()) {
            ControlPanel.playSfx(0, this.faction, SFX.die);
        }
    }
	
	public void onSelection() {
        ControlPanel.playSfx(this.getOwnerID(), this.faction, SFX.select);
    }
	
	public void onOrderedFail(ModelSkill skill) {
    }
	
	 public void onKilled(AbstractEntry<Tile, ModelSkill, Attributes> attacker) {
	    }
	 
	 public boolean isPassive() {
	        return !this.isMoving();
	    }
	 
	 public boolean isOnScreen() {
	        return this.isOnScreen;
	    }
}
		