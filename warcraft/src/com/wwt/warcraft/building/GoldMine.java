package com.wwt.warcraft.building;

import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.wwt.warcraft.ResourceHandler;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.gameplay.Resource;
import com.wwt.warcraft.map.Map;

public class GoldMine extends ModelBuilding{

	public final Resource gold;
	
	public GoldMine(Map m,ResourceHandler r,EntryHandler e) {
		super(m,r,r.get("GOLDMINE"),r.get("CONSTRUCTION").ressource,e);
		this.gold=new Resource(0);
	}

	public void update(Keyboard keyboard,Mouse mouse,float f){
		super.update(keyboard,mouse,f);
		this.gold.update(f,4.0f);
		if(this.gold.get()<=0){
			this.life.set(0);
		}
	}
	
    public boolean isEmpty() {
        return !(this.gold.get() > 0);
    }
}
