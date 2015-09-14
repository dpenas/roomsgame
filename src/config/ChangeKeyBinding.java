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
        	System.out.println("Hola");
        	String key = (String)k;
        	System.out.println("Hola1");
        	swingBinding.cls();
        	System.out.println("Hola2");
        	swingBinding.print(0, 0, key, 12);
        	System.out.println("Hola3");
        	swingBinding.refresh();
        	System.out.println("Hola4");
    		//System.out.println(swingBinding.inkey().code);
    		System.out.println("Hola5");
    		props.setProperty(key, String.valueOf(swingBinding.inkey().code));
    		System.out.println("Hola6");
        }
        props.store(out, null);
        out.close();
    }
}