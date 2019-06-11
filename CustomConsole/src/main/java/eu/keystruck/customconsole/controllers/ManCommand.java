package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;

public class ManCommand extends Command {
    private static final String COMMAND = "man";
    private static final String DESCRIPTION = "Shows the help page fo the target command."
            + "\n\tSyntax: <<color:255,120,120>><<weight:2.0>>man <target><</weight>><</color>>";
    private HashMap<String,Command> traversable = null;
    
    public ManCommand(HashMap<String,Command> traversable) {
        super(COMMAND,DESCRIPTION);
        if(traversable != null)
            this.traversable = traversable;
    }
    
    @Override
    public void execute(UserInterface io, String... args) {
        if(io == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nio = " + String.valueOf(io)
            );
        if(this.traversable == null || this.traversable.isEmpty()) {
            io.push("Could not retrieve the command list!");
            return;
        }
        if(args == null || args.length <= 0 || args[0].isEmpty() || args[0].isBlank()) {
            io.push("<<color:255,100,100>><<font:Lucida Console>><<italic:0.2>><<weight:2.0>>No arguments provided, this command requires a target!<</color>>");
            io.push("<<color:120,120,255>>"+this.getDescription()+"<</color>>");
            return;
        }
        var command = this.traversable.get(args[0]);
        if(command != null) {
            io.push("<<color:120,120,255>>"+command.getDescription()+"<</color>>");
        } else {
            io.push("<<color:255,100,100>><<font:Lucida Console>><<italic:0.2>><<weight:2.0>>Could not find the provided target command in the registered pool!<</color>>");
            io.push("<<color:120,120,255>>"+this.getDescription()+"<</color>>");
        }
    }
}
