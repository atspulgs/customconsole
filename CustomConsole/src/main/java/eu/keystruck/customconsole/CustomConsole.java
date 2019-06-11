package eu.keystruck.customconsole;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.interpriter.Parser;
import eu.keystruck.customconsole.interpriter.Tokenizer;
import eu.keystruck.customconsole.uis.CLI;
import eu.keystruck.customconsole.uis.GUI;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomConsole {
    public static void main(String... args) {
        //(new Thread(new CLI(new Controller(), System.in, System.out))).start();
        (new  Thread(new GUI(new Controller()))).start();
        /*System.out.println(TextAttribute.WEIGHT_EXTRA_LIGHT);   //0.5
        System.out.println(TextAttribute.WEIGHT_LIGHT);         //0.75
        System.out.println(TextAttribute.WEIGHT_DEMILIGHT);     //0.875
        System.out.println(TextAttribute.WEIGHT_REGULAR);       //1.0
        System.out.println(TextAttribute.WEIGHT_SEMIBOLD);      //1.25
        System.out.println(TextAttribute.WEIGHT_MEDIUM);        //1.5
        System.out.println(TextAttribute.WEIGHT_DEMIBOLD);      //1.75
        System.out.println(TextAttribute.WEIGHT_BOLD);          //2.0
        System.out.println(TextAttribute.WEIGHT_HEAVY);         //2.25
        System.out.println(TextAttribute.WEIGHT_EXTRABOLD);     //2.5
        System.out.println(TextAttribute.WEIGHT_ULTRABOLD);     //2.75
        System.out.println(TextAttribute.POSTURE_REGULAR);      //0.0
        System.out.println(TextAttribute.POSTURE_OBLIQUE);      //0.2*/
        /*Tokenizer tk = new Tokenizer();
        tk.tokenize("<<color:255,255,255>>White Text<</color>> Unformatted Text <<weight:2.0>> Bold text <<color:255,0,0>> and this is bold red text<</color>><</weight>>");
        System.out.println(Arrays.toString(tk.getTokens().toArray()));
        Parser p = new Parser();
        p.parse(tk.getTokens());
        p.construct().forEach((entry) -> System.out.println(entry.toString()));*/
        //System.out.println(p.getRoot());
    }
}
