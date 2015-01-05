package characters.active;

import java.util.ArrayList;

import map.Map;
import map.Room;
import characters.Character;
import util.RandUtil;
import util.Tuple;
import items.wereables.WereableWeapon;
import items.wereables.WereableArmor;
import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
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
	private int inventorySpace;
	private ArrayList<WereableWeapon> weaponsEquipped;
	private ArrayList<WereableArmor> armorsEquipped;

	public ActiveCharacter(String name, String description, String gender,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck, int weight, int length, ArrayList<WereableWeapon> weaponsEquipped,
			ArrayList<WereableArmor> armorsEquipped, int inventorySpace, int carryWeight,
			int actualCarryWeight, ArrayList<Item> inventory) {
		super(name, description, gender, map, room, position, weight, length, carryWeight, actualCarryWeight, inventory);
		this.damage = damage;
		this.defense = defense;
		this.life = life;
		this.luck = luck;
		this.weaponsEquipped = weaponsEquipped;
		this.armorsEquipped = armorsEquipped;
		this.inventorySpace = inventorySpace;
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
			for (WeaponType a: w.getWeaponType()){
				availableSlots.remove(a);
			}
		}
		return availableSlots;
	}
	
	public ArrayList<ItemEnumerate.ArmorType> getFreeArmorSlots(){
		ArrayList<ItemEnumerate.ArmorType> availableSlots = new ArrayList<ItemEnumerate.ArmorType>();
		
		for (ItemEnumerate.ArmorType armorType : ItemEnumerate.ArmorType.values()){
			availableSlots.add(armorType);
		}
		for (WereableArmor a: this.getArmorsEquipped()){
			for (ArmorType i: a.getArmorType()){
				availableSlots.remove(i);
			}
		}
		
		return availableSlots;
	}
	
	public boolean equipWeapon(WereableWeapon weapon){
		ArrayList<WeaponType> freeSlots = new ArrayList<WeaponType>(this.getFreeWeaponSlots());
		ArrayList<WeaponType> weaponType = weapon.getWeaponType();
		if (freeSlots.containsAll(weaponType)){
			if ((this.getCarryWeight() >= weapon.getWeight() + this.getActualCarryWeight() )){
				this.setActualCarryWeight(this.getActualCarryWeight() + weapon.getWeight());
				this.weaponsEquipped.add(weapon);
				weapon.setCharacter(this);
				return true;
			}
		}
		return false;
	}
	
	public boolean equipArmor(WereableArmor armor){
		ArrayList<ArmorType> freeSlots = new ArrayList<ArmorType>(this.getFreeArmorSlots());
		ArrayList<ArmorType> armorType = armor.getArmorType();
		if (freeSlots.containsAll(armorType)){
			if ((this.getCarryWeight() >= armor.getWeight() + this.getActualCarryWeight() )){
				this.setActualCarryWeight(this.getActualCarryWeight() + armor.getWeight());
				this.armorsEquipped.add(armor);
				armor.setCharacter(this);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The armor will go to the inventory if there's enough space
	 * @param armor
	 * @return
	 */
	
	public boolean unEquipArmor(WereableArmor armor){
		if (armor.getCharacter().equals(this) && this.getActualCarryWeight() + armor.getWeight() <= this.getCarryWeight()){
			this.getArmorsEquipped().remove(armor);
			this.getInventory().add(armor);
			this.setActualCarryWeight(this.getActualCarryWeight() + armor.getWeight());
			return true;
		}
		
		return false;
	}
	
	public boolean unEquipWeapon(WereableWeapon weapon){
		if (weapon.getCharacter().equals(this) && this.getActualCarryWeight() + weapon.getWeight() <= this.getCarryWeight()){
			this.getWeaponsEquipped().remove(weapon);
			this.getInventory().add(weapon);
			this.setActualCarryWeight(this.getActualCarryWeight() + weapon.getWeight());
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
	
	public ArrayList<WereableWeapon> getWeaponsEquipped(){
		return weaponsEquipped;
	}

	public int getInventorySpace() {
		return inventorySpace;
	}

	public void setInventorySpace(int inventorySpace) {
		this.inventorySpace = inventorySpace;
	}

	public ArrayList<WereableArmor> getArmorsEquipped() {
		return armorsEquipped;
	}

	public void setArmorsEquipped(ArrayList<WereableArmor> armorsEquipped) {
		this.armorsEquipped = armorsEquipped;
	}

	public void setWeaponsEquipped(ArrayList<WereableWeapon> weaponsEquipped) {
		this.weaponsEquipped = weaponsEquipped;
	}

}
