package characters.active.enemies;

import items.Item;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.FireRing;
import magic.Fireball;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.Tuple;

public class Dragon extends ActiveCharacter {
	
	WereableWeapon oneHandSword = new ShortSword("", 0, 0, 100, this, null, null,
			null, 0, 0, true);
	
	public Dragon(Map map, Room room, Tuple<Integer, Integer> position, ArrayList<String> adjectives) {
		super("dragon", "", map, room, position, 8, 3, 50,
				50, 500, 1000, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, new ArrayList<Item>(), 0,
				0, 100, 100, 100, "D", 2, 2, adjectives);
		this.setTirenessTotal(3);
		this.addSpell(new Fireball());
		this.addSpell(new FireRing());
		this.putItemInventory(oneHandSword);
		this.equipWeapon(oneHandSword);
	}
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("old");
		return adjectives;
	}
}
