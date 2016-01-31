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
	private Properties allProperties;
	private Properties newProperties = new Properties();
	private FileOutputStream newPropertiesFile;
	int count = 0;
    public ChangeKeyBinding(WSwingConsoleInterface swingBinding) throws FileNotFoundException {
    	this.swingBinding = swingBinding;
    	FileInputStream in;
    	this.allProperties = new Properties();
		try {
			in = new FileInputStream("src/config/keys.properties");
			this.allProperties.load(in);
	    	in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	this.newPropertiesFile = new FileOutputStream("src/config/keys.properties");
    	jLab = new JLabel("Press key to start");
    	
        addKeyListener(new KeyListener() {
        	@Override
            public void keyPressed(KeyEvent ke) {}
            @Override
            public void keyReleased(KeyEvent ke) {}
            @Override
            public void keyTyped(KeyEvent ke) {
            	try {
            		int keyCode = ke.getKeyChar();
            		count++;
					doSomething(keyCode);
					if (allProperties.size() < count) {
						newProperties.store(newPropertiesFile, null);
						System.out.println("HEY :)");
						try {
							setVisible(false);
							dispose();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        add(jLab);
        pack();
        setVisible(true);
    }
	
    private void doSomething(int keyCode) throws IOException {
    	int i = 1;
    	for(Object k: this.allProperties.keySet()) {
    		if (i == count) {
    			String key = (String)k;
    			jLab.setText(key);
    			System.out.println(keyCode);
    			this.newProperties.setProperty(key, String.valueOf(keyCode));
    		}
    		i++;
    	}
    }
}