package util;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

import characters.active.ActiveCharacter;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.PrintableObject;
import grammars.parsing.JSONParsing;
import items.Item;
import net.slashie.util.Pair;

public class ActionHandler {
	
	private HashMap<String, Integer> keysMap;
	private GrammarsGeneral grammarUseItem;
	private GrammarsGeneral grammarPickItem;
	private GrammarsGeneral grammarMissDescription;
	private GrammarsGeneral grammarAdjectiveDescription;
	private GrammarsGeneral grammarGeneralDescription;
	private GrammarsGeneral grammarAttack;
	private JsonObject rootObjWords;
	private ActiveCharacter user;
	
	public ActionHandler(HashMap<String, Integer> keysMap, ActiveCharacter user, 
			GrammarsGeneral grammarUseItem, GrammarsGeneral grammarPickItem,
			GrammarsGeneral grammarMissDescription, GrammarsGeneral grammarAdjectiveDescription,
			GrammarsGeneral grammarAttack, GrammarsGeneral grammarGeneralDescription,
			JsonObject rootObjWords) {
		this.keysMap = keysMap;
		this.grammarUseItem = grammarUseItem;
		this.grammarPickItem = grammarPickItem;
		this.grammarMissDescription = grammarMissDescription;
		this.grammarAdjectiveDescription = grammarAdjectiveDescription;
		this.grammarAttack = grammarAttack;
		this.rootObjWords = rootObjWords;
		this.grammarGeneralDescription = grammarGeneralDescription;
		this.user = user;
	}

	public void _pickItemAction(boolean usePronoun, boolean hasPickedItem){
		Item item = user.pickItem(user.getPosition(), user.getRoom());
		if (user.getInventory().size() <= user.getMaximumItemsInventory() && item != null ) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			if (hasPickedItem) {
				main.Main.useAndWithItem(item);
			} else {
				hasPickedItem = true;
				main.Main.generatePrintMessage(names, grammarPickItem, "PICK", "PICK", usePronoun, false);
			}
			
			main.Main.printEverything(true);
			main.Main.hasChanged = false;
		} else {
			main.Main._messageUnvalid();
		}
	}
	
	public void _attackAction(boolean usePronoun){
		if (user.getWeaponsEquipped().size() <= 0) {
			PrintableObject weapons = new PrintableObject("weapons", "", null, user.getPosition());
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(weapons);
			main.Main.generatePrintMessage(names, grammarGeneralDescription, "NOTHAVE", "NOTHAVE", usePronoun, false);
		} else {
			if (user.getMap().getMonstersPosition(user).size() > 0) {
				Pair<Boolean, ActiveCharacter> monster = user.weaponAttack();
				main.Main.printEverything(true);
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(user);
				names.add(monster.getB());
				names.add(user.getWeaponsEquipped().get(0));
				GrammarIndividual grammarIndividual = grammarAttack.getRandomGrammar();
				String message = main.Main._getMessage(grammarIndividual, names, "ATTACK", "ATTACK", usePronoun, false);
				if (monster.getA()) {
					if (monster.getB().getLife() <= 0) {
						user.addNewExperience(monster.getB().getExperienceGiven());
						monster.getB().setExperienceGiven(0);
						MessageDescriptionsUtil._messageDescriptionDead(monster.getB(), false, grammarAdjectiveDescription);
						main.Main.hasChanged = true;
					} else {
						if (monster.getB().isHasBeenAttackedByHeroe() && RandUtil.RandomNumber(0, 3) == 1) {
							message += " " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
						} else {
							monster.getB().setHasBeenAttackedByHeroe(true);
						}
						// We only print the message if the enemy is alive
						main.Main.printMessage(message);
					}
				} else {
					GrammarIndividual grammarIndividualMiss = grammarMissDescription.getRandomGrammar();
					ArrayList<PrintableObject> namesMiss = new ArrayList<PrintableObject>();
					ArrayList<String> preposition = new ArrayList<String>();
					preposition.add("but");
					user.setPrepositions(preposition);
					namesMiss.add(user);
					String messageAgain = "";
					String messageMiss = ", " + main.Main._getMessage(grammarIndividualMiss, namesMiss, "MISS", "MISS", true, false);
					if (monster.getB().isHasBeenAttackedByHeroe() && RandUtil.RandomNumber(0, 3) == 1) {
						messageAgain += ", " + JSONParsing.getRandomWord("OTHERS", "again", rootObjWords);
					} else {
						monster.getB().setHasBeenAttackedByHeroe(true);
					}
					main.Main.printMessage(message + messageAgain + messageMiss);
				}
	    	}
		}
	}
	
	public void _inventoryAction(int i, boolean usePronoun){
		int itemNumber = i % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(item);
			user.useItem(item);
			main.Main.printEverything(false);
			if (item.isWereableItem()) {
				if (main.Main.hasEquipedItem) {
					main.Main.useAndWithItem(item);
				} else {
					main.Main.generatePrintMessage(names, grammarUseItem, "EQUIP", "EQUIP", usePronoun, false);
					main.Main.hasEquipedItem = true;
				}
			} else {
				main.Main.generatePrintMessage(names, grammarUseItem, "USE", "USE", usePronoun, false);
			}
		}
		main.Main.hasChanged = false;
	}
	
	public void _throwItem(int keyPressed, boolean usePronoun){
		int itemNumber = keyPressed % keysMap.get("item1");
		if (itemNumber + 1 <= user.getInventory().size()) {
			Item item = user.getInventory().get(itemNumber);
			if (user.throwItem(item)) {
				ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
				names.add(user);
				names.add(item);
				if (main.Main.hasThrownItem) {
					main.Main.useAndWithItem(item);
				} else {
					main.Main.generatePrintMessage(names, grammarPickItem, "THROW", "THROW", usePronoun, false);
					main.Main.hasThrownItem = true;
				}
				main.Main.printEverything(true);
			}
			main.Main.hasChanged = false;
		}
	}
	
	public void _spellAction(int keyPressed, boolean usePronoun){
		int itemNumber = keyPressed % keysMap.get("item1");
		for (ActiveCharacter monsterAffected : user.attackSpell(itemNumber, user)) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(user.getSpells().get(itemNumber));
			names.add(monsterAffected);
			main.Main.generatePrintMessage(names, grammarAttack, "SPELLS", "SPELLS", usePronoun, false);
			if (monsterAffected.isDead()) {
				user.addNewExperience(monsterAffected.getExperienceGiven());
				monsterAffected.setExperienceGiven(0);
				MessageDescriptionsUtil._messageDescriptionDead(monsterAffected, false, grammarAdjectiveDescription);
			}
		}
	}

}
