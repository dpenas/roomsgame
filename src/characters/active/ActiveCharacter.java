package characters.active;

import java.util.ArrayList;

import map.Map;
import map.Room;
import characters.Character;
import util.RandUtil;
import util.Tuple;
import items.wereables.Wereable;
import items.wereables.WereableWeapon;
import items.wereables.WereableArmor;
import items.ItemEnumerate;
import items.ItemEnumerate.WeaponType;

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
	private ArrayList<WereableWeapon> weaponsEquipped;
	private ArrayList<WereableArmor> armorsEquipped;

	public ActiveCharacter(String name, String description, String gender,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck, int weight, int length, ArrayList<WereableWeapon> weaponsEquipped,
			ArrayList<WereableArmor> armorsEquipped) {
		super(name, description, gender, map, room, position, weight, length);
		this.damage = damage;
		this.defense = defense;
		this.life = life;
		this.luck = luck;
		this.weaponsEquipped = weaponsEquipped;
		this.armorsEquipped = armorsEquipped;
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
	
	public ArrayList<ItemEnumerate.WeaponType> getFreeWeaponSlots(){
		
		ArrayList<ItemEnumerate.WeaponType> availableSlots = new ArrayList<ItemEnumerate.WeaponType>();
		
		for (ItemEnumerate.WeaponType weaponType : ItemEnumerate.WeaponType.values()){
			availableSlots.add(weaponType);
		}
		
		for (WereableWeapon w: weaponsEquipped){
			for (ItemEnumerate.WeaponType a: availableSlots){
				if (w.getWeaponType() == a){
					availableSlots.remove(a);
				}
			}
		}
		return availableSlots;
	}
	
	public ArrayList<ItemEnumerate.ArmorType> getFreeArmorSlots(){
		ArrayList<ItemEnumerate.ArmorType> availableSlots = new ArrayList<ItemEnumerate.ArmorType>();
		
		for (ItemEnumerate.ArmorType armorType : ItemEnumerate.ArmorType.values()){
			availableSlots.add(armorType);
		}
		
		for (WereableArmor w: armorsEquipped){
			for (ItemEnumerate.ArmorType a: availableSlots){
				if (w.getArmorType() == a){
					availableSlots.remove(a);
				}
			}
		}
		return availableSlots;
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
	
	public ArrayList<WereableWeapon> getWeaponsEquipped(){
		return weaponsEquipped;
	}
	
	public ArrayList<WereableArmor> getArmorEquipped(){
		return armorsEquipped;
	}
	
	public boolean equipWeapon(WereableWeapon weapon){
		// TODO: Finish this. Probably we need to change the Type in WereableWeapon 
		// to an ArrayList, so we that an item could get multiple spaces.
		ArrayList<WeaponType> freeSlots = new ArrayList<WeaponType>(this.getFreeWeaponSlots());
		WeaponType weaponType = weapon.getWeaponType();
		for (WeaponType a: freeSlots){
			if (weaponType == a){
				
				
				return true;
			}
			
		}
		return false;
	}
	
	public boolean equipArmor(){
		return false;
	}
	
	

}
