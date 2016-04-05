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
		try {
			main.Main.makeMovement(strokeInformer.charCode(arg0), true);
			lengthAfter = main.Main.messageLabel.getText().length();
		} catch (JsonIOException | JsonSyntaxException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (lengthAfter == lengthBefore) {
			j.getTargetFrame().requestFocus();
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
