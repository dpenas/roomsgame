package grammars.grammars;

import java.util.ArrayList;

import grammars.parsing.JSONParsing;
import main.Main;
import net.slashie.util.Pair;
import util.Tuple;

public class PrintableObject {
	
	private String name;
	private String printableName;
	private String printableSentence;
	private String description;
	private ArrayList<String> adjectives;
	private ArrayList<String> prepositions;
	private Tuple<Integer, Integer> position;
	
	public PrintableObject(String name, String description, ArrayList<String> adjectives, Tuple<Integer, Integer> position) {
		this.name = name;
		this.description = description;
		this.adjectives = adjectives;
		this.position = position;
		this.printableName = JSONParsing.getTranslationWord(name, "N", main.Main.rootObjWords);
		ArrayList<String> prepositions = new ArrayList<String>();
		prepositions.add("to");
		this.setPrepositions(prepositions);
		this.printableSentence = "";
	}
	
	public String getPositionDirections(Tuple<Integer, Integer> characterPos) {
		if (Main.debug) {
			System.out.println("BEGIN Position Directions: ");
			System.out.println("object x = " + this.getPosition().x);
			System.out.println("object y = " + this.getPosition().y);
			System.out.println("character x = " + characterPos.x);
			System.out.println("character y = " + characterPos.y);
		}
		int thisx = this.getPosition().x;
		int thisy = this.getPosition().y;
		int characterx = characterPos.x;
		int charactery = characterPos.y;
		if (thisy > charactery) {
			if (thisx > characterx) {
				return "southeast";
			} else if (thisx < characterx) {
				return "northeast";
			}
			return "east";
		} else if (thisy < charactery) {
			if (thisx > characterx) {
				return "southwest";
			} else if (thisx < characterx) {
				return "northwest";
			}
			return "west";
		} else if (thisx > characterx) {
			return "south";
		} else if (thisx < characterx) {
			return "north";
		}
		return "in the same position";
	}
	
	public Pair<String, String> getPositionDirectionsWithNumbers(Tuple<Integer, Integer> characterPos) {
		int thisx = this.getPosition().x;
		int thisy = this.getPosition().y;
		int characterx = characterPos.x;
		int charactery = characterPos.y;
		Pair<String, String> returnValue = new Pair<String, String>("", "");
		if (thisy > charactery) {
			if (thisx > characterx) {
				returnValue.setA("southeast");
				String message = " " + Math.abs(thisx-characterx) + "," + Math.abs(thisy-charactery);
				returnValue.setB(message);
				return returnValue;
			} else if (thisx < characterx) {
				returnValue.setA("northeast");
				String message = " " + Math.abs(thisx-characterx) + "," + Math.abs(thisy-charactery);
				returnValue.setB(message);
				return returnValue;
			}
			String message = " " + Math.abs(thisy-charactery) + "";
			returnValue.setB(message);
			returnValue.setA("east");
			return returnValue;
		} else if (thisy < charactery) {
			if (thisx > characterx) {
				returnValue.setA("southwest");
				String message = " " + Math.abs(thisx-characterx) + "," + Math.abs(thisy-charactery);
				returnValue.setB(message);
				return returnValue;
			} else if (thisx < characterx) {
				returnValue.setA("northwest");
				String message = " " + Math.abs(thisx-characterx) + "," + Math.abs(thisy-charactery);
				returnValue.setB(message);
				return returnValue;
			}
			String message = " " + Math.abs(thisy-charactery) + "";
			returnValue.setB(message);
			returnValue.setA("west");
			return returnValue;
		} else if (thisx > characterx) {
			returnValue.setA("south");
			String message = " " + Math.abs(thisx-characterx) + "";
			returnValue.setB(message);
			return returnValue;
		} else if (thisx < characterx) {
			returnValue.setA("north");
			String message = " " + Math.abs(thisx-characterx) + "";
			returnValue.setB(message);
			return returnValue;
		}
		returnValue.setA("in the same position");
		returnValue.setB("");
		return returnValue;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<String> getAdjectives() {
		return adjectives;
	}
	public void setAdjectives(ArrayList<String> adjectives) {
		this.adjectives = adjectives;
	}
	public Tuple<Integer, Integer> getPosition() {
		return position;
	}
	public void setPosition(Tuple<Integer, Integer> position) {
		this.position = position;
	}

	public ArrayList<String> getPrepositions() {
		return prepositions;
	}

	public void setPrepositions(ArrayList<String> prepositions) {
		this.prepositions = prepositions;
	}

	public String getPrintableName() {
		return printableName;
	}

	public void setPrintableName(String printableName) {
		this.printableName = printableName;
	}

	public String getPrintableSentence() {
		return printableSentence;
	}

	public void setPrintableSentence(String printableSentence) {
		this.printableSentence = printableSentence;
	}

}
