package com.wwt.warcraft;

import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.building.HUMAN.HumansBarracks;
import com.wwt.warcraft.building.HUMAN.HumansFarm;
import com.wwt.warcraft.building.HUMAN.HumansLumberMill;
import com.wwt.warcraft.building.HUMAN.HumansTownHall;
import com.wwt.warcraft.building.ORCS.OrcsBarracks;
import com.wwt.warcraft.building.ORCS.OrcsFarm;
import com.wwt.warcraft.building.ORCS.OrcsLumberMill;
import com.wwt.warcraft.building.ORCS.OrcsTownHall;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.UnitType;
import com.wwt.warcraft.unit.HUMAN.Archer;
import com.wwt.warcraft.unit.HUMAN.Footman;
import com.wwt.warcraft.unit.HUMAN.Peasant;
import com.wwt.warcraft.unit.ORCS.Grunt;
import com.wwt.warcraft.unit.ORCS.Peon;
import com.wwt.warcraft.unit.ORCS.Spearman;

public class EntryFactory {
	final Map map;
	final EntryHandler entrys;
	final ResourceHandler rsc;
	
	public EntryFactory(Map map,EntryHandler entrys,ResourceHandler rsc){
		super();
		this.map=map;
		this.entrys=entrys;
		this.rsc=rsc;
	}
	
	public ModelBuilding createBuilding(BuildingType type,Race race){
		switch(type){
		case GOLDMINE:
			return this.createGoldMine();
		case TOWNHALL:
            switch (race) {
                case humans:
                    return this.createHumansTownHall();
                case orcs:
                    return this.createOrcsTownHall();
            }
            break;
		case FARM:
            switch (race) {
                case humans:
                    return this.createHumansFarm();
                case orcs:
                    return this.createOrcsFarm();
            }
            break;
        case BARRACKS:
            switch (race) {
                case humans:
                    return this.createHumansBarracks();
                case orcs:
                    return this.createOrcsBarracks();
            }
            break;
        case LUMBERMILL:
            switch (race) {
                case humans:
                    return this.createHumansLumberMill();
                case orcs:
                    return this.createOrcsLumberMill();
            }
            break;
		}
		return null;
	}
	
	public ModelUnit createUnit(UnitType type,Race race){
		switch(type){
		case WORKER:
            switch (race) {
                case humans:
                    return this.createPeasant();
                case orcs:
                    return this.createPeon();
            }
            break;
        case WARRIOR:
            switch (race) {
                case humans:
                    return this.createFootman();
                case orcs:
                    return this.createGrunt();
            }
            break;
        case THROWER:
            switch (race) {
                case humans:
                    return this.createArcher();
                case orcs:
                    return this.createSpearman();
            }
            break;
		}
		return null;
	}
	
	public GoldMine createGoldMine(){
		return new GoldMine(this.map,this.rsc,this.entrys);
	}
	
    public HumansTownHall createHumansTownHall() {
        return new HumansTownHall(this.map, this.rsc, this.entrys, this);
    }

    public HumansFarm createHumansFarm() {
        return new HumansFarm(this.map, this.rsc, this.entrys);
    }
    
    public HumansBarracks createHumansBarracks() {
        return new HumansBarracks(this.map, this.rsc, this.entrys, this);
    }

    public HumansLumberMill createHumansLumberMill() {
        return new HumansLumberMill(this.map, this.rsc, this.entrys);
    }

    public Peasant createPeasant() {
        return new Peasant(this.map, this.rsc, this.entrys, this);
    }
    
    public Footman createFootman() {
        return new Footman(this.map, this.rsc);
    }

    public Archer createArcher() {
        return new Archer(this.map, this.rsc);
    }
    
    public OrcsTownHall createOrcsTownHall() {
        return new OrcsTownHall(this.map, this.rsc, this.entrys, this);
    }

    public OrcsFarm createOrcsFarm() {
        return new OrcsFarm(this.map, this.rsc, this.entrys);
    }

    public OrcsBarracks createOrcsBarracks() {
        return new OrcsBarracks(this.map, this.rsc, this.entrys, this);
    }

    public OrcsLumberMill createOrcsLumberMill() {
        return new OrcsLumberMill(this.map, this.rsc, this.entrys);
    }
    
    public Peon createPeon() {
        return new Peon(this.map, this.rsc, this.entrys, this);
    }

    public Grunt createGrunt() {
        return new Grunt(this.map, this.rsc);
    }

    public Spearman createSpearman() {
        return new Spearman(this.map, this.rsc);
    }
}
