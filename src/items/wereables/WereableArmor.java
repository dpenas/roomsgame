package items.wereables;

import items.ItemEnumerate;

public abstract class WereableArmor extends Wereable {

	private ItemEnumerate.ArmorType armorType;
	private int defense;
	
	public WereableArmor(String description, String gender, int weight,
			int space, ItemEnumerate.ArmorType armorType, int durability,
			Character character, int defense) {
		super(description, gender, weight, space, durability, character);
		this.armorType = armorType;
		this.defense = defense;
	}
	
	public ItemEnumerate.ArmorType getArmorType(){
		return armorType;
	}
	
	public int getDefense() {
		return defense;
	}

}
