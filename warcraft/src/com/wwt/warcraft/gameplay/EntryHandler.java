package com.wwt.warcraft.gameplay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import com.b3dgs.lionengine.drawable.AnimatedSprite;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.b3dgs.lionengine.game.strategy.StrategyCamera;
import com.b3dgs.lionengine.game.strategy.StrategyCursor;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.map.CollisionType;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelAttacker;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.ModelWorker;

public class EntryHandler extends AbstractEntryHandler<Tile,ModelSkill,Attributes>{
	final Map map;
	int pathCount,selectionSize;
	boolean cancelClick;
	final TreeMap<Integer,Effect> effects=new TreeMap<Integer,Effect>();
	final Set<Integer> remove=new HashSet<Integer>(1);
	
	public EntryHandler(ControlPanel panel,Map map){
		super(panel);
		this.map=map;
		this.pathCount=0;
		this.selectionSize=0;
		this.cancelClick=false;
	}
	
	public void addEffect(int id, AnimatedSprite eff1, int x1, int y1, AnimatedSprite eff2, int x2, int y2) {
		this.effects.put(id, new Effect(eff1, x1, y1, eff2, x2, y2));
	}

	public void removeEffect(int id) {
		this.remove.add(id);
	}
	
	public void activateEffect(int id,int n,boolean state){
		Effect e = this.effects.get(id);
		if (n == 0) {
			e.activate1(state);
		}else if (n == 1) {
			e.activate2(state);
		}
	}
	
    public void update(Keyboard keyboard, StrategyCursor cursor, StrategyCamera camera, float f) {
        this.pathCount = 0;
        this.selectionSize = (int) Math.sqrt(this.getSelection().size());
        super.update(keyboard, cursor, camera, f);
    }
    
    public void render(Graphics2D graph,StrategyCamera camera,StrategyCursor cursor){
    	super.render(graph, camera,cursor);
    	Set<Integer> set = effects.keySet();
    	boolean state=false;
        for (Integer k : set) {
            Effect e = this.effects.get(k);
            e.render(graph, camera);
            if (e.eff2.getFrame() > 17) {
                state= true;
            }
        }
        if (state) {
            for (Integer e : this.remove) {
                this.effects.remove(e);
            }
        }
    }
    
    public boolean canBeSelected(AbstractEntry<Tile, ModelSkill, Attributes> entry) {
        for (int v = entry.getYInTile(); v < entry.getYInTile() + entry.getHeightInTile(); v++) {
            for (int h = entry.getXInTile(); h < entry.getXInTile() + entry.getWidthInTile(); h++) {
                if (this.map.fogofwar.isVisited(v, h)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void updateEntry(Keyboard keyboard, StrategyCursor cursor, StrategyCamera camera,AbstractEntry<Tile, ModelSkill, Attributes> entry, float f){
    	super.updateEntry(keyboard, cursor, camera, entry, f);
    	int x=cursor.getHorizontalMapTile();
    	int y=cursor.getVerticalMapTile();
    	if(cursor.getClick()==Mouse.LEFT){
            if (!this.cancelClick && !entry.isSelected() && entry instanceof ModelWorker) {
                if (((ModelWorker) entry).player() == this.player) {
                    entry.getSkill(entry.getDataString("FACTION").toUpperCase() + "_CANCEL").action();
                    this.cancelClick = true;
                }
            }
    	}else{
    		this.cancelClick=false;
    	}
    	if(entry.isSelected() && !this.clicked && entry.getOwnerID() == this.player.id){
    		 if (!keyboard.used() && cursor.getClick() == Mouse.RIGHT && cursor.canClick(this.panel)) {
    			 if(entry instanceof ModelUnit){
    				 ModelUnit u = (ModelUnit) entry;
    				 if (u instanceof ModelAttacker) {
    					 int id = u.map.getRef(y, x);
                         if (id > 0) {
                        	 AbstractEntry<Tile, ModelSkill, Attributes> e = ModelUnit.get(id);
                             if (e == null) {
                            	 e = ModelBuilding.get(id);
                             }
                             if (e != null && e.getOwnerID() != u.getOwnerID()) {
                                 ((ModelAttacker) u).attack(e);
                                 return;
                             }
                         }
    				 }
    			 this.move(u,x,y);
    			 }
    		 }
    	}
    }
    
    public void move(ModelUnit unit,int x,int y){
    	int ox=0,oy=0;
    	if(this.pathCount>0){
    		 ox = this.pathCount % this.selectionSize;
             oy = (int) Math.floor(this.pathCount / this.selectionSize);
    	}
    	this.clickedFlag=false;
    	this.pathCount++;
    	if (unit instanceof ModelAttacker) {
    		((ModelAttacker) unit).stopAttack();
    	    ((ModelAttacker) unit).setTarget(null);
    	}
    	if(unit instanceof ModelWorker){
            int id = this.map.getRef(y, x);
            ModelBuilding e = ModelBuilding.get(id);
            if (this.map.getTile(y, x).getCollType() == CollisionType.TREE || (id > 0 && e instanceof GoldMine)) {
                ModelWorker worker = (ModelWorker) unit;
                ModelSkill s = worker.getSkill("EXTRACT");
                s.setDestination(x, y);
                s.action();
                return;
            }
    	}
    	unit.assignDestination(x+ox, y+oy);
    	ControlPanel.playSfx(unit.getOwnerID(),unit.faction,SFX.confirm);
    }
    
    public void renderCursorSelection(Graphics2D graph,Color color,int x,int y,int w,int h){
    	super.renderCursorSelection(graph,color,x,y,w,h);
    }
    
    public void renderEntryOver(Graphics2D graph, AbstractEntry<Tile, ModelSkill, Attributes> entry, StrategyCamera camera, Color color) {
        super.renderEntryOver(graph, entry, camera, Color.GRAY);
    }
    
    public void renderEntrySelection(Graphics2D graph,AbstractEntry<Tile, ModelSkill, Attributes> entry, StrategyCamera camera, Color color){
    	if(entry.getOwnerID()==0){
    		super.renderEntrySelection(graph,entry,camera,Color.LIGHT_GRAY);
    	}else if(entry.getOwnerID()!=this.player.id){
    		super.renderEntrySelection(graph,entry,camera,Color.RED);
    	}else{
    		super.renderEntrySelection(graph,entry,camera,Color.GREEN);
    	}
    }
}
