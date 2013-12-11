package com.wwt.warcraft.map;
import java.io.*;
import com.b3dgs.lionengine.game.map.*;
import com.b3dgs.lionengine.file.FileReader;
import com.b3dgs.lionengine.file.FileWriter;
import com.b3dgs.lionengine.file.XMLNode;

public class Tile extends AbstractPathTile{

	int name;
	Border20 id;
	int add;
	Collision s;
	
	public Tile(int pattern, int number, int x, int y, String collision) {
		super(pattern, number, x, y, collision);
		this.id=Border20.NONE;
		this.add=0;
		if(Map.THEME.equals("swamp")){
			this.add=19;
		}
	}
	
	public void setCollision(String collision){
		super.setCollision(collision);
		this.s=Collision.valueOf(collision);
		switch(this.s){
		case BORDER:
			this.name=CollisionType.BORDER;
			break;
		case WATER:
			this.name=CollisionType.WATER;
			break;
		case TREE_BORDER:
		case TREE:
			this.name=CollisionType.TREE;
			if((this.getNumber()>=125+this.add)&&(this.getNumber()<=144+this.add)){
				this.id = Border20.values()[this.getNumber() - (125 + this.add)];
			}
			break;
		default:
			this.name=CollisionType.GROUND;
			break;
		}
		if(this.name!=CollisionType.GROUND){
			this.setBlocking(true);
			}
	}
	 
	public void setNumber(Border20 id){
		this.id=id;
		super.setNumber(id.ordinal()+125+this.add);
	}
	
	public void setNumber(int n) {
	        super.setNumber(n);
	        if ((n >= 125 + this.add )&& (n <= 144 + this.add)) {
	            this.id = Border20.values()[n- (125 + this.add)];
	        } else {
	            this.id = Border20.NONE;
	        }
	}
	
	public Border20 getId(){
		return this.id;
	}
	
	public void setCollType(int type){
		this.name=type;
	}
	
	public int getCollType(){
		return this.name;
	}
	
	public Collision getCollisionEnum(){
		return this.s;
	}
	
	public void save(FileWriter file) throws IOException{
		file.writeByte((byte) (this.getPattern() + Byte.MIN_VALUE));
        file.writeByte((byte) (this.getNumber() + Byte.MIN_VALUE));
        file.writeShort((short) (this.getX() /16));
        file.writeShort((short) (this.getY() / 16));
	}
	
	 public void load(FileReader file) throws IOException {
	        this.setPattern(file.readByte() - Byte.MIN_VALUE);
	        this.setNumber(file.readByte() - Byte.MIN_VALUE);
	        this.setX(file.readShort() * 16);
	        this.setY(file.readShort() * 16);
	    }
	 
	 public void save(XMLNode node) {
	        node.writeInteger("p", this.getPattern());
	        node.writeInteger("n", this.getNumber());
	        node.writeInteger("x", this.getX() /16);
	        node.writeInteger("y", this.getY() / 16);
	    }
}
