package config;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PressedKeyListener implements KeyListener {
	
	public static int value = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		getEventCode(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {}

	@Override
	public void keyTyped(KeyEvent event) {}

	private void getEventCode(KeyEvent e) {
		value = e.getKeyChar();
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		PressedKeyListener.value = value;
	}

};