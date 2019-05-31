
import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.uis.CLI;

public class CustomConsole {
    public static void main(String... args) {
        (new Thread(new CLI(new Controller(), System.in, System.out))).start();
    }
}
