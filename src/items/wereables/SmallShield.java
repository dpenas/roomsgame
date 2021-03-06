package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;

public class SmallShield extends WereableWeapon {

ArrayList<String> attributes = new ArrayList<String>();
	
	public SmallShield (Character character, Map map, Room room, Tuple<Integer, Integer> position,
			int level, boolean isMagic) {
		super("shield", null, "", "m",
				3, 3, 100, character, 
				new ArrayList<WeaponType>(),
				map, room, position, 0, 0, true, 1+level, level, isMagic);
		this.setAdjectives(this.getSmallShieldAttributes());
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
		this.setAttributes(this.getLevel(), true);
		
	}
	
	public ArrayList<String> getSmallShieldAttributes(){
		attributes.add("small");
		return attributes;
	}
	
}
