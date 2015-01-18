package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import items.Item;
import characters.Character;

public abstract class Consumable extends Item {
	
	private String effectDescription;
	
	public Consumable(String name, ArrayList<String> nameAttributes, 
			String description, String gender, int weight, int space,
			String effectDescription, Character character, Map map, Room room, 
			Tuple<Integer, Integer> position) {
		super(name, nameAttributes, description, gender, weight, space, character, map, room, position);
		this.effectDescription = effectDescription;
	}
	
	public String getEffectDescription(){
		return effectDescription;
	}

	
	
}
