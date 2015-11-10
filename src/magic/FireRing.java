package magic;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import util.RandUtil;
import util.Tuple;

public class FireRing extends Spell {

	public FireRing() {
		super(10, 10);
	}

	@Override
	public
	ArrayList<Tuple<Integer, Integer>> getDamagedPositions(ActiveCharacter user) {
		ArrayList<Tuple<Integer, Integer>> positionsToAdd = new ArrayList<Tuple<Integer, Integer>>();
		positionsToAdd.add(new Tuple<Integer, Integer>(-1, 0));
		positionsToAdd.add(new Tuple<Integer, Integer>(-1, -1));
		positionsToAdd.add(new Tuple<Integer, Integer>(0, 1));
		positionsToAdd.add(new Tuple<Integer, Integer>(0, -1));
		positionsToAdd.add(new Tuple<Integer, Integer>(1, 1));
		positionsToAdd.add(new Tuple<Integer, Integer>(1, -1));
		positionsToAdd.add(new Tuple<Integer, Integer>(-1, 1));
		positionsToAdd.add(new Tuple<Integer, Integer>(1, 0));
		ArrayList<Tuple<Integer, Integer>> damagedPositions = new ArrayList<Tuple<Integer, Integer>>();
		for(Tuple<Integer, Integer> tuple : positionsToAdd) {			
			damagedPositions.add(RandUtil.add(user.getPosition(), tuple));
		}
		return damagedPositions;
	}

}
