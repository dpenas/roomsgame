package config;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.slashie.libjcsi.wswing.WSwingConsoleInterface;

public class ChangeKeyBinding {
	
    public static void editPropertiesFile(WSwingConsoleInterface swingBinding) throws IOException {
    	
    	FileInputStream in = new FileInputStream("src/config/keys.properties");
    	Properties props = new Properties();
    	props.load(in);
    	in.close();

    	FileOutputStream out = new FileOutputStream("src/config/keys.properties");
    	PressedKeyListener listener = new PressedKeyListener();
    	swingBinding.addKeyListener(listener);
  
        for(Object k: props.keySet()) {
        	String key = (String)k;
        	swingBinding.cls();
        	swingBinding.print(0, 0, key, 12);
        	swingBinding.refresh();
    		props.setProperty(key, String.valueOf(swingBinding.inkey().code));
        }
        props.store(out, null);
        out.close();
    }
}