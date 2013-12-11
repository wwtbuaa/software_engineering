package com.wwt.warcraft.gameplay;

import java.awt.Color;
import java.awt.Graphics2D;

import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.StrategyCamera;
import com.b3dgs.lionengine.game.strategy.StrategyCursor;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class Minimap {
	final Map map;
	final StrategyCursor cursor;
	final StrategyCamera camera;
	final EntryHandler entrys;
	int mapViewH,mapViewV;
	boolean minimapLocked,canMinimap;
	Player player;
	
	public Minimap(Map map,StrategyCursor cursor,StrategyCamera camera,EntryHandler entrys){
		this.map=map;
		this.cursor=cursor;
		this.camera=camera;
		this.entrys=entrys;
	}
	
	public void setPlayer(Player player){
		this.player=player;
	}
	
	public void setView(int h,int v){
		this.mapViewH=h;
		this.mapViewV=v;
	}
	
	public void update(int x,int y){
		if(this.cursor.getClick()==0){
			this.minimapLocked=false;
			this.canMinimap=true;
		}
        int cx = this.cursor.getScreenX();
        int cy = this.cursor.getScreenY();
        int maxX = x + this.map.getWidthInTiles();
        int maxY = y + this.map.getHeightInTiles();
        if (!this.minimapLocked) {
            if (cx >= x && cx <= maxX && cy >= y && cy <= maxY) {
                if (this.cursor.getClick() > 0) {
                    this.minimapLocked = true;
                }
            } else {
                if (this.cursor.getClick() > 0) {
                    this.canMinimap = false;
                }
            }
        }
        if (this.minimapLocked && this.canMinimap) {
            int mx = (this.cursor.getScreenX() - this.mapViewH / 2 - x) * this.map.getTileWidth();
            int my = (this.cursor.getScreenY() - this.mapViewV / 2 - y) * this.map.getTileHeight();
            this.camera.place(mx, my);
        }
    }

    public void render(Graphics2D g, int x, int y) {
        this.map.renderMiniMap(g, x, y);
        for (AbstractEntry<Tile, ModelSkill, Attributes> entry : this.entrys.list()) {
            if (entry.isAlive() && entry.isVisible() && entry.isActive()) {
                if (entry.getOwnerID() == this.player.id) {
                    g.setColor(Color.GREEN);
                } else if (entry.getOwnerID() == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.RED);
                }
                if (this.map.fogofwar.isVisited(entry.getYInTile(), entry.getXInTile())
                        && this.map.fogofwar.isFogged(entry.getYInTile(), entry.getXInTile())) {
                    g.fillRect(entry.getXInTile() + x, entry.getYInTile() + y, entry.getWidthInTile(), entry.getHeightInTile());
                }
            }
        }
        g.setColor(Color.GREEN);
        int sx = (this.camera.getOffsetX() + this.camera.getX()) / this.map.getTileWidth();
        int sy = (this.camera.getOffsetY() + this.camera.getY()) / this.map.getTileHeight();
        g.drawRect(x + sx - 1, y + sy - 1, this.mapViewH + 1, this.mapViewV + 1);
	}
}
