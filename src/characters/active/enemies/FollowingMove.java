package characters.active.enemies;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import util.RandUtil;
import util.Tuple;

public class FollowingMove implements Movement {

	public Tuple<Integer, Integer> moveCharacter(ActiveCharacter character, ActiveCharacter user) {
		int minimumSeparation = Integer.MAX_VALUE;
		Tuple<Integer, Integer> characterPos = character.getPosition();
		ArrayList<Tuple<Integer, Integer>> allPosibilities = character.getRoom().getNextPositions(character.getPosition());
		ArrayList<Tuple<Integer, Integer>> newPosibilities = new ArrayList<Tuple<Integer, Integer>>();
		ArrayList<Tuple<Integer, Integer>> columns = character.getRoom().getInsidecolumns();
		for (Tuple<Integer, Integer> tuple : allPosibilities){
			if (!RandUtil.containsTuple(tuple, columns)) {
				newPosibilities.add(tuple);
			}
		}
		Tuple<Integer, Integer> position = character.getPosition();
		for (Tuple<Integer, Integer> tuple : newPosibilities){
			int separationTuples = RandUtil.separationTuples(user.getPosition(), tuple);
			if (separationTuples <= minimumSeparation){
				minimumSeparation = separationTuples;
				characterPos = tuple;
			}
		}
		if (RandUtil.sameTuple(position, characterPos)) {
			for (Tuple<Integer, Integer> tuple : newPosibilities){
				if (tuple.x != characterPos.x) {
					return tuple;
				}
			}
		}
		return characterPos;
	}
	
}
