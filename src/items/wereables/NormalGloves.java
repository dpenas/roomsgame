package items.wereables;

import items.ItemEnumerate.ArmorType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class NormalGloves extends WereableArmor {
	
ArrayList<String> attributes = new ArrayList<String>();
	
	public NormalGloves (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("gloves", null, "", "m", 
				weight, space, new ArrayList<ArmorType>(), durability, character,
				10, map, room, position, erosion, level, isMagic);
		ArrayList<ArmorType> armorType = new ArrayList<ArmorType>();
		armorType.add(ArmorType.HANDS);
		this.setArmorType(armorType);
		this.setNameAttributes(this.getNormalGlovesAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("gloves", this.getNameAttributes()));
		this.setAttributes(level);
		
	}
	
	public ArrayList<String> getNormalGlovesAttributes(){
		attributes.add("normal");
		return attributes;
	}

}
