package util;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import characters.active.ActiveCharacter;
import grammars.grammars.GrammarIndividual;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.GrammarsOperational;
import grammars.grammars.PrintableObject;
import grammars.grammars.WordsGrammar;
import grammars.parsing.JSONParsing;
import items.Item;
import magic.Spell;
import map.Door;

public class MessageDescriptionsUtil {
	public static String _messageDescriptionCharacterWears(ActiveCharacter user, Item item, String usePreposition, String bodyPartString,
			boolean usePronoun, GrammarsGeneral grammarDescribeCharacterWears) {
		ArrayList<String> preposition = new ArrayList<String>();
		preposition.add(usePreposition);
		PrintableObject bodyPart = new PrintableObject(bodyPartString, "", null, null);
		bodyPart.setPrepositions(preposition);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(user);
		names.add(item);
		names.add(bodyPart);
		GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
		return main.Main._getMessage(grammarIndividual, names, "DESCWEARS", "DESCWEARS", usePronoun, false);
	}
	
	public static void _messageDescriptionInventory(ActiveCharacter user, GrammarsGeneral grammarDescribeItem) {
		if (user.getInventory().size() > 0) {
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			for (Item item : user.getInventory()) {
				names.add(item);
			}
			main.Main.generatePrintMessage(names, grammarDescribeItem, "DESCITEM", "DESCITEM", false, false);
		}
	}
	
