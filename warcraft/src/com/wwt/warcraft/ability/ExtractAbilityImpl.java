package com.wwt.warcraft.ability;

import java.util.List;

import com.b3dgs.lionengine.game.map.Border20;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.ability.AbstractExtractAbility;
import com.b3dgs.lionengine.game.strategy.ability.ExtractAbility;
import com.b3dgs.lionengine.geometry.Point2D;
import com.b3dgs.lionengine.utility.Maths;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ResourceType;
import com.wwt.warcraft.map.CollisionType;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelWorker;

public class ExtractAbilityImpl extends AbstractExtractAbility<Tile, ModelSkill, Attributes, ResourceType>implements ExtractAbility<Tile, ModelSkill, Attributes, ResourceType> {
	final ModelWorker worker;
	boolean extract,carry;
	Tile tile;
	
    public ExtractAbilityImpl(AbstractUnit<Tile, ModelSkill, Attributes> worker) {
        super(worker);
        this.worker = (ModelWorker) worker;
    }

	public boolean canCarry() {
		int id=this.worker.map.getRef(this.whsY, this.whsX);
		if(id>0){
			ModelBuilding e=ModelBuilding.get(id);
			if(e!=null){
				if(this.worker.getDistance(e)<=0){
					return true;
				}
			}
		}
		return false;
	}

	public boolean canExtract() {
		this.tile=null;
		this.extract=false;
		this.carry=false;
		if(this.rscX==-1||this.rscY==-1){
			return false;
		}
	      if (this.worker.map.getTile(this.rscY, this.rscX).getCollType() == CollisionType.TREE) {
	            if (Maths.getDistance(this.worker.getXInTile(), this.worker.getYInTile(), this.rscX, this.rscY) <= 1) {
	                this.worker.stopMoves();
	                if (this.worker.hasReachedDestination()) {
	                    this.setRessourceType(ResourceType.WOOD);
	                    this.setExtractionTime(20000);
	                    this.tile = this.worker.map.getTile(this.rscY, this.rscX);
	                    return true;
	                }
	            }
	        } else if (this.worker.map.getTile(this.rscY, this.rscX).getNumber() == this.worker.map.getTCut()) {
	            this.searchNextTree(1);
	            if (this.worker.map.getTile(this.rscY, this.rscX).getCollType() != CollisionType.TREE) {
	            	this.worker.stop();
	            }
	        }else{
	        	 int id = this.worker.map.getRef(this.rscY, this.rscX);
	             if (id > 0) {
	                 ModelBuilding e = ModelBuilding.get(id);
	                 if (e instanceof GoldMine) {
	                     if (this.worker.getDistance((GoldMine) e) <= 1) {
	                         this.worker.stopMoves();
	                         if (this.worker.hasReachedDestination()) {
	                             this.setRessourceType(ResourceType.GOLD);
	                             this.setExtractionTime(1500);
	                             this.worker.setVisibility(false);
	                             this.worker.setActive(false);
	                             this.worker.setAvailable(false);
	                             this.worker.unselect();
	                             return true;
	                         }
	                     }
	                 }
	             }
	        }
	      return false;
	}

	public void onCarried() {
		this.carry=false;
		switch(this.worker.getRessourceType()){
		case GOLD:
			this.worker.player().gold.add(100);
			break;
		case WOOD:
			this.worker.player().wood.add(100);
			break;
		}
	}

	public void onCarry() {
		if(!this.carry){
			switch(this.worker.getRessourceType()){
			case GOLD:
				this.worker.setAnimation("CARRY_GOLD");
				break;
			case WOOD:
				this.worker.setAnimation("CARRY_WOOD");
				break;
			}
			this.carry=true;
			this.worker.setDropOffTime(1500);
		}
	}

	public void onDropOff() {
		this.worker.unselect();
		this.worker.setAvailable(false);
		this.worker.setActive(false);
		this.worker.setVisibility(false);
		if (this.worker.map.getRef(this.worker.getYInTile(), this.worker.getXInTile()) == this.worker.id) {
			this.worker.map.setRef(this.worker.getYInTile(), this.worker.getXInTile(), 0);
		}
	}

	public void onDroppedOff() {
		this.getOutFromTo(this.whsX, this.whsY, this.rscX, this.rscY);
        this.worker.setVisibility(true);
        this.worker.setActive(true);
        this.worker.ignoreID(this.worker.map.getRef(this.whsY, this.whsX), false);
        if (this.getRessourceType() == ResourceType.GOLD) {
            this.worker.ignoreID(this.worker.map.getRef(this.rscY, this.rscX), true);
        }
        if (this.getRessourceType() == ResourceType.WOOD) {
            this.searchNextTree(1);
            if (this.worker.map.getTile(this.rscY, this.rscX).getCollType() != CollisionType.TREE) {
                this.stopExtraction();
            }
        }
        this.worker.setAvailable(true);
	}

