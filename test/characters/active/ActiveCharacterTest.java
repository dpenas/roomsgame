package characters.active;

import static org.junit.Assert.*;

import java.util.ArrayList;

import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import items.ItemEnumerate.WeaponType;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import org.junit.Before;
import org.junit.Test;

import util.Tuple;

public class ActiveCharacterTest {
	
	ActiveCharacter attacker;
	ActiveCharacter defender;
	ActiveCharacter c1;
	ActiveCharacter c2;
	ActiveCharacter c3;
	ActiveCharacter c4;
	ActiveCharacter c5;
	ArrayList<ItemEnumerate.ArmorType> itemTypeArmor;
	ArrayList<ItemEnumerate.ArmorType> itemTypeArmor1;
	ArrayList<ItemEnumerate.ArmorType> itemTypeArmor2;
	ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon;
	ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon2;
	WereableArmor armor1;
	WereableArmor armor2;
	WereableArmor armor3;
	WereableArmor armor4;
	WereableWeapon weapon1;
	WereableWeapon weapon2;
	WereableWeapon weapon3;
	WereableWeapon weapon5;
	WereableWeapon weapon6;
	Tuple<Integer, Integer> position = new Tuple<Integer, Integer>(10, 2);
	
	@Before
	public void setUp(){
		attacker = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 100, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		defender = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 0, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		c1 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		c2 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 95,
				new ArrayList<Item>(), 0, 0);
		c3 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		c4 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		c5 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		itemTypeArmor1 = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor1.add(ArmorType.CHEST);
		itemTypeArmor2 = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor2.add(ArmorType.HANDS);
		itemTypeArmor = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor.add(ArmorType.CHEST);
		itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		itemTypeWeapon2 = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		armor1 = new WereableArmor("", "", 10, 0, itemTypeArmor1, 10, null, 10, null, null, null, 1);
		armor2 = new WereableArmor("", "", 10, 0, itemTypeArmor2, 10, null, 10, null, null, null, 1);
		armor3 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10, null, null, null, 0);
		armor4 = new WereableArmor("", "", 10, 20, itemTypeArmor, 0, null, 10, null, null, null, 0);
		weapon1 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon2, null, null, null, 
				0, 0, true, 0);
		weapon2 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon2, null, null, null, 
				0, 0, true, 0);
		weapon3 = new WereableWeapon("", "", 10, 0, 0, null, new ArrayList<WeaponType>(), 
				null, null, null, 0, 0, true, 0);
		weapon5 = new WereableWeapon("", "", 10, 10, 10, null, itemTypeWeapon, null, null, null, 
				30, 1, true, 0);
		weapon6 = new WereableWeapon("", "", 10, 95, 0, null, itemTypeWeapon2, null, null, null, 
				0, 0, true, 0);
	}

	@Test
	public void testSimpleAttack() {
		if (attacker.attack(defender)){
			assertEquals(defender.getLife(), 100);
		}
		else {
			assertEquals(defender.getLife(), 100);
		}	
	}
	
	@Test
	public void testComplexAttact(){
		attacker.equipWeapon(weapon5);
		assertEquals(weapon5.getCharacter(), attacker);
		defender.equipArmor(armor1);
		assertEquals(armor1.getCharacter(), defender);
		defender.equipArmor(armor2);
		assertEquals(armor2.getCharacter(), defender);
		attacker.attack(defender);
		assertEquals(defender.getLife(), 90);
		assertEquals(attacker.getWeaponsEquipped().get(0).getDurability(), 10);
		assertEquals(defender.getArmorsEquipped().get(0).getDurability(), 9);
	}
	
	@Test
	public void testEquipItems() {
		// Equip character with an armor normally
		c1.equipArmor(armor1);
		assertEquals(armor1.getCharacter(), c1);
		assertEquals(c1.getActualCarryWeight(), 10);
		assertEquals(c1.getArmorsEquipped().get(0), armor1);
		c1.equipArmor(armor3);
		assertEquals(armor3.getCharacter(), null);
		
		// Checking space
		c5.equipArmor(armor4);
		assertEquals(armor4.getCharacter(), c5);
		c5.equipArmor(armor4);
		c5.equipWeapon(weapon6);
		assertEquals(weapon6.getCharacter(), null);
		c5.throwItem(armor4);
		c5.equipWeapon(weapon6);
		assertEquals(weapon6.getCharacter(), c5);
		assertEquals(armor4.getCharacter(), null);
		
		
		// Equip character with an armor when there's not enough weight available
		c2.equipArmor(armor2);
		assertEquals(armor2.getCharacter(), null);
		
		// Equip character with a weapon normally
		c3.equipWeapon(weapon1);
		assertEquals(weapon1.getCharacter(), c3);
		assertEquals(c3.getActualCarryWeight(), 10);
		assertEquals(c3.getWeaponsEquipped().get(0), weapon1);
		c3.equipWeapon(weapon2);
		assertEquals(c3.getWeaponsEquipped().size(), 2);
		
		// Unequip armor
		c1.unEquipArmor(armor1);
		assertEquals(c1.getInventory().get(0), armor1);
		assertEquals(c1.getArmorsEquipped().contains(armor1), false);
		
		// Unequip weapons
		c3.unEquipWeapon(weapon1);
		c3.unEquipWeapon(weapon2);
		assertEquals(c3.getInventory().get(0), weapon1);
		assertEquals(c3.getWeaponsEquipped().contains(weapon1), false);
		
		// Equip weapon again
		c3.equipWeapon(weapon1);
		assertEquals(c3.getWeaponsEquipped().get(0), weapon1);
		c3.throwWeapon(weapon1);
		assertEquals(c3.getWeaponsEquipped().contains(weapon1), false);
		assertEquals(weapon1.getPosition(), position);
		
		// Equip armor again
		
		c1.equipArmor(armor3);
		assertEquals(c1.getArmorsEquipped().get(0), armor3);
		c1.throwItem(armor3);
		assertEquals(c1.getArmorsEquipped().contains(armor3), false);
		assertEquals(armor3.getPosition(), c1.getPosition());
		
		// Equip one hand weapon
		c4.equipWeapon(weapon3);
		assertEquals(weapon3.getCharacter(), c4);
		assertEquals(c4.getFreeWeaponSlots().size(), 1);
		assertEquals(c4.getWeaponsEquipped().get(0), weapon3);
		c4.unEquipWeapon(weapon3);
		assertEquals(weapon3.getWeaponType().size(), 0);
		c4.equipWeapon(weapon3);
		c4.throwWeapon(weapon3);
		assertEquals(weapon3.getWeaponType().size(), 0);
	}
	
}
