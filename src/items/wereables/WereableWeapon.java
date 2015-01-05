package items.wereables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;
import items.ItemEnumerate;

public class WereableWeapon extends Wereable {
	
	private ArrayList<ItemEnumerate.WeaponType> weaponType;

	public WereableWeapon(String description, String gender, int weight,
			int space, int durability, Character character, ArrayList<ItemEnumerate.WeaponType> weaponType,
			Map map, Room room, Tuple<Integer, Integer> position) {
		super(description, gender, weight, space, durability, character, map, room, position);
		this.weaponType = weaponType;
	}
	
	public ArrayList<ItemEnumerate.WeaponType> getWeaponType(){
		return weaponType;
	}
	

}
