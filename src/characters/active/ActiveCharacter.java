package characters.active;

import map.Map;
import map.Room;
import characters.Character;
import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Change the attack damage depending on the weapon, defense o the character, etc.
 * 
 * TODO ADDITIONAL: 
 * - Luck depending on the used weapon.
 * - Introduce criticals, so depending on the draw we could deal more damage
 *
 */

public class ActiveCharacter extends Character {
	private int damage;
	private int defense;
	private int life;
	private int luck;

	public ActiveCharacter(String name, String description, boolean isMale,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck) {
		super(name, description, isMale, map, room, position);
		this.damage = damage;
		this.defense = defense;
		this.life = life;
		this.luck = luck;
	}

	public boolean attack(ActiveCharacter defender){
		int randNumber = RandUtil.RandomNumber(0, 100);
		if (this.luck >= randNumber){
			int defenderLife = defender.getLife() - (this.getDamage() - defender.getDefense());
			defenderLife = defenderLife < 0 ? 0 : defenderLife;
			defender.setLife(defenderLife);
			return true;
		}
		return false;
	}
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
	}
	
	public int getLife() {
		return life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getLuck() {
		return luck;
	}
	
	public void setLuck(int luck) {
		this.luck = luck;
	}
	
	

}
