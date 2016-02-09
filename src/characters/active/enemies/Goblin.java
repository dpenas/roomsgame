package characters.active.enemies;

import items.Item;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.Fireball;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.Tuple;

public class Goblin extends ActiveCharacter {
	
	WereableWeapon oneHandSword = new ShortSword("", 0, 0, 100, this, null, null,
			null, 0, 0, true);

	public Goblin(Map map, Room room, Tuple<Integer, Integer> position, ArrayList<String> adjectives) {
		super("goblin", "", map, room, position, 2, 1, 30,
				30, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, new ArrayList<Item>(), 0, 0, 100, 50, 0, "G", 3, 2, adjectives);
		System.out.println("Adj: " + this.getAdjectives().size());
		this.setTirenessTotal(5);
		this.addSpell(new Fireball());
		this.putItemInventory(oneHandSword);
		this.equipWeapon(oneHandSword);
	}
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("grey");
		return adjectives;
	}

}
