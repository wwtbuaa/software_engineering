package com.wwt.warcraft.skill;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.b3dgs.lionengine.core.Alignment;
import com.b3dgs.lionengine.drawable.TiledSprite;
import com.b3dgs.lionengine.game.strategy.AbstractControlPanel;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.StrategyCursor;
import com.b3dgs.lionengine.utility.Media;
import com.wwt.warcraft.SFX;
import com.wwt.warcraft.building.ModelBuilding;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.Cost;
import com.wwt.warcraft.gameplay.Costs;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.ModelWorker;
import com.wwt.warcraft.gameplay.AI;
import static com.wwt.warcraft.gameplay.ControlPanel.*;
import static com.b3dgs.lionengine.Drawable.DRAWABLE;

public class BuildModel extends ModelSkill{
	static int w,h;
	static boolean blocked,plan;
	final String buildName;
	final Cost cost;
	final String desc,gold,infos;
	boolean ai;
	
	public BuildModel(String name,int priority,AbstractEntry<Tile,ModelSkill,Attributes> owner){
		super(name,priority,owner);
		this.buildName=name;
		this.setIgnore(true);
		this.cost=Costs.get(this.buildName);
		this.desc=this.getDisplayName()+"";
		this.gold = this.desc + "     " + this.cost.gold.get() + " ";
        this.infos = this.gold + "    " + this.cost.wood.get();
	}
	
	public void onClick(){
		super.onClick();
		w=this.cost.w;
		h=this.cost.h;
		plan=true;
		this.setActive(true);
	}
	
    public void renderOnPanel(Graphics2D g, AbstractControlPanel<Tile, ModelSkill, Attributes> panel, StrategyCursor cursor,
            TiledSprite icons, int x, int y, int w, int h) {

        super.renderOnPanel(g, panel, cursor, icons, x, y, w, h);
        int cx = cursor.getScreenX();
        int cy = cursor.getScreenY();
        if (cx >= x && cx <= x + w && cy >= y && cy <= y + h) {
            TEXT.setColor(new Color(255,255,235));
            TEXT.draw(g, this.infos, 72, 192, Alignment.LEFT);
            DRAWABLE.loadSprite(Media.get("sprites", "gold.png")).render(g, 72 + TEXT.getStringWidth(g, this.desc), 192);
            DRAWABLE.loadSprite(Media.get("sprites", "wood.png")).render(g, 72 + TEXT.getStringWidth(g, this.gold), 192);
        }
    }
    
    public void renderOnMap(Graphics2D g, AbstractControlPanel<Tile, ModelSkill, Attributes> panel, StrategyCursor cursor) {
        if (cursor.canClick(panel) && plan && !this.ai) {
            if (this.owner.map.checkFreePlace(cursor.getHorizontalMapTile(), cursor.getVerticalMapTile(), w, h, this.owner.id)) {
                g.setColor(Color.GREEN);
                blocked = false;
            } else {
                g.setColor(Color.RED);
                blocked = true;
            }
            List<ModelBuilding> lst = ModelBuilding.getByOwner(0);
            for (ModelBuilding e : lst) {
                if (e.isActive() && e.getDistance(cursor.getHorizontalMapTile(), cursor.getVerticalMapTile(), w, h) < 6) {
                    g.setColor(Color.RED);
                    blocked = true;
                    break;
                }
            }
            int x = (cursor.getScreenX() - 8) / 16 * 16 + 8;
            int y = (cursor.getScreenY() + 4) /16 * 16 - 4;
            g.drawRect(x, y, w * 16 - 1, h * 16 - 1);
        }
    }
    
    public void action() {
        if (this.owner instanceof ModelWorker) {
            this.ai = (((ModelUnit) owner).player() instanceof AI);
            if (!blocked || this.ai) {
                ModelWorker worker = (ModelWorker) this.owner;
                worker.stop();
                Player player = worker.player();
                if (!worker.isConstructing() && player.gold.canSpend(this.cost.gold.get()) && player.wood.canSpend(this.cost.wood.get())) {
                    worker.buildAt(this.destX, this.destY, this.buildName, this.cost.w, cost.h, this.cost.time);
                    player.incWorkersOnConstructing(1);
                    ControlPanel.playSfx(worker.getOwnerID(), null, SFX.valided);
                    this.setActive(false);
                    if (!this.ai) {
                        plan = false;
                    }
                }
            } else {
                this.setRetry(true);
            }
        }
    }
}
