package items.wereables;

import items.ItemEnumerate.ArmorType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;

public class NormalGloves extends WereableArmor {
	
ArrayList<String> attributes = new ArrayList<String>();
	
	public NormalGloves (Character character, Map map, Room room, Tuple<Integer, Integer> position,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("gloves", null, "", "m", 
				3, 3, new ArrayList<ArmorType>(), 100, character,
				2+level, map, room, position, 0, level, isMagic);
		ArrayList<ArmorType> armorType = new ArrayList<ArmorType>();
		armorType.add(ArmorType.HANDS);
		this.setArmorType(armorType);
		this.setAdjectives(this.getNormalGlovesAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
//		this.setName(Translations.getNameItem("gloves", this.getAdjectives()));
		this.setAttributes(level);
		
	}
	
	public ArrayList<String> getNormalGlovesAttributes(){
		attributes.add("normal");
		return attributes;
	}

}
