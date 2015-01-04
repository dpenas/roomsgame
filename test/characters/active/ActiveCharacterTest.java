package characters.active;

import static org.junit.Assert.*;

import java.util.ArrayList;

import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import items.ItemEnumerate.WeaponType;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;
import map.Map;
import map.Room;

import org.junit.Test;

import util.Tuple;

public class ActiveCharacterTest {

	@Test
	public void testSimpleAttack() {
		ActiveCharacter attacker = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, null, null, 0, 0, 0);
		ActiveCharacter defender = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 100, 100, 100, null, null, 0, 0, 0);
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
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0);
		ArrayList<ItemEnumerate.ArmorType> itemTypeArmor = new ArrayList<ItemEnumerate.ArmorType>();
		itemTypeArmor.add(ArmorType.CHEST);
		WereableArmor armor1 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10);
		c1.equipArmor(armor1);
		assertEquals(armor1.getCharacter(), c1);
		assertEquals(c1.getActualCarryWeight(), 10);
		assertEquals(c1.getArmorsEquipped().get(0), armor1);
		WereableArmor armor3 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 0);
		c1.equipArmor(armor3);
		assertEquals(armor3.getCharacter(), null);
		
		// Equip character with an armor when there's not enough weight available
		ActiveCharacter c2 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 95);
		WereableArmor armor2 = new WereableArmor("", "", 10, 0, itemTypeArmor, 0, null, 10);
		c2.equipArmor(armor2);
		assertEquals(armor2.getCharacter(), null);
		
		// Equip character with an armor normally
		ActiveCharacter c3 = new ActiveCharacter("", "", "", null, null, null, 40, 
				10, 100, 50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 100, 100, 0);
		ArrayList<ItemEnumerate.WeaponType> itemTypeWeapon = new ArrayList<ItemEnumerate.WeaponType>();
		itemTypeWeapon.add(WeaponType.LEFTHAND);
		WereableWeapon weapon1 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon);
		c3.equipWeapon(weapon1);
		assertEquals(weapon1.getCharacter(), c3);
		assertEquals(c3.getActualCarryWeight(), 10);
		assertEquals(c3.getWeaponsEquipped().get(0), weapon1);
		WereableWeapon weapon2 = new WereableWeapon("", "", 10, 0, 0, null, itemTypeWeapon);
		c3.equipWeapon(weapon2);
		assertEquals(weapon2.getCharacter(), null);
	}
	
}
