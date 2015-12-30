package grammars.grammars;

import java.util.ArrayList;

public class PrintableObject {
	
	private String name;
	private String description;
	private ArrayList<String> adjectives;
	
	public PrintableObject(String name, String description, ArrayList<String> adjectives) {
		super();
		this.name = name;
		this.description = description;
		this.adjectives = adjectives;
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

}
