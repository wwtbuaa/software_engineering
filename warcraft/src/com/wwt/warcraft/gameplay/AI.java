package com.wwt.warcraft.gameplay;

import com.b3dgs.lionengine.Geometry;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.geometry.Point2D;
import com.b3dgs.lionengine.utility.Maths;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.map.CollisionType;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelAttacker;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.ModelWorker;
import com.wwt.warcraft.unit.UnitType;

import java.util.ArrayList;
import java.util.List;

public class AI extends Player {

    private ModelWorker firstWorker;
    private GoldMine firstMine;
    private int workersOnProduction, warriorsOnProduction, throwersOnProduction;
    private int minimumWorkers, minimumGoldExtractors, minimumWoodExtractors, minimumWarriors, minimumThrowers;
    private int minimumFarms, minimumBarracks, minimumLumberMills;
    private int firstTown, level;
    private long timerRequirements, timerBuilding, timerProduction, timerExtraction, timerAttack;

    public AI(String name, Race race, Map map, EntryFactory factory, EntryHandler entrys) {
        super(name, race, map, factory, entrys);
        this.workersOnProduction = 0;
        this.warriorsOnProduction = 0;
        this.throwersOnProduction = 0;
        this.minimumWorkers = 0;
        this.minimumGoldExtractors = 0;
        this.minimumWoodExtractors = 0;
        this.minimumFarms = 0;
        this.minimumBarracks = 0;
        this.minimumLumberMills = 0;
        this.minimumWarriors = 0;
        this.minimumThrowers = 0;
        this.firstTown = 0;
        this.level = 0;
        this.timerRequirements = Maths.time();
        this.timerBuilding = Maths.time();
        this.timerProduction = Maths.time();
        this.timerExtraction = Maths.time();
        this.timerAttack = Maths.time();
    }

    public void init() {
        this.addUnit(UnitType.WORKER, this.startX, this.startY);
    }

    public void update(float extrp) {
        super.update(extrp);
        if (this.firstTown == 1) {
            int dist = this.firstWorker.getDistance(this.firstMine);
            if (dist > 5 && dist < 8) {
                this.firstWorker.stop();
                this.setStartingPoint(this.firstWorker.getXInTile(), this.firstWorker.getYInTile());
                this.build(BuildingType.TOWNHALL, this.firstWorker);
                this.firstTown = 2;
            }
        }
        this.checkRequirements();
        this.checkBuildings();
        this.checkProductions();
        this.checkExtractions();
        this.checkAttacks();
    }

    public void checkRequirements() {
        if (Maths.time() - this.timerRequirements > 6000L) {
            this.timerRequirements = Maths.time();
            if (this.level == 0 && this.getTownhalls() == 0) {
                this.minimumWorkers = 0;
                this.minimumGoldExtractors = 0;
                this.minimumWoodExtractors = 0;
                this.minimumFarms = 0;
                this.minimumBarracks = 0;
                this.minimumLumberMills = 0;
                this.minimumWarriors = 0;
                this.minimumThrowers = 0;
                this.level = 1;
            } else if (this.level == 1 && this.getTownhalls() == 1 && this.getWorkers() < 3 && this.getBarracks() == 0) {
                this.minimumWorkers = 3;
                this.minimumGoldExtractors = 2;
                this.minimumWoodExtractors = 1;
                this.minimumFarms = 1;
                this.minimumBarracks = 0;
                this.minimumLumberMills = 0;
                this.minimumWarriors = 0;
                this.minimumThrowers = 0;
                this.level = 2;
            } else if (this.level == 2 && this.getTownhalls() == 1 && this.getWorkers() >= 3 && this.getBarracks() == 0) {
                this.minimumWorkers = 4;
                this.minimumGoldExtractors = 3;
                this.minimumWoodExtractors = 1;
                this.minimumFarms = 1;
                this.minimumBarracks = 1;
                this.minimumLumberMills = 0;
                this.minimumWarriors = 0;
                this.minimumThrowers = 0;
                this.level = 3;
            } else if (this.level == 3 && this.getTownhalls() == 1 && this.getBarracks() == 1) {
                this.minimumWorkers = 5;
                this.minimumGoldExtractors = 3;
                this.minimumWoodExtractors = 2;
                this.minimumFarms = 2;
                this.minimumBarracks = 1;
                this.minimumLumberMills = 1;
                this.minimumWarriors = 3;
                this.minimumThrowers = 0;
                this.level = 4;
            } else if (this.level == 4 && this.getTownhalls() == 1 && this.getBarracks() == 1 && this.getLumbermills() == 1) {
                this.minimumWorkers = 7;
                this.minimumGoldExtractors = 4;
                this.minimumWoodExtractors = 3;
                this.minimumFarms = 3;
                this.minimumBarracks = 2;
                this.minimumLumberMills = 1;
                this.minimumWarriors = 5;
                this.minimumThrowers = 3;
                this.level = 5;
            } else if (this.level == 5) {
                this.minimumWarriors = 8;
                this.minimumThrowers = 6;
                this.level = 6;
            }
        }
    }

