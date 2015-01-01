package items;

public abstract class Item {
	
	private String description;
	private String gender;
	private int weight;
	private int space;
	private Character character;
	
	public Item(String description, String gender, int weight, int space, Character character) {
		super();
		this.description = description;
		this.gender = gender;
		this.weight = weight;
		this.space = space;
		this.character = character;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getSpace() {
		return space;
	}
	
	public int getCharacter() {
		return character;
	}

}
