package map;

import items.Item;

import java.util.ArrayList;
import java.util.Iterator;

import characters.active.ActiveCharacter;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import main.Main;
import util.GenericMatrixFunctions;
import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Create the doors algorithm between the rooms (should it be here or the room itself) -> Probably here
 * - Test
 * 
 * TODO ADDITIONAL: 
 * - Map of maps? So we can teleport to other maps and stuff.
 *
 */

public class Map {

	private Tuple<Integer, Integer> global_init;
	private Tuple<Integer, Integer> global_fin;
	private int real_x;
	private int real_y;
	byte[][] free_room;
	boolean is_there_free_space = true;
	private ArrayList<Room> rooms;
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_fin(global_fin);
		real_x = this.global_fin().x - this.global_init().x;
		real_y = this.global_fin().y - this.global_init().y;
		size = real_x * real_y;
		free_room = new byte[real_x][real_y];
		initializeMatrix(free_room);
		rooms = new ArrayList<Room>();
		this.initialize_rooms_map();
	}
	
	/**
	 * Given the dimensions of the map, it returns the number of rooms
	 * that the map will have
	 * @return
	 */
	public int obtainNumberRooms(){
		double reduced_size = size/100;
		int min_num = (int) Math.floor(reduced_size);
		int max_num = (int) Math.ceil(reduced_size);
		if (min_num == max_num) return min_num;
		return RandUtil.RandomNumber(min_num, max_num);
	}
	
	/**
	 * Given a byte matrix, it initializes it to 0
	 * @param matrix
	 * @return
	 */
	public void initializeMatrix(byte[][] matrix){
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				matrix[i][j] = 1;
			}
		}
	}
	
	/**
	 * 
	 * @return Tuple of the position of the next room that we must extend
	 */
	public Tuple<Integer, Integer> obtainAvailableRoom(){
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		ArrayList<Tuple<Integer, Integer>> possibleExtensionPoints = new ArrayList<>();
		if (this.free_room[0][0] == 1){
			possibleExtensionPoints.add(new Tuple<Integer, Integer>(0,0));
		}
		
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (i == 0 && j != 0){
					if (free_room[0][j] == 1 && free_room[0][j-1] == 0){
						possibleExtensionPoints.add(new Tuple<Integer, Integer>(0, j));
					}
				} else {
					if (j == 0 && i != 0){
						if (free_room[i][0] == 1 && free_room[i-1][0] == 0){
							possibleExtensionPoints.add(new Tuple<Integer, Integer>(i, 0));
						}
					} else {
						if (j != 0 && i != 0 && (free_room[i-1][j-1] == 0 && free_room[i][j] == 1)){
							possibleExtensionPoints.add(new Tuple<Integer, Integer>(i, j));
						}
					}
				}
			}
		}
		
		possibleExtensionPoints = cleanArray(possibleExtensionPoints);
		
		for(int i = 0; i < possibleExtensionPoints.size(); i++) {
		    // System.out.print("Posible punto: " + possibleExtensionPoints.get(i).x + " " + possibleExtensionPoints.get(i).y + "\n");
		}
		
		int position_random_selected = RandUtil.RandomNumber(0, possibleExtensionPoints.size());
		
		return possibleExtensionPoints.get(position_random_selected);

	}
	
	/**
	 * Returns a tuple without the useless positions of the map. We want to get the
	 * positions where either i or j are 0 and then the corners; the rest of the positions
	 * are not interesting for the map generation since it would create very small rooms
	 * @param tuple1
	 * @return
	 */
	public ArrayList<Tuple<Integer, Integer>> cleanArray(ArrayList<Tuple<Integer, Integer>> arrayTuple){
		ArrayList<Tuple<Integer, Integer>> finalArrayList = new ArrayList<>();
		Tuple<Integer, Integer> cornerTuple = null; // Tuple in the corner
		Tuple<Integer, Integer> leftTuple = null; // Tuple in the left
		Tuple<Integer, Integer> rightTuple = null; // Tuple in the right
		for (int i = 0; i < arrayTuple.size(); i++){
			Tuple<Integer, Integer> actualTuple = arrayTuple.get(i);
			if (i == 0){
				leftTuple = arrayTuple.get(i);
				cornerTuple = arrayTuple.get(i);
				rightTuple = arrayTuple.get(i);
			} else{
				// If it is the point most on the left
				if (actualTuple.x < leftTuple.x){
					leftTuple = actualTuple;
				}
				// If it is the point most of the right and down
				if (actualTuple.y >= cornerTuple.y && actualTuple.x > cornerTuple.x){
					cornerTuple = actualTuple;
				}
				
				if(actualTuple.y <= rightTuple.y && actualTuple.x > rightTuple.x){
					rightTuple = actualTuple;
				}
			}
		}
		
		// We check if there are tuples that are the same before
		// putting them into the array
		finalArrayList.add(leftTuple);
		if (leftTuple.x != rightTuple.x || leftTuple.y != rightTuple.y){
			finalArrayList.add(rightTuple);
		}
		if (leftTuple.x != cornerTuple.x || leftTuple.y != cornerTuple.y){
			if (rightTuple.x != cornerTuple.x || rightTuple.y != cornerTuple.y){
				finalArrayList.add(cornerTuple);
			}
		}
		
		return finalArrayList;
	}
	
	
	/**
	 * Sets to 1 the space of the two rooms defined by the tuples in the free room array
	 * @param tuple1
	 * @param tuple2
	 */
	public void createRoomMatrix(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2){
		int lowestX;
		int highestX;
		int lowestY;
		int highestY;
		
		if (tuple1.x < tuple2.x){
			lowestX = tuple1.x;
			highestX = tuple2.x;
		} else {
			lowestX = tuple2.x;
			highestX = tuple1.x;
		}
		if (tuple1.y < tuple2.y){
			lowestY = tuple1.y;
			highestY = tuple2.y;
			//System.out.println("Entro en la primera: highestY " + highestY + "\n");
			//System.out.println("Entro en la primera: lowestY " + lowestY + "\n");
		} else {
			//System.out.println("Entro en la segunda " + tuple2.y + "\n");
			lowestY = tuple2.y;
			highestY = tuple1.y;
		}
		if (Main.debug){
			System.out.println("lowestY: " + lowestY + "\n");
			System.out.println("lowestX: " + lowestX + "\n");
			System.out.println("highestX: " + highestX + "\n");
			System.out.println("highestY: " + highestY + "\n");
		}
		for (int i = lowestX; i < highestX; i++){
			for (int j = lowestY; j < highestY; j ++){
				free_room[i][j] = 0;
			}
		}
	}
	

	/**
	 * Given a Tuple, it returns the length from that x and y to the next 1 of the free_room array.
	 * This is useful to know the space we have left in a certain space for the next room
	 * @param initial_tuple
	 * @return
	 */
	public Tuple<Integer, Integer> get_free_room_x_y(Tuple<Integer, Integer> initial_tuple){
		// System.out.println("initial_tuple: " + initial_tuple.x + " " + initial_tuple.y);
		int initial_x = initial_tuple.x;
		int initial_y = initial_tuple.y;
		if (Main.debug){
			System.out.println("Initial_tuple x: " + initial_tuple.x);
			System.out.println("Initial_tuple y: " + initial_tuple.y);
			System.out.println("Real_x: " + real_x);
			System.out.println("Real_y: " + real_y);
		}
		for (int i = initial_x; i < real_x; i++){
			for (int j = initial_y; j < real_y; j++){
				if (free_room[i][j] == 0){
					if (Main.debug){
						System.out.println("i = " + i + " j = " + j + " free_room[i][j] = " + free_room[i][j] + "\n");
					}
					Tuple<Integer, Integer> free_x_and_y = new Tuple<Integer, Integer>(i, j);
					// System.out.println("final_tuple!!!!!: " + free_x_and_y.x + " " + free_x_and_y.y);
					return free_x_and_y;
				}
			}
		}
		return new Tuple<Integer, Integer>(real_x, real_y);
	}
	
	/**
	 * Given an initial point, it returns the other point where the rooms should be extended to
	 * @param originalPoint
	 * @return
	 */
	public Tuple<Integer, Integer> nextPoint(Tuple<Integer, Integer> originalPoint, int remainingRooms){
		Tuple<Integer, Integer> freeRoomSpace = get_free_room_x_y(originalPoint);
		int free_room_space_x = freeRoomSpace.x;
		int free_room_space_y = freeRoomSpace.y;
		if (Main.debug){
			System.out.println("originalPoint_x: " + originalPoint.x + " originalPoint_y: " + originalPoint.y + "\n");
		}
		if (remainingRooms == 1){
			int point_x = free_room_space_x;
			int point_y = free_room_space_y;
			if (Main.debug){
				System.out.println("Entro aqui y la x es: " + point_x + " y la y: " + point_y + "\n");
			}
			// If there's only one room left, then we cover all the space
			Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(point_x, point_y);
			return nextRoom;
		} else{
			double possible_real_x = free_room_space_x/remainingRooms;
			double possible_real_y = free_room_space_y/remainingRooms;
			if (Main.debug){
				System.out.println("possible_real_x: " + possible_real_x + " possible_real_y: " + possible_real_y + "\n");
				System.out.println("free_room_space_x: " + free_room_space_x + " free_room_space_x: " + free_room_space_x + "\n");
			}
			int possible_real_x_low = (int) Math.floor(possible_real_x);
			int possible_real_x_high = (int) Math.ceil(possible_real_x);
			int possible_real_y_low = (int) Math.floor(possible_real_y);
			int possible_real_y_high = (int) Math.ceil(possible_real_y);
			int rand_number = RandUtil.RandomNumber(0, 1);
			if (rand_number == 0){
				int actual_x = originalPoint.x + possible_real_x_low;
				int actual_y = originalPoint.y + possible_real_y_low;
				if (actual_x > global_fin.x){
					actual_x = global_fin.x;
				}
				if (actual_y > global_fin.y){
					actual_y = global_fin.y;
				}
				if (Main.debug){
					System.out.println("originalPoint.x: " + originalPoint.x + " originalPoint.y: " + originalPoint.y + "\n");
					System.out.println("possible_real_x_high: " + possible_real_x_high + " possible_real_y_high: " + possible_real_y_high + "\n");
					System.out.println("actual_x: " + actual_x + " actual_y: " + actual_y + "\n");
				}
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			} else {
				int actual_x = originalPoint.x + possible_real_x_high;
				int actual_y = originalPoint.y + possible_real_y_high;
				if (Main.debug){
					System.out.println("originalPoint.x: " + originalPoint.x + " originalPoint.y: " + originalPoint.y + "\n");
					System.out.println("possible_real_x_high: " + possible_real_x_high + " possible_real_y_high: " + possible_real_y_high + "\n");
					System.out.println("actual_x: " + actual_x + " actual_y: " + actual_y + "\n");
				}
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			}
		}
	}
	
	/**
	 * Sets the is_there_free_space boolean value to true or false depending on 
	 * the matrix free_room. If it still has space in it (0 values), then it is
	 * true, if not; it is false.
	 * 
	 */
	public void check_free_space(){
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (free_room[i][j] == 1){
					this.is_there_free_space = true;
					return;
				}
			}
		}
		this.is_there_free_space = false;
	}
	
	public void clearUselessRooms(){
		
		ArrayList<Room> rooms = this.getRooms();
		
		Iterator<Room> iter = rooms.iterator();

		while (iter.hasNext()) {
		    Room r = iter.next();

		    if (r.getGlobal_initial().x > r.getGlobal_final().x || r.getGlobal_initial().y > r.getGlobal_final().y){
		        iter.remove();
		    }
		}
		
		this.setRooms(rooms);
	}
	
	/**
	 * It is possible that after initializing a room there are some spaces without
	 * rooms, so this function will create rooms in those free spaces
	 */
	public void complete_map(){
		Tuple<Integer, Integer> initialPoint = new Tuple<Integer, Integer>(0,0);
		Tuple<Integer, Integer> finalPoint = new Tuple<Integer, Integer>(0,0);
		int final_y = 0;
		int final_x = 0;
		if (Main.debug){
			System.out.println("IN COMPLETE MAP");
			System.out.println("Real x:" + this.real_x);
			System.out.println("Real y:" + this.real_y);
		}
		while (is_there_free_space){
			GenericMatrixFunctions.printMatrix(this.getFreeRoom());
			outerloop:
			for (int i = 0; i < real_x; i++){
				for (int j = 0; j < real_y; j++){
					if (free_room[i][j] == 1){
						initialPoint = new Tuple<Integer, Integer>(i, j);
						break outerloop;
					}
				}
			}
			final_x = initialPoint.x;
			final_y = initialPoint.y;
			firstloop:
			for (int i = initialPoint.x; i < real_x; i++){
				if (free_room[i][initialPoint.y] == 0){
					final_x = i;
					break firstloop;
				}
			}
			if (final_x == initialPoint.x){
				final_x = real_x;
			}
			secondloop:
			for (int j = initialPoint.y; j < real_y; j++){
				if (free_room[initialPoint.x][j] == 0){
					final_y = j;
					break secondloop;
				}
			}
			if (final_y == initialPoint.y){
				final_y = real_y;
			}
			finalPoint = new Tuple<Integer, Integer>(final_x, final_y);
			if (Main.debug){
				System.out.println("COMPLETE: Initial Point: (" + initialPoint.x + "," + initialPoint.y + ")");
				System.out.println("COMPLETE: Final Point: (" + finalPoint.x + "," + finalPoint.y + ")");
			}
			createRoomMatrix(initialPoint, finalPoint);
			int final_x_room = finalPoint.x - 1;
			int final_y_room = finalPoint.y - 1;
			Tuple<Integer, Integer> finalPointRoom = new Tuple<Integer, Integer>(final_x_room, final_y_room);
			Room r = new Room(initialPoint, finalPointRoom);
			this.rooms.add(r);
			check_free_space();
		}
		
		clearUselessRooms();
	}
	
	/**
	 * Main function that creates a map given its size, using the rest of the
	 * class functions
	 */
	public void initialize_rooms_map(){
		int number_rooms = 0;
		Tuple<Integer, Integer> initialPoint;
		Tuple<Integer, Integer> finalPoint;
		//int total_number_rooms = this.obtainNumberRooms();
		int total_number_rooms = 3;
		
		while (number_rooms < total_number_rooms){
			initialPoint = this.obtainAvailableRoom();
			finalPoint = nextPoint(initialPoint, total_number_rooms - number_rooms);
			int final_x = finalPoint.x - 1;
			int final_y = finalPoint.y - 1;
			Tuple<Integer, Integer> finalPointRoom = new Tuple<Integer, Integer>(final_x, final_y);
			Room r = new Room(initialPoint, finalPointRoom);
			createRoomMatrix(initialPoint, finalPoint);
			this.rooms.add(r);
			number_rooms++;
			System.out.println("Initial Point: (" + initialPoint.x + "," + initialPoint.y + ")");
			System.out.println("Final Point: (" + finalPoint.x + "," + finalPoint.y + ")");
			GenericMatrixFunctions.printMatrix(this.getFreeRoom());
			
		}
		complete_map();
		
		assignDoors();
	}
	
	public void assignIndividualDoor(Room room){
		
		int ini_x = room.getGlobal_initial().x;
		int ini_y = room.getGlobal_initial().y;
		int fin_x = room.getGlobal_final().x;
		int fin_y = room.getGlobal_final().y;
		ArrayList<Tuple<Integer, Integer>> doorPoints = new ArrayList<Tuple<Integer, Integer>>();
		Tuple<Integer, Integer> doorFirstPosition = new Tuple<Integer, Integer>(0, 0);
		ArrayList<Tuple<Integer, Integer>> otherRoomDoorPositions = new ArrayList<Tuple<Integer, Integer>>();
		
		for (int i = ini_y; i < fin_y; i++){
			doorPoints.add(new Tuple<Integer, Integer>(ini_x, i));
		}
		
		for (int i = ini_y; i < fin_y; i++){
			doorPoints.add(new Tuple<Integer, Integer>(fin_x, i));
		}
		
		for (int i = ini_x; i < fin_x; i++){
			doorPoints.add(new Tuple<Integer, Integer>(i, ini_y));
		}
		
		for (int i = ini_x; i < fin_x; i++){
			doorPoints.add(new Tuple<Integer, Integer>(i, fin_y));
		}
		
		Iterator<Tuple<Integer, Integer>> iter = doorPoints.iterator();

		while (iter.hasNext()) {
		    Tuple<Integer, Integer> pointDoor = iter.next();

		    if (pointDoor.x == 0 || pointDoor.y == 0 || pointDoor.x == real_x - 1 || pointDoor.y == real_y - 1){
		        iter.remove();
		    }
		}
		
		int randNumber = RandUtil.RandomNumber(0, doorPoints.size());
		
		doorFirstPosition = doorPoints.get(randNumber);
		otherRoomDoorPositions.add(new Tuple<Integer, Integer>(doorFirstPosition.x - 1, doorFirstPosition.y));
		otherRoomDoorPositions.add(new Tuple<Integer, Integer>(doorFirstPosition.x, doorFirstPosition.y - 1));
		otherRoomDoorPositions.add(new Tuple<Integer, Integer>(doorFirstPosition.x + 1, doorFirstPosition.y));
		otherRoomDoorPositions.add(new Tuple<Integer, Integer>(doorFirstPosition.x, doorFirstPosition.y + 1));
		
		iter = otherRoomDoorPositions.iterator();

		while (iter.hasNext()) {
		    Tuple<Integer, Integer> pointDoor2 = iter.next();

		    if (pointDoor2.x == 0 || pointDoor2.y == 0 || pointDoor2.x == real_x - 1 || pointDoor2.y == real_y - 1){
		        iter.remove();
		    }
		}
		
		randNumber = RandUtil.RandomNumber(0, otherRoomDoorPositions.size());
		Tuple <Integer, Integer> finalPositionOtherRoom = otherRoomDoorPositions.get(randNumber); 
		
		for (Room roomDoor: this.getRooms()){
			if (roomDoor.isMapPositionHere(finalPositionOtherRoom) && !roomDoor.equals(room) && 
					!room.getConnected_rooms().contains(roomDoor)) {
				Door door = new Door(doorFirstPosition, finalPositionOtherRoom, room, roomDoor);
				System.out.println("LOLOLO");
				if (!room.isInCorner(door.getPositionRoom1()) &&
						!room.isInCorner(door.getPositionRoom2()) &&
						!roomDoor.isInCorner(door.getPositionRoom1()) &&
						!roomDoor.isInCorner(door.getPositionRoom2())){
					System.out.println(door.getPositionRoom1().x + "," + door.getPositionRoom1().y);
					System.out.println(door.getPositionRoom2().x + "," + door.getPositionRoom2().y);
					System.out.println("ROOM CORNERS: ");
					for (Tuple<Integer, Integer> tuple: room.getCorners()){
						System.out.println(tuple.x + "," + tuple.y);
					}
					System.out.println("THE OTHER ONE: ");
					for (Tuple<Integer, Integer> tuple: roomDoor.getCorners()){
						System.out.println(tuple.x + "," + tuple.y);
					}
					room.getDoors().add(door);
					roomDoor.getDoors().add(door);
					room.getConnected_rooms().add(roomDoor);
					roomDoor.getConnected_rooms().add(room);
					break;
				}
			}
		}
	}
	
	public String getSymbolPosition(Tuple<Integer, Integer> tuple) {
		for (Room room : this.getRooms()){
			if (room.isInside(tuple)){
				return room.getSymbolPosition(tuple);
			}
		}
		return ".";
		
	}
	
	public void assignDoors(){
		for(Room room : this.rooms){
			if (room.getDoors().size() == 0){
				this.assignIndividualDoor(room);
			}
		}
		
		if (Main.debug){
			System.out.println("Doors: ");
			
			for (Room r : this.getRooms()){
				System.out.println("Room Initial Point: (" + r.getGlobal_initial().x + "," + r.getGlobal_initial().y + ")");
				System.out.println("Final Point: (" + r.getGlobal_final().x + "," + r.getGlobal_final().y + ")");
				for (Door d : r.getDoors()){
					System.out.println("Door POS 1: (" + d.getPositionRoom1().x + "," + d.getPositionRoom1().y + ")");
					System.out.println("Door POS 2: (" + d.getPositionRoom2().x + "," + d.getPositionRoom2().y + ")");
				}
			}	
		}
		
		fixUnreachableRooms();
		for(Room room : this.rooms){
			room.initializeColumns();
		}
	}
	
	public ArrayList<Room> getUnreachableRooms(){
		ArrayList<Room> unreachable = new ArrayList<Room>(this.getRooms());
		ArrayList<Room> visitedRooms = new ArrayList<Room>();
		ArrayList<Room> nextRooms = new ArrayList<Room>();
		nextRooms.add(this.getRooms().get(0));
		
		while (nextRooms.size() != 0){
			Room room = nextRooms.get(0);
			for (Room r: room.getConnected_rooms()){
				if (!visitedRooms.contains(r)){
					nextRooms.add(r);
				}
			}
			nextRooms.remove(room);
			unreachable.remove(room);
			visitedRooms.add(room);
		}
		
		for (Room r: unreachable){
			System.out.println("Unreachable INI: (" + r.getGlobal_initial().x + "," + r.getGlobal_initial().y + ")");
			System.out.println("Unreachable FIN: (" + r.getGlobal_final().x + "," + r.getGlobal_final().y + ")");
		}
		
		return unreachable;
	}
	
	public void fixUnreachableRooms(){
		ArrayList<Room> unreachable = this.getUnreachableRooms();
		while (unreachable.size() != 0){
			System.out.println(this.getRooms().size());
			for (Room r : unreachable){
				this.assignIndividualDoor(r);
			}
			unreachable = this.getUnreachableRooms();
		}
	}
	
	public void printBorders(WSwingConsoleInterface j, ActiveCharacter user){
		Tuple<Integer, Integer> tuple1 = new Tuple<Integer, Integer>(0, 0);
		Tuple<Integer, Integer> tuple2 = new Tuple<Integer, Integer>(0, 0);
		for(Room room : this.getRooms()){
			if (user.getRoom().equals(room)){
				for (Tuple<Integer, Integer> pos: room.getBorders()){
					// System.out.println("Borders: (" + pos.x + "," + pos.y + ")");
					if (RandUtil.containsTuple(pos, user.getVisiblePositions())){
						j.print(pos.y, pos.x, '#', 12);
					}
				}
				for (Door d : room.getDoors()){
					tuple1 = new Tuple<Integer, Integer>(d.getPositionRoom1().x, d.getPositionRoom1().y);
					tuple2 = new Tuple<Integer, Integer>(d.getPositionRoom2().x, d.getPositionRoom2().y);
					if (RandUtil.containsTuple(tuple1, user.getVisiblePositions())){
						j.print(d.getPositionRoom1().y, d.getPositionRoom1().x, 'O', 12);
					} 
					if (RandUtil.containsTuple(tuple2, user.getVisiblePositions())){
						j.print(d.getPositionRoom2().y, d.getPositionRoom2().x, 'O', 12);
					}
				}
			}
		}
	}
	
	public ArrayList<ActiveCharacter> getMonstersPosition(ActiveCharacter character){
		ArrayList<ActiveCharacter> monsters = new ArrayList<ActiveCharacter>();
		Room room = character.getRoom();
		for (ActiveCharacter monster : room.getMonsters()){
			if (RandUtil.sameTuple(character.getPosition(), monster.getPosition())){
				monsters.add(monster);
			}
		}
		return monsters;
	}
	
	public void printInside(WSwingConsoleInterface j, ActiveCharacter user){
		for(Room room : this.getRooms()){
			if (user.getRoom().equals(room)){
				for (Tuple<Integer, Integer> pos: room.getInsidePositions()){
					if (RandUtil.containsTuple(pos, user.getVisiblePositions())){
						if (RandUtil.containsTuple(pos, room.getInsidecolumns())) {
							j.print(pos.y, pos.x, '#', 12);
						} else {
							j.print(pos.y, pos.x, '.', 12);
						}
					}
				}
			}
		}
	}
	
	public void printItems(WSwingConsoleInterface j, ActiveCharacter user){
		for (Room room : getRooms()){
			if (user.getRoom().equals(room)){
				for (Tuple<Integer, Integer> pos: room.getInsidePositions()){
					if (RandUtil.containsTuple(pos, user.getVisiblePositions())){
						room.printItems(j, user.getVisiblePositions());
					}
				}
			}
		}
	}
	
	public void printMonsters(WSwingConsoleInterface j, ActiveCharacter user){
		for (Room room : getRooms()){
			if (user.getRoom().equals(room)){
				for (ActiveCharacter enemy: room.getMonsters()){
					if (RandUtil.containsTuple(enemy.getPosition(), user.getVisiblePositions())){
						room.printMonsters(j, user.getVisiblePositions());
					}
				}
			}
		}
	}
	
	public boolean putItemRoom(Item item){
		for (Room room : getRooms()){
			if (room.putItemRoom(item)){
				return true;
			}
		}
		return false;
	}
	
	public Room obtainRoomByPosition(Tuple<Integer, Integer> position){
		for (Room room : this.getRooms()){
			if (room.isMapPositionHere(position)){
				return room;
			}
		}
		return null;
	}
	
	public Tuple<Integer, Integer> global_init() {
		return global_init;
	}

	public Tuple<Integer, Integer> global_fin() {
		return global_fin;
	}
	
	public void set_global_fin(Tuple<Integer, Integer> tuple) {
		this.global_fin = tuple;
	}
	
	public void set_global_init(Tuple<Integer, Integer> tuple) {
		this.global_init = tuple;
	}
	
	public int get_size() {
		return this.size;
	}
	
	public void set_size(int size) {
		this.size = size;
	}
	
	public ArrayList<Room> getRooms(){
		return this.rooms;
	}
	
	public void setRooms(ArrayList<Room> rooms){
		this.rooms = rooms;
	}
	
	public byte[][] getFreeRoom(){
		return free_room;
	}

}
