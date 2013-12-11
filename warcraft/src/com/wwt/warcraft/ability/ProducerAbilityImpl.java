package com.wwt.warcraft.ability;

import com.b3dgs.lionengine.game.strategy.AbstractBuilding;
import com.b3dgs.lionengine.game.strategy.AbstractEntryHandler;
import com.b3dgs.lionengine.game.strategy.AbstractUnit;
import com.b3dgs.lionengine.game.strategy.ability.AbstractProducerAbility;
import com.b3dgs.lionengine.game.strategy.ability.ProducerAbility;
import com.wwt.warcraft.EntryFactory;
import com.wwt.warcraft.building.ModelProductor;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;
import com.wwt.warcraft.unit.ModelUnit;
import com.wwt.warcraft.unit.UnitType;

public class ProducerAbilityImpl extends AbstractProducerAbility<Tile,ModelSkill,Attributes> implements ProducerAbility<Tile,ModelSkill,Attributes>{
	final EntryFactory factory;
	final ModelProductor building;
	
	public ProducerAbilityImpl(AbstractBuilding<Tile,ModelSkill,Attributes> building,AbstractEntryHandler<Tile,ModelSkill,Attributes> handler,EntryFactory factory){
		super(building,handler);
		this.factory=factory;
		this.building=(ModelProductor)building;
	}
	
	public AbstractUnit<Tile,ModelSkill,Attributes> getUnitToProduce(String name){
		int i=name.indexOf('_');
		Race race=Race.valueOf(name.substring(0,i).toLowerCase());
		UnitType type=UnitType.valueOf(name.substring(i+1,name.length()));
		return this.factory.createUnit(type,race);
	}
	
	public boolean canProduce(){
		if(this.building.player().getFarmUsed()<this.building.player().getFarmGrowth()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean canBeProduced(){
		return this.canProduce();
	}

	public void onCanNotProduce() {
	}

	public void onProduced(AbstractUnit<Tile, ModelSkill, Attributes> unit) {
		ModelUnit u=(ModelUnit) unit;
		Player player=u.player();
		this.checkUpgrade(unit, "ATTACK_SWORD", player.getSwordLvl());
        this.checkUpgrade(unit, "ATTACK_AXE", player.getAxeLvl());
        this.checkUpgrade(unit, "ATTACK_ARROW", player.getArrowLvl());
        this.checkUpgrade(unit, "ATTACK_SPEAR", player.getSpearLvl());
        player.onProduced(u);
	}

	public void onProducing() {
	}
	
	public void checkUpgrade(AbstractUnit<Tile,ModelSkill,Attributes> unit,String skill,int n){
		ModelSkill s=unit.getSkill(skill);
		if(s!=null){
			s.setLvl(n);
		}
	}
}