    public void checkBuildings() {
        if (Maths.time() - this.timerBuilding > 10000L) {
            this.timerBuilding = Maths.time();
            if (this.getTownhalls() == 0 && this.getWorkersOnTownHall() == 0) {
                if (this.firstTown == 0) {
                    this.firstWorker = this.getPassiveWorker(true);
                    this.searchGoldMine(this.firstWorker);
                    if (this.firstMine != null) {
                        this.firstWorker.assignDestination(this.firstMine.getXInTile(), this.firstMine.getYInTile());
                        this.firstTown = 1;
                    } else {
                        this.firstTown = 2;
                    }
                }
                return;
            }
            if (this.getTownhalls() > 0) {
                if ((this.getFarms() < this.minimumFarms || this.getFarmUsed() >= this.getFarmGrowth()) && this.getWorkersOnFarm() == 0) {
                    this.build(BuildingType.FARM, this.getPassiveWorker(true));
                    return;
                }
                if (this.getBarracks() < this.minimumBarracks && this.getWorkersOnBarracks() == 0) {
                    this.build(BuildingType.BARRACKS, this.getPassiveWorker(true));
                    return;
                }
                if (this.getLumbermills() < this.minimumLumberMills && this.getWorkersOnLumberMill() == 0) {
                    this.build(BuildingType.LUMBERMILL, this.getPassiveWorker(true));
                    return;
                }

                if (this.getWorkersOnRepairing() < 2) {
                    for (ModelBuilding b : this.buildings) {
                        if (b.life.getCurrent() < b.life.getMax()) {
                            this.repair(this.getPassiveWorker(true), b);
                            this.incWorkersOnRepairing(1);
                        }
                    }
                }
            }
        }
    }

    public void repair(ModelWorker w, ModelBuilding building) {
        if (w != null) {
            w.repair(building);
            w.assignDestination(building.getXInTile(), building.getYInTile());
        }
    }

    public void checkProductions() {
        if (Maths.time() - this.timerProduction > 15000L) {
            this.timerProduction = Maths.time();
            if (this.getWorkers() < this.minimumWorkers && this.workersOnProduction == 0) {
                this.produce(BuildingType.TOWNHALL, UnitType.WORKER);
            }
            if (this.getBarracks() > 0 && (this.getWarriors() < this.minimumWarriors || this.gold.get() > 1000) && this.warriorsOnProduction < 2) {
                this.produce(BuildingType.BARRACKS, UnitType.WARRIOR);
            }
            if (this.getBarracks() > 0 && this.getLumbermills() > 0 && (this.getThrowers() < this.minimumThrowers || (this.gold.get() > 1000 && this.wood.get() > 200)) && this.throwersOnProduction < 2) {
                this.produce(BuildingType.BARRACKS, UnitType.THROWER);
            }
        }
    }

    public void checkExtractions() {
        if (Maths.time() - this.timerExtraction > 8000L) {
            this.timerExtraction = Maths.time();
            ModelWorker w = null;
            for (ModelWorker m : this.workers) {
                if (m.isPassive()) {
                    m.setAvailable(true);
                }
                if (m.isConstructing() && !m.isMoving() && m.isVisible()) {
                    m.stop();
                    m.setAvailable(true);
                }
            }
            if (this.getWorkersOnGold() < this.minimumGoldExtractors) {
                w = this.getPassiveWorker(false);
                this.goToGoldExtraction(w);
            }
            if (this.getWorkersOnWood() < this.minimumWoodExtractors && this.getWorkersOnGold() > 1) {
                ModelWorker w2 = this.getPassiveWorker(false);
                if (w2 != w) {
                    this.goToWoodExtraction(w2);
                }
            }
        }
    }

