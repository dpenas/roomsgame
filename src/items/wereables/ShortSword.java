package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;

public class ShortSword extends WereableWeapon {
	
	ArrayList<String> attributes = new ArrayList<String>();

	public ShortSword(Character character, Map map, Room room, Tuple<Integer, Integer> position,
			int level, boolean isMagic) {
		super("sword", null, "", "m", 
				3, 3, 100, character, new ArrayList<WeaponType>(),
				map, room, position, 4+level, 0, true, 0, level, isMagic);
		this.setAdjectives(this.getOneHandSwordAttributes());
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
		this.setAttributes(this.getLevel(), false);
		
	}
	
	public ArrayList<String> getOneHandSwordAttributes(){
		attributes.add("short");
		return attributes;
	}
	

}
