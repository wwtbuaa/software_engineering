package com.wwt.warcraft;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.b3dgs.lionengine.File;
import com.b3dgs.lionengine.core.Screen;
import com.b3dgs.lionengine.file.FileReader;
import com.b3dgs.lionengine.file.FileWriter;
import com.b3dgs.lionengine.file.XMLNode;
import com.b3dgs.lionengine.game.AbstractWorld;
import com.b3dgs.lionengine.game.strategy.AbstractEntry;
import com.b3dgs.lionengine.game.strategy.StrategyCamera;
import com.b3dgs.lionengine.game.strategy.StrategyCursor;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.b3dgs.lionengine.utility.Displays;
import com.b3dgs.lionengine.utility.LevelRipConverter;
import com.b3dgs.lionengine.utility.Media;
import com.wwt.warcraft.building.BuildingType;
import com.wwt.warcraft.building.GoldMine;
import com.wwt.warcraft.gameplay.Attributes;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.EntryHandler;
import com.wwt.warcraft.gameplay.Minimap;
import com.wwt.warcraft.gameplay.Player;
import com.wwt.warcraft.gameplay.Race;
import com.wwt.warcraft.map.FogOfWar;
import com.wwt.warcraft.map.Map;
import com.wwt.warcraft.map.Tile;
import com.wwt.warcraft.skill.ModelSkill;

public class World extends AbstractWorld{
	final Map map;
	final FogOfWar fogOfWar;
	final StrategyCamera camera;
	final Scene scene;
	final StrategyCursor cursor;
	final EntryHandler entrys;
	final ResourceHandler resource;
	final ControlPanel panel;
	final EntryFactory factory;
	final Player[] player;
	final Minimap minimap;
	
	public World(Scene scene,Screen screen){
		super(screen);
		this.scene=scene;
		this.fogOfWar=new FogOfWar();
		this.map=new Map(this,this.fogOfWar);
		this.cursor=new StrategyCursor(screen,Media.get("sprites","cursor.png"),this.map);
		this.resource=new ResourceHandler();
		this.panel=new ControlPanel(GameConfig.getPlayerRace());
		this.camera=new StrategyCamera(screen,this.panel,this.map.getTileWidth(),this.map.getTileHeight());
		this.entrys=new EntryHandler(this.panel,this.map);
		this.factory=new EntryFactory(this.map,this.entrys,this.resource);
		this.player=new Player[2];
		this.player[0]=new Player("Player1",GameConfig.getPlayerRace(),this.map,this.factory,this.entrys);
		this.player[1]=new Player("Player2",GameConfig.getOpponentRace(),this.map,this.factory,this.entrys);
		this.panel.setPlayer(this.player[0]);
		this.entrys.setPlayer(this.player[0]);
		this.minimap=new Minimap(this.map,this.cursor,this.camera,this.entrys);
		this.minimap.setPlayer(this.player[0]);
		if(scene!=null){
			scene.playMusic(GameConfig.getPlayerRace()+".mid");
		}
	}
	
	public void update(Keyboard keyboard,Mouse mouse,float f){
		this.camera.update(keyboard, mouse, f);
		this.cursor.update(mouse, false, this.camera,f);
		this.entrys.update(keyboard,this.cursor,this.camera,f);
		for(int i=0;i<2;i++){
			this.player[i].update(f);
		}
		this.minimap.update(3,6);
	}
	
	public void render(Graphics2D graph){
		this.map.render(graph,this.camera,this.mapViewH-1,this.mapViewV-1);
		this.entrys.render(graph,this.camera,this.cursor);
		this.fogOfWar.render(graph,this.camera,this.mapViewH-1,this.mapViewV-1);
		this.entrys.renderCursorSelection(graph,this.camera);
		this.panel.render(graph,this.cursor);
		this.player[0].render(graph);
		this.minimap.render(graph, 3,6);
		this.cursor.render(graph);
	}
	
	public void save(FileWriter file) throws IOException{
	}

	public void load(FileReader file) throws IOException {
		this.map.load(file);
		this.fogOfWar.create(this.map);
		this.map.createMiniMap();
		this.entrys.createLayers(this.map);
		this.calculateMapView(this.map,this.panel);
		this.minimap.setView(this.mapViewH,this.mapViewV);
		this.camera.setBorders(this.map,this.panel);
		
		String data=file.getFileName().substring(0,file.getFileName().length()-4)+".txt";
		BufferedReader buffer=new BufferedReader(new InputStreamReader(new FileInputStream(Media.getTempFile(data,true))));
		String s=buffer.readLine();
		if(s.charAt(0)=='{'){
			buffer.readLine();
			this.player[0].setStartingPoint(Integer.parseInt(buffer.readLine()),Integer.parseInt(buffer.readLine()));
			buffer.readLine();
			this.player[1].setStartingPoint(Integer.parseInt(buffer.readLine()),Integer.parseInt(buffer.readLine()));
			s=buffer.readLine();
			while(s.charAt(0)=='+'){
				this.addGoldMine(Integer.parseInt(buffer.readLine()),Integer.parseInt(buffer.readLine()),Integer.parseInt(buffer.readLine()));
				s=buffer.readLine();
				if(s.charAt(0)=='}'){
					break;
				}
			}
		}
		this.player[0].setAttack(this.player[1].getStartX(),this.player[1].getStartY());
		this.player[1].setAttack(this.player[0].getStartX(),this.player[0].getStartY());
		for(int i=0;i<2;i++){
			this.player[i].init();
		}
		this.camera.place((this.player[0].getStartX()-this.mapViewH/2)*16,(this.player[0].getStartY()-this.mapViewV/2)*16);
		this.entrys.update(null, this.cursor, this.camera,1.0f);
		this.fogOfWar.setOwner(this.entrys.list(),this.player[0].id);
		this.fogOfWar.setFogOfWar(GameConfig.getHide(), GameConfig.getFog());
	}
	
	public void addNeutralEntry(BuildingType type,int x,int y){
		AbstractEntry<Tile,ModelSkill,Attributes> entry=this.factory.createBuilding(BuildingType.GOLDMINE,Race.neutral);
		entry.setOwnerID(0);
		this.entrys.add(entry);
		entry.place(x,y);
	}
	
	public void addGoldMine(int x,int y,int gold){
		GoldMine g=this.factory.createGoldMine();
		g.setOwnerID(0);
		g.gold.add(gold);
		this.entrys.add(g);
		g.place(x, y);
	}
	
	public void load(XMLNode arg0) {
	}

	public void save(XMLNode arg0) {
	}
	
	public void importLevel(String s,String out){
		LevelRipConverter<Tile> l=new LevelRipConverter<Tile>();
		l.start(Media.get("levels",s),this.map,"tiles","forest",false);
		try{
			FileWriter f=File.FILE.createFileWriter(Media.get("levels",out));
			this.map.save(f);
			f.close();
		}catch(IOException e){
			Displays.error("importlevel","error");
		}
	}
}
