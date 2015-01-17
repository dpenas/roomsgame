package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;
import main.Main;

public class OneHandSword extends WereableWeapon {

	public OneHandSword(String name, String description, String gender, int weight,
			int space, int durability, Character character,
			ArrayList<WeaponType> weaponType, Map map, Room room,
			Tuple<Integer, Integer> position, int attack, int erosion,
			boolean isSingleHand, int defense, int level) {
		super(name, description, gender, 
				weight, space, durability, character, weaponType,
				map, room, position, attack, erosion, isSingleHand, defense, level);
		
	}
	

}
