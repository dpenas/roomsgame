package characters.active;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import map.Map;
import map.Room;
import characters.Character;
import characters.active.enemies.Movement;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarSelectorS;
import grammars.grammars.PrintableObject;
import util.RandUtil;
import util.Tuple;
import magic.Spell;
import items.consumables.Consumable;
import items.wereables.WereableWeapon;
import items.wereables.WereableArmor;
import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import items.ItemEnumerate.WeaponType;
import main.Main;

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
	private int maxNumberSpells = 2;
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
	private int vision;
	private int movementType;
	private boolean isDead;
	private boolean isFirstTimeDead;
	private ArrayList<WereableWeapon> weaponsEquipped;
	private ArrayList<WereableArmor> armorsEquipped;
	private ArrayList<Tuple<Integer, Integer>> visiblePositions = new ArrayList<Tuple<Integer, Integer>>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();

	public ActiveCharacter(String name, String description,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck, int weight, int length, 
			ArrayList<WereableWeapon> weaponsEquipped,
			ArrayList<WereableArmor> armorsEquipped, int inventorySpace, int carryWeight,
			int actualCarryWeight, ArrayList<Item> inventory, int actualInventorySpace, int evasion,
			int totalLife, int magic, int totalMagic, String symbolRepresentation, int vision, int movementType,
			ArrayList<String> adjectives) {
		super(name, description, map, room, position, weight, length, carryWeight, actualCarryWeight, 
				inventory, symbolRepresentation, adjectives);
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
		this.vision = vision;
		this.isDead = false;
		this.isFirstTimeDead = true;
		this.movementType = movementType;
		this.spells = new ArrayList<Spell>();
	}
	
	public void setVisiblePositions(){
		this.visiblePositions = new ArrayList<Tuple<Integer, Integer>>();
		Tuple<Integer, Integer> position = this.getPosition();
		int minMap_x = position.x - this.getVision(); 
		if (minMap_x < this.getMap().global_init().x) minMap_x = this.getMap().global_init().x;
		
		int minMap_y = position.y - this.getVision(); 
		if (minMap_y < this.getMap().global_init().y) minMap_y = this.getMap().global_init().y;
		
		int maxMap_x = position.x + this.getVision(); 
		if (maxMap_x > this.getMap().global_fin().x) maxMap_x = this.getMap().global_fin().x;
		
		int maxMap_y = position.y + this.getVision();
		if (maxMap_y > this.getMap().global_fin().y) maxMap_y = this.getMap().global_fin().y;

		for (int i = minMap_x; i <= maxMap_x; i++){
			for (int j = minMap_y; j <= maxMap_y; j++){
				this.visiblePositions.add(new Tuple<Integer, Integer>(i, j));
			}
		}
	}
	
	public ArrayList<Tuple<Integer, Integer>> getImmediateReachablePositions() {
		ArrayList<Tuple<Integer, Integer>> allWalkablePositions = new ArrayList<Tuple<Integer, Integer>>();
		ArrayList<Tuple<Integer, Integer>> walkablePositions = new ArrayList<Tuple<Integer, Integer>>();
		allWalkablePositions.add(new Tuple<Integer, Integer>(this.getPosition().x - 1, this.getPosition().y));
		allWalkablePositions.add(new Tuple<Integer, Integer>(this.getPosition().x + 1, this.getPosition().y));
		allWalkablePositions.add(new Tuple<Integer, Integer>(this.getPosition().x, this.getPosition().y - 1));
		allWalkablePositions.add(new Tuple<Integer, Integer>(this.getPosition().x, this.getPosition().y + 1));
		for (Tuple<Integer, Integer> pos : allWalkablePositions) {
			if (this.canMove(pos)) {
				walkablePositions.add(pos);
			}
		}
		return walkablePositions;
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
	
	public void dropAllItems(ActiveCharacter character){
		for(Item item : character.getInventory()){
			if (Main.debug){
				System.out.println("Im dropping: " + item.getName());
			}
			item.setAttributesFromCharacter(character);
			character.getMap().putItemRoom(item);
		}
		character.setInventory(new ArrayList<Item>());
	}
	
	private void setCharacterDead(ActiveCharacter character) {
		if (character.getLife() <= 0){
			character.setDead(true);
			this.dropAllItems(character);
		}
	}

	private boolean attack(ActiveCharacter defender){
		int damageDone = this.getFullAttackNumbers(this, defender);
		if (Main.debug){
			System.out.println("Attack Done: " + damageDone);
		}
		
		if (damageDone <= 0) return false;
		int defenderLife = defender.getLife() - damageDone;
		defenderLife = defenderLife < 0 ? 0 : defenderLife;
		defender.setLife(defenderLife);
		System.out.println("Defender Life: " + defenderLife);
		this.setCharacterDead(defender);
		return true;
	}
	
	public ActiveCharacter weaponAttack() {
		Map map = this.getMap();
		ActiveCharacter monster = map.getMonstersPosition(this).get(0);
		for (int i = 0; i < map.getMonstersPosition(this).size(); i++) {
			monster = map.getMonstersPosition(this).get(i);
			if (!monster.isDead()) {
				monster = map.getMonstersPosition(this).get(i);
				break;
			}
		}

		this.attack(monster);
		System.out.println("Vida monster: " + map.getMonstersPosition(this).get(0).getLife());
		return monster;
	}
	
	private void attackWithSpell(ActiveCharacter defender, Spell spell) {
		if (Main.debug){
			System.out.println("Spell attack Done: " + spell.getDamage());
		}
		int defenderLife = defender.getLife() - spell.getDamage();
		defenderLife = defenderLife < 0 ? 0 : defenderLife;
		defender.setLife(defenderLife);
		System.out.println("Defender Life: " + defenderLife);
		this.setCharacterDead(defender);
	}
	
	public boolean attackSpell(int itemNumber) {
		if (this.getSpells().size() > itemNumber) {
			Spell spell = this.getSpells().get(itemNumber);
			Room room = this.getRoom();
			if (this.generateSpell(spell)) {
				ArrayList<Tuple<Integer, Integer>> spellDamagedPositions = spell.getDamagedPositions(this);
				ArrayList<Tuple<Integer, Integer>> monstersPositions = room.getPositionsOfMonsters();
				if (spellDamagedPositions.size() > 0) {
					for (Tuple<Integer, Integer> pos : spellDamagedPositions) {
						if (RandUtil.containsTuple(pos, monstersPositions)) {
							for (ActiveCharacter monsterDamaged : room.getMonstersPosition(pos)) {
								this.attackWithSpell(monsterDamaged, spell);
							}
						}
					}
					return true;
				}
			}
		} else {
			System.out.println("No spells");
		}
		return false;
	}
	
	private boolean generateSpell(Spell spell) {
		if (this.getMagic() < spell.getManaCost()) {
			if (Main.debug){
				System.out.println("Not enough mana: " + this.getMagic() + " for the spell: " + spell.getManaCost());
			}
			return false;
		}
		this.setMagic(this.getMagic() - spell.getManaCost());
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
	
	public Item getWearHelmet() {
		for (WereableArmor armorEquiped : this.getArmorsEquipped()) {
			if (armorEquiped.getArmorType().get(0).equals(ItemEnumerate.ArmorType.HEAD)){
				return armorEquiped;
			}
		}
		return null;
	}
	
	public Item getWearChest() {
		for (WereableArmor armorEquiped : this.getArmorsEquipped()) {
			if (armorEquiped.getArmorType().get(0).equals(ItemEnumerate.ArmorType.CHEST)){
				return armorEquiped;
			}
		}
		return null;
	}
	
	public Item getWearPants() {
		for (WereableArmor armorEquiped : this.getArmorsEquipped()) {
			if (armorEquiped.getArmorType().get(0).equals(ItemEnumerate.ArmorType.PANTS)){
				return armorEquiped;
			}
		}
		return null;
	}
	
	public Item getWearGloves() {
		for (WereableArmor armorEquiped : this.getArmorsEquipped()) {
			if (armorEquiped.getArmorType().get(0).equals(ItemEnumerate.ArmorType.HANDS)){
				return armorEquiped;
			}
		}
		return null;
	}
	
	public ArrayList<Item> getWearHandsDefense() {
		ArrayList<Item> handsWereable = new ArrayList<Item>(); 
		for (WereableArmor armorEquiped : this.getArmorsEquipped()) {
			if (armorEquiped.getArmorType().get(0).equals(ItemEnumerate.ArmorType.HANDS)){
				handsWereable.add(armorEquiped);
			}
		}
		return handsWereable;
	}
	
	public ArrayList<Item> getWearHandsAttack() {
		ArrayList<Item> handsWereable = new ArrayList<Item>(); 
		for (WereableWeapon weaponEquiped : this.getWeaponsEquipped()) {
			if (weaponEquiped.getWeaponType().get(0).equals(ItemEnumerate.WeaponType.LEFTHAND) ||
					weaponEquiped.getWeaponType().get(0).equals(ItemEnumerate.WeaponType.RIGHTHAND)){
				handsWereable.add(weaponEquiped);
			}
		}
		return handsWereable;
	}
	
	public boolean equipWeapon(WereableWeapon weapon){
		ArrayList<WeaponType> freeSlots = new ArrayList<WeaponType>(this.getFreeWeaponSlots());
		ArrayList<WeaponType> weaponType = weapon.getWeaponType();
		if (freeSlots.containsAll(weaponType) || (weapon.getIsSingleHand() && !freeSlots.isEmpty())){
			if (this.getInventory().contains(weapon)){
				if (weapon.getIsSingleHand()){
					ArrayList<WeaponType> type = new ArrayList<WeaponType>();
					type.add(freeSlots.get(0));
					weapon.setWeaponType(type);
				}
				this.weaponsEquipped.add(weapon);
				weapon.setCharacter(this);
				this.getInventory().remove(weapon);
				return true;
			}
		}
		return false;
	}
	
	public boolean equipArmor(WereableArmor armor){
		ArrayList<ArmorType> freeSlots = new ArrayList<ArmorType>(this.getFreeArmorSlots());
		ArrayList<ArmorType> armorType = armor.getArmorType();
		if (freeSlots.containsAll(armorType)){
			if (this.getInventory().contains(armor)){
				this.armorsEquipped.add(armor);
				armor.setCharacter(this);
				this.getInventory().remove(armor);
				return true;
			}
		}
		return false;
	}
	
	public void printInventory(ArrayList<Item> inventory, WSwingConsoleInterface j, int initPos_i, int initPos_j){
		System.out.println("Printing Inventory: ");
		for (int i = 0; i < inventory.size(); i++){
			System.out.println("Item: " + 1);
			String name = i + 1 + " - " + inventory.get(i).getName();
			j.print(initPos_j, initPos_i + i, name);
		}
	}
	
	public void _printLife(WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String life = "Life: " + this.getLife() + "/" + this.getTotalLife();
		j.print(initPos_j, initPos_i, life);
	}
	
	public void _printMana(WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String magic = "Mana: " + this.getMagic() + "/" + this.getTotalMagic();
		j.print(initPos_j, initPos_i, magic);
	}
	
	public void _printName(WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String name = this.getName();
		j.print(initPos_j, initPos_i, name);
	}
	
	public void printMonstersInformation(WSwingConsoleInterface j, int initPos_i, int initPos_j){
		_printName(j, initPos_j, initPos_i);
		Main.countElements++;
		_printLife(j, initPos_j + 1, initPos_i);
	}
	
	public boolean unequipItem(Item item) {
		if (item instanceof WereableArmor) {
			return this.unEquipArmor((WereableArmor)item);
		} else if (item instanceof WereableWeapon) {
			return this.unEquipWeapon((WereableWeapon)item);
		}
		return false;
	}
	
	/**
	 * The armor will go to the inventory if there's enough space
	 * @param armor
	 * @return
	 */
	
	public boolean unEquipArmor(WereableArmor armor){
		if ((this.getInventorySpace() >= this.getActualInventorySpace() + armor.getSpace()) && this.getArmorsEquipped().contains(armor)){
			this.getArmorsEquipped().remove(armor);
			this.getInventory().add(armor);
			this.setActualInventorySpace(this.getActualInventorySpace() + armor.getSpace());
			return true;
		}
		
		return false;
	}
	
	public boolean unEquipWeapon(WereableWeapon weapon){
		if ((this.getInventorySpace() >= this.getActualInventorySpace() + weapon.getSpace()) && this.getWeaponsEquipped().contains(weapon)){
			if (weapon.getIsSingleHand()){
				weapon.setWeaponType(new ArrayList<ItemEnumerate.WeaponType>());
			}
			this.getWeaponsEquipped().remove(weapon);
			this.getInventory().add(weapon);
			this.setActualInventorySpace(this.getActualInventorySpace() + weapon.getSpace());
			return true;
		}
		
		return false;
	}
	
	public boolean throwItem(Item item){
		if (item.getCharacter().equals(this)){
			item.setCharacter(null);
			item.setMap(this.getMap());
			item.setRoom(this.getRoom());
			item.setPosition(this.getPosition());
			item.getRoom().getItemsRoom().add(item);
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
	
	public boolean putItemInventory(Item item){
		if (this.getActualCarryWeight() + item.getWeight() <= this.getWeight()){
			if (this.getActualInventorySpace() + item.getSpace() <= this.getInventorySpace()){
				this.setActualCarryWeight(this.getActualCarryWeight() + item.getWeight());
				this.setActualInventorySpace(this.getActualInventorySpace() + item.getSpace());
				this.getInventory().add(item);
				item.setCharacter(this);
				return true;
			}
		}
		return false;
	}
	
	public Item pickItem(Tuple<Integer, Integer> pos, Room room){
		if (room.isMapPositionHere(pos)){
			for (Item item : room.getItemsRoom()){
				if (pos.x == item.getPosition().x && pos.y == item.getPosition().y){
					if (this.putItemInventory(item)){
						room.getItemsRoom().remove(item);
						return item;
					}
				}
			}
		}
		return null;
	}
	
	// TODO: Change this so we can use another inventory
	public boolean useConsumable(Consumable consumable){
		if (this.getInventory().contains(consumable)){
			if (consumable.getCharacter().equals(this)){
				consumable.consume(this);
				this.getInventory().remove(consumable);
				consumable.setCharacter(null);
				return true;
			} else {
				if (Main.debug){
					System.out.println("The item is not associated to the Character");
				}
			}
		}
		if (Main.debug){
			System.out.println("The item is not in the inventory");
		}
		return false;
	}
	
	public boolean move(Tuple<Integer, Integer> position){
		Room room = this.getMap().obtainRoomByPosition(position); 
		if (this.canMove(position)) {
			this.setPosition(position);
			this.setRoom(room);
			return true;
		}
		return false;
	}
	
	public boolean canMove(Tuple<Integer, Integer> position) {
		Room room = this.getMap().obtainRoomByPosition(position); 
		if (room != null && !RandUtil.containsTuple(position, room.getInsidecolumns()) 
				&& (room.isInside(position) || room.isADoor(position))){
			return true;
		}
		return false;
	}
	
	public void useItem(Item item){
		if (item.isWereableItem()){
			if (item.isWereableArmor()){
				this.equipArmor((WereableArmor)item);
			} else {
				if (item.isWereableWeapon()){
					this.equipWeapon((WereableWeapon)item);
				}
			}
		} else {
			if (item.isConsumableItem()){
				this.useConsumable((Consumable)item);
			}
		}
	}
	
	public String doTurn(ActiveCharacter user, GrammarIndividual grammarAttack, JsonObject rootObjWords){
		if (this.getRoom().equals(user.getRoom()) && !this.isDead()){
			if (RandUtil.sameTuple(this.getPosition(), user.getPosition())){
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(this);
				names.add(user);
				GrammarSelectorS selector = null;
				try {
					selector = new GrammarSelectorS(grammarAttack, rootObjWords, names, "ATTACK");
				} catch (JsonIOException | JsonSyntaxException | FileNotFoundException | InstantiationException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
				this.attack(user);
				return selector.getRandomSentence();
			} else {
				Tuple<Integer, Integer> pos = Movement.moveCharacter(this, user);
				if (pos != null) {
					this.move(pos);
				}
			}
		}
		return "";	
	}
	
	public String getLifeAdjective() {
		if (this.getLife() > 75 && this.getLife() <= 100) {
			return "a lot of";
		} else if (this.getLife() > 40 && this.getLife() <= 75) {
				return "some";
		} else {
			return "a little";
		}
	}
	
	public String getManaAdjective() {
		if (this.getMagic() > 75 && this.getMagic() <= 100) {
			return "a lot of";
		} else if (this.getMagic() > 40 && this.getMagic() <= 75) {
				return "some";
		} else {
			return "a little";
		}
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
	
	public ArrayList<Tuple<Integer, Integer>> getVisiblePositions(){
		return this.visiblePositions;
	}

	public int getVision() {
		return vision;
	}

	public void setVision(int vision) {
		this.vision = vision;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getMovementType() {
		return movementType;
	}

	public void setMovementType(int movementType) {
		this.movementType = movementType;
	}

	public boolean isFirstTimeDead() {
		return isFirstTimeDead;
	}

	public void setFirstTimeDead(boolean isFirstTimeDead) {
		this.isFirstTimeDead = isFirstTimeDead;
	}
	
	public int getMaxNumberSpells() {
		return maxNumberSpells;
	}

	public void setMaxNumberSpells(int maxNumberSpells) {
		this.maxNumberSpells = maxNumberSpells;
	}

	public boolean addSpell(Spell spell) {
		if (this.getSpells().size() <= this.getMaxNumberSpells()) {
			ArrayList<Spell> newSpells = new ArrayList<Spell>();
			for (Spell oldSpell: this.getSpells()) {
				newSpells.add(oldSpell);
			}
			newSpells.add(spell);
			this.setSpells(newSpells);
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

}
