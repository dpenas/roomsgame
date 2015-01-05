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

public class ActiveCharacterTest {

	@Test
	public void testSimpleAttack() {
		ActiveCharacter attacker = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, null, null, 0, 0, 0, new ArrayList<Item>());
		ActiveCharacter defender = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 100, 100, 100, null, null, 0, 0, 0, new ArrayList<Item>());
		if (attacker.attack(defender)){
			assertEquals(defender.getLife(), 70);
		}
		else {
			assertEquals(defender.getLife(), 100);
		}
	}
	
	@Test
	public void testEquipItems() {
		// Equip character with an armor normally
		ActiveCharacter c1 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>());
		ArrayList<ItemEnumerate.ArmorType> itemTypeArmor = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor.add(ArmorType.CHEST);
		WereableArmor armor1 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10, null, null, null);
		c1.equipArmor(armor1);
		assertEquals(armor1.getCharacter(), c1);
		assertEquals(c1.getActualCarryWeight(), 10);
		assertEquals(c1.getArmorsEquipped().get(0), armor1);
		WereableArmor armor3 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 0, null, null, null);
		c1.equipArmor(armor3);
		assertEquals(armor3.getCharacter(), null);
		
		// Equip character with an armor when there's not enough weight available
		ActiveCharacter c2 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 95,
				new ArrayList<Item>());
		WereableArmor armor2 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10, null, null, null);
		c2.equipArmor(armor2);
		assertEquals(armor2.getCharacter(), null);
		
		// Equip character with an armor normally
		ActiveCharacter c3 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>());
		ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		WereableWeapon weapon1 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon, null, null, null);
		c3.equipWeapon(weapon1);
		assertEquals(weapon1.getCharacter(), c3);
		assertEquals(c3.getActualCarryWeight(), 10);
		assertEquals(c3.getWeaponsEquipped().get(0), weapon1);
		WereableWeapon weapon2 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon, null, null, null);
		c3.equipWeapon(weapon2);
		assertEquals(weapon2.getCharacter(), null);
		
		// Unequip armor
		c1.unEquipArmor(armor1);
		assertEquals(c1.getInventory().get(0), armor1);
		assertEquals(c1.getArmorsEquipped().contains(armor1), false);
		
		// Unequip weapon
		c3.unEquipWeapon(weapon1);
		assertEquals(c3.getInventory().get(0), weapon1);
		assertEquals(c3.getWeaponsEquipped().contains(weapon1), false);
	}
	
}
