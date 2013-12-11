package com.wwt.warcraft;
/*这个类用来处理在这个游戏里将要用到的resource，因resource是来源自原作者，
 * 所以这个类的写法大多按照原作者的方式
 */
import com.b3dgs.lionengine.game.*;
import com.b3dgs.lionengine.utility.*;
import com.b3dgs.lionengine.audio.*;
import com.wwt.warcraft.gameplay.*;
import static com.b3dgs.lionengine.Audio.AUDIO;
import java.awt.image.*;

public class ResourceHandler extends AbstractRessourcesHandler<BufferedImage>{

	public static final Sound[][] SND_SELECT = getRacedSounds(SFX.select, 4);
	public static final Sound[][] SND_CONFIRM = getRacedSounds(SFX.confirm, 2);
	public static final Sound[][] SND_DIE = getRacedSounds(SFX.die, 1);
	public static final Sound[] SND_HIT = getSounds(SFX.hit, 3);
	public static final Sound[] SND_ARROW_HIT = getSounds(SFX.arrow_hit, 1);
	public static final Sound[] SND_ARROW_THROWN = getSounds(SFX.arrow_thrown, 1);
	public static final Sound[] SND_CONSTRUCTION = getSounds(SFX.construction, 1);
	public static final Sound[] SND_DECONSTRUCTION = getSounds(SFX.deconstruction, 2);
	public static final Sound[] SND_CLICK = getSounds(SFX.click, 1);
	public static final Sound[] SND_VALIDED = getSounds(SFX.valided,1);
	
	public ResourceHandler(){
		super();
		this.add("GOLDMINE",Media.get("buildings",Race.neutral.name(),"01"));
		this.add("HUMANS_TOWNHALL", getHumansBuilding("01"));
        this.add("HUMANS_FARM", getHumansBuilding("02"));
        this.add("HUMANS_BARRACKS", getHumansBuilding("03"));
        this.add("HUMANS_LUMBERMILL", getHumansBuilding("04"));
        this.add("PEASANT", getHumansUnit("01"));
        this.add("FOOTMAN", getHumansUnit("02"));
        this.add("ARCHER", getHumansUnit("03"));
        this.add("ORCS_TOWNHALL", getOrcsBuilding("01"));
        this.add("ORCS_FARM", getOrcsBuilding("02"));
        this.add("ORCS_BARRACKS", getOrcsBuilding("03"));
        this.add("ORCS_LUMBERMILL", getOrcsBuilding("04"));
        this.add("PEON", getOrcsUnit("01"));
        this.add("GRUNT", getOrcsUnit("02"));
        this.add("SPEARMAN", getOrcsUnit("03"));
        this.add("CONSTRUCTION", getSprite("construction"));
        this.add("ARROW", getSprite("arrow"));
        this.add("CORPSE", getSprite("corpse"));
        this.add("BURNING", getSprite("burning"));
        this.add("EXPLODE", getSprite("explode"));
	}

	public static Sound[][] getRacedSounds(SFX s, int n) {
		 Sound[][] sounds = new Sound[2][n];
	        for (int i = 1; i <= n; i++) {
	            sounds[0][i-1] = AUDIO.loadSound(Media.get("sfx", Race.humans + "_" + s.name() + i + ".wav"));
	            sounds[1][i-1] = AUDIO.loadSound(Media.get("sfx", Race.orcs + "_" +s.name() + i + ".wav"));
	        }
	     return sounds;
	}

	public static Sound[] getSounds(SFX s, int n) {
		 Sound[] sounds = new Sound[n];
	        for (int i = 1; i <= n; i++) {
	            sounds[i - 1] = AUDIO.loadSound(Media.get("sfx", s.name() + i + ".wav"));
	        }
	     return sounds;
	}

	public static String getSprite(String string) {
		return Media.get("sprites",string);
	}

	public static String getOrcsUnit(String string) {
		return Media.get("units",Race.orcs.name(),string);
	}

	public static String getOrcsBuilding(String string) {
		return Media.get("buildings",Race.orcs.name(),string);
	}

	public static String getHumansUnit(String string) {
		return Media.get("units",Race.humans.name(),string);
	}

	public static String getHumansBuilding(String string) {
		return Media.get("buildings",Race.humans.name(),string);
	}

	public void add(String string, String string2) {
		super.add(string,new MediaRessource<BufferedImage>(string2+".txt",this.getImage(string2 + ".png", false)));
	}
}