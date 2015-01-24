package items.wereables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;
import items.ItemEnumerate;

public class WereableWeapon extends Wereable {

	private ArrayList<ItemEnumerate.WeaponType> weaponType;
	private int attack;
	private int defense;
	private boolean isSingleHand; // If it needs one or two hands to be used

	public WereableWeapon(String name, ArrayList<String> nameAttributes, 
			String description, String gender, int weight,
			int space, int durability, Character character,
			ArrayList<ItemEnumerate.WeaponType> weaponType, Map map, Room room,
			Tuple<Integer, Integer> position, int attack, int erosion, boolean isSingleHand,
			int defense, int level, boolean isMagic) {
		super(name, nameAttributes, description, gender, weight, space, durability, character, map,
				room, position, erosion, level, isMagic);
		this.attack = attack;
		this.defense = defense;
		this.weaponType = weaponType;
		this.isSingleHand = isSingleHand;
	}
	
	public void setAttributes(int level, boolean isShield){
		int attribute = 0;
		if (this.isMagic()){
			attribute = level + 2;
		}
		if (isShield){
			this.setAttack(0);
			this.setDefense(attribute + 8 + level * 2);
		} else {
			if (this.getWeaponType().size() == 2){
				this.setAttack(attribute + 15 + level * 2);
				this.setDefense(0);
			} else {
				this.setAttack(attribute + 5 + level * 2);
				this.setDefense(0);
			}
		}	
	}
	
	public ArrayList<ItemEnumerate.WeaponType> getWeaponType() {
		return weaponType;
	}

	public int getAttack() {
		return this.attack;
	}
	
	public boolean getIsSingleHand(){
		return this.isSingleHand;
	}

	public void setWeaponType(ArrayList<ItemEnumerate.WeaponType> weaponType) {
		this.weaponType = weaponType;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setSingleHand(boolean isSingleHand) {
		this.isSingleHand = isSingleHand;
	}

}
