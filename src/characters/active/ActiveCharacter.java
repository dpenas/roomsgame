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
	private int totalLife; // TotalLife
	private int life;
	private int magic;
	private int totalMagic;
	private int luck;
	private int inventorySpace;
	private int actualInventorySpace;
	private int evasion;
	private ArrayList<WereableWeapon> weaponsEquipped;
	private ArrayList<WereableArmor> armorsEquipped;

	public ActiveCharacter(String name, String description, String gender,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck, int weight, int length, 
			ArrayList<WereableWeapon> weaponsEquipped,
			ArrayList<WereableArmor> armorsEquipped, int inventorySpace, int carryWeight,
			int actualCarryWeight, ArrayList<Item> inventory, int actualInventorySpace, int evasion,
			int totalLife, int magic, int totalMagic) {
		super(name, description, gender, map, room, position, weight, length, carryWeight, actualCarryWeight, inventory);
		this.damage = damage;
		this.totalMagic = totalMagic;
		this.magic = magic;
		this.defense = defense;
		this.life = life;
		this.luck = luck; // number between 0 and 100
		this.weaponsEquipped = weaponsEquipped;
		this.armorsEquipped = armorsEquipped;
		this.inventorySpace = inventorySpace;
		this.actualInventorySpace = actualInventorySpace;
		this.evasion = evasion; // number between 0 and 100
		this.totalLife = totalLife;
	}

	public int getAttackFromWeapons(ActiveCharacter character){
		int damage = 0;
		for (WereableWeapon w: character.getWeaponsEquipped()){
			if (w.getDurability() > 0){
				int randNumber = RandUtil.RandomNumber(0, 100);
				if (character.getLuck() <= randNumber){
					w.setDurability(w.getDurability() - w.getErosion());
				}
				damage += w.getAttack();
			}
		}
		return damage;
	}

	public int getDefenseFromArmor(ActiveCharacter character){
		int defense = 0;
		for (WereableArmor w: character.getArmorsEquipped()){
			if (w.getDurability() > 0){
				int randNumber = RandUtil.RandomNumber(0, 100);
				if (character.getLuck() <= randNumber){
					w.setDurability(w.getDurability() - w.getErosion());
				}
				defense += w.getDefense();
			}
		}
		return defense;
	}
	
	public int getDefenseFromShields(ActiveCharacter character){
		int defense = 0;
		for (WereableWeapon w: character.getWeaponsEquipped()){
			if (w.getDurability() > 0){
				int randNumber = RandUtil.RandomNumber(0, 100);
				if (character.getLuck() <= randNumber){
					w.setDurability(w.getDurability() - w.getErosion());
				}
				defense += w.getDefense();
			}
		}
		return defense;
	}

	public int getFullAttackNumbers(ActiveCharacter attacker, ActiveCharacter defender){
		int randNumber = RandUtil.RandomNumber(0, 100);
		if (attacker.getLuck() >= randNumber && defender.evasion <= randNumber){
			int damage = this.getAttackFromWeapons(attacker) - this.getDefenseFromArmor(defender)
					- this.getDefenseFromShields(defender);
			if (damage > 0){
				return damage;
			}
		}
		return 0;
	}

	public boolean attack(ActiveCharacter defender){
		int damageDone = this.getFullAttackNumbers(this, defender);
		if (damageDone <= 0) return false;
		int defenderLife = defender.getLife() - damageDone;
		defenderLife = defenderLife < 0 ? 0 : defenderLife;
		defender.setLife(defenderLife);
		return true;
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
		if (freeSlots.containsAll(weaponType) || (weapon.getIsSingleHand() && !freeSlots.isEmpty())){
			if ((this.getCarryWeight() >= weapon.getWeight() + this.getActualCarryWeight() )){
				if (this.getInventorySpace() >= this.getActualInventorySpace() + weapon.getSpace()){
					this.setActualCarryWeight(this.getActualCarryWeight() + weapon.getWeight());
					this.setInventorySpace(this.getActualInventorySpace() + weapon.getSpace());
					if (weapon.getIsSingleHand()){
						ArrayList<WeaponType> type = new ArrayList<WeaponType>();
						type.add(freeSlots.get(0));
						weapon.setWeaponType(type);
					}
					this.weaponsEquipped.add(weapon);
					weapon.setCharacter(this);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean equipArmor(WereableArmor armor){
		ArrayList<ArmorType> freeSlots = new ArrayList<ArmorType>(this.getFreeArmorSlots());
		ArrayList<ArmorType> armorType = armor.getArmorType();
		if (freeSlots.containsAll(armorType)){
			if ((this.getCarryWeight() >= armor.getWeight() + this.getActualCarryWeight() )){
				if (this.getInventorySpace() >= this.getActualInventorySpace() + armor.getSpace()){
					this.setActualCarryWeight(this.getActualCarryWeight() + armor.getWeight());
					this.setActualInventorySpace(this.getActualInventorySpace() + armor.getSpace());
					this.armorsEquipped.add(armor);
					armor.setCharacter(this);
					return true;
				}
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
		if (armor.getCharacter().equals(this) && this.getActualCarryWeight() + armor.getWeight() <= this.getCarryWeight()
				&& this.getInventorySpace() >= this.getActualInventorySpace() + armor.getSpace()){
			this.getArmorsEquipped().remove(armor);
			this.getInventory().add(armor);
			this.setActualInventorySpace(this.getActualInventorySpace() - armor.getSpace());
			this.setActualCarryWeight(this.getActualCarryWeight() - armor.getWeight());
			return true;
		}
		
		return false;
	}
	
	public boolean unEquipWeapon(WereableWeapon weapon){
		if (weapon.getCharacter().equals(this) && this.getActualCarryWeight() + weapon.getWeight() <= this.getCarryWeight()
				&& this.getInventorySpace() >= this.getActualInventorySpace() + weapon.getSpace()){
			if (weapon.getIsSingleHand()){
				weapon.setWeaponType(new ArrayList<ItemEnumerate.WeaponType>());
			}
			this.getWeaponsEquipped().remove(weapon);
			this.getInventory().add(weapon);
			this.setActualInventorySpace(this.getActualInventorySpace() - weapon.getSpace());
			this.setActualCarryWeight(this.getActualCarryWeight() - weapon.getWeight());
			return true;
		}
		
		return false;
	}
	
	public boolean throwItem(Item item){
		if (item.getCharacter().equals(this)){
			item.setCharacter(null);
			item.setRoom(this.getRoom());
			item.setPosition(this.getPosition());
			if (this.getWeaponsEquipped().contains(item)){
				this.getWeaponsEquipped().remove(item);
			} else if (this.getArmorsEquipped().contains(item)){
				this.getArmorsEquipped().remove(item);
			} else if (this.getInventory().contains(item)){
				this.getInventory().remove(item);
			}
			this.setActualCarryWeight(this.getActualCarryWeight() - item.getWeight());
			this.setActualInventorySpace(this.getActualInventorySpace() - item.getSpace());
			return true;
		}
		return false;
	}
	
	/**
	 * Needs its own method, since the weapon's type needs to be set to
	 * empty in case it doesn't 2 hands
	 * @return
	 */
	public boolean throwWeapon(WereableWeapon weapon){
		if (weapon.getIsSingleHand() && weapon.getCharacter().equals(this)){
			weapon.setWeaponType(new ArrayList<WeaponType>());
		}
		return this.throwItem(weapon);
		
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
	
	public int getActualInventorySpace() {
		return actualInventorySpace;
	}

	public void setActualInventorySpace(int actualInventorySpace) {
		this.actualInventorySpace = actualInventorySpace;
	}

	public int getEvasion(){
		return this.evasion;
	}

	public void setEvasion(int evasion){
		this.evasion = evasion;
	}

	public int getTotalLife() {
		return totalLife;
	}

	public void setTotalLife(int totalLife) {
		this.totalLife = totalLife;
	}
	
	public int getTotalMagic() {
		return totalMagic;
	}

	public void setTotalMagic(int totalMagic) {
		this.totalMagic = totalMagic;
	}
	
	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

}
