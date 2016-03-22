package characters.active.enemies;

import util.Tuple;

import characters.active.ActiveCharacter;

public interface Movement {
	
	public Tuple<Integer, Integer> moveCharacter(ActiveCharacter character, ActiveCharacter user);
}
