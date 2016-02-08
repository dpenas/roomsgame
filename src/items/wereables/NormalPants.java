package items.wereables;

import items.ItemEnumerate.ArmorType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;

public class NormalPants extends WereableArmor {
	
ArrayList<String> attributes = new ArrayList<String>();
	
	public NormalPants (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("pants", null, "", "m", 
				weight, space, new ArrayList<ArmorType>(), durability, character,
				10, map, room, position, erosion, level, isMagic);
		ArrayList<ArmorType> armorType = new ArrayList<ArmorType>();
		armorType.add(ArmorType.PANTS);
		this.setArmorType(armorType);
		this.setAdjectives(this.getNormalPantsAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getAdjectives();
			attributes.add("magic");
			this.setAdjectives(attributes);
		}
//		this.setName(Translations.getNameItem("pants", this.getAdjectives()));
		this.setAttributes(level);
		
	}
	
	public ArrayList<String> getNormalPantsAttributes(){
		attributes.add("normal");
		return attributes;
	}

}
