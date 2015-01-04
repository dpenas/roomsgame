package items.wereables;

import java.util.ArrayList;

import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import characters.Character;

public class WereableArmor extends Wereable {

	private ArrayList<ItemEnumerate.ArmorType> armorType;
	private int defense;
	
	public WereableArmor(String description, String gender, int weight,
			int space, ArrayList<ItemEnumerate.ArmorType> armorType, int durability,
			Character character, int defense) {
		super(description, gender, weight, space, durability, character);
		this.armorType = armorType;
		this.defense = defense;
	}
	
	public ArrayList<ArmorType> getArmorType(){
		return armorType;
	}
	
	public int getDefense() {
		return defense;
	}

}
