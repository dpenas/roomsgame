package items.consumables;

import items.Item;

public abstract class Consumable extends Item {
	
	private String effectDescription;
	
	public Consumable(String description, String gender, int weight, int space,
			String effectDescription, Character character) {
		super(description, gender, weight, space, character);
		this.effectDescription = effectDescription;
	}
	
	public String getEffectDescription(){
		return effectDescription;
	}

	
	
}
