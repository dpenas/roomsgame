import net.slashie.libjcsi.CSIColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;

/**
 * This will display in a repeating pattern all of the custom named colors
 * available in CSIColor.  The test include displaying the colors onto a
 * black, grey, and white background.
 * @author Eben Howard
 */
public class Prueba {

    static WSwingConsoleInterface mainInterface;
    Random rng = new Random();

    @SuppressWarnings({"unchecked" })
	public Prueba() {
        ArrayList<CSIColor> list = new ArrayList<CSIColor>();
        for (int i = 0; i < CSIColor.FULL_PALLET.length; i++) {
            list.add(CSIColor.FULL_PALLET[i]);
        }

        Collections.sort(list);

        try {
            mainInterface = new WSwingConsoleInterface("CSIColor Test");
        } catch (ExceptionInInitializerError eiie) {
            System.out.println("Fatal Error Initializing Swing Console Box");
            eiie.printStackTrace();
            System.exit(-1);
        }

        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 4; i++) {
                mainInterface.print(i, k, 'Q', new CSIColor(CSIColor.WHITE), new CSIColor(CSIColor.BLACK));
            }
        }
        mainInterface.cls();
        mainInterface.print(1, 1, '#', CSIColor.WHITE, CSIColor.BLACK);
        mainInterface.refresh();
    }

    public static void main(String[] args) {
        new Prueba();
    }
}

