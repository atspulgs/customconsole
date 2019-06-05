package eu.keystruck.customconsole;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.uis.CLI;
import eu.keystruck.customconsole.uis.GUI;

public class CustomConsole {
    public static void main(String... args) {
        //(new Thread(new CLI(new Controller(), System.in, System.out))).start();
        (new  Thread(new GUI(new Controller()))).start();
    }
}
