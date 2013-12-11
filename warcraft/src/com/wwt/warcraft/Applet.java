package com.wwt.warcraft;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import com.b3dgs.lionengine.Core;
import com.b3dgs.lionengine.core.Config;
import com.b3dgs.lionengine.core.Display;
import com.b3dgs.lionengine.core.Filter;
import com.b3dgs.lionengine.core.Rendering;
import com.b3dgs.lionengine.engine.Initializer;
import com.b3dgs.lionengine.engine.Loader;
import static com.b3dgs.lionengine.Engine.ENGINE;

public class Applet extends JApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Applet(){
		super();
	}
	
	public void init(){
		try{
			SwingUtilities.invokeAndWait(new Runnable(){
				public void run(){
					if(getWidth()==304){
						Launcher launcher=new Launcher(Applet.this, Main.initialize(true));
						launcher.start();
					}else{
						   Initializer init = Main.initialize(true);
						   Display display = Core.CORE.createDisplay(getWidth(), getHeight(), init.depthRef, init.rateRef, true);
	                       Config config = Core.CORE.createConfig(display, Rendering.SCREEN_SCALED, Filter.NONE, null);
	                       config.setApplet(Applet.this);
	                       Loader game = ENGINE.createLoader(init, config, null);
	                       game.setNextSequence(new Menu(game));
	                       game.start();
	                       validate();
	                       setVisible(true);
	                       requestFocus();
					}
				}
			});
		}catch(Exception e){
		}
	}
	
	public void terminate(){
		try{
			this.getAppletContext().showDocument(new URL(getCodeBase().toString() + "index.html"), "_self");
		}catch(MalformedURLException e){
		}
		System.exit(0);
	}
	
	public void destroy(){
		this.terminate();
	}
}
