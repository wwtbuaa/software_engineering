package com.wwt.warcraft.map;

import com.b3dgs.lionengine.game.map.*;
import com.b3dgs.lionengine.drawable.*;
import com.b3dgs.lionengine.utility.*;
import java.awt.*;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import static com.b3dgs.lionengine.Drawable.DRAWABLE;

public class FogOfWar extends AbstractTiledFogOfWar<Tile>{

	Map map;
	
	public FogOfWar(){
		super();
		TiledSprite hide= DRAWABLE.loadTiledSprite(Media.get("tiles","hide.png"),16,16);
		hide.load(false);
		TiledSprite fog= DRAWABLE.loadTiledSprite(Media.get("tiles","fog.png"),16,16);
		fog.load(false);
		this.setFogTiles(hide, fog);
	}
	
	public void setMap(Map map){
		this.map=map;
	}
	
	protected void onFogChanges(AbstractEntry<?, ?, ?> arg0) {
		if(this.hasFogOfWar()){
			Graphics2D graph=this.map.createMiniMapGraphics();
			graph.drawImage(this.map.getMiniMap(),0,0 ,null);
			for(int v=0;v<this.map.getHeightInTiles();v++){
				for(int h=0;h<this.map.getWidthInTiles();h++){
					if(!this.isFogged(v,h)){
						graph.setColor(Color.DARK_GRAY);
						graph.fillRect(h, v, 1, 1);	
					}
					if(!this.isVisited(v,h)){
						graph.setColor(Color.BLACK);
						graph.fillRect(h,v,1,1);
					}
				}
			}
			graph.dispose();
			}
		}
	}