	public void onExtract() {
		if(!this.extract){
			 if (this.getRessourceType() == ResourceType.WOOD) {
	                this.worker.setAnimation("EXTRACT");
			 }
			 int id=this.worker.map.getRef(this.rscX,this.rscY);
			 if(id>0){
				 ModelBuilding e=ModelBuilding.get(id);
				 if(e instanceof GoldMine){
					 GoldMine g=(GoldMine) e;
					 if(g.isEmpty()){
						 this.worker.stopExtraction();
						 this.getOutFromTo(this.rscX,this.rscY,this.whsX,this.whsY);
					 }else if(g.gold.canSpend(100)){
						 g.gold.spend(100);
					 }
				 }
			 }
			 this.extract=true;
		}
		if(this.getRessourceType()==ResourceType.GOLD){
			if(this.worker.map.getRef(this.worker.getYInTile(),this.worker.getXInTile())==this.worker.id){
				this.worker.map.setRef(this.worker.getYInTile(),this.worker.getXInTile(),0);
			}
		}
		if(this.getRessourceType()==ResourceType.WOOD){
			 if (this.worker.map.getTile(this.rscY, this.rscX).getCollType() != CollisionType.TREE) {
	                this.searchNextTree(1);
	                this.startExtraction();
			 }
		}
	}

	public void getOutFromTo(int rscX, int rscY, int whsX, int whsY) {
		List<Point2D> places=this.worker.map.getAllFreePlaceArround(rscX,rscY,1,1,0,3);
		Point2D best=null;
		int min=Integer.MAX_VALUE;
		for(Point2D p:places){
			int d=Maths.getDistance(whsX,whsY,p.getX(),p.getY());
			if(d<min){
				min=d;
				best=p;
			}
		}
		places.clear();
		if(best!=null){
			this.worker.place(best.getX(), best.getY());
		}
	}

	public void onExtracted() {
		if(!this.hasWarehouse()){
			 ModelBuilding b = worker.player().getClosestBuilding(this.worker, BuildingType.TOWNHALL);
			 this.worker.setWarehouse(b.getXInTile() + b.getXInTile() / 2, b.getYInTile() + b.getYInTile() / 2);
		}
		this.extract=false;
		this.worker.setAvailable(true);
		if(this.tile!=null){
			Map map=this.worker.map;
			int w=map.getTileWidth();
			int h=map.getTileHeight();
			this.cutTree(map,w,h,this.tile);
			this.tile=null;
		}else{
			this.getOutFromTo(this.rscX,this.rscY,this.whsX,this.whsY);
			this.worker.setVisibility(true);
			this.worker.setActive(true);
		}
		if(this.getRessourceType()==ResourceType.GOLD){
			this.worker.ignoreID(this.worker.map.getRef(this.rscY, this.rscX),false);
		}
		this.worker.ignoreID(this.worker.map.getRef(this.whsY, this.whsX),false);
	}

	public void cutTree(Map map, int w, int h, Tile tile) {
		this.cut(tile);
		Border20 n;
		Tile top=this.getTop(map,w,h,tile);
		if(top!=null){
			n=top.getId();
			if (n == Border20.DOWN || n == Border20.DOWN_MIDDLE || n == Border20.CORNER_DOWN_LEFT || n == Border20.CORNER_DOWN_RIGHT) {
				this.cut(top);
			}
		}
		Tile down = this.getDown(map, w, h, tile);
		if (down != null) {
			n = down.getId();
			if (n == Border20.TOP || n == Border20.TOP_MIDDLE || n == Border20.CORNER_TOP_LEFT || n == Border20.CORNER_TOP_RIGHT) {
				this.cut(down);
			}
		}
		Tile left = this.getLeft(map, w, h, tile);
        if (this.getLeft(map, w, h, left).getId() == Border20.NONE && this.getRight(map, w, h, left).getId() == Border20.NONE) {
            if (left.getId() != Border20.NONE) {
                left.setNumber(Border20.TOP_MIDDLE);
                this.worker.map.setAxis(left, Border20.TOP_MIDDLE);
                Tile t = this.getTop(map, w, h, left);
                if (t.getId() == Border20.RIGHT) {
                    t.setNumber(Border20.RIGHT_MIDDLE);
                    this.worker.map.setAxis(t, Border20.RIGHT_MIDDLE);
                }
            }		
        }
        this.worker.map.updateTree(tile, false);
        if(top!=null){
        	this.worker.map.updateTree(top,true);
        }
        if(down!=null){
        	this.worker.map.updateTree(down,true);
        }
	}

	public void onGotoRessource() {
	}
	
	public void searchNextTree(int n){
		for(int h=this.rscX-n;h<=this.rscX+n;h++){
			try{
				if(this.worker.map.getTile(this.rscY, h).getCollType()==CollisionType.TREE){
					this.setRessourceLocation(h, this.rscY);
					return;
				}
			}catch(NullPointerException e){
				continue;
			}
		}
		if(n<64){
			this.searchNextTree(n+1);
		}
	}
	
	public void cut(Tile tile){
		tile.setNumber(this.worker.map.getTCut());
		tile.setBlocking(false);
		tile.setCollType(CollisionType.GROUND);
	}
	
    public Tile getTop(Map map, int w, int h, Tile tile) {
        return map.getTile(-tile.getY() / h - 1, tile.getX() / w);
    }

    public Tile getLeft(Map map, int w, int h, Tile tile) {
        return map.getTile(-tile.getY() / h, tile.getX() / w - 1);
    }

    public Tile getRight(Map map, int w, int h, Tile tile) {
        return map.getTile(-tile.getY() / h, tile.getX() / w + 1);
    }

    public Tile getDown(Map map, int w, int h, Tile tile) {
        return map.getTile(-tile.getY() / h + 1, tile.getX() / w);
    }
}
