package items.wereables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import characters.Character;

public class WereableArmor extends Wereable {

	private ArrayList<ItemEnumerate.ArmorType> armorType;
	private int defense;
	
	public WereableArmor(String description, String gender, int weight,
			int space, ArrayList<ItemEnumerate.ArmorType> armorType, int durability,
			Character character, int defense, Map map, Room room, Tuple<Integer, Integer> position) {
		super(description, gender, weight, space, durability, character, map, room, position);
		this.armorType = armorType;
		this.defense = defense;
	}
	
	public ArrayList<ArmorType> getArmorType(){
		return armorType;
	}
	
	public int getDefense() {
		return defense;
	}

}
