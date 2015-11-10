package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;
import characters.active.ActiveCharacter;

public class LifePotion extends Consumable {
	
	int lifeEffect = 30;
	ArrayList<String> attributes = new ArrayList<String>();

	public LifePotion(Character character, Map map, Room room,
			Tuple<Integer, Integer> position) {
		super("potion", null, null, 5, 1, "Cures the user",
				character, map, room, position);
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
