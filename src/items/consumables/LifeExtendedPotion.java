package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import util.Tuple;
import characters.Character;
import characters.active.ActiveCharacter;

public class LifeExtendedPotion extends Consumable {
	
	int lifeExtendedEffect;
	ArrayList<String> attributes = new ArrayList<String>();

	public LifeExtendedPotion(int weight, int space, String effectDescription,
			Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int lifeExtendedEffect) {
		super("potion", null, null, weight, space, effectDescription,
				character, map, room, position);
		this.lifeExtendedEffect = lifeExtendedEffect;
		this.setAdjectives(this.getLifeExtendedPotionAttributes());
//		this.setName(Translations.getNameItem("potion", this.getAdjectives()));
	}
	
	public ArrayList<String> getLifeExtendedPotionAttributes(){
		attributes.add("life");
		attributes.add("extended");
		return attributes;
	}
	
	public int getLifeExtendedEffect(){
		return this.lifeExtendedEffect;
	}

	@Override
	public void consume(ActiveCharacter character) {
		character.setTotalLife(character.getTotalLife() + this.getLifeExtendedEffect());
		character.setLife(character.getLife() + this.getLifeExtendedEffect());
	}

}
