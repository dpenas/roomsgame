package items.wereables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import items.ItemEnumerate;
import items.ItemEnumerate.ArmorType;
import characters.Character;

public class WereableArmor extends Wereable {

	private ArrayList<ItemEnumerate.ArmorType> armorType;
	private int defense;

	public WereableArmor(String name, ArrayList<String> nameAttributes, 
			String description, String gender, int weight,
			int space, ArrayList<ItemEnumerate.ArmorType> armorType,
			int durability, Character character, int defense, Map map,
			Room room, Tuple<Integer, Integer> position,
			int erosion, int level, boolean isMagic) {
		super(name, nameAttributes, description, gender, weight, space, durability, character, map,
				room, position, erosion, level, isMagic);
		this.armorType = armorType;
		this.defense = defense;
	}
	
	public void setAttributes(int level){
		int attribute = 0;
		int basic = 2;
		if (this.isMagic()){
			attribute = level + 2;
		}		
		if (this.getArmorType().size() == 0){
			this.setDefense(attribute + basic);
		} else{
			switch (this.getArmorType().get(0)) {
			case HANDS:
				attribute = 3; 
				break;
			case CHEST:
				attribute = 5;
				break;
			case PANTS:
				attribute = 4;
				break;
			case HEAD:
				attribute = 3;
				break;
			default : attribute = 2;
			}
		}
		
		this.setDefense(attribute + level * 2);
	}

	public ArrayList<ArmorType> getArmorType() {
		return armorType;
	}
	
	public void setArmorType(ArrayList<ArmorType> armorType) {
		this.armorType = armorType;
	}

	public int getDefense() {
		return defense;
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
	}

}
