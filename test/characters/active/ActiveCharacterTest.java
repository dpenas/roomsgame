package characters.active;

import static org.junit.Assert.*;
import map.Map;
import map.Room;

import org.junit.Test;

import util.Tuple;

public class ActiveCharacterTest {

	@Test
	public void test() {
		ActiveCharacter attacker = new ActiveCharacter("", "", "", null, null, null, 40, 10, 100, 50, 100, 100, null, null);
		ActiveCharacter defender = new ActiveCharacter("", "", "", null, null, null, 40, 10, 100, 100, 100, 100, null, null);
		if (attacker.attack(defender)){
			assertEquals(defender.getLife(), 70);
		}
		else {
			assertEquals(defender.getLife(), 100);
		}
	}
	
}
