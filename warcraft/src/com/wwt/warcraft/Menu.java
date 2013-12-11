package com.wwt.warcraft;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.b3dgs.lionengine.Audio;
import com.b3dgs.lionengine.audio.Midi;
import com.b3dgs.lionengine.audio.Sound;
import com.b3dgs.lionengine.core.Alignment;
import com.b3dgs.lionengine.drawable.Cursor;
import com.b3dgs.lionengine.drawable.Sprite;
import com.b3dgs.lionengine.drawable.Text;
import com.b3dgs.lionengine.drawable.TiledSprite;
import com.b3dgs.lionengine.engine.Loader;
import com.b3dgs.lionengine.input.Keyboard;
import com.b3dgs.lionengine.input.Mouse;
import com.b3dgs.lionengine.utility.Maths;
import com.b3dgs.lionengine.utility.Media;
import com.wwt.warcraft.gameplay.ControlPanel;
import com.wwt.warcraft.gameplay.Race;

import static com.b3dgs.lionengine.Drawable.DRAWABLE;
import static com.b3dgs.lionengine.utility.Maths.fixBetween;

public class Menu extends AbstractSequence{
	static boolean clicked;
	static MENUS MENU=MENUS.INTRO_UP;
	static final Text FONT = DRAWABLE.createText(Font.DIALOG, 10, Text.NORMAL);
	final Race[] RACES = {Race.humans, Race.orcs};
	final String[] MAPS = {"2_bridges.map", "lake.map"};
	final String[] FOGS = {"Revealed", "Hidden"};
	static enum MENUS{
		INTRO_UP, INTRO_DOWN, MAIN_UP, MAIN, NEW, NEW_OUT, LOAD, ABOUT, PLAY, EXIT
	}
	
	static class Button{
		final TiledSprite surface;
		final String text;
		final int x,y,w,h;
		final MENUS menu;
		boolean over;
		
		Button(TiledSprite surface,String text,int x,int y,MENUS menu){
			this.surface=surface;
			this.text=text;
			this.x=x;
			this.y=y;
			this.w=surface.getTileWidth();
			this.h=surface.getTileHeight();
			this.menu=menu;
			this.over=false;
		}
		
		void update(Cursor cursor){
			int x=cursor.getX();
			int y=cursor.getY();
			this.over=(x>=this.x&&y>this.y&&x<=this.x+this.w&&y<=this.y+this.h);
			if(!clicked&&this.over&&cursor.getClick()==Mouse.LEFT){
				ControlPanel.playSfx(SFX.click);
				clicked=true;
				MENU=this.menu;
				this.over=false;
			}
		}
		
		void render(Graphics2D graph){
			if(this.over){
				this.surface.render(graph,1,this.x,this.y);
				FONT.setColor(new Color(255, 245, 70));
			}else{
				this.surface.render(graph,0,this.x,this.y);
				FONT.setColor(new Color(255, 255, 235));
			}
			FONT.setColor(new Color(255, 255, 235));
			FONT.draw(graph, this.text, this.x + this.w / 2, this.y + 4, Alignment.CENTERED);
			FONT.setColor(new Color(255, 244, 69));
			FONT.draw(graph,this.text.substring(0, 1), this.x + this.w / 2 - FONT.getStringWidth(graph, this.text) / 2, this.y + 4, Alignment.LEFT);
		}
	}
	
	static class Choice{
		final TiledSprite surface;
		final int x,y,w,h;
		boolean right,over;
		
		Choice(TiledSprite surface,int x,int y,boolean right){
			this.surface=surface;
			this.x=x;
			this.y=y;
			this.w=surface.getTileWidth();
			this.h=surface.getTileHeight();
			this.right=right;
			this.over=false;
		}
		
		boolean update(Cursor cursor){
			int x=cursor.getX();
			int y=cursor.getY();
			boolean pressed=false;
		    this.over = (x >= this.x && y >= this.y && x <= this.x + this.w && y <= this.y + this.h);
	        if (!clicked && this.over && cursor.getClick() == Mouse.LEFT) {
	        	ControlPanel.playSfx(SFX.click);
	            pressed = true;
	            clicked = true;
	        }
	        return pressed;
		}
		
