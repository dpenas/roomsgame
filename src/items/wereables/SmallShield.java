package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class SmallShield extends WereableWeapon {

ArrayList<String> attributes = new ArrayList<String>();
	
	public SmallShield (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("shields", null, description, "m", 
				weight, space, durability, character, 
				new ArrayList<WeaponType>(),
				map, room, position, 0, erosion, true, 10, level, isMagic);
		this.setAdjectives(this.getSmallShieldAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
//		this.setName(Translations.getNameItem("shield", this.getAdjectives()));
		this.setAttributes(this.getLevel(), true);
		
	}
	
	public ArrayList<String> getSmallShieldAttributes(){
		attributes.add("small");
		return attributes;
	}
	
}
