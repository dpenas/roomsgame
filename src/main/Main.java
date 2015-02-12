package main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import util.Tuple;
import map.Map;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;


public class Main {
	public static String language = new String("es");
	public static String country = new String("ES");
	public static Locale currentLocale = new Locale(language, country);
	public static ResourceBundle messagesWereables;
	public static boolean debug = true;
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(10, 10);

	public static void main(String[] args) throws IOException {
		Map map = new Map(initial_point, final_point);
		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		WSwingConsoleInterface j = new WSwingConsoleInterface("asdasd");
		j.cls();
		map.printBorders(j);
		
		j.print(0, 0, "@", 12);
		for (;;) {
			int i = j.inkey().code;
            System.out.println(i);
			j.refresh();
		}
	}
}
