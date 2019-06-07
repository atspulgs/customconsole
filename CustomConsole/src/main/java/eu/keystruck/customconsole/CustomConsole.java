package eu.keystruck.customconsole;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.interpriter.Parser;
import eu.keystruck.customconsole.interpriter.Tokenizer;
import eu.keystruck.customconsole.uis.CLI;
import eu.keystruck.customconsole.uis.GUI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomConsole {
    public static void main(String... args) {
        //(new Thread(new CLI(new Controller(), System.in, System.out))).start();
        //(new  Thread(new GUI(new Controller()))).start();
        Tokenizer tk = new Tokenizer();
        tk.tokenize("Hello<<c:100,200,250>>World<<c:100,200,250>>World<<c:100,200,250>>World<</c>><</c>><<upper>><</c>>, This<</b>> <</b>>is me!<<c:100,200,250>>World<</c>>");
        System.out.println(Arrays.toString(tk.getTokens().toArray()));
        Parser p = new Parser();
        p.parse(tk.getTokens());
        System.out.println(p.getRoot());
    }
}
