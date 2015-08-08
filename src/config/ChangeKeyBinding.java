package config;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChangeKeyBinding {
	
    public static void editPropertiesFile() throws IOException {
    	
    	FileInputStream in = new FileInputStream("src/config/keys.properties");
    	Properties props = new Properties();
    	props.load(in);
    	in.close();

    	FileOutputStream out = new FileOutputStream("src/config/keys.properties");
        
        for(Object k: props.keySet()) {
        	JFrame frame = new JFrame("Key Binding");
        	
        	Container contentPane = frame.getContentPane();
        	PressedKeyListener listener = new PressedKeyListener();
        	String key = (String)k;
        	JLabel labelField = new JLabel("Pressed Key for: " + key);
        	JTextField textField = new JTextField();

    		textField.addKeyListener(listener);

    		contentPane.add(labelField, BorderLayout.NORTH);
    		contentPane.add(textField, BorderLayout.SOUTH);

    		frame.pack();

    		frame.setVisible(true);
    		while (listener.getValue() == -1) {System.out.println(listener.getValue());}
    		System.out.println(listener.getValue());
    		props.setProperty(key, String.valueOf(listener.getValue()));
    		frame.dispose();
    		listener.setValue(-1);
        }
        props.store(out, null);
        out.close();
    }
}