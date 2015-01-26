package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;
import characters.active.ActiveCharacter;

public class LifePotion extends Consumable {
	
	int lifeEffect;
	ArrayList<String> attributes = new ArrayList<String>();

	public LifePotion(int weight, int space, String effectDescription,
			Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int lifeEffect) {
		super("potion", null, null, weight, space, effectDescription,
				character, map, room, position);
		this.lifeEffect = lifeEffect;
		this.setNameAttributes(this.getLifePotionAttributes());
		this.setName(Translations.getNameItem("potion", this.getNameAttributes()));
	}
	
	public ArrayList<String> getLifePotionAttributes(){
		attributes.add("life");
		return attributes;
	}

	@Override
	public void consume(ActiveCharacter character) {
		if (character.getLife() < character.getTotalLife()){
			if ((character.getLife() + this.lifeEffect) > character.getTotalLife()){
				character.setLife(character.getTotalLife());
			} else {
				character.setLife(character.getLife() + lifeEffect);
			}
		}
	}

}
