package characters.active;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import net.slashie.util.Pair;
import map.Map;
import map.Room;
import characters.Character;
import characters.active.enemies.Movement;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarSelectorNP;
import grammars.grammars.GrammarSelectorS;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.PrintableObject;
import grammars.parsing.JSONParsing;
import util.RandUtil;
import util.Tuple;
import magic.Spell;
import items.consumables.Consumable;
import items.wereables.WereableWeapon;
import items.wereables.LongSword;
import items.wereables.NormalArmor;
import items.wereables.NormalGloves;
import items.wereables.NormalHelmet;
import items.wereables.NormalPants;
import items.wereables.ShortSword;
import items.wereables.SmallShield;
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
	private Movement movementType;
	private boolean isDead;
	private boolean isFirstTimeDead;
	private boolean hasAttackedHeroe;
	private int maximumItemsInventory;
	private ArrayList<WereableWeapon> weaponsEquipped;
	private ArrayList<WereableArmor> armorsEquipped;
	private ArrayList<Tuple<Integer, Integer>> visiblePositions = new ArrayList<Tuple<Integer, Integer>>();
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private int tirenessTotal = 0;
	private int tirenessCurrent = 0;
	private int level = 1;
	private int experience = 0;
	private int nextLevelExperience = 0;
	private int experienceGiven = 0;
	private boolean hasBeenAttackedByHeroe = false;

	public ActiveCharacter(String name, String description,
			Map map, Room room, Tuple<Integer, Integer> position, int damage,
			int defense, int life, int luck, int weight, int length, 
			ArrayList<WereableWeapon> weaponsEquipped,
			ArrayList<WereableArmor> armorsEquipped, int inventorySpace, int carryWeight,
			int actualCarryWeight, ArrayList<Item> inventory, int actualInventorySpace, int evasion,
			int totalLife, int magic, int totalMagic, String symbolRepresentation, int vision, Movement movementType,
			ArrayList<String> adjectives, int level) {
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
		this.maximumItemsInventory = 6;
		this.level = level;
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
	
	public void putRandomItemInventory() {
		int itemRandom = RandUtil.RandomNumber(0, 7);
		int itemLevel = RandUtil.RandomNumber(this.getLevel(), this.getLevel() + 3);
		boolean isMagic = RandUtil.RandomNumber(0, 2) > 0 ? true : false; 
		Item item = null;
		switch(itemRandom) {
			case 0:
				item = new LongSword(this, null, null, null, itemLevel, isMagic);
			break;
			case 1:
				item = new ShortSword(this, null, null, null, itemLevel, isMagic);
			break;
			case 2:
				item = new NormalArmor(this, null, null, null, itemLevel, isMagic);
			break;
			case 3:
				item = new NormalGloves(this, null, null, null, itemLevel, isMagic);
			break;
			case 4:
				item = new NormalHelmet(this, null, null, null, itemLevel, isMagic);
			break;
			case 5:
				item = new SmallShield(this, null, null, null, itemLevel, isMagic);
			break;
			case 6:
				item = new NormalPants(this, null, null, null, itemLevel, isMagic);
			break;
		}
		
		if (item != null) {
			this.getInventory().add(item);
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
				damage += w.getAttack() + this.getLevel();
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
				defense += w.getDefense() + this.getLevel();
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
		this.setCharacterDead(defender);
		return true;
	}
	
	public Pair<Boolean, ActiveCharacter> weaponAttack() {
		Map map = this.getMap();
		ActiveCharacter monster = map.getMonstersPosition(this).get(0);
		for (int i = 0; i < map.getMonstersPosition(this).size(); i++) {
			monster = map.getMonstersPosition(this).get(i);
			if (!monster.isDead()) {
				monster = map.getMonstersPosition(this).get(i);
				break;
			}
		}
		Pair<Boolean, ActiveCharacter> returnValue = new Pair<Boolean, ActiveCharacter>(this.attack(monster), monster);
		return returnValue;
	}
	
	private void attackWithSpell(ActiveCharacter defender, Spell spell) {
		if (Main.debug){
			System.out.println("Spell attack Done: " + spell.getDamage());
		}
		int defenderLife = defender.getLife() - spell.getDamage();
		defenderLife = defenderLife < 0 ? 0 : defenderLife;
		defender.setLife(defenderLife);
		this.setCharacterDead(defender);
	}
	
	public ArrayList<ActiveCharacter> attackSpell(int itemNumber, ActiveCharacter user) {
		ArrayList<ActiveCharacter> hurtCharacters = new ArrayList<ActiveCharacter>();
		if (this.getSpells().size() > itemNumber) {
			Spell spell = this.getSpells().get(itemNumber);
			Room room = this.getRoom();
			if (this.generateSpell(spell)) {
				ArrayList<Tuple<Integer, Integer>> spellDamagedPositions = spell.getDamagedPositions(this);
				ArrayList<Tuple<Integer, Integer>> charactersPositions = room.getPositionsOfMonsters();
				charactersPositions.add(user.getPosition());
				if (spellDamagedPositions.size() > 0) {
					for (Tuple<Integer, Integer> pos : spellDamagedPositions) {
						if (RandUtil.sameTuple(pos, user.getPosition())) {
							hurtCharacters.add(user);
							this.attackWithSpell(user, spell);
						}
						if (RandUtil.containsTuple(pos, charactersPositions)) {
							for (ActiveCharacter monsterDamaged : room.getMonstersPosition(pos)) {
								hurtCharacters.add(monsterDamaged);
								this.attackWithSpell(monsterDamaged, spell);
							}
						}
					}
				}
			}
		}
		return hurtCharacters;
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
				this.getWeaponsEquipped().add(weapon);
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
		for (int i = 0; i < inventory.size(); i++){
			String name = i + 1 + " - " + inventory.get(i).getName();
			j.print(initPos_j, initPos_i + i, name);
		}
	}
	
	public void _printInventory(WSwingConsoleInterface j, JsonObject rootObjGrammar, JsonObject rootObjWords){
		JsonObject rootObjNames = null;
		rootObjNames = JSONParsing.getElement(rootObjGrammar, "GENERAL").getAsJsonObject();
		
		for (int i = 0; i < this.getInventory().size(); i++){
			if (this.getInventory().get(i).getPrintableSentence().length() <= 0) {
				GrammarsGeneral grammarGeneral = new GrammarsGeneral(rootObjNames);
				GrammarSelectorNP grammarIndividual = new GrammarSelectorNP(grammarGeneral.getRandomGrammar(), rootObjWords, this.getInventory().get(i), "GENERAL");
				this.getInventory().get(i).setPrintableSentence(grammarIndividual.getRandomSentenceTranslated());
			}
			j.print(0, this.getMap().global_fin().x + 1 + i, i + 1 + " - " + this.getInventory().get(i).getPrintableSentence());
		}
	}
	
	public void _printLife(JsonObject rootObjWords, WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String translation = JSONParsing.getTranslationWord("life", "N", rootObjWords);
		String life = translation + ": " + this.getLife() + "/" + this.getTotalLife();
		j.print(initPos_j, initPos_i, life);
	}
	
	public void _printMana(JsonObject rootObjWords, WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String translation = JSONParsing.getTranslationWord("mana", "N", rootObjWords);
		String magic = translation + ": " + this.getMagic() + "/" + this.getTotalMagic();
		j.print(initPos_j, initPos_i, magic);
	}
	
	public void _printName(WSwingConsoleInterface j, int initPos_i, int initPos_j){
		String name = this.getName();
		j.print(initPos_j, initPos_i, name);
	}
	
	public void printMonstersInformation(JsonObject rootObjWords, WSwingConsoleInterface j, int initPos_i, int initPos_j){
		_printName(j, initPos_j, initPos_i);
		Main.countElements++;
		_printLife(rootObjWords, j, initPos_j + 1, initPos_i);
	}
	
	public void _printGroundObjects(WSwingConsoleInterface j, JsonObject rootObjWords){
		if (this.getRoom().getItemsPosition(this.getPosition()).size() > 0) {
			main.Main.countElements += 2;
			j.print(this.getMap().global_fin().y + 1, main.Main.countElements, JSONParsing.getTranslationWord("items", "N", rootObjWords) + ": ");
		}
		for (Item item : this.getRoom().getItemsPosition(this.getPosition())) {
			main.Main.countElements += 1;
			item.printItemsInformation(j, this.getMap().global_fin().y + 1, main.Main.countElements);
		}
	}
	
	public boolean unequipItem(Item item) {
		if (item != null && this.getInventory().size() < this.getMaximumItemsInventory()) {
			if (item instanceof WereableArmor) {
				return this.unEquipArmor((WereableArmor)item);
			} else if (item instanceof WereableWeapon) {
				return this.unEquipWeapon((WereableWeapon)item);
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
		if (this.getActualCarryWeight() + item.getWeight() <= this.getWeight() && this.getInventory().size() < this.getMaximumItemsInventory()){
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
		if (room.isMapPositionHere(pos) && this.getInventory().size() < this.getMaximumItemsInventory()){
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
	
	public boolean useItem(Item item){
		if (item.isWereableItem()){
			if (item.isWereableArmor()){
				return this.equipArmor((WereableArmor)item);
			} else {
				if (item.isWereableWeapon()){
					return this.equipWeapon((WereableWeapon)item);
				}
			}
		} else {
			if (item.isConsumableItem()){
				return this.useConsumable((Consumable)item);
			}
		}
		
		return false;
	}
	
	public Pair<Boolean, String> doTurn(ActiveCharacter user, GrammarIndividual grammarAttack, JsonObject rootObjWords){
		if (this.getRoom().equals(user.getRoom()) && !this.isDead()){
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(this);
			for (int i = 0; i < this.getSpells().size(); i++) {
				Spell spell = this.getSpells().get(i);
					if (RandUtil.containsTuple(user.getPosition(), spell.getDamagedPositions(this))
							&& spell.getManaCost() <= this.getMagic()) {
						names.add(spell);
						names.add(user);
						GrammarSelectorS selector = null;
						try {
							selector = new GrammarSelectorS(grammarAttack, rootObjWords, names, "SPELLS", "SPELLS");
						} catch (JsonIOException | JsonSyntaxException | FileNotFoundException | InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
						boolean hasWorked = false; 
						if (this.attackSpell(i, user).size() > 0) hasWorked = true;
						String message = selector.getRandomSentence();
						if (spell.isHasBeenUsed() && RandUtil.RandomNumber(0, 2) == 1) {
							message += ", " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
						} else {
							spell.setHasBeenUsed(true);
						}
						Pair<Boolean, String> returnValue = new Pair<Boolean, String>(hasWorked, message);
						return returnValue;
					}
			}
			if (this.getWeaponsEquipped().size() > 0 && RandUtil.sameTuple(this.getPosition(), user.getPosition())) {
				names.add(user);
				names.add(this.getWeaponsEquipped().get(0));
				GrammarSelectorS selector = null;
				try {
					selector = new GrammarSelectorS(grammarAttack, rootObjWords, names, "ATTACK", "ATTACK");
				} catch (JsonIOException | JsonSyntaxException | FileNotFoundException | InstantiationException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
				String message = selector.getRandomSentence();
				if (hasAttackedHeroe && RandUtil.RandomNumber(0, 2) == 1) {
					message += ", " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
				} else {
					hasAttackedHeroe = true;
				}
				Pair<Boolean, String> returnValue = new Pair<Boolean, String>(this.attack(user), message);
				return returnValue;
			} else {
				if (this.tirenessTotal <= 0 || this.tirenessCurrent != this.tirenessTotal) {
					Tuple<Integer, Integer> pos = this.movementType.moveCharacter(this, user);
					if (pos != null) {
						this.move(pos);
						this.tirenessCurrent++;
					}
				} else {
					this.tirenessCurrent = 0;
				}
			}
		}
		return new Pair<Boolean, String>(false, "");	
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
	
	public void setNextLevelExperience() {
		int nextExperienceLevel = this.getLevel() * 150;
		this.setNextLevelExperience(nextExperienceLevel); 
	}
	
	public void setNewLevel(int newLevel) {
		this.setLevel(newLevel);
		this.setNextLevelExperience();
	}
	
	public void addNewExperience(int addExperience) {
		if (this.getExperience() + addExperience >= this.getNextLevelExperience()) {
			int experienceToNextLevel = this.getNextLevelExperience() - this.getExperience();
			this.setExperience(addExperience - experienceToNextLevel);
			this.setNewLevel(this.getLevel() + 1);
		} else {
			this.setExperience(this.getExperience() + addExperience);
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

	public Movement getMovementType() {
		return movementType;
	}

	public void setMovementType(Movement movementType) {
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
	
	public ArrayList<String> getAdjectivesIndividual() {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("good");
		return adjectives;
	}
	
	public void setAdjectivesMonster(ActiveCharacter user) {
		ArrayList<String> adjectives = this.getAdjectivesIndividual();
		if (user.getLife() >= 70 && this.getLife() <= 20) {
			adjectives.add("small");
			adjectives.add("scared");
		} else if (user.getLife() <= 30 && this.getLife() >= 50){
			adjectives.add("scary");
			adjectives.add("big");
		}
		this.setAdjectives(adjectives);
	}
	
	public void setAdjectivesUser() {
		ArrayList<String> adjectivesUser = this.getAdjectivesIndividual();
		if (this.getLife() >= 70) {
			adjectivesUser.add("big");
			adjectivesUser.add("brave");
			adjectivesUser.add("glorious");
		} else if (this.getLife() <= 30){
			adjectivesUser.add("small");
			adjectivesUser.add("scared");
		} else {
			adjectivesUser.add("average");
		}
		this.setAdjectives(adjectivesUser);
	}
	

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public int getMaximumItemsInventory() {
		return maximumItemsInventory;
	}

	public void setMaximumItemsInventory(int maximumItemsInventory) {
		this.maximumItemsInventory = maximumItemsInventory;
	}

	public void setVisiblePositions(ArrayList<Tuple<Integer, Integer>> visiblePositions) {
		this.visiblePositions = visiblePositions;
	}

	public int getTirenessTotal() {
		return tirenessTotal;
	}

	public void setTirenessTotal(int tirenessTotal) {
		this.tirenessTotal = tirenessTotal;
	}

	public int getTirenessCurrent() {
		return tirenessCurrent;
	}

	public void setTirenessCurrent(int tirenessCurrent) {
		this.tirenessCurrent = tirenessCurrent;
	}

	public int getExperienceGiven() {
		return experienceGiven;
	}

	public void setExperienceGiven(int experienceGiven) {
		this.experienceGiven = experienceGiven;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextLevelExperience() {
		return nextLevelExperience;
	}

	public void setNextLevelExperience(int nextLevelExperience) {
		this.nextLevelExperience = nextLevelExperience;
	}
	
	public boolean isHasAttackedHeroe() {
		return hasAttackedHeroe;
	}

	public void setHasAttackedHeroe(boolean hasAttackedHeroe) {
		this.hasAttackedHeroe = hasAttackedHeroe;
	}

	public boolean isHasBeenAttackedByHeroe() {
		return hasBeenAttackedByHeroe;
	}

	public void setHasBeenAttackedByHeroe(boolean hasBeenAttackedByHeroe) {
		this.hasBeenAttackedByHeroe = hasBeenAttackedByHeroe;
	}

}
