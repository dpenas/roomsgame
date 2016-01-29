package config;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.*;

import net.slashie.libjcsi.wswing.WSwingConsoleInterface;

public class ChangeKeyBinding extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLab;
	private WSwingConsoleInterface swingBinding;
    public ChangeKeyBinding(WSwingConsoleInterface swingBinding) {
    	this.swingBinding = swingBinding;
        jLab = new JLabel("Example");
        addKeyListener(new KeyListener() {
        	@Override
            public void keyPressed(KeyEvent ke) {}
            @Override
            public void keyReleased(KeyEvent ke) {}
            @Override
            public void keyTyped(KeyEvent ke) {
            	try {
					doSomething();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        add(jLab);
        pack();
        setVisible(true);
    }
	
    private void doSomething() throws IOException {
    	
    	FileInputStream in = new FileInputStream("src/config/keys.properties");
    	Properties props = new Properties();
    	props.load(in);
    	in.close();

    	FileOutputStream out = new FileOutputStream("src/config/keys.properties");
    	PressedKeyListener listener = new PressedKeyListener();
    	this.swingBinding.addKeyListener(listener);
  
        for(Object k: props.keySet()) {
        	String key = (String)k;
        	this.swingBinding.cls();
        	this.swingBinding.print(0, 0, key, 12);
        	this.swingBinding.refresh();
    		props.setProperty(key, String.valueOf(this.swingBinding.inkey().code));
        }
        props.store(out, null);
        out.close();
    }
}