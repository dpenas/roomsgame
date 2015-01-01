package items.wereables;

import items.ItemEnumerate;

public class WereableWeapon extends Wereable {
	
	private ItemEnumerate.WeaponType weaponType;

	public WereableWeapon(String description, String gender, int weight,
			int space, int durability, Character character, ItemEnumerate.WeaponType weaponType) {
		super(description, gender, weight, space, durability, character);
		this.weaponType = weaponType;
	}
	
	public ItemEnumerate.WeaponType getWeaponType(){
		return weaponType;
	}
	

}
