package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.interpriter.UChars;
import eu.keystruck.customconsole.uis.UserInterface;

public class VersionCommand extends Command {
    private static final String NAME = "Version";
    private static final String COMMAND = "version";
    private static final String DESCRIPTION = "Prints the information about this Custom Console.";
    private static final String SYNTAX = "";
    private static final String NOTES = "";
    
    public VersionCommand() {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
    }
    @Override
    public void execute(UserInterface io, String... args) {
        if(io == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nio = " + String.valueOf(io)
            );
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%1$s%2$100s%3$s\n", UChars.S_TLCORNER, String.valueOf(UChars.S_HORIZONTAL).repeat(100), UChars.S_TRCORNER));
        sb.append(String.format("%1$s%2$10s : %3$-87s%1$s\n", UChars.S_VERTICAL, "Version", "1.0.1"));
        sb.append(String.format("%1$s%2$10s : %3$-87s%1$s\n", UChars.S_VERTICAL, "Author", "Toms"));
        sb.append(String.format("%1$s%2$100s%3$s\n", UChars.S_BLCORNER, String.valueOf(UChars.S_HORIZONTAL).repeat(100), UChars.S_BRCORNER));
        io.push("<<font:Consolas>>"+sb.toString()+"<</font>>");
        //https://altcodeunicode.com/
        //https://en.wikipedia.org/wiki/Box-drawing_character
    }
}
