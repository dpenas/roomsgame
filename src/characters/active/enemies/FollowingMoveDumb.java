package characters.active.enemies;

import characters.active.ActiveCharacter;
import util.RandUtil;
import util.Tuple;

public class FollowingMoveDumb implements Movement {

	public Tuple<Integer, Integer> moveCharacter(ActiveCharacter character, ActiveCharacter user) {
		int minimumSeparation = Integer.MAX_VALUE;
		Tuple<Integer, Integer> minimumTuple = character.getPosition();
		for (Tuple<Integer, Integer> tuple : character.getRoom().getNextPositions(character.getPosition())){
			int separationTuples = RandUtil.separationTuples(user.getPosition(), tuple);
			if (separationTuples < minimumSeparation){
				minimumSeparation = separationTuples;
				minimumTuple = tuple;
			}
		}
		return minimumTuple;
	}
	
}
