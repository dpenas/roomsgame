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
import util.RandUtil;
import util.Tuple;

public class Goblin extends ActiveCharacter {

	public Goblin(Map map, Room room, Tuple<Integer, Integer> position, ArrayList<String> adjectives, int level) {
		super("goblin", "", map, room, position, 2, 1, 30,
				30, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 60,
				70, 0, new ArrayList<Item>(), 0, 0, 100, 50, 0, "G", 3, new FollowingMove(), adjectives, level);
		WereableWeapon oneHandSword = new ShortSword(this, null, null, null, level, false);
		this.setTirenessTotal(8);
		this.setExperienceGiven(35+level*10);
		this.addSpell(new Fireball());
		this.putItemInventory(oneHandSword);
		this.equipWeapon(oneHandSword);
		if (RandUtil.RandomNumber(0, 5) == 1) {
			this.putRandomItemInventory();
		}
	}
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("grey");
		return adjectives;
	}

}
