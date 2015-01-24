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
		super("shield", null, description, "m", 
				weight, space, durability, character, 
				new ArrayList<WeaponType>(),
				map, room, position, 0, erosion, true, 10, level, isMagic);
		this.setNameAttributes(this.getSmallShieldAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("shield", this.getNameAttributes()));
		this.setAttributes(this.getLevel(), true);
		
	}
	
	public ArrayList<String> getSmallShieldAttributes(){
		attributes.add("small");
		return attributes;
	}
	
}
