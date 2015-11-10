package magic;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import util.Tuple;

public abstract class Spell {
	
	int damage;
	int manaCost;
	
	public Spell (int damage, int manaCost) {
		this.damage = damage;
		this.manaCost = manaCost;
	}
	
	public abstract ArrayList<Tuple<Integer, Integer>> getDamagedPositions(ActiveCharacter user);

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

}
