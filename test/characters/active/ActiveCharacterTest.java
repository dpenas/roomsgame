package characters.active;

import static org.junit.Assert.*;
import map.Map;
import map.Room;

import org.junit.Test;

import util.Tuple;

public class ActiveCharacterTest {

	@Test
	public void test() {
		ActiveCharacter attacker = new ActiveCharacter("", "", true, null, null, null, 40, 10, 100, 50);
		ActiveCharacter defender = new ActiveCharacter("", "", true, null, null, null, 40, 10, 100, 100);
		if (attacker.attack(defender)){
			assertEquals(defender.getLife(), 70);
		}
		else {
			assertEquals(defender.getLife(), 100);
		}
	}
	
}
