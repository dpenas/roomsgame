package characters.active.enemies;

import util.RandUtil;
import util.Tuple;

import java.util.ArrayList;

import characters.active.ActiveCharacter;

public final class Movement {
	
	public static Tuple<Integer, Integer> _randomMove(ActiveCharacter character){
		int newPosition = RandUtil.RandomNumber(0, character.getRoom().getNextPositions(character.getPosition()).size());
		return character.getRoom().getNextPositions(character.getPosition()).get(newPosition);
	}
	
	public static Tuple<Integer, Integer> _followingMoveDumb(ActiveCharacter character, ActiveCharacter user){
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
	
	public static Tuple<Integer, Integer> _followingMove(ActiveCharacter character, ActiveCharacter user){
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

	public static Tuple<Integer, Integer> moveCharacter(ActiveCharacter character, ActiveCharacter user) {
		switch (character.getMovementType()){
			case 1: return _randomMove(character);	
			case 2: return _followingMove(character, user);
			case 3: return _followingMoveDumb(character, user);
		}
		return null;
	}
	
}
