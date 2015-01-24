package items.wereables;

import items.ItemEnumerate.ArmorType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class NormalChest extends WereableArmor {
	
ArrayList<String> attributes = new ArrayList<String>();
	
	public NormalChest (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("chest", null, "", "m", 
				weight, space, new ArrayList<ArmorType>(), durability, character,
				10, map, room, position, erosion, level, isMagic);
		ArrayList<ArmorType> armorType = new ArrayList<ArmorType>();
		armorType.add(ArmorType.CHEST);
		this.setArmorType(armorType);
		this.setNameAttributes(this.getNormalChestAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("chest", this.getNameAttributes()));
		this.setAttributes(level);
		
	}
	
	public ArrayList<String> getNormalChestAttributes(){
		attributes.add("normal");
		return attributes;
	}

}
