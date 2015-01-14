package characters.active;

import static org.junit.Assert.*;

import java.util.ArrayList;

import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import items.ItemEnumerate.WeaponType;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import org.junit.Test;

import util.Tuple;

public class ActiveCharacterTest {

	@Test
	public void testSimpleAttack() {
		ActiveCharacter attacker = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 
				0, 0, 0, new ArrayList<Item>(), 0, 0);
		ActiveCharacter defender = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 100, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 
				0, 0, 0, new ArrayList<Item>(), 0, 0);
		if (attacker.attack(defender)){
			assertEquals(defender.getLife(), 70);
		}
		else {
			assertEquals(defender.getLife(), 100);
		}	
	}
	
	@Test
	public void testComplexAttact(){
		ActiveCharacter attacker = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 100, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		ActiveCharacter defender = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 0, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		ArrayList<ItemEnumerate.ArmorType> itemTypeArmor1 = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor1.add(ArmorType.CHEST);
		ArrayList<ItemEnumerate.ArmorType> itemTypeArmor2 = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor2.add(ArmorType.HANDS);
		WereableArmor armor1 = new WereableArmor("", "", 10, 0, itemTypeArmor1, 10, null, 10, null, null, null, 1);
		WereableArmor armor2 = new WereableArmor("", "", 10, 0, itemTypeArmor2, 10, null, 10, null, null, null, 1);
		ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		WereableWeapon weapon5 = new WereableWeapon("", "", 10, 10, 10, null, itemTypeWeapon, null, null, null, 
				30, 1, true, 0);
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
		Tuple<Integer, Integer> position = new Tuple<Integer, Integer>(10, 2);
		// Equip character with an armor normally
		ActiveCharacter c1 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		ArrayList<ItemEnumerate.ArmorType> itemTypeArmor = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor.add(ArmorType.CHEST);
		WereableArmor armor1 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10, null, null, null, 0);
		c1.equipArmor(armor1);
		assertEquals(armor1.getCharacter(), c1);
		assertEquals(c1.getActualCarryWeight(), 10);
		assertEquals(c1.getArmorsEquipped().get(0), armor1);
		WereableArmor armor3 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 0, null, null, null, 0);
		c1.equipArmor(armor3);
		assertEquals(armor3.getCharacter(), null);
		
		// Checking space
		ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		ActiveCharacter c5 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		WereableArmor armor4 = new WereableArmor("", "", 10, 20, itemTypeArmor, 0, null, 10, null, null, null, 0);
		c5.equipArmor(armor4);
		assertEquals(armor4.getCharacter(), c5);
		c5.equipArmor(armor4);
		WereableWeapon weapon5 = new WereableWeapon("", "", 10, 95, 0, null, itemTypeWeapon, null, null, null, 
				0, 0, true, 0);
		c5.equipWeapon(weapon5);
		assertEquals(weapon5.getCharacter(), null);
		c5.throwItem(armor4);
		c5.equipWeapon(weapon5);
		assertEquals(weapon5.getCharacter(), c5);
		assertEquals(armor4.getCharacter(), null);
		
		
		// Equip character with an armor when there's not enough weight available
		ActiveCharacter c2 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 95,
				new ArrayList<Item>(), 0, 0);
		WereableArmor armor2 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10, null, null, null, 0);
		c2.equipArmor(armor2);
		assertEquals(armor2.getCharacter(), null);
		
		// Equip character with a weapon normally
		ActiveCharacter c3 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		WereableWeapon weapon1 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon, null, null, null, 
				0, 0, true, 0);
		c3.equipWeapon(weapon1);
		assertEquals(weapon1.getCharacter(), c3);
		assertEquals(c3.getActualCarryWeight(), 10);
		assertEquals(c3.getWeaponsEquipped().get(0), weapon1);
		WereableWeapon weapon2 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon, null, null, null, 
				0, 0, true, 0);
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
		
		c1.equipArmor(armor1);
		assertEquals(c1.getArmorsEquipped().get(0), armor1);
		c1.throwItem(armor1);
		assertEquals(c1.getArmorsEquipped().contains(armor1), false);
		assertEquals(armor1.getPosition(), c1.getPosition());
		
		// Equip one hand weapon
		ActiveCharacter c4 = new ActiveCharacter("", "", "", null, null, position, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0);
		WereableWeapon weapon3 = new WereableWeapon("", "", 10, 0, 0, null, new ArrayList<WeaponType>(), 
				null, null, null, 0, 0, true, 0);
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
