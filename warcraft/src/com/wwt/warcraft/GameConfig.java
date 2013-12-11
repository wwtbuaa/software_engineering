package com.wwt.warcraft;

import com.wwt.warcraft.gameplay.Race;

public class GameConfig {
	static Race playerRace=Race.humans;
	static Race opponentRace = Race.orcs;
	static String map="lake.map";
	static boolean hide,fog;
	
	public GameConfig(){
	}
	
	public static void setConfig(Race playerRace, Race opponentRace, String map, boolean hide, boolean fog) {
		GameConfig.playerRace = playerRace;
	    GameConfig.opponentRace = opponentRace;
	    GameConfig.map = map;
	    GameConfig.hide = hide;
	    GameConfig.fog = fog;
	}
	
	public static Race getPlayerRace(){
		return playerRace;
	}
	
	public static Race getOpponentRace(){
		return opponentRace;
	}
	
    public static String getMap() {
        return map;
    }

    public static boolean getHide() {
        return hide;
    }

    public static boolean getFog() {
        return fog;
    }
}
