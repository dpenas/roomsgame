package magic;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import util.RandUtil;
import util.Tuple;

public class Fireball extends Spell {

	public Fireball() {
		super(5, 5);
		this.setName("fireball");
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("magic");
		this.setAdjectives(adjectives);
	}

	@Override
	public
	ArrayList<Tuple<Integer, Integer>> getDamagedPositions(ActiveCharacter user) {
		ArrayList<Tuple<Integer, Integer>> positionsToAdd = new ArrayList<Tuple<Integer, Integer>>();
		positionsToAdd.add(new Tuple<Integer, Integer>(0, 1));
		positionsToAdd.add(new Tuple<Integer, Integer>(0, -1));
		ArrayList<Tuple<Integer, Integer>> damagedPositions = new ArrayList<Tuple<Integer, Integer>>();
		for(Tuple<Integer, Integer> tuple : positionsToAdd) {			
			damagedPositions.add(RandUtil.add(user.getPosition(), tuple));
		}
		return damagedPositions;
	}

}
