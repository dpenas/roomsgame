package items.consumables;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;
import characters.active.ActiveCharacter;

public class MagicPotion extends Consumable{
	
	int magicEffect = 10;
	ArrayList<String> attributes = new ArrayList<String>();

	public MagicPotion(Character character, Map map, Room room,
			Tuple<Integer, Integer> position) {
		super("potion", null, null, 2, 1, "", character, map, room, position);
		this.setAdjectives(this.getMagicPotionAttributes());
//		this.setName(Translations.getNameItem("potion", this.getAdjectives()));
	}
	
	public ArrayList<String> getMagicPotionAttributes(){
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
