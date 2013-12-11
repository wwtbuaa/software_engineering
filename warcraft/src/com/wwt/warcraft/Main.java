package com.wwt.warcraft;
import com.b3dgs.lionengine.engine.*;
import com.b3dgs.lionengine.utility.*;
import com.b3dgs.lionengine.Engine;
import static com.b3dgs.lionengine.Engine.ENGINE;

public class Main {

	public static Initializer initialize(boolean start){
		if(start){
			Media.loadFromJar(Main.class);
		}
		Engine.start("Warcraft","0.0.6","ressources",true,Theme.SYSTEM);
		return ENGINE.createInitializer(320,200,32,60);
	}
	
	public static void main(String[] args){
		boolean start = false;
		try{
			start=Boolean.parseBoolean(args[0]);
		}catch(Exception exc){
		}
		Launcher launcher=new Launcher(null,initialize(start));
		launcher.start();
	}
}
