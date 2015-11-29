package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class OneHandSword extends WereableWeapon {
	
	ArrayList<String> attributes = new ArrayList<String>();

	public OneHandSword(String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the 10 (which is the attack of the weapon) to the algorithm based on level
		super("sword", null, description, "m", 
				weight, space, durability, character, new ArrayList<WeaponType>(),
				map, room, position, 10, erosion, true, 0, level, isMagic);
		this.setAdjectives(this.getOneHandSwordAttributes());
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
		this.setName(Translations.getNameItem("sword", this.getAdjectives()));
		this.setAttributes(this.getLevel(), false);
		
	}
	
	public ArrayList<String> getOneHandSwordAttributes(){
		attributes.add("one");
		attributes.add("hand");
		return attributes;
	}
	

}
