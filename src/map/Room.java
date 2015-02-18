package map;

import java.util.ArrayList;

import main.Main;
import util.RandUtil;
import util.Tuple;

/**
 * A room is randomly generated depending on the given size and
 * number of elements in it. These values don't have to be specified.
 * @author Dario
 *
 */
public class Room {
	
	/**
	 *  individual_x and individual_y represent the length of the room
	 *  itself, whereas global_x and global_y refers to the location of the
	 *  room inside a map
	 */
	private Tuple<Integer, Integer> individual_initial;
	private Tuple<Integer, Integer> individual_final;
	private Tuple<Integer, Integer> global_initial;
	private Tuple<Integer, Integer> global_final;
	private ArrayList<Door> doors = new ArrayList<Door>();
	private ArrayList<Room> connected_rooms = new ArrayList<Room>();
	private ArrayList<Tuple<Integer, Integer>> bordersMap = new ArrayList<Tuple<Integer, Integer>>();
	private ArrayList<Tuple<Integer, Integer>> borders = new ArrayList<Tuple<Integer, Integer>>();
	private ArrayList<Tuple<Integer, Integer>> corners = new ArrayList<Tuple<Integer, Integer>>();
	private ArrayList<Tuple<Integer, Integer>> insidePositions = new ArrayList<Tuple<Integer, Integer>>();
	int ini_x;
	int ini_y;
	int fin_x;
	int fin_y;
	
	public Room(Tuple<Integer, Integer> global_initial, Tuple<Integer, Integer> global_final){
		int individual_final_x = global_final.x - global_initial.x;
		int individual_final_y = global_final.y - global_initial.y;
		this.individual_initial = new Tuple<Integer, Integer>(0, 0);
		this.individual_final = new Tuple<Integer, Integer>(individual_final_x, individual_final_y);
		this.global_initial = global_initial;
		this.global_final = global_final;
		ini_x = this.getGlobal_initial().x;
		ini_y = this.getGlobal_initial().y;
		fin_x = this.getGlobal_final().x;
		fin_y = this.getGlobal_final().y;
		this.initializeBorders();
		this.initializeCorners();
		this.initializeInsidePositions();
	}
	
	public boolean isInCorner(Tuple<Integer, Integer> tuple){
		for (Tuple<Integer, Integer> tuple2 : this.getCorners()){
			if (tuple.x == tuple2.x){
				if (tuple.y == tuple2.y){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isInside(Tuple<Integer, Integer> tuple){
		if (Main.debug){
			System.out.println("Initial Values: (" + ini_x + "," + ini_y + ")");
			System.out.println("Initial Values: (" + fin_x + "," + fin_y + ")");
			for (Tuple<Integer, Integer> pos : this.getInsidePositions()){
				System.out.println("Inside position: (" + pos.x + "," + pos.y + ")");
			}
		}
		for (Tuple<Integer, Integer> tuple2 : this.getInsidePositions()){
			if (tuple.x == tuple2.x){
				if (tuple.y == tuple2.y){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isADoor(Tuple<Integer, Integer> position){
		for (Door door : this.getDoors()){
			Tuple<Integer, Integer> posDoor = door.getPositionRoom(position);
			if (posDoor != null){
				if (posDoor.x == position.x && posDoor.y == position.y){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isMapPositionHere(Tuple<Integer, Integer> position){
		Tuple<Integer, Integer> initialPoint = this.getGlobal_initial();
		Tuple<Integer, Integer> finalPoint = this.getGlobal_final();
		if (position.x >= initialPoint.x && position.y >= initialPoint.y){
			if (position.x <= finalPoint.x && position.y <= finalPoint.y){
				return true;
			}
		}
		return false;
	}
	
	public Tuple<Integer, Integer> getRandomInsidePosition(){
		return this.getInsidePositions().get(RandUtil.RandomNumber(0, this.getInsidePositions().size()));
	}
	
	public void initializeInsidePositions(){
		
		for (int i = ini_x + 1; i < fin_x; i++){
			for (int j = ini_y + 1; j < fin_y; j++){
				insidePositions.add(new Tuple<Integer, Integer>(i, j));
			}
		}
	}
	
	public void initializeCorners(){
		
		this.corners.add(new Tuple<Integer, Integer>(ini_x, ini_y));
		this.corners.add(new Tuple<Integer, Integer>(fin_x, fin_y));
		this.corners.add(new Tuple<Integer, Integer>(fin_x, ini_y));
		this.corners.add(new Tuple<Integer, Integer>(ini_x, fin_y));
	}
	
	public void initializeBorders(){
		
		for (int i = ini_y; i <= fin_y; i++){
			bordersMap.add(new Tuple<Integer, Integer>(i, ini_x));
			borders.add(new Tuple<Integer, Integer>(ini_x, i));
		}
		
		for (int i = ini_y; i <= fin_y; i++){
			bordersMap.add(new Tuple<Integer, Integer>(i, fin_x));
			borders.add(new Tuple<Integer, Integer>(fin_x, i));
		}
		
		for (int i = ini_x; i <= fin_x; i++){
			bordersMap.add(new Tuple<Integer, Integer>(ini_y, i));
			borders.add(new Tuple<Integer, Integer>(i, ini_y));
		}
		
		for (int i = ini_x; i <= fin_x; i++){
			bordersMap.add(new Tuple<Integer, Integer>(fin_y, i));
			borders.add(new Tuple<Integer, Integer>(i, fin_y));
		}
	}

	public Tuple<Integer, Integer> getIndividual_initial() {
		return individual_initial;
	}

	public void setIndividual_initial(Tuple<Integer, Integer> individual_initial) {
		this.individual_initial = individual_initial;
	}

	public Tuple<Integer, Integer> getIndividual_final() {
		return individual_final;
	}

	public void setIndividual_final(Tuple<Integer, Integer> individual_final) {
		this.individual_final = individual_final;
	}

	public Tuple<Integer, Integer> getGlobal_initial() {
		return global_initial;
	}

	public void setGlobal_initial(Tuple<Integer, Integer> global_initial) {
		this.global_initial = global_initial;
	}

	public Tuple<Integer, Integer> getGlobal_final() {
		return global_final;
	}

	public void setGlobal_final(Tuple<Integer, Integer> global_final) {
		this.global_final = global_final;
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}

	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}

	public ArrayList<Room> getConnected_rooms() {
		return connected_rooms;
	}

	public void setConnected_rooms(ArrayList<Room> connected_rooms) {
		this.connected_rooms = connected_rooms;
	}
	
	public ArrayList<Tuple<Integer, Integer>> getBordersMap(){
		return this.bordersMap;
	}
	
	public void setBordersMap(ArrayList<Tuple<Integer, Integer>> borders){
		this.bordersMap = borders;
	}
	
	public ArrayList<Tuple<Integer, Integer>> getBorders(){
		return this.borders;
	}
	
	public void setBorders(ArrayList<Tuple<Integer, Integer>> borders){
		this.borders = borders;
	}
	
	public ArrayList<Tuple<Integer, Integer>> getCorners(){
		return this.corners;
	}
	
	public void setCorners(ArrayList<Tuple<Integer, Integer>> corners){
		this.corners = corners;
	}
	
	public ArrayList<Tuple<Integer, Integer>> getInsidePositions(){
		return this.insidePositions;
	}
	
	public void setInsidePositions(ArrayList<Tuple<Integer, Integer>> insidePositions){
		this.insidePositions = insidePositions;
	}
}
