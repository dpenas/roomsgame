package characters.active.enemies;

import util.RandUtil;
import util.Tuple;
import characters.active.ActiveCharacter;

public final class Movement {
	
	public static void _randomMove(ActiveCharacter character){
		int newPosition = RandUtil.RandomNumber(0, character.getRoom().getNextPositions(character.getPosition()).size());
		character.setPosition(character.getRoom().getNextPositions(character.getPosition()).get(newPosition));
	}
	
	public static void _followingMove(ActiveCharacter character, ActiveCharacter user){
		int minimumSeparation = Integer.MAX_VALUE;
		Tuple<Integer, Integer> minimumTuple = character.getPosition();
		for (Tuple<Integer, Integer> tuple : character.getRoom().getNextPositions(character.getPosition())){
			int separationTuples = RandUtil.separationTuples(user.getPosition(), tuple);
			if (separationTuples < minimumSeparation){
				minimumSeparation = separationTuples;
				minimumTuple = tuple;
			}
			
		}
		character.setPosition(minimumTuple);
		
	}

	public static void moveCharacter(ActiveCharacter character, ActiveCharacter user) {
		switch (character.getMovementType()){
			case 1: _randomMove(character);	
				break;
			case 2: _followingMove(character, user);
				break;
		}
	}
	
}
