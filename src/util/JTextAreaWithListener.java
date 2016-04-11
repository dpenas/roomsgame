package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.slashie.libjcsi.CharKey;
import net.slashie.libjcsi.wswing.StrokeInformer;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;

@SuppressWarnings("serial")
public class JTextAreaWithListener extends JTextArea implements KeyListener{
	private WSwingConsoleInterface j;

	@Override
	public void keyPressed(KeyEvent arg0) {
		int lengthBefore = main.Main.messageLabel.getText().length();
		int lengthAfter = lengthBefore;
		StrokeInformer strokeInformer = new StrokeInformer();
		int code = strokeInformer.charCode(arg0);
		try {
			System.out.println("unequipPressed: " + main.Main.unequipPressed);
			if (main.Main.unequipPressed) {
				main.Main.unequipItemAction(code);
				main.Main.unequipPressed = false;
			}
			else if (main.Main.spellsPressed) {
				main.Main.spellAction(code);
				main.Main.spellsPressed = false;
			}
			else if (main.Main.throwPressed) {
				main.Main.throwAction(code);
				main.Main.throwPressed = false;
			}
			else {
				main.Main.makeMovement(code);
				lengthAfter = main.Main.messageLabel.getText().length();
				if (lengthAfter == lengthBefore && !main.Main.isTwoKeysInput(code)) {
					j.getTargetFrame().requestFocus();
				}
			}
		} catch (JsonIOException | JsonSyntaxException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if (main.Main.isUnequipItemInput(strokeInformer.charCode(arg0))) {
			main.Main.unequipPressed = true;
		} else if (main.Main.isSpellInput(strokeInformer.charCode(arg0))) {
			main.Main.spellsPressed = true;
		} else if (main.Main.isThrowItemInput(strokeInformer.charCode(arg0))) {
			main.Main.throwPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public JTextAreaWithListener(WSwingConsoleInterface j) {
		this.j = j;
		addKeyListener(this);
	}

}
