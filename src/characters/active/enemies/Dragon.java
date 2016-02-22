package characters.active.enemies;

import items.Item;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import magic.FireRing;
import magic.Fireball;

import java.util.ArrayList;

import characters.Character;
import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.RandUtil;
import util.Tuple;

public class Dragon extends ActiveCharacter {
	
	public Dragon(Map map, Room room, Tuple<Integer, Integer> position, ArrayList<String> adjectives, int level) {
		super("dragon", "", map, room, position, 8+level, 3+level, 50+(level*5),
				50, 500, 1000, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, new ArrayList<Item>(), 0,
				0, 100, 100, 100, "D", 2, 2, adjectives, level);
		this.setTirenessTotal(3);
		this.setExperienceGiven(100+level*10);
		WereableWeapon oneHandSword = new ShortSword(this, null, null, null, level, true);
		this.addSpell(new Fireball());
		this.addSpell(new FireRing());
		this.putItemInventory(oneHandSword);
		this.equipWeapon(oneHandSword);
		if (RandUtil.RandomNumber(0, 5) == 1) {
			this.putRandomItemInventory();
		}
	}
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("old");
		return adjectives;
	}
}
