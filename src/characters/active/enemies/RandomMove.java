package characters.active.enemies;

import characters.active.ActiveCharacter;
import util.RandUtil;
import util.Tuple;

public class RandomMove implements Movement {

	public Tuple<Integer, Integer> moveCharacter(ActiveCharacter character, ActiveCharacter user) {
		int newPosition = RandUtil.RandomNumber(0, character.getRoom().getNextPositions(character.getPosition()).size());
		return character.getRoom().getNextPositions(character.getPosition()).get(newPosition);
	}
}
