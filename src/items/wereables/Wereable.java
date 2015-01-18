package items.wereables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import items.Item;
import characters.Character;

public abstract class Wereable extends Item {
	
	private int durability;
	private boolean isWereable;
	private boolean isMagic;
	private int erosion; // Durability points that the weapon loses every time
	private int level; // Level of the wereable, which will determine its stats
	
	public Wereable(String name, ArrayList<String> nameAttributes, String description, 
			String gender, int weight, int space, int durability, Character character, 
			Map map, Room room, Tuple<Integer, Integer> position, 
			int erosion, int level, boolean isMagic) {
		super(name, nameAttributes, description, gender, weight, space, character, map, room, position);
		this.durability = durability;
		this.erosion = erosion;
		this.isWereable = (this.durability > 0) ? true : false;
		this.isMagic = isMagic;
	}
	
	public boolean getIsWereable(){
		return isWereable;
	}
	
	public int getDurability(){
		return durability;
	}
	
	public int getErosion(){
		return erosion;
	}

	public void setDurability(int durability){
		this.durability = durability;
	}

	public void setErosion(int erosion){
		this.erosion = erosion;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setWereable(boolean isWereable) {
		this.isWereable = isWereable;
	}

	public boolean isMagic() {
		return isMagic;
	}

	public void setMagic(boolean isMagic) {
		this.isMagic = isMagic;
	}

}