		void render(Graphics2D graph){
			int a=0;
			if(this.right){
				a=2;
			}
			if(this.over){
				this.surface.render(graph,1+a,this.x,this.y);
			}else{
				this.surface.render(graph,a,this.x,this.y);
			}
		}
		
		int getSide(){
			return this.right?1:-1;
		}
	}
	
	Midi music;
    Sprite logo, background;
    Cursor cursor;
    Button[] buttons;
    Choice[] choices;
    Color[] alphas;
    int alpha, playerRace, opponentRace, map, fog;
    long introTimer;
    boolean pressed, end;
    
    public Menu(Loader loader){
    	super(loader);
    }
    
    public void load(){
        TiledSprite bigButton = DRAWABLE.loadTiledSprite(Media.get("sprites","case1.png"), 112, 16);
        bigButton.load(false);
        TiledSprite smallButton = DRAWABLE.loadTiledSprite(Media.get("sprites", "case2.png"), 54, 16);
        smallButton.load(false);
        TiledSprite arrowButton = DRAWABLE.loadTiledSprite(Media.get("sprites", "case3.png"), 15, 15);
        arrowButton.load(false);
    	this.alphas=new Color[256];
    	for(int i=0;i<256;i++){
    		this.alphas[i]=new Color(0,0,0,i);
    	}
    	this.music = Audio.AUDIO.loadMidi(Media.get("musics", "menu.mid"));
        this.logo = DRAWABLE.loadSprite(Media.get("sprites", "blizzard.png"));
        this.logo.load(false);
        this.background = DRAWABLE.loadSprite(Media.get("sprites", "menu.png"));
        this.background.load(false);
        this.cursor = DRAWABLE.createCursor(this.screen, Media.get("sprites", "cursor.png"));
        this.cursor.place(this.screen.init.widthRef / 2, this.screen.init.heightRef / 2);
        this.buttons = new Button[7];
        this.buttons[0] = new Button(bigButton, "Start a new game", 104, 93, MENUS.NEW);
        this.buttons[1] = new Button(bigButton, "Load existing game", 104, 111, MENUS.MAIN);
        this.buttons[2] = new Button(bigButton, "About", 104, 129, MENUS.ABOUT);
        this.buttons[3] = new Button(bigButton, "Quit game", 104, 163, MENUS.EXIT);
        this.buttons[4] = new Button(smallButton, "Ok", 84, 170, MENUS.NEW_OUT);
        this.buttons[5] = new Button(smallButton, "Cancel", 183, 170, MENUS.MAIN);
        this.buttons[6] = new Button(smallButton, "Back", 133, 170, MENUS.MAIN);
        this.choices = new Choice[8];
        this.choices[0] = new Choice(arrowButton, 142, 100, false);
        this.choices[1] = new Choice(arrowButton, 218, 100, true);
        this.choices[2] = new Choice(arrowButton, 142, 116, false);
        this.choices[3] = new Choice(arrowButton, 218, 116, true);
        this.choices[4] = new Choice(arrowButton, 142, 132, false);
        this.choices[5] = new Choice(arrowButton, 218, 132, true);
        this.choices[6] = new Choice(arrowButton, 142, 148, false);
        this.choices[7] = new Choice(arrowButton, 218, 148, true);
        this.playerRace = 0;
        this.opponentRace = 1;
        this.map = 0;
        this.alpha = 0;
        this.end = false;
        this.fog = 0;
        this.fpsOffsetY = 80;
        clicked = false;
        if (MENU == MENUS.INTRO_UP) {
            Sound sound = Audio.AUDIO.loadSound(Media.get("sfx", "blizzard.wav"));
            sound.play();
        }
        if (this.keyboard.used()) {
            this.pressed = true;
        }
        this.introTimer = Maths.time();
    }
    
