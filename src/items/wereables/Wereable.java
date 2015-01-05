package items.wereables;

import map.Map;
import map.Room;
import util.Tuple;
import items.Item;
import characters.Character;

public abstract class Wereable extends Item {
	
	private int durability;
	private boolean isWereable;
	private int erosion; // Durability points that the weapon loses every time
	
	public Wereable(String description, String gender, int weight, int space, int durability, Character character,
			Map map, Room room, Tuple<Integer, Integer> position) {
		super(description, gender, weight, space, character, map, room, position);
		this.durability = durability;
		this.isWereable = (this.durability > 0) ? true : false;
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

}
