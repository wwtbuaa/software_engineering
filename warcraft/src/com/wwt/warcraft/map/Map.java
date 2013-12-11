package com.wwt.warcraft.map;

import java.awt.*;
import java.io.*;
import com.b3dgs.lionengine.file.FileReader;
import com.b3dgs.lionengine.game.map.*;
import com.wwt.warcraft.*;

public class Map extends AbstractPathMap<Tile>{

	public static String THEME;
	static final Color[] GROUND0 = {new Color(9, 43, 8), new Color(45, 29, 25)};
	static final Color[] GROUND1 = {new Color(11, 45, 8), new Color(56, 37, 29)}; 
	static final Color[] GROUND2 = {new Color(17, 51, 8), new Color(59, 39, 30)};
	static final Color[] GROUND3 = {new Color(19, 52, 8), new Color(63, 42, 31)};
	static final Color[] GROUND4 = {new Color(23, 55, 8), new Color(68, 46, 34)};
	static final Color[] GROUND5 = {new Color(23, 59, 6), new Color(74, 50, 35)};
	static final Color[] GROUND6 = {new Color(32, 61, 5), new Color(80, 54, 38)}; 
	static final Color[] GROUND7 = {new Color(82, 40, 19), new Color(79, 56, 39)};
	static final Color[] GROUND8 = {new Color(66, 30, 15), new Color(62, 42, 29)}; 
	static final Color[] GROUND9 = {new Color(128, 72, 32), new Color(128, 72, 12)}; 
	static final Color[] TREE_BORDER = {new Color(10, 28, 9), new Color(42, 40, 21)};
	static final Color[] TREE = {new Color(14, 25, 9), new Color(41, 44, 22)};
	static final Color[] BORDER = {new Color(35, 52, 85), new Color(85, 69, 32)};
	static final Color[] WATER = {new Color(27, 40, 101), new Color(59, 63, 0)};
	public final FogOfWar fogofwar;
	public final Border20Map mapedge;
	Border20[][] trees;
	int id;
	int treecut;
	
	public Map(World world,FogOfWar fogofwar) {
		super("tiles",16,16, world.height);
		this.fogofwar=fogofwar;
		this.fogofwar.setMap(this);
		this.mapedge=new Border20Map(true);
		this.id=0;
	}
	
	public Tile createTile(int arg0, int arg1, int arg2, int arg3, String arg4) {
		return new Tile(arg0,arg1,arg2,arg3,arg4);
	}
	
	public void create(int width,int height){
		super.create(width,height);
		this.trees=new Border20[height][width];
		this.mapedge.create(this);
		THEME=this.theme;
		if(this.theme.equals("swam'")){
			this.id=1;
			this.treecut=143;
		}
	}

	public int getTCut(){
		return this.treecut;
	}
	
	public void updateTree(Tile tile,boolean check){
		int x=tile.getX();
		int y =-tile.getY();
		if(check){
			this.mapedge.checkAll(this.trees, x, y,0, 0, 1);
		}else{
			this.mapedge.updateExclude(this.trees,x,y);
		}
		this.mapedge.finalCheck(this.trees,x,y);
		for(int v=y-1;v<=y+1;v++){
			for(int h=x-1;h<=x+1;h++){
				Tile t = this.getTile(v,h);
				if(t!=null){
					if(t.getId()!=Border20.NONE){
						Border20 a =this.mapedge.get(this.trees,v,h);
						t.setNumber(a);
					}else{
						this.mapedge.set(this.trees,v,h,Border20.NONE);
					}
				}
			}
		}
	}
	
	public void setAxis(Tile tile,Border20 border20){
		this.mapedge.set(this.trees,-tile.getY()/this.getTileHeight(),tile.getX()/this.getTileWidth(), border20);
	}
	
	public void renderTile(Graphics2D graph,Tile tile,int x,int y,int xtree,int ytree){
		if(this.fogofwar.isVisited(xtree, ytree)){
			super.renderTile(graph, tile, x,y,xtree,ytree);
		}
	}
	
	public void load(FileReader fr) throws IOException {
        super.load(fr);
        for (int v = 0; v < this.heightInTile; v++) {
            for (int h = 0; h < this.widthInTile; h++) {
                Tile tile = this.getTile(v, h);
                this.trees[v][h] = tile.getId();
            }
        }
    }
	
	public Color getTilePixelColor(Tile tile){
		switch (tile.getCollisionEnum()) {
		case GROUND0:
			return GROUND0[this.id];
         case GROUND1:
             return GROUND1[this.id];
         case GROUND2:
             return GROUND2[this.id];
         case GROUND3:
             return GROUND3[this.id];
         case GROUND4:
             return GROUND4[this.id];
         case GROUND5:
             return GROUND5[this.id];
         case GROUND6:
             return GROUND6[this.id];
         case GROUND7:
             return GROUND7[this.id];
         case GROUND8:
             return GROUND8[this.id];
         case GROUND9:
             return GROUND9[this.id];
         case TREE_BORDER:
             return TREE_BORDER[this.id];
         case TREE:
             return TREE[this.id];
         case BORDER:
             return BORDER[this.id];
         case WATER:
             return WATER[this.id];
         default:
             return Color.BLACK;
             }
		}
}