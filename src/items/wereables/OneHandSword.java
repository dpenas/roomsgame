package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class OneHandSword extends WereableWeapon {

	public OneHandSword(String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) throws UnsupportedEncodingException {
		//TODO: Change the 10 (which is the attack of the weapon) to the algorithm based on level
		super("sword", null, description, "m", 
				weight, space, durability, character, new ArrayList<WeaponType>(),
				map, room, position, 10, erosion, true, 0, level, isMagic);
		this.setNameAttributes(this.getOneHandSwordAttributes());
		if (isMagic){
			ArrayList<String> attributes = new ArrayList<String>();
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("sword", this.getNameAttributes()));
		
	}
	
	public ArrayList<String> getOneHandSwordAttributes(){
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("one");
		attributes.add("hand");
		return attributes;
	}
	

}
