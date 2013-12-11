package com.wwt.warcraft;

import static com.b3dgs.lionengine.utility.Swing.addButton;
import static com.b3dgs.lionengine.Engine.ENGINE;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import com.b3dgs.lionengine.engine.AbstractLauncher;
import com.b3dgs.lionengine.engine.Initializer;
import com.b3dgs.lionengine.engine.Loader;

public class Launcher extends AbstractLauncher{

	/**
	 这条语句是这个引擎强制要求加入的，但是从我的角度看来，没有觉得这条语句有什么实际含义
	 */
	private static final long serialVersionUID = 1L;

	public Launcher(final Applet applet,final Initializer initializer){
		super(initializer,320,240);
		
		this.loadOptions();
		JPanel centerPanel=new JPanel(new GridLayout(2,2));
		this.mainPanel.add(centerPanel,BorderLayout.CENTER);
		addButton("Warcraft",centerPanel,new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Loader game=ENGINE.createLoader(initializer, config, Launcher.this);
				startSequence(new Menu(game),game);
			}
		});
		
		JPanel southPanel=new JPanel();
		this.mainPanel.add(southPanel,BorderLayout.SOUTH);
		addButton("Options",southPanel,new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0){
				Options options=new Options(Launcher.this,config);
				options.start();
			}
		});
		addButton("Exit",southPanel,new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0){
				saveOptions();
				if(applet==null){
					System.exit(0);
				}else{
					applet.terminate();
				}
			}
		});
	}
}
