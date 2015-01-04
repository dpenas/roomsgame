package items.wereables;

import items.Item;
import characters.Character;

public abstract class Wereable extends Item {
	
	private int durability;
	private boolean isWereable;
	private int erosion; // Durability points that the weapon loses every time
	
	public Wereable(String description, String gender, int weight, int space, int durability, Character character) {
		super(description, gender, weight, space, character);
		this.durability = durability;
		this.isWereable = (this.durability > 0) ? true : false;
	}
	
	public boolean getIsWereable(){
		return isWereable;
	}
	
	public int getDurability(){
		return durability;
	}
	
	public int getErosion(){
		return erosion;
	}

}
