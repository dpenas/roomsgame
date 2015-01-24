package items.wereables;

import items.ItemEnumerate.ArmorType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class NormalHelmet extends WereableArmor {
	
ArrayList<String> attributes = new ArrayList<String>();
	
	public NormalHelmet (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the defense (the 10) to the algorithm based on level
		super("shield", null, "", "m", 
				weight, space, new ArrayList<ArmorType>(), durability, character,
				10, map, room, position, erosion, level, isMagic);
		ArrayList<ArmorType> armorType = new ArrayList<ArmorType>();
		armorType.add(ArmorType.HEAD);
		this.setArmorType(armorType);
		this.setNameAttributes(this.getSmallShieldAttributes());
		//TODO: Change this to be a function
		if (isMagic){
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("helmet", this.getNameAttributes()));
		
	}
	
	public ArrayList<String> getSmallShieldAttributes(){
		attributes.add("normal");
		return attributes;
	}

}
