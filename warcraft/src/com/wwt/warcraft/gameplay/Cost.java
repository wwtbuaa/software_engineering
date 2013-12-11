package com.wwt.warcraft.gameplay;

public class Cost {
	public final Resource gold;
	public final Resource wood;
	public final int time,w,h;
	
	public Cost(int gold,int wood,int time,int w,int h){
		this.gold=new Resource(gold);
		this.wood=new Resource(wood);
		this.time=time;
		this.w=w;
		this.h=h;
	}
}
