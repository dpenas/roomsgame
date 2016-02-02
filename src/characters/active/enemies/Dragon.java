package characters.active.enemies;

import items.Item;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.Tuple;

public class Dragon extends ActiveCharacter {
	
	private Dragon(Map map, Room room, Tuple<Integer, Integer> position, int inventorySpace,
			ArrayList<Item> inventory, ArrayList<String> adjectives) {
		super("dragon", "", map, room, position, 8, 3, 50,
				50, 500, 1000, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, inventory, 0,
				0, 100, 0, 0, "D", 2, 2, adjectives);
		this.setTirenessTotal(3);
	}
}