    public void checkAttacks() {
        if (Maths.time() - this.timerAttack > 45000L) {
            this.timerAttack = Maths.time();

            int attackWarriors = Maths.random(0, 3);
            int attackThrowers = Maths.random(0, 3);
            if (this.getBarracks() == 0) {
                attackWarriors = 0;
                attackThrowers = 0;
            }
            if (this.getLumbermills() == 0) {
                attackThrowers = 0;
            }
            if ((attackWarriors > 0 || attackThrowers > 0)) {
                this.performAttack(attackWarriors, attackThrowers, false, true, false, false, this.attackX, this.attackY, null);
            }
        }
    }

    public void performAttack(int warriors, int throwers, boolean force, boolean assault, boolean urgent, boolean defend,
            int tx, int ty, AbstractEntry<Tile, ModelSkill, Attributes> entry) {
        List<ModelAttacker> list = this.getAttacker(UnitType.WARRIOR, warriors, force);
        list.addAll(this.getAttacker(UnitType.THROWER, throwers, force));
        if (list.size() >= warriors || urgent) {
            for (ModelAttacker att : list) {
                att.setAssault(assault);
                att.setDefend(defend);
                att.setRiposte(true);
                att.setTarget(null);
                if (entry == null) {
                    att.assignDestination(tx, ty);
                } else {
                    att.attack(entry);
                }
            }
        }
        list.clear();
    }

    public void build(BuildingType type, ModelWorker w) {
        if (w != null) {
            Cost cost = this.canSpend(type);
            if (cost != null) {
                w.stop();
                ModelSkill s = w.getSkill(this.getTypeName(type));
                int rx = Maths.random(-6, 6);
                int ry = Maths.random(-6, 6);
                if (type == BuildingType.TOWNHALL) {
                    rx = Maths.random(-1, 1);
                    ry = Maths.random(-1, 1);
                }
                s.setDestination(this.startX + rx, this.startY + ry);
                s.action();
            }
        }
    }

    public void produce(BuildingType producer, UnitType type) {
        ModelProductor prod = this.getProducer(producer);
        if (prod != null && !prod.isUnderConstruction() && this.getFarmUsed() < this.getFarmGrowth()) {
            Cost cost = this.canSpend(type);
            if (cost != null) {
                prod.addToProductionQueue(this.getTypeName(type), cost.time);
                this.gold.spend(cost.gold.get());
                this.wood.spend(cost.wood.get());
                if (type == UnitType.WORKER) {
                    this.workersOnProduction++;
                } else if (type == UnitType.WARRIOR) {
                    this.warriorsOnProduction++;
                } else if (type == UnitType.THROWER) {
                    this.throwersOnProduction++;
                }
            }
        }
    }

    public Cost canSpend(Enum<?> type) {
        Cost cost = Costs.get(this.getTypeName(type));
        if (this.gold.canSpend(cost.gold.get()) && this.wood.canSpend(cost.wood.get())) {
            return cost;
        } else {
            return null;
        }
    }

    public void onProduced(ModelUnit unit) {
        super.onProduced(unit);
        switch (unit.type) {
            case WORKER:
                this.workersOnProduction--;
                if (this.workersOnProduction < 0) {
                    this.workersOnProduction = 0;
                }
                break;
            case WARRIOR:
                this.warriorsOnProduction--;
                if (this.warriorsOnProduction < 0) {
                    this.warriorsOnProduction = 0;
                }
                break;
            case THROWER:
                this.throwersOnProduction--;
                if (this.throwersOnProduction < 0) {
                    this.throwersOnProduction = 0;
                }
                break;
        }
    }

    public void onBuilt(ModelBuilding building) {
        super.onBuilt(building);
        ModelWorker w = building.getBuilder();
        if (w != null) {
            w.setAvailable(true);
        }
    }

    public void defendMe(AbstractEntry<Tile, ModelSkill, Attributes> entry, AbstractEntry<Tile, ModelSkill, Attributes> attacker) {
        this.performAttack(2, 1, true, true, true, true, entry.getXInTile(), entry.getYInTile(), attacker);
    }

    public ModelWorker getPassiveWorker(boolean passive) {
        ModelWorker last = null;
        for (ModelUnit u : this.units) {
            if (u instanceof ModelWorker) {
                ModelWorker w = (ModelWorker) u;
                if (w.isPassive() && w.isAvailable()) {
                    w.setAvailable(false);
                    return w;
                }
                if (w.isAlive() && w.isVisible() && w.isAvailable()) {
                    last = w;
                }
            }
        }
        if (passive && last != null) {
            last.setAvailable(false);
            for (ModelBuilding b : this.buildings) {
                if (b != null && last.collide(b)) {
                    Point2D p = last.map.getFreePlaceArround(last.getXInTile(), last.getYInTile(), 1, 1, b.getWidthInTile(), 0);
                    if (p != null) {
                        last.place(p.getX(), p.getY());
                    }
                }
            }
            return last;
        }
        return null;
    }

