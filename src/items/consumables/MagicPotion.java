package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;
import characters.active.ActiveCharacter;

public class MagicPotion extends Consumable{
	
	int magicEffect;
	ArrayList<String> attributes = new ArrayList<String>();

	public MagicPotion(int weight, int space, String effectDescription,
			Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int magicEffect) {
		super("potion", null, null, weight, space, effectDescription,
				character, map, room, position);
		this.magicEffect = magicEffect;
		this.setNameAttributes(this.getLifePotionAttributes());
		this.setName(Translations.getNameItem("potion", this.getNameAttributes()));
	}
	
	public ArrayList<String> getLifePotionAttributes(){
		attributes.add("magic");
		return attributes;
	}

	@Override
	public void consume(ActiveCharacter character) {
		if (character.getMagic() < character.getTotalMagic()){
			if ((character.getMagic() + this.magicEffect) > character.getTotalMagic()){
				character.setMagic(character.getTotalMagic());
			} else {
				character.setMagic(character.getMagic() + magicEffect);
			}
		}
	}

}