    public void update(float f){
    	super.update(1.0f);
    	this.cursor.update(this.mouse, false,1.0f);
    	if(this.cursor.getClick()==0){
    		clicked=false;
    	}
    	if(!this.keyboard.used()){
    		this.pressed=false;
    	}
    	switch(MENU){
    	case INTRO_UP:
    		this.alpha+=3;
    		this.alpha = fixBetween(this.alpha, 0, 255);
            if (Maths.time() - this.introTimer > 3000) {
                MENU = MENUS.INTRO_DOWN;
            }
            break;
    	case INTRO_DOWN:
    		this.alpha -= 10;
            this.alpha = fixBetween(this.alpha, 0, 255);
            if (this.alpha == 0) {
                MENU = MENUS.MAIN_UP;
            }
            break;
    	case MAIN_UP:
    		this.alpha += 10;
            this.alpha = fixBetween(this.alpha, 0, 255);
            if (this.alpha == 255) {
                this.music.setLoop(6300, this.music.getTicks() - 3680);
                this.music.play(true);
                MENU = MENUS.MAIN;
            }
            break;
    	case MAIN:
    		 for (int i = 0; i < 4; i++) {
                 this.buttons[i].update(this.cursor);
             }
             break;
    	case NEW:
    	    for (int i = 0; i < 8; i++) {
                if (this.choices[i].update(this.cursor)) {
                    if (Math.floor(i / 2) == 0) {
                        this.playerRace = fixBetween(this.playerRace + this.choices[i].getSide(), 0, 1);
                        if (this.playerRace == 0) {
                            this.opponentRace = 1;
                        } else if (this.playerRace == 1) {
                            this.opponentRace = 0;
                        }
                    } else if (Math.floor(i / 2) == 1) {
                        this.opponentRace = fixBetween(this.opponentRace + this.choices[i].getSide(), 0, 1);
                        if (this.opponentRace == 0) {
                        	  this.playerRace = 1;
                        } else if (this.opponentRace == 1) {
                            this.playerRace = 0;
                        }
                    } else if (Math.floor(i / 2) == 2) {
                        this.map = fixBetween(this.map + this.choices[i].getSide(), 0, 1);
                    } else if (Math.floor(i / 2) == 3) {
                        this.fog = fixBetween(this.fog + this.choices[i].getSide(), 0, 1);
                    }
                }
            }
    	    for (int i = 4; i < 6; i++) {
                this.buttons[i].update(this.cursor);
            }
            break;
    	case ABOUT:
    		 this.buttons[6].update(this.cursor);
             break;
    	case NEW_OUT:
    		  this.alpha -= 10;
              this.alpha = fixBetween(this.alpha, 0, 255);
              if (this.alpha == 0) {
                  MENU = MENUS.PLAY;
              }
              break;
    	case PLAY:
    		 boolean hide = false;
             boolean ffog = false;
             if (this.fog >= 1) {
                 hide = true;
             }
             if (this.fog == 2) {
                 ffog = true;
             }
             GameConfig.setConfig(RACES[this.playerRace], RACES[this.opponentRace], MAPS[this.map], hide, ffog);
             this.music.stop();
             MENU = MENUS.MAIN_UP;
             this.alpha = 0;
             this.end(new Scene(this.loader));
             break;
    	case EXIT:
    		this.music.stop();
    		this.end();
    		break;
    	}
    	if(!this.pressed&&this.keyboard.isPressed(Keyboard.ESCAPE)){
    		this.music.stop();
    		this.end();
    	}
    }
    