    public ModelProductor getProducer(BuildingType type) {
        int min = Integer.MAX_VALUE;
        ModelProductor best = null;
        for (ModelBuilding b : this.buildings) {
            if (b.type == type && b instanceof ModelProductor) {
                ModelProductor p = (ModelProductor) b;
                int q = p.getQueueLength();
                if (q < min) {
                    min = q;
                    best = p;
                }
            }
        }
        return best;
    }

    public List<ModelAttacker> getAttacker(UnitType type, int count, boolean force) {
        List<ModelAttacker> list = new ArrayList<ModelAttacker>(count);
        if (count == 0) {
            return list;
        }
        int num = 0;
        for (ModelAttacker att : this.attackers) {
            List<Point2D> lst = att.map.getAllFreePlaceArround(att.getXInTile(), att.getYInTile(), 1, 1, att.id, 1);
            if (att.type == type) {
                if ((!att.getAssault() || force) && !att.isDefending() && !lst.isEmpty()) {
                    list.add(att);
                    num++;
                    if (num >= count) {
                        break;
                    }
                }
            }
            lst.clear();
        }
        return list;
    }

    public void searchGoldMine(ModelWorker w) {
        if (w != null) {
            List<ModelBuilding> mines = ModelBuilding.getByOwner(0);
            int min = Integer.MAX_VALUE;
            GoldMine best = null;
            for (ModelBuilding m : mines) {
                if (m.isAlive() && m instanceof GoldMine) {
                    GoldMine mine = (GoldMine) m;
                    if (!mine.isEmpty()) {
                        int dist = mine.getDistance(this.startX, this.startY, 2, 2);
                        if (dist < min) {
                            min = dist;
                            best = mine;
                        }
                    }
                }
            }
            this.firstMine = best;
            mines.clear();
        }
    }

    public void goToGoldExtraction(ModelWorker w) {
        if (w != null) {
            if (this.firstMine == null || this.firstMine.isEmpty()) {
                this.searchGoldMine(w);
            }
            if (this.firstMine != null) {
                ModelSkill s = w.getSkill("EXTRACT");
                s.setDestination(this.firstMine.getXInTile() + this.firstMine.getWidthInTile() / 2,
                        this.firstMine.getYInTile() + this.firstMine.getHeightInTile() / 2);
                s.action();
            } else {
                w.stop();
            }
        }
    }

    public void goToWoodExtraction(ModelWorker w) {
        if (w != null) {
            Point2D tree = this.getTreeLocation();
            if (tree != null) {
                ModelSkill s = w.getSkill("EXTRACT");
                s.setDestination(tree.getX(), tree.getY());
                s.action();
            }
        }
    }

    public Point2D getTreeLocation() {
        int x = this.startX, y = this.startY;
        int size = 4;
        boolean search = true;
        Point2D p;
        while (search) {
            for (int v = y - size; v <= y + size; v++) {
                if (v >= 0 && v < this.map.getHeightInTiles()) {
                    if (x - size >= 0) {
                        p = this.searchTree(v, x - size);
                        if (p != null) {
                            return p;
                        }
                    }
                    if (x + size < this.map.getWidthInTiles()) {
                        p = this.searchTree(v, x + size);
                        if (p != null) {
                            return p;
                        }
                    }
                }
            }
            for (int h = x - size; h <= x + size; h++) {
                if (h >= 0 && h < this.map.getWidthInTiles()) {
                    if (y - size >= 0) {
                        p = this.searchTree(y - size, h);
                        if (p != null) {
                            return p;
                        }
                    }
                    if (y + size < this.map.getHeightInTiles()) {
                        p = this.searchTree(y + size, h);
                        if (p != null) {
                            return p;
                        }
                    }
                }
            }
            size++;
            if (size > 40) {
                search = false;
            }
        }
        return null;
    }

    public Point2D searchTree(int v, int h) {
        if (this.map.getTile(v, h).getCollType() == CollisionType.TREE) {
            return Geometry.GEOMETRY.createPoint2D(h, v);
        }
        return null;
    }
}