	public static String _messageDescriptionEnvironment(PrintableObject object, String directions, GrammarsGeneral grammarDescribeEnvironment) {
		ArrayList<String> adjectives = new ArrayList<String>();
		PrintableObject direction = new PrintableObject(directions, "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(object);
		names.add(direction);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironment.getRandomGrammar();
		return main.Main._getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	public static void _messageDescriptionEnvironment(ActiveCharacter user, boolean isNumericDescription, 
			GrammarsGeneral grammarDescribeEnvironment, GrammarsGeneral grammarDescribeEnvironmentSimple) {
		String message = "";
		ArrayList<Door> alreadyPrintedDoors = new ArrayList<Door>();
		for (Tuple<Integer, Integer> pos : user.getVisiblePositions()) {
			for (ActiveCharacter enemy : user.getRoom().getMonstersPosition(pos)) {
				if (enemy.isDead()) {
					ArrayList<String> adjectives = new ArrayList<String>();
					adjectives.add("dead");
					enemy.setAdjectives(adjectives);
				}
				if (isNumericDescription) {
					String messagePosition = enemy.getPositionDirectionsWithNumbers(user.getPosition()).getA();
					String messageNumbers = enemy.getPositionDirectionsWithNumbers(user.getPosition()).getB();
					message += MessageDescriptionsUtil._messageDescriptionEnvironment(enemy, messagePosition, grammarDescribeEnvironment) + messageNumbers;
				} else {
					message += MessageDescriptionsUtil._messageDescriptionEnvironment(enemy, enemy.getPositionDirections(user.getPosition()), grammarDescribeEnvironment);
				}
				message += ". ";
			}
			for (Item item : user.getRoom().getItemsPosition(pos)) {
				if (isNumericDescription) {
					String messagePosition = item.getPositionDirectionsWithNumbers(user.getPosition()).getA();
					String messageNumbers = item.getPositionDirectionsWithNumbers(user.getPosition()).getB();
					message += MessageDescriptionsUtil._messageDescriptionEnvironment(item, messagePosition, grammarDescribeEnvironment) + messageNumbers;
				} else {
					message += MessageDescriptionsUtil._messageDescriptionEnvironment(item, item.getPositionDirections(user.getPosition()), grammarDescribeEnvironment);
				}
				message += ". ";
			}
			for (Door door : user.getRoom().getDoorsPosition(pos)) {
				Tuple<Integer, Integer> position = door.getPositionRoom(user);
				if (position != null && !alreadyPrintedDoors.contains(door)) {
					PrintableObject doorPrintable = new PrintableObject("door", "", door.getAdjectives(), position);
					if (isNumericDescription) {
						String messagePosition = doorPrintable.getPositionDirectionsWithNumbers(user.getPosition()).getA();
						String messageNumbers = doorPrintable.getPositionDirectionsWithNumbers(user.getPosition()).getB();
						message += MessageDescriptionsUtil._messageDescriptionEnvironment(doorPrintable, messagePosition, grammarDescribeEnvironment) + messageNumbers;
					} else {
						message += MessageDescriptionsUtil._messageDescriptionEnvironment(doorPrintable, doorPrintable.getPositionDirections(user.getPosition()), grammarDescribeEnvironment);
					}
					alreadyPrintedDoors.add(door);
					message += ". ";
				}
			}
			for (Tuple<Integer, Integer> portal : user.getRoom().getPortalsPosition(pos)) {
				if (portal != null) {
					PrintableObject portablePrintable = new PrintableObject("portal", "", null, portal);
					if (isNumericDescription) {
						String messagePosition = portablePrintable.getPositionDirectionsWithNumbers(user.getPosition()).getA();
						String messageNumbers = portablePrintable.getPositionDirectionsWithNumbers(user.getPosition()).getB();
						message += _messageSimpleEnvironment(portablePrintable, messagePosition, grammarDescribeEnvironmentSimple) + messageNumbers;
					} else {
						message += _messageSimpleEnvironment(portablePrintable, portablePrintable.getPositionDirections(user.getPosition()), 
								grammarDescribeEnvironmentSimple);
					}
					message += ". ";
				}
			}
		}
		main.Main.printMessage(message);
	}
	
	private static String _messageSimpleEnvironment(PrintableObject object, String directions, GrammarsGeneral grammarDescribeEnvironmentSimple) {
		PrintableObject direction = new PrintableObject(directions, "", null, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(object);
		names.add(direction);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
		return main.Main._getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	public static void _messageDescriptionWalkablePositions(ActiveCharacter user, JsonObject rootObjWords) {
		String message = "";
		JsonObject others = JSONParsing.getElement(rootObjWords, "OTHERS").getAsJsonObject();
		JsonArray reachablePositionsMessage = JSONParsing.getElement(others, "reachable positions").getAsJsonArray();
		String translationreachablePositionsMessage = JSONParsing.getElement(reachablePositionsMessage, "translation");
		message += translationreachablePositionsMessage + ": ";
		int count = 0;
		ArrayList<String> reachablePositions = user.getRoom().printableReachablePositionsCharacter(user);
		JsonObject names = JSONParsing.getElement(rootObjWords, "N").getAsJsonObject();
		JsonArray reachablePositionMessage;
		for (String reachablePosition : reachablePositions) {
			reachablePositionMessage = JSONParsing.getElement(names, reachablePosition).getAsJsonArray();
			message += JSONParsing.getElement(reachablePositionMessage, "translation");
			if (count != reachablePositions.size() - 1) {
				message += ", ";
			}
			count++;
		}
		main.Main.printMessage(message);
	}
	
	public static String _messageDescriptionCharacterWearsHands(ActiveCharacter user, GrammarsGeneral grammarDescribeCharacterWears, 
			boolean usePronoun) {
		String message = "";
		if (user.getWeaponsEquipped().size() > 0) {
			Item weapon = user.getWeaponsEquipped().get(0);
			ArrayList<String> preposition = new ArrayList<String>();
			preposition.add("in");
			PrintableObject hand = new PrintableObject("hand", "", null, null);
			hand.setPrepositions(preposition);
			ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
			names.add(user);
			names.add(weapon);
			names.add(hand);
			GrammarIndividual grammarIndividual = grammarDescribeCharacterWears.getRandomGrammar();
			message += main.Main._getMessage(grammarIndividual, names, "DESCWEARS", "DESCWEARS", usePronoun, false);
		}
		
		return message;
	}
	
	public static String _messageDescriptionLife(ActiveCharacter character, boolean usePronoun, boolean isNumericDescription,
			GrammarsGeneral grammarDescribePersonal, JsonObject rootObjWords) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getLifeAdjective());
		PrintableObject life = new PrintableObject("life", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(life);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = main.Main._getMessage(grammarIndividual, names, "DESCPERSONAL", "DESCPERSONAL", usePronoun, usePronoun);
		if (isNumericDescription) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getLife()));
		}
		return message;
	}
	
