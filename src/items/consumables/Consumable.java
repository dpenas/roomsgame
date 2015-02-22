package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import items.Item;
import characters.Character;
import characters.active.ActiveCharacter;

public abstract class Consumable extends Item {
	
	private String effectDescription;
	
	public Consumable(String name, ArrayList<String> nameAttributes, String gender, int weight, int space,
			String effectDescription, Character character, Map map, Room room, 
			Tuple<Integer, Integer> position) {
		super(name, nameAttributes, "", gender, weight, space, character, map, room, position, "P");
		this.effectDescription = effectDescription;
	}
	
	public String getEffectDescription(){
		return effectDescription;
	}
	
	public String setEffectDescription(){
		return effectDescription;
	}
	
	public abstract void consume(ActiveCharacter character);
	
}
