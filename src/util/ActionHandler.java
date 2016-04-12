package util;

import java.util.ArrayList;
import java.util.HashMap;

import characters.active.ActiveCharacter;
import grammars.grammars.GrammarsGeneral;
import grammars.grammars.PrintableObject;
import items.Item;

public class ActionHandler {
	
	private HashMap<String, Integer> keysMap;
	private GrammarsGeneral grammarUseItem;
	private GrammarsGeneral grammarPickItem;
	private ActiveCharacter user;
	
	public ActionHandler(HashMap<String, Integer> keysMap, ActiveCharacter user, 
			GrammarsGeneral grammarUseItem, GrammarsGeneral grammarPickItem) {
		this.keysMap = keysMap;
		this.grammarUseItem = grammarUseItem;
		this.grammarPickItem = grammarPickItem;
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

}
