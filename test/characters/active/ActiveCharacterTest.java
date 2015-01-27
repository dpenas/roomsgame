package characters.active;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import items.Item;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import items.ItemEnumerate.WeaponType;
import items.consumables.LifeExtendedPotion;
import items.consumables.LifePotion;
import items.consumables.MagicPotion;
import items.wereables.OneHandSword;
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
	ActiveCharacter c6;
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
	OneHandSword oneHandSword;
	Tuple<Integer, Integer> position = new Tuple<Integer, Integer>(10, 2);
	LifePotion lifePotion30;
	MagicPotion magicPotion30;
	LifeExtendedPotion lifeExtendedPotion30;

	public static String language;
	public static String country;
	public static Locale currentLocale;
	public static ResourceBundle messagesWereables;

	@Before
	public void setUp() throws IOException {
		language = new String("en");
		country = new String("US");
		currentLocale = new Locale(language, country);
		messagesWereables = ResourceBundle.getBundle(
				"translations.files.MessagesWereable", currentLocale);
		main.Main.main(null);

		attacker = new ActiveCharacter("", "", "", null, null, null, 40, 10,
				100, 100, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		defender = new ActiveCharacter("", "", "", null, null, null, 40, 10,
				100, 0, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c1 = new ActiveCharacter("", "", "", null, null, position, 40, 10, 100,
				50, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c2 = new ActiveCharacter("", "", "", null, null, null, 40, 10, 100, 50,
				100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 95,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c3 = new ActiveCharacter("", "", "", null, null, position, 40, 10, 100,
				50, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c4 = new ActiveCharacter("", "", "", null, null, position, 40, 10, 100,
				50, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c5 = new ActiveCharacter("", "", "", null, null, position, 40, 10, 100,
				50, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
		c6 = new ActiveCharacter("", "", "", null, null, position, 40, 10, 100,
				50, 100, 100, new ArrayList<WereableWeapon>(),
				new ArrayList<WereableArmor>(), 100, 100, 0,
				new ArrayList<Item>(), 0, 0, 100, 100, 100);
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
		armor1 = new WereableArmor("", null, "", "", 10, 0, itemTypeArmor1, 10,
				null, 10, null, null, null, 1, 1, false);
		armor2 = new WereableArmor("", null, "", "", 10, 0, itemTypeArmor2, 10,
				null, 10, null, null, null, 1, 1, false);
		armor3 = new WereableArmor("", null, "", "", 10, 0, itemTypeArmor, 0,
				null, 10, null, null, null, 0, 1, false);
		armor4 = new WereableArmor("", null, "", "", 10, 20, itemTypeArmor, 0,
				null, 10, null, null, null, 0, 1, false);
		weapon1 = new WereableWeapon("", null, "", "", 10, 0, 0, null,
				itemTypeWeapon2, null, null, null, 0, 0, true, 0, 1, false);
		weapon2 = new WereableWeapon("", null, "", "", 10, 0, 0, null,
				itemTypeWeapon2, null, null, null, 0, 0, true, 0, 1, false);
		weapon3 = new WereableWeapon("", null, "", "", 10, 0, 0, null,
				new ArrayList<WeaponType>(), null, null, null, 0, 0, true, 0,
				1, false);
		weapon5 = new WereableWeapon("", null, "", "", 10, 10, 10, null,
				itemTypeWeapon, null, null, null, 30, 1, true, 0, 1, false);
		weapon6 = new WereableWeapon("", null, "", "", 10, 95, 0, null,
				itemTypeWeapon2, null, null, null, 0, 0, true, 0, 1, false);
		oneHandSword = new OneHandSword("", 0, 0, 0, attacker, null, null,
				position, 0, 0, false);
		lifePotion30 = new LifePotion(0, 10, "", null, null, null, null, 30);
		magicPotion30 = new MagicPotion(0, 10, "", null, null, null, null, 30);
		lifeExtendedPotion30 = new LifeExtendedPotion(0, 10, "", null, null,
				null, null, 30);
	}

	@Test
	public void testSimpleAttack() {
		if (attacker.attack(defender)) {
			assertEquals(defender.getLife(), 100);
		} else {
			assertEquals(defender.getLife(), 100);
		}
	}

	@Test
	public void testConsumables() {
		// Life potion test
		ArrayList<Item> inventory = new ArrayList<Item>();
		inventory.add(lifePotion30);
		c6.setInventory(inventory);
		lifePotion30.setCharacter(c6);
		c6.setLife(80);
		c6.useConsumable(lifePotion30);
		assertEquals(c6.getLife(), 100);
		assertEquals(c6.getTotalLife(), 100);

		inventory.add(lifePotion30);
		c6.setInventory(inventory);
		lifePotion30.setCharacter(c6);
		c6.setLife(60);
		c6.useConsumable(lifePotion30);
		assertEquals(c6.getLife(), 90);
		assertEquals(c6.getTotalLife(), 100);

		// Magic potion test
		inventory.add(magicPotion30);
		c6.setInventory(inventory);
		magicPotion30.setCharacter(c6);
		c6.setMagic(80);
		c6.useConsumable(magicPotion30);
		assertEquals(c6.getMagic(), 100);
		assertEquals(c6.getTotalMagic(), 100);

		inventory.add(magicPotion30);
		c6.setInventory(inventory);
		magicPotion30.setCharacter(c6);
		c6.setMagic(60);
		c6.useConsumable(magicPotion30);
		assertEquals(c6.getMagic(), 90);
		assertEquals(c6.getTotalMagic(), 100);

		// Magic potion test
		inventory.add(magicPotion30);
		c6.setInventory(inventory);
		magicPotion30.setCharacter(c6);
		c6.setMagic(80);
		c6.useConsumable(magicPotion30);
		assertEquals(c6.getMagic(), 100);
		assertEquals(c6.getTotalMagic(), 100);

		inventory.add(magicPotion30);
		c6.setInventory(inventory);
		magicPotion30.setCharacter(c6);
		c6.setMagic(60);
		c6.useConsumable(magicPotion30);
		assertEquals(c6.getMagic(), 90);
		assertEquals(c6.getTotalMagic(), 100);

		// Life Extended potion test
		inventory.add(lifeExtendedPotion30);
		c6.setInventory(inventory);
		lifeExtendedPotion30.setCharacter(c6);
		c6.setLife(100);
		c6.useConsumable(lifeExtendedPotion30);
		assertEquals(c6.getLife(), 130);
		assertEquals(c6.getTotalLife(), 130);
	}

	@Test
	public void testComplexAttact() {
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

		// Equip character with an armor when there's not enough weight
		// available
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

	@Test
	public void oneHandSword() throws UnsupportedEncodingException {
		c4.equipWeapon(oneHandSword);
		assertEquals(oneHandSword.getCharacter(), c4);
	}

}
