package characters.active.enemies;

import items.Item;
import items.wereables.ShortSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.RandUtil;
import util.Tuple;

public class Rat extends ActiveCharacter {
	
	public Rat(Map map, Room room, Tuple<Integer, Integer> position, ArrayList<String> adjectives, int level) {
		super("rat", "", map, room, position, 2, 1, 30,
				50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, new ArrayList<Item>(), 0, 0, 100, 0, 0, "R", 3, 2, adjectives, level);
		WereableWeapon oneHandSword = new ShortSword(this, null, null, null, level, false);
		this.putItemInventory(oneHandSword);
		this.setExperienceGiven(20+level*10);
		this.equipWeapon(oneHandSword);
		if (RandUtil.RandomNumber(0, 5) == 1) {
			this.putRandomItemInventory();
		}
	}
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("small");
		return adjectives;
	}
}