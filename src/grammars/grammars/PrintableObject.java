package grammars.grammars;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import grammars.parsing.JSONParsing;
import util.Tuple;

public class PrintableObject {
	
	private String name;
	private String printableName;
	private String description;
	private ArrayList<String> adjectives;
	private ArrayList<String> prepositions;
	private Tuple<Integer, Integer> position;
	
	public PrintableObject(String name, String description, ArrayList<String> adjectives, Tuple<Integer, Integer> position) {
		this.name = name;
		this.description = description;
		this.adjectives = adjectives;
		this.position = position;
		this.printableName = "";
	}
	
	public String getPositionDirections(Tuple<Integer, Integer> characterPos) {
		System.out.println("BEGIN Position Directions: ");
		System.out.println("object x = " + this.getPosition().x);
		System.out.println("object y = " + this.getPosition().y);
		System.out.println("character x = " + characterPos.x);
		System.out.println("character y = " + characterPos.y);
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

}