	private static String _messageDescriptionMana(ActiveCharacter character, boolean usePronoun, boolean useAnd,
			boolean isNumericDescription, GrammarsGeneral grammarDescribePersonal, JsonObject rootObjWords) {
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add(character.getManaAdjective());
		PrintableObject mana = new PrintableObject("mana", "", adjectives, null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(mana);
		GrammarIndividual grammarIndividual = grammarDescribePersonal.getRandomGrammar();
		String message = main.Main._getMessage(grammarIndividual, names, "DESCPERSONAL", "DESCPERSONAL", usePronoun, useAnd);
		if (isNumericDescription) {
			String valueToChange = JSONParsing.getElement(WordsGrammar.getAdjectives(rootObjWords, adjectives).get(0).getB(), "translation");
			message = message.replaceAll(valueToChange, String.valueOf(character.getMagic()));
		}

		return message;
	}
	
	public static String _messageDescriptionStats(ActiveCharacter character, boolean isMonster,
			boolean isNumericDescription, GrammarsGeneral grammarDescribepersonal, JsonObject rootObjWords,
			ActiveCharacter user, GrammarsGeneral grammarDescribeEnvironmentSimple) {
		String message = "";
		message += _messageDescriptionLife(character, false, isNumericDescription, grammarDescribepersonal, rootObjWords) + " " + 
				GrammarsOperational.getAndTranslation(rootObjWords);
		message += _messageDescriptionMana(character, true, false, isNumericDescription, grammarDescribepersonal, rootObjWords) + ".";
		
		PrintableObject level = new PrintableObject("level", "", new ArrayList<String>(), null);
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		names.add(character);
		names.add(level);
		GrammarIndividual grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
		message = message + main.Main._getMessage(grammarIndividual, names, "DESCGENERAL", "DESCGENERAL", true, false) + " " + character.getLevel();
		
		if (!isMonster) {
			PrintableObject experience = new PrintableObject("experience", "", new ArrayList<String>(), null);
			ArrayList<PrintableObject> namesExperience = new ArrayList<PrintableObject>();
			namesExperience.add(character);
			namesExperience.add(experience);
			grammarIndividual = grammarDescribeEnvironmentSimple.getRandomGrammar();
			message = message + 
					main.Main._getMessage(grammarIndividual, namesExperience, "DESCPERSONAL", "DESCPERSONAL", true, false) + 
					" " + user.getExperience();
			JsonObject others = JSONParsing.getElement(rootObjWords, "OTHERS").getAsJsonObject();
			JsonArray outOf = JSONParsing.getElement(others, "out of").getAsJsonArray();
			message += " " + JSONParsing.getElement(outOf, "translation") + " " + user.getNextLevelExperience();
		}
		return message;
	}
	
	public static void _messageDescriptionDead(ActiveCharacter character, boolean popup, GrammarsGeneral grammarAdjectiveDescription) {
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		ArrayList<String> adjectives = new ArrayList<String>();
		adjectives.add("dead");
		character.setAdjectives(adjectives);
		names.add(character);
		if (popup) {
			GrammarIndividual grammarIndividual = grammarAdjectiveDescription.getRandomGrammar();
			String message = main.Main._getMessage(grammarIndividual, names, "DESCTOBE", "DESCTOBE", false, false);
			JLabel label= new JLabel();
			label.setText(message);
			label.requestFocusInWindow();
			JOptionPane.showMessageDialog(null, message, "", JOptionPane.PLAIN_MESSAGE);
		}
		main.Main.generatePrintMessage(names, grammarAdjectiveDescription, "DESCTOBE", "DESCTOBE", false, false);
	}
	
	public static void describeSpells(ActiveCharacter user, JsonObject rootObjWords, GrammarsGeneral grammarSimpleVerb){
		ArrayList<PrintableObject> names = new ArrayList<PrintableObject>();
		PrintableObject spells = new PrintableObject("spells", "", null, null);
		names.add(spells);
		GrammarIndividual grammarIndividual = grammarSimpleVerb.getRandomGrammar();
		String message = main.Main._getMessage(grammarIndividual, names, "DESCGENERAL", "DESCGENERAL", false, false) + ": ";
		
		JsonObject namesWords = JSONParsing.getElement(rootObjWords, "N").getAsJsonObject();
		for (Spell spell : user.getSpells()) {
			JsonArray spellName = JSONParsing.getElement(namesWords, spell.getName()).getAsJsonArray();
			message += JSONParsing.getElement(spellName, "translation") + " ";
		}
		main.Main.printMessage(message);
	}
}