	public void terminate() {
		super.terminate();
		this.end = true;
        this.music = null;
        this.logo = null;
        this.background = null;
        this.cursor = null;
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i] = null;
        }
        for (int i = 0; i < this.choices.length; i++) {
            this.choices[i] = null;
        }
        this.buttons = null;
        this.choices = null;
	}
	
	public void render(Graphics2D graph){
		if(this.end){
			return;
		}
		switch(MENU){
		case INTRO_UP:
			this.logo.render(graph,0,0);
			this.applyAlpha(graph,this.alphas[255-this.alpha]);
		case INTRO_DOWN:
			this.logo.render(graph,0,0);
			this.applyAlpha(graph,this.alphas[255-this.alpha]);
		case MAIN_UP:
			this.background.render(graph,0,0);
			this.applyAlpha(graph,this.alphas[255-this.alpha]);
		case MAIN:
			this.background.render(graph,0,0);
			for (int i = 0; i < 4; i++) {
                this.buttons[i].render(graph);
            }
            this.cursor.render(graph);
            break;
		case NEW:
		    this.drawNew(graph);
            this.cursor.render(graph);
            break;
		case ABOUT:
			this.background.render(graph,0,0);
			graph.setColor(new Color(190, 190, 190));
			graph.drawRect(80,84,160,71);
			graph.setColor(new Color(20, 48, 77));
			graph.drawRect(81,85,159,70);
			FONT.setColor(new Color(255, 244, 69));
			FONT.draw(graph, "warcraft",160,89,Alignment.CENTERED);
			FONT.draw(graph, "Author:", 115,110,Alignment.CENTERED);
            FONT.draw(graph, "Graphics:", 115, 120, Alignment.CENTERED);
            FONT.draw(graph, "Musics:", 115, 130, Alignment.CENTERED);
            FONT.draw(graph, "Website:", 115, 140, Alignment.CENTERED);
            FONT.setColor(new Color(255, 255, 235));
            FONT.draw(graph,"wwt",185, 110, Alignment.CENTERED);
            FONT.draw(graph, "Blizzard", 185, 120, Alignment.CENTERED);
            FONT.draw(graph, "Blizzard", 185, 130, Alignment.CENTERED);
            FONT.draw(graph, "wwt", 185, 140, Alignment.CENTERED);
            this.buttons[6].render(graph);
            this.cursor.render(graph);
            break;
        case NEW_OUT:
        	this.drawNew(graph);
            this.applyAlpha(graph, this.alphas[255 - this.alpha]);
            break;
        case PLAY:
            this.applyAlpha(graph, Color.BLACK);
            break;
		}
        if (MENU.ordinal() > 1) {
            this.text.setColor(new Color(208,208,240,120));
            this.text.draw(graph, "warcraft"+ " " + 0.6, (int) (320 * this.wide) - 2, 0, Alignment.RIGHT);
            this.text.draw(graph, "wwt", 2, 0, Alignment.LEFT);
            super.render(graph);
        }
	}
	
	public void applyAlpha(Graphics2D graph,Color color){
		graph.setColor(color);
		graph.fillRect(0,0,this.screen.init.widthRef,this.screen.init.heightRef);
	}
	
	public void drawNew(Graphics2D graph){
		this.background.render(graph, 0, 0);
        graph.setColor(new Color(190, 190, 190));
        graph.drawRect(80, 84, 160, 82);
        graph.setColor(new Color(20, 48, 77));
        graph.fillRect(81, 85, 159, 81);
        FONT.setColor(new Color(255, 244, 69));
        FONT.draw(graph, "Select game type", 160, 89, Alignment.CENTERED);
        FONT.setColor(new Color(255, 255, 235));
        FONT.draw(graph, "Race:", 136, 104, Alignment.RIGHT);
        FONT.draw(graph, this.format(RACES[this.playerRace]), 188, 104, Alignment.CENTERED);
        FONT.draw(graph, "Opponent:", 136, 120, Alignment.RIGHT);
        FONT.draw(graph, this.format(RACES[this.opponentRace]), 188, 120, Alignment.CENTERED);
        FONT.draw(graph, "Map:", 136, 136, Alignment.RIGHT);
        FONT.draw(graph, this.format(MAPS[this.map], true), 188, 136, Alignment.CENTERED);
        FONT.draw(graph, "View:", 136, 152, Alignment.RIGHT);
        FONT.draw(graph, FOGS[this.fog], 188, 152, Alignment.CENTERED);
        for (int i = 4; i < 6; i++) {
            this.buttons[i].render(graph);
        }
        for (int i = 0; i < 8; i++) {
            this.choices[i].render(graph);
        }
	}
	
    public String format(Race name) {
        return this.format(name.name(), false);
    }

    public String format(String name, boolean ext) {
        String str = name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
        if (ext) {
            str = str.substring(0, str.length() - 4);
            str = str.replace('_', ' ');
        }
        return str;
    }
}
